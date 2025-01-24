package com.torresj.infosas.telegrambot.bots;

import com.torresj.infosas.telegrambot.services.TelegramHandlerMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.torresj.infosas.telegrambot.models.Commands.COMMANDS;


@Component
@Slf4j
public class InfosasBot extends TelegramLongPollingBot {

    private final TelegramHandlerMessageService telegramHandlerMessageService;

    private final String name;

    public InfosasBot(
            @Value("${telegram.bot.name}") String name,
            @Value("${telegram.bot.token}") String token,
            TelegramHandlerMessageService telegramHandlerMessageService,
            DefaultBotOptions options
    ){
        super(options, token);
        this.name = name;
        this.telegramHandlerMessageService = telegramHandlerMessageService;

        try {
            List<BotCommand> commands = COMMANDS.values().stream()
                    .map(info -> new BotCommand(info.command(), info.description()))
                    .toList();
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Failed to set bot commands: {}", e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
         log.info("Command received: {}", update.getMessage().getText());
         sendMessage(telegramHandlerMessageService.handleMessage(update.getMessage()));
        } else if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            log.info("Callback received: {}", callbackQuery.getData());
            sendMessage(
                    telegramHandlerMessageService.handleMessage(
                            callbackQuery.getMessage().getChatId(),
                            callbackQuery.getData(),
                            callbackQuery.getFrom().getUserName()
                    )
            );
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId: {} with error: {}", message.getChatId(), e.getMessage());
        }
    }
}
