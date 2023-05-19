package com.example.tcpclient.tcpclient.config;

import com.example.tcpclient.tcpclient.model.ServerConnection;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ServerConfig {
    private ServerConnection primaryServer;

    private ServerConnection secondaryServer;

    @Getter
    private Map<String, ServerConnection> serverMap = new HashMap<>(2);

    public ServerConfig() {
        primaryServer = new ServerConnection("localhost", 8888, 0L);
        secondaryServer = new ServerConnection("localhost", 9999, 0L);
        serverMap.put("primary", primaryServer);
        serverMap.put("secondary", secondaryServer);
    }
}
