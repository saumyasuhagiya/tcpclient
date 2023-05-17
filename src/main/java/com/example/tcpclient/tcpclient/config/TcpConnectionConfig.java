package com.example.tcpclient.tcpclient.config;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.connection.*;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
@EnableIntegration
@Slf4j
public class TcpConnectionConfig implements ApplicationEventPublisher {

    //This is for testing purpose only. Actual server resides on external server.
    private String tcpServerAddress = "localhost";

    @Autowired
    MessageChannel primaryMessageChannel;

    @Autowired
    MessageChannel secondaryMessageChannel;

    private int tcpServerPort = 8888;
    private String tcpServerFailoverAddress = "localhost";

    private int tcpServerFailoverPort = 9999;

    @Bean
    public AbstractClientConnectionFactory secondaryFCCF() {
        List<AbstractClientConnectionFactory> tcpNetClientConnectionFactoryList = new ArrayList<>();
        tcpNetClientConnectionFactoryList.add(
                getTcpNetClientConnectionFactory(tcpServerFailoverAddress, tcpServerFailoverPort));
        tcpNetClientConnectionFactoryList.add(
                getTcpNetClientConnectionFactory(tcpServerAddress, tcpServerPort));

        return getFailOverClientConnection(tcpNetClientConnectionFactoryList);
    }

    @Bean
    public AbstractClientConnectionFactory primaryFCCF() {
        List<AbstractClientConnectionFactory> tcpNetClientConnectionFactoryList = new ArrayList<>();
        tcpNetClientConnectionFactoryList.add(
                getTcpNetClientConnectionFactory(tcpServerAddress, tcpServerPort));
        tcpNetClientConnectionFactoryList.add(
                getTcpNetClientConnectionFactory(tcpServerFailoverAddress, tcpServerFailoverPort));

        return getFailOverClientConnection(tcpNetClientConnectionFactoryList);
    }

    private TcpNetClientConnectionFactory getTcpNetClientConnectionFactory(String address, int port) {
        TcpNetClientConnectionFactory tcpNetClientConnectionFactory =
                new TcpNetClientConnectionFactory(address, port);
        tcpNetClientConnectionFactory.setTcpSocketFactorySupport(new DefaultTcpNetSocketFactorySupport());
        tcpNetClientConnectionFactory.setSoKeepAlive(true);
        tcpNetClientConnectionFactory.setSingleUse(false);
        tcpNetClientConnectionFactory.setSoTcpNoDelay(true);
        tcpNetClientConnectionFactory.setDeserializer(TcpCodecs.lengthHeader1());
        tcpNetClientConnectionFactory.setApplicationEventPublisher(this);
        return tcpNetClientConnectionFactory;
    }

    private static FailoverClientConnectionFactory getFailOverClientConnection(
            List<AbstractClientConnectionFactory> tcpNetClientConnectionFactoryList) {
        var failoverClientConnectionFactory =
                new FailoverClientConnectionFactory(tcpNetClientConnectionFactoryList);
        return failoverClientConnectionFactory;
    }

    @EventListener
    public void listen(TcpConnectionOpenEvent event) {
        log.info("Event Received");
    }

    @SneakyThrows
    @Override
    public void publishEvent(ApplicationEvent event) {
        log.info("appEvent: " + event.getClass().getName());
        if (event.getClass().getName().equals(TcpConnectionOpenEvent.class.getCanonicalName())) {
            log.info("Connection Opened");

        }
        if (event.getClass().getName().equals(TcpConnectionCloseEvent.class.getCanonicalName())) {
            log.info("Connection Closed");
            Thread.sleep(1000);
            String messagePayload = "Hello, TCP server!";
            Message<byte[]> message = MessageBuilder.withPayload(messagePayload.getBytes()).build();
            primaryMessageChannel.send(message);
            secondaryMessageChannel.send(message);
            log.info("Message sent to TCP server: " + messagePayload);
        }
        if (event.getClass().getName().equals(TcpConnectionExceptionEvent.class.getCanonicalName())) {
            log.info("Connection Exception");
        }
        ApplicationEventPublisher.super.publishEvent(event);
    }

    @Override
    public void publishEvent(Object event) {
        log.info("appEvent: " + event.getClass().getName());
    }
}
