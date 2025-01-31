package com.torresj.infosas.telegrambot.models;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
public class UrlStatus implements Serializable {
    private String url;
    private boolean provisional;
    private UrlType type;
}
