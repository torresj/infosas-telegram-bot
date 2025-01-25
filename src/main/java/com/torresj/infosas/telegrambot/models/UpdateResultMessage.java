package com.torresj.infosas.telegrambot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UpdateResultMessage extends QueueMessage implements Serializable {
    private boolean success;
    private String duration;
}
