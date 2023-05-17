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

        if (message.getHeaders().containsKey("ip_tcp_remotePort")) {
            if (message.getHeaders().get("ip_tcp_remotePort").toString().equals("8888")) {
                log.info("Sending data from primary server to subscribers");
            } else if (message.getHeaders().get("ip_tcp_remotePort").toString().equals("9999")) {
                log.info("Sending data from secondary server to subscribers");
            }
        }
        log.info("{}", data);
    }
}