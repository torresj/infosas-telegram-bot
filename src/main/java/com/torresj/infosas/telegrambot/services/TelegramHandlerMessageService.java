package com.torresj.infosas.telegrambot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramHandlerMessageService {
    SendMessage handleMessage(Message message);
    SendMessage handleMessage(long chatId, String receivedMessage, String username);
}
