package com.torresj.infosas.telegrambot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class MetricsResultMessage extends QueueMessage implements Serializable {
    private long users;
    private long exams;
    private long jobBanks;
    private long specificJobBanks;
}
