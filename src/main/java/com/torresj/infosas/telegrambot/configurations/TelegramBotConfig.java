package com.torresj.infosas.telegrambot.configurations;

import com.torresj.infosas.telegrambot.bots.InfosasBot;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Getter
public class TelegramBotConfig {

	@Bean
	public DefaultBotOptions defaultBotOptions() {
		return new DefaultBotOptions();
	}

	@Bean
	public TelegramBotsApi telegramBotsApi(InfosasBot bot) throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(bot);
		return telegramBotsApi;
	}
}