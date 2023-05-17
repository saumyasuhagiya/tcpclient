package com.example.tcpclient.tcpclient.service;

import com.example.tcpclient.tcpclient.config.TcpConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class TcpConnectionListner implements ApplicationEventPublisher {


    @Override
    public void publishEvent(ApplicationEvent event) {
        System.out.println("appEvent: " + event.getClass().getName());
        if (event.getClass().getName().equals(TcpConnectionOpenEvent.class.getCanonicalName())) {
            System.out.println("Connection Opened");
        }
        if (event.getClass().getName().equals(TcpConnectionCloseEvent.class.getCanonicalName())) {
            System.out.println("Connection Closed");
        }
        ApplicationEventPublisher.super.publishEvent(event);
    }

    @Override
    public void publishEvent(Object event) {
        System.out.println("objectEvent: " + event.getClass());
    }
}
