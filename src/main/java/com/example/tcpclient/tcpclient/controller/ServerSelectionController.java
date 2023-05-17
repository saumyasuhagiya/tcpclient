package com.example.tcpclient.tcpclient.controller;

import com.example.tcpclient.tcpclient.service.ServerSelectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerSelectionController {


    @PostMapping("/server")
    public String setActiveServer(@RequestBody String serverName) {
        ServerSelectionService.getInstance().setActiveServer(serverName);
        return serverName;
    }

    @GetMapping("/server")
    public String getActiveServer() {
        return ServerSelectionService.getInstance().getActiveServer();
    }
}
