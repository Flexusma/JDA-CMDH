/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.command;

import net.dv8tion.jda.api.Permission;

public abstract class Command {
    public String name;
    public String help;
    public Permission[] botPermissions;
    public boolean guildOnly;
    public boolean ignoreOtherBot= true;
    public String usage;

    public abstract void execute(CommandEvent event);

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public String getUsage() { return usage; }
}
