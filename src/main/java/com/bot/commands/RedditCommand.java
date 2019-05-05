package com.bot.commands;

import com.bot.exceptions.ForbiddenCommandException;
import com.bot.exceptions.PermsOutOfSyncException;
import com.bot.metrics.MetricsManager;
import com.bot.utils.CommandCategories;
import com.bot.utils.CommandPermissions;
import com.bot.utils.Logger;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public abstract class RedditCommand extends Command {
    protected MetricsManager metricsManager;
    protected Logger logger;

    public RedditCommand() {
        this.category = CommandCategories.REDDIT;
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS};
        this.cooldownScope = CooldownScope.USER;
        this.cooldown = 2;

        this.metricsManager = MetricsManager.getInstance();
        this.logger = new Logger(this.getClass().getSimpleName());
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        metricsManager.markCommand(this, commandEvent.getAuthor(), commandEvent.getGuild());

        // Check the permissions to do the command
        try {
            if (!CommandPermissions.canExecuteCommand(this, commandEvent))
                return;
        } catch (ForbiddenCommandException e) {
            commandEvent.replyWarning(e.getMessage());
            return;
        } catch (PermsOutOfSyncException e) {
            commandEvent.replyError("Could not find the role required for " + this.category.getName() + " commands. Please have a mod set a new role.");
            logger.warning(e.getMessage() + " " + commandEvent.getGuild().getId());
            return;
        } catch (Exception e) {
            commandEvent.replyError("Something went wrong with permissions, please try again or go checkout the support server and report the bug.");
            e.printStackTrace();
            logger.severe("Failed to get perms for " + this.getClass().getName(), e);
            return;
        }

        try {
            commandEvent.async(() -> executeCommand(commandEvent));
        } catch (Exception e) {
            commandEvent.replyError("Something went wrong, please try again later");
            logger.severe("Failed command " + this.getClass().getName() + ": ", e);
            e.printStackTrace();
        }
    }

    protected abstract void executeCommand(CommandEvent commandEvent);
}
