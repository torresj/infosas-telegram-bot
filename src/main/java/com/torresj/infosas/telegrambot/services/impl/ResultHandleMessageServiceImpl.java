package com.torresj.infosas.telegrambot.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torresj.infosas.telegrambot.bots.InfosasBot;
import com.torresj.infosas.telegrambot.models.MetricsResultMessage;
import com.torresj.infosas.telegrambot.models.QueueMessage;
import com.torresj.infosas.telegrambot.models.UpdateResultMessage;
import com.torresj.infosas.telegrambot.services.ResultHandleMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultHandleMessageServiceImpl implements ResultHandleMessageService {

    private final ObjectMapper objectMapper;
    private final InfosasBot infosasBot;

    @Override
    public void handleMessage(String message) {
        try {
            QueueMessage queueMessage = objectMapper.readValue(message, QueueMessage.class);
            switch (queueMessage.getType()) {
                case UPDATE -> handleUpdateResultMessage(objectMapper.readValue(message, UpdateResultMessage.class));
                case METRICS -> handleMetricsResultMessage(objectMapper.readValue(message, MetricsResultMessage.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateResultMessage(UpdateResultMessage updateResultMessage) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(updateResultMessage.getChatId())
                .text("Datod de Infosas actualizados en " + updateResultMessage.getDuration())
                .build();
        infosasBot.sendMessage(messageToSend);
    }

    private void handleMetricsResultMessage(MetricsResultMessage metricsResultMessage) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(metricsResultMessage.getChatId())
                .text(createMetricsTable(metricsResultMessage))
                .parseMode(ParseMode.HTML)
                .build();
        infosasBot.sendMessage(messageToSend);
    }

    private String createMetricsTable(MetricsResultMessage metricsResultMessage) {
        return "<b> ENTIDAD                 |   CANTIDAD</b>\n" +
                "------------------------------------\n" +
                "<b>Persona</b>                   |    " + metricsResultMessage.getUsers() + "\n" +
                "<b>Oposicion</b>                |    " + metricsResultMessage.getExams() + "\n" +
                "<b>Bolsa</b>                        |    " + metricsResultMessage.getJobBanks() + "\n" +
                "<b>Bolsa específicas</b>   |    " + metricsResultMessage.getSpecificJobBanks() + "\n";
    }
}
