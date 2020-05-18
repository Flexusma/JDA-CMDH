package de.flexusma.jdacmdh.utils.embeds;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageEmbedField extends MessageEmbed.Field {
    public MessageEmbedField(String name, String value, boolean inline, boolean checked) {
        super(name, value, inline, checked);
    }

    public MessageEmbedField(String name, String value, boolean inline) {
        super(name, value, inline);
    }
}
