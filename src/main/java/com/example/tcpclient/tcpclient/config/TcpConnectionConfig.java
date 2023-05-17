package com.example.tcpclient.tcpclient.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.DefaultTcpNetSocketFactorySupport;
import org.springframework.integration.ip.tcp.connection.FailoverClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;


@Configuration
public class TcpConnectionConfig {

  //This is for testing purpose only. Actual server resides on external server.
  private String tcpServerAddress = "localhost";

  private int tcpServerPort = 8888;

  @Bean
  public AbstractClientConnectionFactory clientConnectionFactory() {
    List<AbstractClientConnectionFactory> tcpNetClientConnectionFactoryList = new ArrayList<>();
    /*tcpNetClientConnectionFactoryList.add(
    getTcpNetClientConnectionFactory(tcpServerFailoverAddress, tcpServerFailoverPort));*/
    tcpNetClientConnectionFactoryList.add(
        getTcpNetClientConnectionFactory(tcpServerAddress, tcpServerPort));

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
    return tcpNetClientConnectionFactory;
  }

  private static FailoverClientConnectionFactory getFailOverClientConnection(
      List<AbstractClientConnectionFactory> tcpNetClientConnectionFactoryList) {
    var failoverClientConnectionFactory =
        new FailoverClientConnectionFactory(tcpNetClientConnectionFactoryList);
    return failoverClientConnectionFactory;
  }
}
