package org.module.structure;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.module.Constants;
import org.module.util.EmbedUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public record CommandContext(SlashCommandInteractionEvent event, CommandClient client, Command command) {
	private static final Consumer<InteractionHook> SUCCESS = i -> i.deleteOriginal().queueAfter(20, TimeUnit.SECONDS);

	private static GuildProvider.Settings settings = null;

	public void reply(String message) {
		event.reply(message).queue();
	}

	public void reply(MessageEmbed embed) {
		event.replyEmbeds(embed).queue();
	}

	public void replySuccess(String message) {
		MessageEmbed embed = new EmbedUtil(Constants.SUCCESS, message).build();
		event.replyEmbeds(embed).queue(SUCCESS);
	}

	public void replyError(String message) {
		MessageEmbed embed = new EmbedUtil(Constants.ERROR, message).build();
		event.replyEmbeds(embed).setEphemeral(true).queue();
	}

	public void replyHelp() {
		EmbedUtil embed = new EmbedUtil(command);
		event.replyEmbeds(embed.build()).setEphemeral(true).queue();
	}

	public boolean isOwner() {
		return getUser().getId().equals(getClient().getOwnerId());
	}

	public boolean isModerator() {
		Member member = event.getMember();

		if (member == null) return false;
		if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) {
			return true;
		}

		return member.getRoles().contains(settings.getModeratorRole());
	}

	public Member getOptionAsMember(String key) {
		return getOptionAsMember(key, null);
	}

	public Member getOptionAsMember(String key, Member defaultValue) {
		return event.getOption(key, defaultValue, OptionMapping::getAsMember);
	}

	public User getOptionAsUser(String key, User defaultValue) {
		return event.getOption(key, defaultValue, OptionMapping::getAsUser);
	}

	public Role getOptionAsRole(String key) {
		return event.getOption(key, null, OptionMapping::getAsRole);
	}

	public TextChannel getOptionAsTextChannel(String key) {
		return event.getOption(key, null, OptionMapping::getAsChannel).asTextChannel();
	}

	public int getOptionAsInt(String key, int... defaultValue) {
		return event.getOption(key, defaultValue.length == 0 ? -1 : defaultValue[0], OptionMapping::getAsInt);
	}

	public String getOptionAsString(String key) {
		return event.getOption(key, "", OptionMapping::getAsString);
	}

	public String getSubcommandName() {
		return event.getSubcommandName();
	}

	public JDA getJDA() {
		return event.getJDA();
	}

	public User getUser() {
		return event.getUser();
	}

	public Member getMember() {
		return event.getMember();
	}

	public Member getSelfMember() {
		return Objects.requireNonNull(event.getGuild()).getSelfMember();
	}

	public Guild getGuild() {
		return event.getGuild();
	}

	public MessageChannel getChannel() {
		return event.getChannel();
	}

	public GuildChannel getGuildChannel() {
		return event.getGuildChannel();
	}

	public TextChannel getTextChannel() {
		return event.getChannel().asTextChannel();
	}

	public GuildProvider.Settings getSettings() {
		if (settings == null) {
			settings = getClient().getManager().getSettings(getGuild());
		}
		return settings;
	}

	public CommandClient getClient() {
		return client;
	}

	public boolean isBanned(String memberId) {
		var ban = getGuild()
			.retrieveBanList()
			.complete()
			.stream()
			.filter(obj -> obj.getUser().getId().equals(memberId))
			.findFirst();

		return ban.isPresent();
	}
}
