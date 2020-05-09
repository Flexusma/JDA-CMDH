/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.utils;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.util.List;

public class EmbededBuilder {
    public static EmbedBuilder create(String title, String desc , Color c, List<MessageEmbed.Field> f) {

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

/*
    Set the title:
    1. Arg: title as string
    2. Arg: URL as string or could also be null
 */
        eb.setTitle(title, null);

/*
    Set the color
 */
        eb.setColor(c);


/*
    Set the text of the Embed:
    Arg: text as string
 */
        eb.setDescription(desc);

/*
    Add fields to embed:
    1. Arg: title as string
    2. Arg: text as string
    3. Arg: inline mode true / false
 */
        //   eb.addField("Title of field", "test of field", false);
        if(f!=null)
            for(MessageEmbed.Field fd : f){
                eb.addField(fd);
            }

/*
    Add embed author:
    1. Arg: name as string
    2. Arg: url as string (can be null)
    3. Arg: icon url as string (can be null)
 */
        //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set footer:
    1. Arg: text as string
    2. icon url as string (can be null)
 */
        //eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set image:
    Arg: image url as string
 */
        //eb.setImage("https://flexusma.de/imag/wave.png");

/*
    Set thumbnail image:
    Arg: image url as string
 */

        return eb;
    }

    public static EmbedBuilder create(String title, String desc , Color c, MessageEmbed.Field... f) {

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

/*
    Set the title:
    1. Arg: title as string
    2. Arg: URL as string or could also be null
 */
        eb.setTitle(title, null);

/*
    Set the color
 */
        eb.setColor(c);


/*
    Set the text of the Embed:
    Arg: text as string
 */
        eb.setDescription(desc);

/*
    Add fields to embed:
    1. Arg: title as string
    2. Arg: text as string
    3. Arg: inline mode true / false
 */
     //   eb.addField("Title of field", "test of field", false);
        if(f!=null)
        for(MessageEmbed.Field fd : f){
            eb.addField(fd);
        }

/*
    Add embed author:
    1. Arg: name as string
    2. Arg: url as string (can be null)
    3. Arg: icon url as string (can be null)
 */
        //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set footer:
    1. Arg: text as string
    2. icon url as string (can be null)
 */
        //eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set image:
    Arg: image url as string
 */
        //eb.setImage("https://flexusma.de/imag/wave.png");

/*
    Set thumbnail image:
    Arg: image url as string
 */

        return eb;
    }

    public static EmbedBuilder create(String title, String desc , Color c, String image_url,boolean isThumbnail, MessageEmbed.Field... f) {

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

/*
    Set the title:
    1. Arg: title as string
    2. Arg: URL as string or could also be null
 */
        eb.setTitle(title, null);

/*
    Set the color
 */
        eb.setColor(c);


/*
    Set the text of the Embed:
    Arg: text as string
 */
        eb.setDescription(desc);

/*
    Add fields to embed:
    1. Arg: title as string
    2. Arg: text as string
    3. Arg: inline mode true / false
 */
        //   eb.addField("Title of field", "test of field", false);
        if(f!=null)
            for(MessageEmbed.Field fd : f){
                eb.addField(fd);
            }

/*
    Add embed author:
    1. Arg: name as string
    2. Arg: url as string (can be null)
    3. Arg: icon url as string (can be null)
 */
        //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set footer:
    1. Arg: text as string
    2. icon url as string (can be null)
 */
        //eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

    if(isThumbnail)
        eb.setThumbnail(image_url);
    else
        eb.setImage(image_url);


        return eb;
    }
    public static EmbedBuilder create(String title, String desc , Color c, String image_url,boolean isThumbnail, List<MessageEmbed.Field> f) {

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

/*
    Set the title:
    1. Arg: title as string
    2. Arg: URL as string or could also be null
 */
        eb.setTitle(title, null);

/*
    Set the color
 */
        eb.setColor(c);


/*
    Set the text of the Embed:
    Arg: text as string
 */
        eb.setDescription(desc);

/*
    Add fields to embed:
    1. Arg: title as string
    2. Arg: text as string
    3. Arg: inline mode true / false
 */
        //   eb.addField("Title of field", "test of field", false);
        if(f!=null)
            for(MessageEmbed.Field fd : f){
                eb.addField(fd);
            }

/*
    Add embed author:
    1. Arg: name as string
    2. Arg: url as string (can be null)
    3. Arg: icon url as string (can be null)
 */
        //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

/*
    Set footer:
    1. Arg: text as string
    2. icon url as string (can be null)
 */
        //eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");

        if(isThumbnail)
            eb.setThumbnail(image_url);
        else
            eb.setImage(image_url);


        return eb;
    }

    public static EmbedBuilder create(String title, String desc , Color c) {

        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(title, null);

        eb.setColor(c);

        eb.setDescription(desc);


        return eb;
    }
}
