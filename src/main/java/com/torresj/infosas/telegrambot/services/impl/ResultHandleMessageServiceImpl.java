package com.torresj.infosas.telegrambot.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.torresj.infosas.telegrambot.bots.InfosasBot;
import com.torresj.infosas.telegrambot.models.MetricsResultMessage;
import com.torresj.infosas.telegrambot.models.QueueMessage;
import com.torresj.infosas.telegrambot.models.StatusesResultMessage;
import com.torresj.infosas.telegrambot.models.UpdateResultMessage;
import com.torresj.infosas.telegrambot.models.UrlType;
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
                case STATUS -> handleStatusesResultMessage(objectMapper.readValue(message, StatusesResultMessage.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateResultMessage(UpdateResultMessage updateResultMessage) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(updateResultMessage.getChatId())
                .text("Datos de Infosas actualizados en " + updateResultMessage.getDuration())
                .build();
        infosasBot.sendMessage(messageToSend);
    }

    private void handleMetricsResultMessage(MetricsResultMessage metricsResultMessage) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(metricsResultMessage.getChatId())
                .text(createMetricsTable(metricsResultMessage))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        infosasBot.sendMessage(messageToSend);
    }

    private String createMetricsTable(MetricsResultMessage metricsResultMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append("```\n");
        builder.append("| ENTIDAD              | CANTIDAD \n");
        builder.append("----------------------------------\n");
        builder.append("| Personas             | ").append(metricsResultMessage.getUsers()).append("\n");
        builder.append("| Oposiciones          | ").append(metricsResultMessage.getExams()).append("\n");
        builder.append("| Bolsas               | ").append(metricsResultMessage.getJobBanks()).append("\n");
        builder.append("| Bolsas específicas   | ").append(metricsResultMessage.getSpecificJobBanks()).append("\n");
        builder.append("```");
        return builder.toString();
    }

    private void handleStatusesResultMessage(StatusesResultMessage statusesResultMessage) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(statusesResultMessage.getChatId())
                .text(createStatusesTable(statusesResultMessage))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        infosasBot.sendMessage(messageToSend);
    }

    private String createStatusesTable(StatusesResultMessage statusesResultMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append("```\n");
        builder.append("  Listas provisionales ❌ / definitivas ✅ \n\n");
        builder.append("|------------------------------------------------|\n");
        statusesResultMessage.getStatuses().forEach( urlStatus ->
                builder.append("| ")
                        .append(formatTypeName(42,urlStatus.getType()))
                        .append(urlStatus.isProvisional() ? "| ❌ |" : "| ✅ |")
                        .append("\n")
                        .append("|------------------------------------------------|\n")
        );
        builder.append("```");
        return builder.toString();
    }

    private String formatTypeName(int size, UrlType urlType) {
        String typeNameInSpanish = switch (urlType){
            case TCAE_JOB_BANK -> "Bolsa TCAE";
            case TEAP_JOB_BANK -> "Bolsa TEAP";
            case TEDN_JOB_BANK -> "Bolsa TEDN";
            case TEDS_JOB_BANK -> "Bolsa TEDS";
            case TEL_JOB_BANK -> "Bolsa TEL";
            case TEMN_JOB_BANK -> "Bolsa TEMN";
            case TERD_JOB_BANK -> "Bolsa TERD";
            case TER_JOB_BANK -> "Bolsa TER";
            case TF_JOB_BANK -> "Bolsa TF";
            case NURSE_JOB_BANK -> "Bolsa enfemero/a";
            case FISIO_JOB_BANK -> "Bolsa fisioterapeuta";
            case SPEECH_THERAPIST_JOB_BANK -> "Bolsa logopeda";
            case OCCUPATIONAL_THERAPY_JOB_BANK -> "Bolsa terapeuta ocupacional";
            case NURSE_FAMILY_JOB_BANK -> "Bolsa esp. en enfermería familiar";
            case NURSE_GYNECOLOGY_JOB_BANK -> "Bolsa esp. en enfermería ginecológica";
            case NURSE_PEDIATRICIAN_JOB_BANK -> "Bolsa esp. en enfermería pediátrica";
            case NURSE_MENTAL_HEALTH_JOB_BANK -> "Bolsa esp. en enfermería de salud mental";
            case NURSE_WORK_JOB_BANK -> "Bolsa esp. en enfermería del trabajo";
            case NURSE_CRITICS_SPECIFIC_JOB_BANK -> "Bolsa enfermería en cuidados críticos";
            case NURSE_DIALYSIS_SPECIFIC_JOB_BANK -> "Bolsa enfermería en dialisis";
            case NURSE_MENTAL_HEALTH_SPECIFIC_JOB_BANK -> "Bolsa enfermería en salud mental";
            case NURSE_NEONATES_SPECIFIC_JOB_BANK -> "Bolsa enfermería en neonatos";
            case NURSE_NUCLEAR_SPECIFIC_JOB_BANK -> "Bolsa enfermería en medicina nuclear";
            case NURSE_SURGERY_ROOM_SPECIFIC_JOB_BANK -> "Bolsa enfemería en quirófano";
            case NURSE_EXAM -> "Oposición en enfemería";
            case TCAE_EXAM -> "Oposición en TCAE";
        };
        return String.format("%-" + size + "s", typeNameInSpanish);
    }
}
