package com.torresj.infosas.telegrambot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class StatusesResultMessage extends QueueMessage implements Serializable {
    private List<UrlStatus> statuses;
}
