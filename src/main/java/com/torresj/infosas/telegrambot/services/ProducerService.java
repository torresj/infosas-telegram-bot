package com.torresj.infosas.telegrambot.services;

import com.torresj.infosas.telegrambot.models.QueueMessage;

public interface ProducerService {
    void sendMessage(QueueMessage message);
}
