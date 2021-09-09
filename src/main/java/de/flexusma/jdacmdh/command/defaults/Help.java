/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */
package de.flexusma.jdacmdh.command.defaults;

import de.flexusma.jdacmdh.CommandPreferences;
import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.command.CommandEvent;
import de.flexusma.jdacmdh.database.Database;
import de.flexusma.jdacmdh.utils.embeds.EmbededBuilder;
import de.flexusma.jdacmdh.utils.embeds.MessageEmbedField;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Help extends Command {

    public Help() {
        this.name = "help";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly = false;
        this.help = "Displays a help message, explaining and listing the commands";
        this.usage = "help";
    }

    @Override
    public void execute(CommandEvent event) {
        List<MessageEmbedField> fields = new ArrayList<>();
        CommandPreferences pref = Database.initPref(event.getJDA(), event.getGuild().getId());
        if (event.getMessageRecieved().isFromGuild()) {
            fields.add(new MessageEmbedField("**Prefix:**", "The current prefix on this server is " + pref.getPrefix(), false));
        } else {
            fields.add(new MessageEmbedField("**Prefix:**", "To write commands to me, simply @ me with the command right afterwards:\n```@Bot#tag <command>```", false));
        }

        for (Command c : event.getCommands()) {
            fields.add(new MessageEmbedField("**" + c.getName() + "**\n", "Usage: ```" + c.getUsage() + "``` \n" + c.getHelp(), false));
        }

        event.reply(EmbededBuilder.create("Command Help", "These are all the Commands you can use on this server:", Color.green, fields).build());
    }
}
