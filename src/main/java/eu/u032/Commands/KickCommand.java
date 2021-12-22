package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class KickCommand extends Command {

    public KickCommand() {
        this.name = "kick";
        this.help = "Mute member";
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.category = new Category("Moderation");
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");

        if (args[0].isEmpty()) {
            event.replyError("Required arguments are missing!");
            return;
        }

        try {
            event.getGuild().retrieveMemberById(args[0]).complete()
                    .kick(args[1]).queue();
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}