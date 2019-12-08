package de.flexusma.jdacmdh;

import net.dv8tion.jda.api.Permission;

public class Command {
    public  String name;
    public  String help;
    public  Permission[] botPermissions;
    public  boolean guildOnly;


    protected void execute(CommandEvent event){

    }

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
}
