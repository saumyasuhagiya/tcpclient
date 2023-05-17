package com.example.tcpclient.tcpclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class TcpMessageSender implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    TcpSendingMessageHandler tcpSendingMessageHandler;
    private final MessageChannel tcpClientChannel;

    public TcpMessageSender(@Qualifier("messageChannel") MessageChannel messageChannel) {
        this.tcpClientChannel = messageChannel;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String messagePayload = "Hello, TCP server!";
        Message<byte[]> message = MessageBuilder.withPayload(messagePayload.getBytes()).build();
        tcpClientChannel.send(message);
        System.out.println("Message sent to TCP server: " + messagePayload);
    } 
}
