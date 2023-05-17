package com.example.tcpclient.tcpclient.config;

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
public class TcpClientConfig {

  @Bean
  public MessageChannel messageChannel() {
    return new DirectChannel();
  }

  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata defaultPoller() {
    PollerMetadata pollerMetadata = new PollerMetadata();
    pollerMetadata.setTrigger(new PeriodicTrigger(0));
    return pollerMetadata;
  }

  @Bean
  public TcpReceivingChannelAdapter in(AbstractClientConnectionFactory connectionFactory) {
    TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
    adapter.setConnectionFactory(connectionFactory);
    adapter.setOutputChannel(messageChannel());
    //adapter.setClientMode(true);
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "messageChannel")
  public TcpSendingMessageHandler out(AbstractClientConnectionFactory connectionFactory) {
    TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
    tcpSendingMessageHandler.setConnectionFactory(connectionFactory);
    tcpSendingMessageHandler.setLoggingEnabled(true);
    return tcpSendingMessageHandler;
  }

  @EventListener
  public void connectionEvent(TcpConnectionEvent event) {
    log.info("Event received.. {}", event);
  }

  @Bean
  public ApplicationEventListeningMessageProducer eventsAdapter(
          MessageChannel messageChannel, MessageChannel errorChannel) {

    ApplicationEventListeningMessageProducer producer =
            new ApplicationEventListeningMessageProducer();
    producer.setEventTypes(TcpConnectionEvent.class);
    producer.setOutputChannel(messageChannel);
    producer.setErrorChannel(errorChannel);
    return producer;
  }

}
