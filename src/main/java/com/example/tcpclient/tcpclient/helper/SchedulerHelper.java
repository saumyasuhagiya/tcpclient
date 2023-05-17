package com.example.tcpclient.tcpclient.helper;

import com.example.tcpclient.tcpclient.service.TcpMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerHelper {

    @Autowired
    TcpMessageSender tcpMessageSender;

    @Scheduled(fixedRate = 10000)
    public void printOnSchedule() {
        System.out.println("Hello from SchedulerHelper");
        tcpMessageSender.onApplicationEvent(null);
    }
}
