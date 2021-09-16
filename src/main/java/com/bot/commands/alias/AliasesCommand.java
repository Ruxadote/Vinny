package com.bot.commands.alias;

import com.bot.commands.GeneralCommand;
import com.bot.db.AliasDAO;
import com.bot.db.GuildDAO;
import com.bot.models.Alias;
import com.bot.models.InternalGuild;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import datadog.trace.api.Trace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AliasesCommand extends GeneralCommand {

    private GuildDAO guildDAO;
    private AliasDAO aliasDAO;
    private final Paginator.Builder builder;

    public AliasesCommand(EventWaiter waiter, AliasDAO aliasDAO, GuildDAO guildDAO) {
        this.name = "aliases";
        this.help = "Shows a list of all aliases available to you";
        this.aliasDAO = aliasDAO;
        this.guildDAO = guildDAO;
        builder = new Paginator.Builder()
                .setColumns(1)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .showPageNumbers(true)
                .setEventWaiter(waiter)
                .setTimeout(30, TimeUnit.SECONDS)
                .waitOnSinglePage(false)
                .setFinalAction(message -> message.clearReactions().queue());
    }

    @Override
    @Trace(operationName = "executeCommand", resourceName = "Aliases")
    protected void executeCommand(CommandEvent commandEvent) {
        // TODO: Will need updating for channel and user aliases
        InternalGuild guild = guildDAO.getGuildById(commandEvent.getGuild().getId());

        if (guild.getAliasList().isEmpty()) {
            commandEvent.reply("There are no aliases available to you here");
            return;
        }

        ArrayList<String> aliases = new ArrayList<>();

        for (Map.Entry<String, Alias> entry : guild.getAliasList().entrySet()) {
            aliases.add(entry.getKey() + " -> " + entry.getValue().getCommand());
        }

        builder.setText("Aliases you can use here: ");
        builder.setItems(aliases.toArray(new String[]{}));
        builder.build().paginate(commandEvent.getChannel(), 1);
    }
}
