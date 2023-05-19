package com.example.tcpclient.tcpclient.service;

import org.springframework.stereotype.Service;

//create singleton class to store active server
public class ServerSelectionService {

    private static ServerSelectionService instance = null;

    private String activeServer = "primary";

    private String defaultServer = "primary";

    private ServerSelectionService() {

    }

    public static ServerSelectionService getInstance() {
        if (instance == null) {
            instance = new ServerSelectionService();
        }
        return instance;
    }

    public String getActiveServer() {
        return activeServer;
    }

    public String getDefaultServer() {
        return defaultServer;
    }

    public void setActiveServer(String serverName) {
        activeServer = serverName;
    }

    public void setDefaultServer(String serverName) {
        defaultServer = serverName;
    }

    public boolean checkIfRunningDefaultServer() {
        return activeServer.equals(defaultServer);
    }
}
