package com.example.tcpclient.tcpclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;

@EnableIntegration
@Component
@Slf4j
public class TcpMessageSender {

    @Autowired
    MessageChannel primaryMessageChannel;

    @Autowired
    MessageChannel secondaryMessageChannel;


    /*@Override*/
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Thread.sleep(1000);
            String messagePayload = "Hello, TCP server!";
            Message<byte[]> message = MessageBuilder.withPayload(messagePayload.getBytes()).build();
            primaryMessageChannel.send(message);
            Thread.sleep(1000);
            secondaryMessageChannel.send(message);
            log.info("Message sent to TCP server: " + messagePayload);
        } catch (Exception e) {
            log.error("Error sending message to TCP server: " + e.getMessage());
        }
    }
}
