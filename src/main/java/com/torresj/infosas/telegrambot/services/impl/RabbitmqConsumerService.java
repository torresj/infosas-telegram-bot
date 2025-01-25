package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.services.ConsumerService;
import com.torresj.infosas.telegrambot.services.ResultHandleMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConsumerService implements ConsumerService {

    private final ResultHandleMessageService resultHandleMessageService;

    @RabbitListener(queues = "resultsQueue")
    @Override
    public void consumeMessage(String message) {
        log.info("Received result message {}", message);
        resultHandleMessageService.handleMessage(message);
    }
}
