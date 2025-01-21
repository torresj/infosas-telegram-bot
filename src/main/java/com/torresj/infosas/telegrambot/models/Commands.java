package com.torresj.infosas.telegrambot.models;

import java.util.Map;

import static java.util.Map.entry;

public class Commands {
    public static final Map<CommandType, BotCommandInfo> COMMANDS = Map.ofEntries(
            entry(CommandType.START,new BotCommandInfo("/start", "Greets and recommends help command", "Start")),
            entry(CommandType.GET_METRICS,new BotCommandInfo("/get_metrics", "Returns the metrics of the Infosas data volume", "Get metrics")),
            entry(CommandType.UPDATE,new BotCommandInfo("/update_infosas_data", "Update Infosas data", "Update Infosas data"))
    );

    public static BotCommandInfo getCommand(CommandType type) {
        return COMMANDS.get(type);
    }
}
