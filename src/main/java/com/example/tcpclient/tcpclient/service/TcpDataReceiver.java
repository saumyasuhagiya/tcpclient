package com.example.tcpclient.tcpclient.service;

import java.nio.charset.StandardCharsets;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@MessageEndpoint
public class TcpDataReceiver {

  @ServiceActivator(inputChannel = "primaryMessageChannel")
  public void replyHandler(Message message) {

    String data = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);

    log.info(
        "PRIMARY: ConnectionId: {}, IP Address: {}, IP Hostname: {}, IP Port: {}",
        message.getHeaders().get("ip_connectionId"),
        message.getHeaders().get("ip_address"),
        message.getHeaders().get("ip_hostname"), message.getHeaders().get("ip_tcp_remotePort"));
    log.info("{}", data);
  }

  @ServiceActivator(inputChannel = "secondaryMessageChannel")
  public void replyHandlerSecondary(Message message) {

    String data = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);

    log.info(
            "SECONDARY: ConnectionId: {}, IP Address: {}, IP Hostname: {}, IP Port: {}",
            message.getHeaders().get("ip_connectionId"),
            message.getHeaders().get("ip_address"),
            message.getHeaders().get("ip_hostname"), message.getHeaders().get("ip_tcp_remotePort"));
    log.info("{}", data);
  }
}