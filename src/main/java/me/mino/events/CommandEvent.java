package me.mino.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandEvent {
    private final MessageReceivedEvent event;
    private final String name;
    private final String[] args;

    public CommandEvent(MessageReceivedEvent event, String name, String[] args) {
        this.event = event;
        this.name = name;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommandName() {
        return name;
    }

    public Message sendMessageComplete(String message){
        return event.getTextChannel().sendMessage(message).complete();
    }

    public Message sendMessageComplete(MessageEmbed embed){
        return event.getTextChannel().sendMessage(embed).complete();
    }

    public Message sendMessageComplete(String message, MessageEmbed embed){
        return event.getTextChannel().sendMessage(message).embed(embed).complete();
    }

    public void sendMessageQueue(String message){
        event.getTextChannel().sendMessage(message).queue();
    }

    public void sendMessageQueue(MessageEmbed embed){
        event.getTextChannel().sendMessage(embed).queue();
    }

    public void sendMessageQueue(String message, MessageEmbed embed){
        event.getTextChannel().sendMessage(message).embed(embed).queue();
    }

    public void deleteMessage(){
        event.getMessage().delete().queue();
    }

    public Member getMember(){
        return event.getMember();
    }

    public User getAuthor(){
        return event.getAuthor();
    }

    public String getMemberID(){
        return event.getMember().getId();
    }

    public String getAuthorID(){
        return event.getAuthor().getId();
    }

    public String getChannelID(){
        return event.getTextChannel().getId();
    }

    public Message getMessage(){
        return event.getMessage();
    }

    public JDA getJDA(){
        return event.getJDA();
    }
}
