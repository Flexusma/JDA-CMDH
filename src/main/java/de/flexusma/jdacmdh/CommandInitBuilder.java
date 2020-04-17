/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * CC0 1.0 Universal
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.database.Database;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.exception.DatabaseInitializationFailedException;

public class CommandInitBuilder {

     LogType logLevel = LogType.WARN;
     Database db = null;
     boolean isDatabase = false;
     CommandPreferences commandPreferences;
     IntiCommands cmds = null;


    public CommandInitBuilder logLevel(LogType logLevel){
        this.logLevel=logLevel;
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
