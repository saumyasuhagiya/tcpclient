package com.example.tcpclient.tcpclient.helper;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.example.tcpclient.tcpclient.Constant.IP_TCP_REMOTE_PORT;

@Component
public class ActiveConnectionListener implements ChannelInterceptor {
    private long lastMessageReceivedPrimary;
    private long lastMessageReceivedSecondary;

    public ActiveConnectionListener() {
        lastMessageReceivedPrimary = 0;
        lastMessageReceivedSecondary = 0;
    }

    public boolean isPrimaryActive() {
        return (System.currentTimeMillis() - lastMessageReceivedPrimary) < 60000;
    }

    public boolean isSecondaryActive() {
        return (System.currentTimeMillis() - lastMessageReceivedSecondary) < 60000;
    }

    @Override
    public void postSend(org.springframework.messaging.Message<?> message, MessageChannel channel, boolean sent) {
        if (message.getHeaders().containsKey(IP_TCP_REMOTE_PORT)) {
            if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString().equals("8888")) {
                lastMessageReceivedPrimary = System.currentTimeMillis();
            } else if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString().equals("9999")) {
                lastMessageReceivedSecondary = System.currentTimeMillis();
            }
        }
    }

}
