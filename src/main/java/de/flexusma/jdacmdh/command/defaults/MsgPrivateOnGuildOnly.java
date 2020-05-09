/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */
package de.flexusma.jdacmdh.command.defaults;

import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class MsgPrivateOnGuildOnly extends Command {

    public MsgPrivateOnGuildOnly(){
        this.name="";
        this.help="Executed when guildOnly command is sent in private messages";
        this.botPermissions= new Permission[]{};
        this.guildOnly=false;
    }

    @Override
    public void execute(CommandEvent event) {
        event.replyError("Sorry, this command is Server-only", "To use this command, please write me on a Discord Server!",null);
    }
}
