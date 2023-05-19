package com.example.tcpclient.tcpclient.helper;

import com.example.tcpclient.tcpclient.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.example.tcpclient.tcpclient.Constant.IP_TCP_REMOTE_PORT;

@Component
public class ActiveConnectionListener implements ChannelInterceptor {


    @Autowired
    ServerConfig serverConfig;

    public boolean isPrimaryActive() {
        return (System.currentTimeMillis() -
                serverConfig.getServerMap().get("primary").getLastDataReceived()) < 5000;
    }

    public boolean isSecondaryActive() {
        return (System.currentTimeMillis() -
                serverConfig.getServerMap().get("secondary").getLastDataReceived()) < 5000;
    }

    @Override
    public void postSend(org.springframework.messaging.Message<?> message, MessageChannel channel, boolean sent) {
        if (message.getHeaders().containsKey(IP_TCP_REMOTE_PORT) && message.getHeaders().get(IP_TCP_REMOTE_PORT) != null) {
            if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString()
                    .equals(String.valueOf(serverConfig.getServerMap().get("primary").getPort()))) {
                serverConfig.getServerMap().get("primary").setLastDataReceived(System.currentTimeMillis());
            } else if (message.getHeaders().get(IP_TCP_REMOTE_PORT).toString()
                    .equals(String.valueOf(serverConfig.getServerMap().get("secondary").getPort()))) {
                serverConfig.getServerMap().get("secondary").setLastDataReceived(System.currentTimeMillis());
            }
        }
    }

}
