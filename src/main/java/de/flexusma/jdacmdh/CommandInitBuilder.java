/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.database.Database;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.exception.DatabaseInitializationFailedException;

import java.nio.file.Path;

public class CommandInitBuilder {

     LogType logLevel;
     boolean writeToFile = true;
     String path = null;
     Database db = null;
     boolean isDatabase = false;
     CommandPreferences commandPreferences;
     String activity="";
     IntiCommands cmds = null;

     //default overrides
    Command MsgPrivateOnGuildOnly=null;
    Command helpCommand=null;

    public CommandInitBuilder log(LogType logLevel, boolean writeToFile, String folder){
        this.logLevel=logLevel;
        this.writeToFile=writeToFile;
        this.path= folder;

        Logger.setup(logLevel, writeToFile, path);
        return this;
    }

    public CommandInitBuilder activity(String new_activity){
        activity=new_activity ;
        return this;
    }

    public CommandInitBuilder MsgPrivateOnGuildOnly(Command override){
        MsgPrivateOnGuildOnly=override;
        return this;
    }

    public CommandInitBuilder HelpCommand(Command override){
        helpCommand=override;
        return this;
    }

    public CommandInitBuilder preferences(CommandPreferences preferences){
        this.commandPreferences=preferences;
        return this;
    }
    public CommandInitBuilder database(Database database) {
        try {
            if (isDatabase = database.initDB(commandPreferences))
                this.db = database;
            else
                throw new DatabaseInitializationFailedException("Initialization Failed");
        }catch (DatabaseInitializationFailedException r){
            Logger.log(LogType.ERROR,r.getMessage());
        }
        return this;
    }
    public CommandInitBuilder commands(Command... commands){
        this.cmds=new IntiCommands(commands);
        return this;
    }

    public CommandListener build(){
        return new CommandListener(this);
    }

}
