package com.example.tcpclient.tcpclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerConnection {
    private String ip;
    private int port;
    private long lastDataReceived;
}
