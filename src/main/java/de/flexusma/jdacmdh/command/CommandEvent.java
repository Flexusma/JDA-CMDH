package de.flexusma.jdacmdh.command;



import de.flexusma.jdacmdh.CommandPreferences;
import de.flexusma.jdacmdh.EmbededBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class CommandEvent {
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public JDA getJDA() {
        return jda;
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public MessageReceivedEvent getMessageRecieved() {
        return mRevE;
    }

    public void setMessageRecieved(MessageReceivedEvent mRevE) {
        this.mRevE = mRevE;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Message getMessage() {
        return mes;
    }

    public void setMessage(Message mes) {
        this.mes = mes;
    }

    public OffsetDateTime getCreateTime() {
        return creTime;
    }

    public void setCreateTime(OffsetDateTime creTime) {
        this.creTime = creTime;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public CommandPreferences getPref() {
        return pref;
    }

    public void setPref(CommandPreferences pref) {
        this.pref = pref;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public List<Member> getMentions() {
        return mentions;
    }

    public void setMentions(List<Member> mentions) {
        this.mentions = mentions;
    }

    Guild guild;
    MessageChannel channel;
    JDA jda;
    MessageReceivedEvent mRevE;
    User sender;
    Member member;
    Message mes;
    OffsetDateTime creTime;
    TextChannel textChannel;
    String args;
    List<Command> commands;
    List<Member> mentions;

    CommandPreferences pref;

    public CommandEvent(MessageReceivedEvent e, CommandPreferences pref, String Args, HashMap<String,Command> cmdList){
        this.guild=e.getGuild();
        this.channel=e.getChannel();
        this.jda=e.getJDA();
        this.mRevE=e;
        this.sender=e.getAuthor();
        this.mes=e.getMessage();
        this.creTime=e.getMessage().getTimeCreated();
        this.textChannel=e.getTextChannel();
        this.pref=pref;
        this.member=e.getMember();
        this.args=Args;
        this.commands= new ArrayList(cmdList.values());
        this.mentions = e.getMessage().getMentionedMembers();

    }

    public void replySuccess(String title, String description, List<MessageEmbed.Field> embds){
        textChannel.sendMessage(
                EmbededBuilder.create(pref.getEmoticons().success+title, description, Color.GREEN,embds).build()
        ).queue();
    }
    public void replyWarn(String title, String description, List<MessageEmbed.Field> embds){
        textChannel.sendMessage(
                EmbededBuilder.create(pref.getEmoticons().warn+title, description, Color.ORANGE,embds).build()
        ).queue();
    }
    public void replyError(String title, String description, List<MessageEmbed.Field> embds){
        textChannel.sendMessage(
                EmbededBuilder.create(pref.getEmoticons().error+title, description, Color.RED,embds).build()
        ).queue();
    }
    public void reply(Message e){
        textChannel.sendMessage(
                e
        ).queue();
    }
    public void reply(MessageEmbed e){
        textChannel.sendMessage(
                e
        ).queue();
    }
    public void reply(String e){
        textChannel.sendMessage(
                e
        ).queue();
    }


}