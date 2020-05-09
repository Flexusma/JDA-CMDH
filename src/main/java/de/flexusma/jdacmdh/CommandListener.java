/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.command.Command;
import de.flexusma.jdacmdh.command.CommandEvent;
import de.flexusma.jdacmdh.command.defaults.MsgPrivateOnGuildOnly;
import de.flexusma.jdacmdh.database.Database;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.utils.EmbededBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sun.security.util.Debug;

import javax.annotation.Nonnull;
import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandListener extends ListenerAdapter {
    CommandPreferences preferences;
    private boolean isDatabase;
    private CommandInitBuilder builder;
    private static IntiCommands cmd = new IntiCommands(
         /*   new About(),
            new Delete(),
            new Prefix(),
            new Play(),
            new SkipTrack(),

            new StopPlayback(),
            new Resume(),
            new Pause(),
            new AutoDelete(),

            new Watch2Gether(),
            new Queue(),
            new Volume(),
            new Lewd(),
            new de.flexusma.wavybot.cmd.Activity()*/
    );

    public CommandListener(CommandInitBuilder cmbdBuilder) {
        cmd = cmbdBuilder.cmds;

        this.isDatabase = cmbdBuilder.isDatabase;
        this.preferences = cmbdBuilder.commandPreferences;
        builder=cmbdBuilder;
    }

    //guild messages
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild() && isDatabase)
            preferences = Database.initPref(event.getJDA(), event.getGuild().getId());

        Logger.log(LogType.DEBUG, "onMessagerecieved: Sender: "+event.getMessage().getAuthor().getName() +"["+event.getMessage().getAuthor().getId()+"]");
        if (!event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            Logger.log(LogType.DEBUG, "Message recieved: " + event.getMessage().getContentRaw());
            if (event.getMessage().getContentRaw().startsWith(preferences.getPrefix()) ||
                    event.getMessage().getContentRaw().replace("!", "").startsWith(event.getJDA().getSelfUser().getAsMention()) ){
                Logger.log(LogType.DEBUG, "Command detected");
                handlercommand(event, preferences);
            }
        }
    }

    private void handlercommand(MessageReceivedEvent event, CommandPreferences commandPreferences) {
        String[] raw;
        String command;
        if (event.getMessage().getContentRaw().replace("!", "").startsWith(event.getJDA().getSelfUser().getAsMention())) {
            raw = event.getMessage().getContentRaw().replaceFirst(event.getJDA().getSelfUser().getAsMention(), "").split(" ");
            command = raw[1];
        } else {
            raw = event.getMessage().getContentRaw().replaceFirst("[" + commandPreferences.getPrefix() + "]", "").split(" ");
            command = raw[0];
        }

        Logger.log(LogType.INFO, "Command detected: " + command);
        for (String registercmds : cmd.cmds.keySet()) {
            //Logger.log(LogType.DEBUG,command+" "+registercmds);
            if (registercmds.equals(command)) {
                String args = String.join(" ", Arrays.copyOfRange(raw, 1, raw.length)).replaceFirst(registercmds, "");
                if (event.getMessage().getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention()))
                    args = args.replaceFirst(" ", "");
                Logger.log(LogType.DEBUG, "Command Arguments: " + args);
                Command command1 = cmd.cmds.get(registercmds);
                List<Permission> missingPerms = new ArrayList<>();
                CommandEvent event1 = new CommandEvent(event, commandPreferences, args, cmd.cmds);
                if (event.isFromGuild()) {
                    for (Permission p : command1.getBotPermissions()) {
                        if (!event.getGuild().getSelfMember().hasPermission(p)) {
                            missingPerms.add(p);
                        }
                    }
                }
                if (missingPerms.size() >= 1) {
                    List<MessageEmbed.Field> fields = new ArrayList<>();
                    for (Permission p : missingPerms) {
                        fields.add(new MessageEmbed.Field("" + missingPerms.indexOf(p), p.getName(), missingPerms.indexOf(p) % 2 == 0));
                    }
                    Logger.log(LogType.WARN, "Missing Permissions on command:[" + command1.getName() + "] at guild:[" + event.getGuild().getName() + "|" + event.getGuild().getId() + "]");
                    event1.reply(EmbededBuilder.create("Error, not enough Permissions!", "Hey, it seems that I'm missing some Permissions... Please check that!", Color.red, fields).build());
                } else if (!event.isFromGuild() && command1.guildOnly) {
                    if (builder.MsgPrivateOnGuildOnly != null) builder.MsgPrivateOnGuildOnly.execute(event1);
                    else new MsgPrivateOnGuildOnly().execute(event1);
                } else

                    if (!(event.getAuthor().isBot() && command1.ignoreOtherBot)) {
                    command1.execute(event1);
                    }
            }
        }
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.listening("@" + event.getJDA().getSelfUser().getAsTag() + " "));
        super.onReady(event);
    }


 /*  public void onUserActivityStart(@Nonnull UserActivityStartEvent event) {

        Game now = Database.getGame(event.getJDA(),event.getMember().getId(),event.getNewActivity().getName());
        if(now!=null){

            now.startPlay(event.getNewActivity().getName(),System.currentTimeMillis());
        }else{
            now=new Game();
            now.startPlay(event.getNewActivity().getName(),System.currentTimeMillis());
        }
        Database.saveGame(event.getJDA(),event.getMember().getId(),now);
        super.onUserActivityStart(event);
    }*/

 /*   @Override
    public void onUserActivityEnd(@Nonnull UserActivityEndEvent event) {


        Logger.log(LogType.DEBUG,"Activity: "+event.getOldActivity().getName()+" | "+ event.getOldActivity().getTimestamps().getElapsedTime(ChronoUnit.SECONDS));

        if(now!=null){
            now.addPlayTime(Duration.ofMillis(event.getOldActivity().getTimestamps().getElapsedTime(ChronoUnit.MILLIS)));
            /*
            now.startPlay(event.getOldActivity().getName(),event.getOldActivity().getTimestamps().getStart());
            now.stopPlay(event.getOldActivity().getTimestamps().getEnd());


        }else{
            now=new Game();
            now.addPlayTime(Duration.ofMillis(event.getOldActivity().getTimestamps().getElapsedTime(ChronoUnit.MILLIS)));
            /*now.startPlay(event.getOldActivity().getName(),event.getOldActivity().getTimestamps().getStart());
            now.stopPlay(event.getOldActivity().getTimestamps().getEnd());
        }
        Database.saveGame(event.getJDA(),event.getMember().getId(),now);
        super.onUserActivityEnd(event);
    }*/
}
