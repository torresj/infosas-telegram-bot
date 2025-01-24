package com.torresj.infosas.telegrambot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QueueMessage implements Serializable {
    private MessageType type;
    private long chatId;
}
