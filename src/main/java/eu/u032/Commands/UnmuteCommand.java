package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Config;
import net.dv8tion.jda.api.Permission;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        this.name = "unmute";
        this.help = "Unmute member";
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
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
            event.getGuild().removeRoleFromMember(
                    args[0], event.getJDA().getRoleById(Config.getString("MUTE_ROLE"))
            ).queue();

            event.reactSuccess();
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }

}
