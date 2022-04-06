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

package org.module.service;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.module.Locale;

public interface MessageService {
	void sendError(CommandEvent event, Locale locale, String key, Object... args);

	void sendSuccess(CommandEvent event, Locale locale, String key, Object... args);

	void sendLog(Guild guild, EmbedBuilder embed);

	void sendLog(Guild guild, EmbedBuilder embed, byte[] file);

	void sendHelp(CommandEvent event, Command command, Locale locale);

	Locale getLocale(String lang);

	Locale getLocale(Guild guild);
}