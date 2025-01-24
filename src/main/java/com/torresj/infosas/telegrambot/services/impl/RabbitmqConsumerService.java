package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.services.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConsumerService implements ConsumerService {

    @RabbitListener(queues = "resultsQueue")
    @Override
    public void consumeMessage(String message) {
        log.info("Received result message {}", message);
    }
}
