package com.torresj.infosas.telegrambot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface HandlerMessageService {
    SendMessage handleMessage(Message message);
}
