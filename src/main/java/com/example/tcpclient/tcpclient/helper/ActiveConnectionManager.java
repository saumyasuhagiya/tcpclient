package com.example.tcpclient.tcpclient.helper;

import com.example.tcpclient.tcpclient.service.ServerSelectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class ActiveConnectionManager {

    @Autowired
    MessageChannel primaryMessageChannel;

    @Autowired
    MessageChannel secondaryMessageChannel;

    @Autowired
    ActiveConnectionListener activeConnectionListener;

    @Scheduled(fixedRate = 10000)
    public void checkConnectionHealth() {
        if(!activeConnectionListener.isPrimaryActive()) {
            ServerSelectionService.getInstance().setActiveServer("secondary");
            log.warn("Primary connection is not active. Falling back to Secondary. Trying to activate primary connection.");
            sendMessage(primaryMessageChannel);
        } else {
            log.info("Primary connection is active.");
        }
        if(!activeConnectionListener.isSecondaryActive()) {
            ServerSelectionService.getInstance().setActiveServer("primary");
            log.warn("Secondary connection is not active. Falling back to Primary. Trying to activate secondary connection.");
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
            log.info("Message sent to {} TCP server: {} ", ((DirectChannel) channel).getBeanName(), messagePayload);
        } catch (Exception e) {
            log.error("Error sending message to {} TCP server: {} ", ((DirectChannel) channel).getBeanName(), e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void fallBackToDefault() {
        if(!ServerSelectionService.getInstance().checkIfRunningDefaultServer() && activeConnectionListener.isPrimaryActive()) {
            log.error("Default Server is Active now. Falling back to default server stream.");
            ServerSelectionService.getInstance().setActiveServer(ServerSelectionService.getInstance().getDefaultServer());
        }
    }
}
