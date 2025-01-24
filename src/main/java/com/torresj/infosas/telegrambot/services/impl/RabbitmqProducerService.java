package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.models.QueueMessage;
import com.torresj.infosas.telegrambot.services.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqProducerService implements ProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(QueueMessage message) {
        rabbitTemplate.convertAndSend("commandsExchange",message.getType().name(), message);
    }
}
