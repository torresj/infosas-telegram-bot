package com.torresj.infosas.telegrambot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public class NotificationMessage {
    private String text;
    private String type;
    private String chatId;
}
