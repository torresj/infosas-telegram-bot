package com.torresj.infosas.telegrambot.models;

import java.util.Map;

import static java.util.Map.entry;

public class Commands {
    public static final Map<CommandType, BotCommandInfo> COMMANDS = Map.ofEntries(
            entry(
                    CommandType.START,
                    new BotCommandInfo(
                        "/start",
                        "Comando para iniciar el bot y ver la ayuda",
                        "Start"
                    )
            ),
            entry(
                    CommandType.GET_METRICS,
                    new BotCommandInfo(
                            "/get_metrics",
                            "Obtener las métricas de los datos de Infosas",
                            "Get metrics"
                    )
            ),
            entry(
                    CommandType.UPDATE,
                    new BotCommandInfo(
                            "/update_infosas_data",
                            "Actualizar los datos de Infosas",
                            "Update Infosas data"
                    )
            ),
            entry(
                    CommandType.GET_LISTS_STATUS,
                    new BotCommandInfo(
                            "/get_list_status",
                            "Ver el estado de las listas de bolsas y oposiciones",
                            "Get lists status"
                    )
            )
    );

    public static BotCommandInfo getCommand(CommandType type) {
        return COMMANDS.get(type);
    }
}
