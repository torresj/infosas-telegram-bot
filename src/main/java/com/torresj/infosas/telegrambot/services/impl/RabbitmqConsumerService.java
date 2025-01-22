package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.services.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConsumerService implements ConsumerService {

    @RabbitListener(queues = {"updateStatusQueue","metricsQueue"})
    @Override
    public void consumeMessage(String message) {
        log.info("Received update status message {}", message);
    }
}
