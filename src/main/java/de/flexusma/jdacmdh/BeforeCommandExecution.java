package de.flexusma.jdacmdh;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface BeforeCommandExecution{
    boolean onBeforeExecution(MessageReceivedEvent event, CommandPreferences preferences);
}
