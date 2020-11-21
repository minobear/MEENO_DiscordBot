package me.mino;

import me.mino.commands.Command;
import me.mino.commands.TicTacToeCloseCommand;
import me.mino.commands.TicTacToeCommand;
import me.mino.listener.CommandListener;
import me.mino.listener.TicTacToeListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private static DiscordBot plugin;
    public JDA jda;

    public DiscordBot() {
        plugin = this;
        buildBot();
        registerListeners();
        registerCommands();
    }

    public static void main(String[] args) {
        new DiscordBot();
    }

    private void buildBot(){
        try {
            jda = JDABuilder.createDefault("MzY2NDc1MzQzBjA1MDA2MzM2.WdnIwg.i4xJrouOEUqBOtrAZnjQnCF3jPs").build();
            System.out.println("Discord bot is log on "+jda.getSelfUser().getAsTag());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners(){
        jda.addEventListener(new CommandListener());
        jda.addEventListener(new TicTacToeListener());
    }

    private void registerCommands(){
        Command.commands.add(new TicTacToeCommand("tictactoe", null));
        Command.commands.add(new TicTacToeCommand("ttt", null));
        Command.commands.add(new TicTacToeCloseCommand("close", null));
    }

    public static DiscordBot getInstance(){
        return plugin;
    }
}
