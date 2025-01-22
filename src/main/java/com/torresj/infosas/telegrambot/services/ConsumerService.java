package com.torresj.infosas.telegrambot.services;

import org.springframework.amqp.core.Message;

public interface ConsumerService {
    void consumeMessage(String message);
}
