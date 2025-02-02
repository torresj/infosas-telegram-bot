package com.torresj.infosas.telegrambot.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torresj.infosas.telegrambot.bots.InfosasBot;
import com.torresj.infosas.telegrambot.models.NotificationMessage;
import com.torresj.infosas.telegrambot.services.NotificationHandleMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationHandleMessageServiceImpl implements NotificationHandleMessageService {

    private final ObjectMapper objectMapper;
    private final InfosasBot infosasBot;

    @Override
    public void handleMessage(String message) {
        try {
            NotificationMessage notificationMessage = objectMapper.readValue(message, NotificationMessage.class);
            SendMessage messageToSend = SendMessage.builder()
                    .chatId(notificationMessage.getChatId())
                    .text(notificationMessage.getText())
                    .build();
            infosasBot.sendMessage(messageToSend);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
