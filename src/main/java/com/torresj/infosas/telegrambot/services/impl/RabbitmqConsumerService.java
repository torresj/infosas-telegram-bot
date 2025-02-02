package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.services.NotificationHandleMessageService;
import com.torresj.infosas.telegrambot.services.ResultHandleMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConsumerService {

    private final ResultHandleMessageService resultHandleMessageService;
    private final NotificationHandleMessageService notificationHandleMessageService;

    @RabbitListener(queues = "resultsQueue")
    public void consumeResultMessage(String message) {
        log.info("Received result message {}", message);
        resultHandleMessageService.handleMessage(message);
    }

    @RabbitListener(queues = "notificationsQueue")
    public void consumeNotificationMessage(String message) {
        log.info("Received notification message {}", message);
        notificationHandleMessageService.handleMessage(message);
    }
}
