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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
                case START -> startHandler(message.getChatId());
                case UPDATE -> updateHandler(message.getChatId(), message.getFrom().getUserName());
                case GET_METRICS -> getMetricsHandler(message.getChatId());
            };
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return defaultHandler(message.getChatId());
        }
    }

    @Override
    public SendMessage handleMessage(long chatId, String receivedMessage, String username) {
        try {
            String messageText = getCommandKey(receivedMessage);

            CommandType command = CommandType.fromString(messageText);
            return switch (command) {
                case START -> startHandler(chatId);
                case UPDATE -> updateHandler(chatId, username);
                case GET_METRICS -> getMetricsHandler(chatId);
            };
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return defaultHandler(chatId);
        }
    }

    private SendMessage defaultHandler(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Unrecognized command, try again")
                .replyMarkup(getInlineKeyboard())
                .build();
    }

    private SendMessage startHandler(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Welcome to Infosas Telegram Bot")
                .replyMarkup(getInlineKeyboard())
                .build();
    }

    private SendMessage updateHandler(long chatId, String username) {
        if(isAdmin(username)){
            producerService.sendMessage(
                    QueueMessage.builder()
                        .type(MessageType.UPDATE)
                        .chatId(chatId)
                        .build()
            );
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("infosas is updating all the data, waiting for it to finish ...")
                    .build();
        }else{
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("User is not admin. Command not allowed")
                    .replyMarkup(getInlineKeyboard())
                    .build();
        }
    }

    private SendMessage getMetricsHandler(long chatId) {
        producerService.sendMessage(
                QueueMessage.builder()
                    .type(MessageType.METRICS)
                    .chatId(chatId)
                    .build()
        );
        return SendMessage.builder()
                .chatId(chatId)
                .text("Getting metrics ...")
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText(Commands.getCommand(START).label());
        startButton.setCallbackData(Commands.getCommand(START).command());

        InlineKeyboardButton getMetricsButton = new InlineKeyboardButton();
        getMetricsButton.setText(Commands.getCommand(GET_METRICS).label());
        getMetricsButton.setCallbackData(Commands.getCommand(GET_METRICS).command());

        InlineKeyboardButton updateButton = new InlineKeyboardButton();
        updateButton.setText(Commands.getCommand(UPDATE).label());
        updateButton.setCallbackData(Commands.getCommand(UPDATE).command());

        List<InlineKeyboardButton> rowInlineOne = new ArrayList<>();
        rowInlineOne.add(startButton);
        rowInlineOne.add(getMetricsButton);

        List<InlineKeyboardButton> rowInlineTwo = new ArrayList<>();
        rowInlineTwo.add(updateButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(rowInlineOne);
        rowsInline.add(rowInlineTwo);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
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
