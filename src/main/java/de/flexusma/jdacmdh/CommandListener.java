package de.flexusma.jdacmdh;

import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.EmbededBuilder;
import de.flexusma.jdacmdh.Database;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.xml.crypto.Data;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

public class CommandListener extends ListenerAdapter {
    Preferences preferences;
    private boolean isDatabase;
    public static IntiCommands cmd =new IntiCommands(
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

    public CommandListener(LogType logLevel, boolean isDatabase, Preferences preferences, Command... cmds){
        cmd = new IntiCommands(cmds);
        Logger.logLevel=logLevel;
        this.isDatabase =isDatabase;
        this.preferences=preferences;
    }
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(isDatabase)
            preferences = Database.initPref(event.getJDA(),event.getGuild().getId());

        Logger.log(LogType.DEBUG,"Message recieved: "+event.getMessage().getContentRaw());
        if (event.getMessage().getContentRaw().startsWith(preferences.getPrefix()) ||
                event.getMessage().getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention())
                        && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            Logger.log(LogType.DEBUG,"Command detected");
            handlercommand(event,preferences);
        }
    }



    private void handlercommand(MessageReceivedEvent event, Preferences preferences) {
        String[] raw;
        String command;
        if(event.getMessage().getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention())) {
             raw = event.getMessage().getContentRaw().replaceFirst(event.getJDA().getSelfUser().getAsMention(), "").split(" ");
            command = raw[1];
        }else{
             raw = event.getMessage().getContentRaw().replaceFirst(preferences.getPrefix(), "").split(" ");
            command = raw[0];
        }

        Logger.log(LogType.INFO,"Command detected: "+command);
        for (String registercmds:cmd.cmds.keySet()) {
            if (registercmds.equals(command)) {
                String args = String.join(" ", Arrays.copyOfRange(raw, 1, raw.length)).replaceFirst(registercmds , "");
                if(event.getMessage().getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention())) args=args.replaceFirst(" ","");
                Logger.log(LogType.DEBUG,"Command Arguments: "+args);
                cmd.cmds.get(registercmds).execute(new CommandEvent(event, preferences , args , cmd.cmds));
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.listening("@"+event.getJDA().getSelfUser().getAsTag()+ " about"));
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
