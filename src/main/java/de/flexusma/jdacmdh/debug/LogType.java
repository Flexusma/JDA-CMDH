/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * CC0 1.0 Universal
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.debug;

public class LogType {
    public final static LogType INFO = new LogType("INFO",2);
    public final static LogType WARN = new LogType("WARN",1);
    public final static LogType DEBUG = new LogType("DEBUG",3);
    public final static LogType ERROR = new LogType("ERROR",0);


    String type;
    int level;

    public LogType(String type,int level){
        this.type=type;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

}
