/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.command;

import de.flexusma.jdacmdh.exception.IllegalCommandModificationException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class SlashCommand {
    @NotNull
    public String name;
    @NotNull
    public String description;
    public Permission[] botPermissions;
    public boolean guildOnly = true;
    public DefaultMemberPermissions defaultPermissions;

    private SlashCommandData jdaCommandData;
    private boolean hasInit = false;

    public SlashCommand(){
        this.jdaCommandData = Commands.slash(this.name,this.description);
        this.jdaCommandData.setGuildOnly(this.guildOnly);
        if(defaultPermissions!=null)this.jdaCommandData.setDefaultPermissions(defaultPermissions);
    }

    protected void addOption(OptionType type, String name, String description) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addOption(type,name,description);
        }
    }
    protected void addOption(OptionType type, String name, String description, boolean required) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addOption(type,name,description,required);
        }
    }
    protected void addOption(OptionType type, String name, String description, boolean required, boolean autocomplete) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addOption(type,name,description,required,autocomplete);
        }
    }

    protected void addOptions(OptionData... options) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addOptions(options);
        }
    }

    protected void addSubcommands(SubcommandData... subcommands) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addSubcommands(subcommands);
        }
    }

    protected void addSubcommandGroups(SubcommandGroupData... subcommandGroups) throws IllegalCommandModificationException {
        if(hasInit) throw new IllegalCommandModificationException(this.name);
        else{
            this.jdaCommandData.addSubcommandGroups(subcommandGroups);
        }
    }

    public abstract void execute(CommandEvent event);

    public String getName() {
        return name;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

}
