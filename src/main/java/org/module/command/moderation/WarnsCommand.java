/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.command.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.module.Constants;
import org.module.Locale;
import org.module.model.WarnModel;
import org.module.service.MessageService;
import org.module.service.ModerationService;
import org.module.util.ArgsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarnsCommand extends Command {
	private final ModerationService moderationService;
	private final MessageService messageService;

	@Autowired
	public WarnsCommand(ModerationService moderationService, MessageService messageService) {
		this.moderationService = moderationService;
		this.messageService = messageService;
		this.name = "warns";
		this.category = Constants.MODERATION;
	}

	@Override
	protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		Member member = event.getMember();

		if (!event.getArgs().isEmpty()) {
			member = ArgsUtil.getMember(event, event.getArgs());
		}
		if (member == null || member.getUser().isBot()) {
			messageService.sendHelp(event, this, locale);
			return;
		}

		List<WarnModel> warnModels = moderationService.getWarns(member);

		StringBuilder warnsMessage = new StringBuilder("Warns count: " + warnModels.size() + "\n");
		for (WarnModel warnModel : warnModels) {
			warnsMessage.append("ID: `").append(warnModel.getId()).append("`").append(" ")
				.append(member.getAsMention())
				.append(warnModel.getReason().isEmpty() ? "" : ": ")
				.append(warnModel.getReason()).append("\n");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.DEFAULT)
			.setDescription(warnsMessage.isEmpty() ? "" : warnsMessage)
			.setFooter("ID: " + member.getId());
		event.reply(embed.build());
	}
}