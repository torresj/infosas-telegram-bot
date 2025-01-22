package com.torresj.infosas.telegrambot.services.impl;

import com.torresj.infosas.telegrambot.models.CommandType;
import com.torresj.infosas.telegrambot.models.Commands;
import com.torresj.infosas.telegrambot.models.MessageType;
import com.torresj.infosas.telegrambot.models.QueueMessage;
import com.torresj.infosas.telegrambot.services.ProducerService;
import com.torresj.infosas.telegrambot.services.TelegramHandlerMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.torresj.infosas.telegrambot.models.BotCommandInfo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.torresj.infosas.telegrambot.models.CommandType.GET_METRICS;
import static com.torresj.infosas.telegrambot.models.CommandType.START;
import static com.torresj.infosas.telegrambot.models.CommandType.UPDATE;
import static com.torresj.infosas.telegrambot.models.Commands.COMMANDS;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramHandlerMessageServiceImpl implements TelegramHandlerMessageService {

    private final ProducerService producerService;

    private final String ADMIN_USER = "torresjb";

    @Override
    public SendMessage handleMessage(Message message) {
        try {
            String messageText = getCommandKey(message.getText());

            CommandType command = CommandType.fromString(messageText);
            return switch (command) {
                case START -> startHandler(message);
                case UPDATE -> updateHandler(message);
                case GET_METRICS -> getMetricsHandler(message);
            };
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return defaultHandler(message);
        }
    }

    private SendMessage defaultHandler(Message message) {
        StringBuilder commands_list = new StringBuilder("Unrecognized command\n");
        commands_list.append("Available commands:\n");
        for (BotCommandInfo info : COMMANDS.values()) {
            commands_list.append(info.command())
                    .append(" -> ")
                    .append(info.description())
                    .append("\n");
        }

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(commands_list.toString())
                .replyMarkup(getCommandKeyboard())
                .build();
    }

    private SendMessage startHandler(Message message) {
        StringBuilder commands_list = new StringBuilder("Welcome to Infosas Telegram Bot\n");
        commands_list.append("Available commands:\n");
        for (BotCommandInfo info : COMMANDS.values()) {
            commands_list.append(info.command())
                    .append(" -> ")
                    .append(info.description())
                    .append("\n");
        }

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(commands_list.toString())
                .replyMarkup(getCommandKeyboard())
                .build();
    }

    private SendMessage updateHandler(Message message) {
        if(isAdmin(message.getFrom().getUserName())){
            producerService.sendMessage(
                    QueueMessage.builder()
                        .type(MessageType.UPDATE)
                        .chatId(message.getChatId())
                        .build()
            );
            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text("Updated")
                    .build();
        }else{
            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text("User is not admin. Command not allowed")
                    .build();
        }
    }

    private SendMessage getMetricsHandler(Message message) {
        producerService.sendMessage(
                QueueMessage.builder()
                    .type(MessageType.METRICS)
                    .chatId(message.getChatId())
                    .build()
        );
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Metrics")
                .build();
    }

    private ReplyKeyboardMarkup getCommandKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(Commands.getCommand(START).label());
        firstRow.add(Commands.getCommand(GET_METRICS).label());
        keyboard.add(firstRow);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(Commands.getCommand(UPDATE).label());
        keyboard.add(secondRow);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private String getCommandKey(String message){
        String messageText = message.trim();

        Optional<String> optionalCommand = COMMANDS.values().stream()
                .filter(info -> info.label().equalsIgnoreCase(messageText))
                .map(BotCommandInfo::command)
                .findFirst();

        String commandKey;

        if (optionalCommand.isPresent()) {
            commandKey = optionalCommand.get().toLowerCase();
        } else if (messageText.startsWith("/")) {
            commandKey = messageText.split(" ")[0].toLowerCase();
        } else {
            commandKey = messageText.toLowerCase();
        }

        return commandKey;
    }

    private boolean isAdmin(String user){
        return ADMIN_USER.equals(user);
    }
}
