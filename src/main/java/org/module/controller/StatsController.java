package org.module.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.module.configuration.BotConfiguration;
import org.module.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
	private final StatsService statsService;

	@Autowired
	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	@GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public String stats() {
		JDA jda = BotConfiguration.jda;

		long channelsCount = 0;

		for (Guild guild : jda.getGuilds()) {
			channelsCount += guild.getChannels().size();
		}

		JsonObject json = new JsonObject();
		json.addProperty("guilds", jda.getGuilds().size());
		json.addProperty("users", jda.getUsers().size());
		json.addProperty("channels", channelsCount);
		json.addProperty("executedCommands", statsService.getStats().getExecutedCommands());
		json.addProperty("shards", jda.getShardInfo().getShardTotal());

		return new Gson().toJson(json);
	}
}
