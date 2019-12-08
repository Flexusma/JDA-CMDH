package de.flexusma.jdacmdh;

import java.util.HashMap;

class IntiCommands {
    HashMap<String, Command> cmds = new HashMap<>();
    IntiCommands(Command... t) {
        for (Command cmd:t) {
            cmds.put(cmd.name , cmd);
        }
    }
}
