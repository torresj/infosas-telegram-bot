package com.torresj.infosas.telegrambot.models;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
public class QueueMessage implements Serializable {
    private MessageType type;
    private long chatId;
}
