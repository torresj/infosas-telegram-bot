package com.torresj.infosas.telegrambot.models;

import lombok.Getter;

@Getter
public enum CommandType {
    START("/start"),
    UPDATE("/update_infosas_data"),
    GET_METRICS("/get_metrics"),
    GET_LISTS_STATUS("/get_list_status");

    private final String label;

    CommandType(String label) {
        this.label = label;
    }

    public static CommandType fromString(String value) {
        for (CommandType type : CommandType.values()) {
            if (type.getLabel().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant found for value: " + value);
    }
}
