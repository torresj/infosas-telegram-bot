package com.torresj.infosas.telegrambot.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torresj.infosas.telegrambot.bots.InfosasBot;
import com.torresj.infosas.telegrambot.models.MessageType;
import com.torresj.infosas.telegrambot.models.UpdateResultMessage;
import com.torresj.infosas.telegrambot.services.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConsumerService implements ConsumerService {

    private final ObjectMapper objectMapper;
    private final InfosasBot infosasBot;

    @RabbitListener(queues = "resultsQueue")
    @Override
    public void consumeMessage(String message) {
        try {
            log.info("Received result message {}", message);
            UpdateResultMessage updateResultMessage = objectMapper.readValue(message, UpdateResultMessage.class);
            if(updateResultMessage != null && updateResultMessage.getType().equals(MessageType.UPDATE)) {
                SendMessage messageToSend = SendMessage.builder()
                        .chatId(updateResultMessage.getChatId())
                        .text("Infosas data updated in " + updateResultMessage.getDuration())
                        .build();
                infosasBot.sendMessage(messageToSend);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
