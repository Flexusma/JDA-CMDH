package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.exception.DatabaseInitializationFailedException;
import jdk.jfr.internal.LogLevel;

public class CommandInitBuilder {

    LogType logLevel = LogType.WARN;
    Database db = null;
    boolean isDatabase = false;
    CommandPreferences commandPreferences = new CommandPreferences();
    IntiCommands cmds = null;

    public CommandInitBuilder logLevel(LogType logLevel){
        this.logLevel=logLevel;
        return this;
    }
    public CommandInitBuilder database(Database database) {
        try {


            if (isDatabase = database.initDB())
                this.db = database;
            else
                throw new DatabaseInitializationFailedException("Initialization Failed");

        }catch (DatabaseInitializationFailedException r){
            Logger.log(LogType.ERROR,r.getMessage());
        }
        return this;
    }
    public CommandInitBuilder preferences(CommandPreferences preferences){
        this.commandPreferences=preferences;
        return this;
    }
    public CommandInitBuilder commands(Command... commands){
        this.cmds=new IntiCommands(commands);
        return this;
    }

}
