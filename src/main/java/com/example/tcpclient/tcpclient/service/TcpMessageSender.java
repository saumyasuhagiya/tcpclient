package com.example.tcpclient.tcpclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class TcpMessageSender implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    MessageChannel primaryMessageChannel;

    @Autowired
    MessageChannel secondaryMessageChannel;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String messagePayload = "Hello, TCP server!";
        Message<byte[]> message = MessageBuilder.withPayload(messagePayload.getBytes()).build();
        primaryMessageChannel.send(message);
        secondaryMessageChannel.send(message);
        System.out.println("Message sent to TCP server: " + messagePayload);
    }
}
