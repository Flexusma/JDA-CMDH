package de.flexusma.jdacmdh.debug;

import de.flexusma.jdacmdh.CommandInitBuilder;
import de.flexusma.jdacmdh.CommandListener;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static LogType logLevel=LogType.INFO;
    public static String loggerName="JDA-CMDH | Log: ";



    public static void log(LogType toLog, String info) {
        if (toLog != logLevel) {
            switch (toLog.getLevel()) {
                case 0:
                    if (0>=logLevel.getLevel()) {
                        System.out.println(getColor(toLog)+loggerName + toLog.getType() + " | " + info+ANSI_RESET);
                        break;
                    }

                case 1:

                    if (1>=logLevel.getLevel()) {
                        System.out.println(getColor(toLog)+loggerName+ toLog.getType() + " | " + info+ANSI_RESET);
                        break;
                    }

                case 2:

                    if (2>=logLevel.getLevel()) {
                        System.out.println(getColor(toLog)+loggerName + toLog.getType() + " | " + info+ANSI_RESET);
                        break;
                    }

                case 3:

                    if (3>=logLevel.getLevel()) {
                        System.out.println(getColor(toLog)+loggerName + toLog.getType() + " | " + info+ANSI_RESET);
                        break;
                    }
            }
        }else{
            System.out.println(getColor(toLog)+loggerName + toLog.getType() + " | " + info+ANSI_RESET);
        }
    }

    static String getColor(LogType type){
        switch (type.getLevel()){
            case 0:
                return ANSI_RED;
            case 1:
                return ANSI_YELLOW;
            case 2:
                return ANSI_GREEN;
            case 3:
                return ANSI_WHITE;
            default:
                return ANSI_CYAN;

        }
    }
}
