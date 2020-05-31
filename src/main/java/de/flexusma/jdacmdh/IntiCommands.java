/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */
package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.command.Command;

import java.util.HashMap;

class IntiCommands {
    HashMap<String, Command> cmds = new HashMap<>();

    IntiCommands(Command... t) {
        for (Command cmd : t) {
            cmds.put(cmd.name, cmd);
        }
    }

}
