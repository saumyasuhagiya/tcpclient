package com.example.tcpclient.tcpclient.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class SchedulerHelper {

    @Autowired
    MessageChannel primaryMessageChannel;

    @Autowired
    MessageChannel secondaryMessageChannel;

    @Autowired
    ActiveConnectionMonitor activeConnectionMonitor;

    @Scheduled(fixedRate = 10000)
    public void printOnSchedule() {
        if(!activeConnectionMonitor.isPrimaryActive()) {
            log.error("Primary connection is not active. Trying to activate primary connection.");
            sendMessage(primaryMessageChannel);
        } else {
            log.info("Primary connection is active.");
        }
        if(!activeConnectionMonitor.isSecondaryActive()) {
            log.error("Secondary connection is not active. Trying to activate secondary connection.");
            sendMessage(secondaryMessageChannel);
        } else {
            log.info("Secondary connection is active.");
        }
    }

    private void sendMessage(MessageChannel channel) {
        try {
            Thread.sleep(1000);
            String messagePayload = "Hello, TCP server!";
            Message<byte[]> message = MessageBuilder.withPayload(messagePayload.getBytes()).build();
            channel.send(message);
            log.info("Message sent to TCP server: " + messagePayload);
        } catch (Exception e) {
            log.error("Error sending message to TCP server: " + e.getMessage());
        }
    }
}
