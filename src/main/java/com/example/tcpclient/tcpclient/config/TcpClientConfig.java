package com.example.tcpclient.tcpclient.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import lombok.extern.slf4j.Slf4j;

@ComponentScan
@Configuration
@Slf4j
public class TcpClientConfig{

  @Bean
  public MessageChannel primaryMessageChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel secondaryMessageChannel() {
    return new DirectChannel();
  }

  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata defaultPoller() {
    PollerMetadata pollerMetadata = new PollerMetadata();
    pollerMetadata.setTrigger(new PeriodicTrigger(0));
    return pollerMetadata;
  }

  @Bean
  public TcpReceivingChannelAdapter inPrimary(AbstractClientConnectionFactory primaryFCCF) {
    TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
    adapter.setConnectionFactory(primaryFCCF);
    adapter.setOutputChannel(primaryMessageChannel());
    //adapter.setClientMode(true);
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "primaryMessageChannel")
  public TcpSendingMessageHandler outPrimary(AbstractClientConnectionFactory primaryFCCF) {
    TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
    tcpSendingMessageHandler.setConnectionFactory(primaryFCCF);
    tcpSendingMessageHandler.setLoggingEnabled(true);
    return tcpSendingMessageHandler;
  }

  @Bean
  public TcpReceivingChannelAdapter inSecondary(AbstractClientConnectionFactory secondaryFCCF) {
    TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
    adapter.setConnectionFactory(secondaryFCCF);
    adapter.setOutputChannel(primaryMessageChannel());
    //adapter.setClientMode(true);
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "secondaryMessageChannel")
  public TcpSendingMessageHandler outSecondary(AbstractClientConnectionFactory secondaryFCCF) {
    TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
    tcpSendingMessageHandler.setConnectionFactory(secondaryFCCF);
    tcpSendingMessageHandler.setLoggingEnabled(true);
    return tcpSendingMessageHandler;
  }
  @EventListener
  public void connectionEvent(TcpConnectionEvent event) {
    log.info("Event received.. {}", event);
  }
}
