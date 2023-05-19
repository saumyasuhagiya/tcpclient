package com.example.tcpclient.tcpclient.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerConnection {
    private String ip;
    private int port;
}
