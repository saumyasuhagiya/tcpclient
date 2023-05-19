package com.example.tcpclient.tcpclient.service;

import java.nio.charset.StandardCharsets;

import com.example.tcpclient.tcpclient.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.example.tcpclient.tcpclient.Constant.IP_TCP_REMOTE_PORT;

@Slf4j
@Getter
@MessageEndpoint
public class TcpDataReceiver {

    @Autowired
    ServerConfig serverConfig;

    @ServiceActivator(inputChannel = "primaryMessageChannel")
    public void replyHandler(Message message) {

        String data = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);

        if (message.getHeaders().containsKey(IP_TCP_REMOTE_PORT) && message.getHeaders().get(IP_TCP_REMOTE_PORT) != null) {
            if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString()
                    .equals(String.valueOf(serverConfig.getServerMap().get("primary").getPort()))
                    && ServerSelectionService.getInstance().getActiveServer().equals("primary")) {
                log.info("Sending data from primary server to subscribers");
            } else if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString()
                    .equals(String.valueOf(serverConfig.getServerMap().get("secondary").getPort()))
                    && ServerSelectionService.getInstance().getActiveServer().equals("secondary")) {
                log.info("Sending data from secondary server to subscribers");
            }
        }
        log.info("{}", data);
    }
}