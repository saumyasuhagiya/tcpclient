package com.example.tcpclient.tcpclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import lombok.extern.slf4j.Slf4j;

@ComponentScan
@Configuration
@Slf4j
public class TcpClientConfig {

  @Bean
  public PollableChannel responseChannel() {
    return new QueueChannel();
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
    adapter.setOutputChannel(responseChannel());
    return adapter;
  }

  @Bean
  //@ServiceActivator(inputChannel = "messageChannel")
  public TcpSendingMessageHandler out(AbstractClientConnectionFactory connectionFactory)
      throws InterruptedException {
    log.info(
        "Connecting to : {}:{}",
        connectionFactory.getConnection().getHostAddress(),
        connectionFactory.getConnection().getSocketInfo().getLocalPort());
    TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
    tcpSendingMessageHandler.setConnectionFactory(connectionFactory);
    tcpSendingMessageHandler.setLoggingEnabled(true);
    connectionFactory.registerSender(tcpSendingMessageHandler);
    tcpSendingMessageHandler.handleMessage(MessageBuilder.withPayload("payload").build());
    return tcpSendingMessageHandler;
  }
}
