package com.example.tcpclient.tcpclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.connection.*;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class TcpConnectionListener implements ApplicationEventPublisher {

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void publishEvent(ApplicationEvent event) {
        System.out.println("appEvent: " + event.getClass().getName());
        if (event.getClass().getName().equals(TcpConnectionOpenEvent.class.getCanonicalName())) {
            System.out.println("Connection Opened");
        }
        if (event.getClass().getName().equals(TcpConnectionCloseEvent.class.getCanonicalName())) {
            System.out.println("Connection Closed");
        }
        if (event.getClass().getName().equals(TcpConnectionExceptionEvent.class.getCanonicalName())) {
            System.out.println("Connection Exception");
        }
        ApplicationEventPublisher.super.publishEvent(event);

    }

    @Override
    public void publishEvent(Object event) {
        System.out.println("objectEvent: " + event.getClass());
    }
}
