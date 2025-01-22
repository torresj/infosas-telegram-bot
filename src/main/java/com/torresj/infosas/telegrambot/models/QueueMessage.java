package com.torresj.infosas.telegrambot.models;

import lombok.Builder;

import java.io.Serializable;

@Builder(toBuilder = true)
public class QueueMessage implements Serializable {
    private MessageType type;
    private long chatId;
}
