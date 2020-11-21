package me.mino.commands;

import me.mino.events.CommandEvent;
import me.mino.games.Game;
import me.mino.games.tictactoe.TicTacToeGame;
import net.dv8tion.jda.api.Permission;

public class TicTacToeCloseCommand extends Command {

    public TicTacToeCloseCommand(String name, Permission permission) {
        super(name, permission);
    }

    @Override
    public void execute(CommandEvent e) {
        TicTacToeGame game = (TicTacToeGame) Game.getGameByMember(e.getMember());
        if (game != null){
            game.close();
            e.getMessage().delete().queue();
            e.sendMessageQueue(e.getMember().getAsMention()+" 你已成功關閉當前的遊戲");
        }else{
            e.sendMessageQueue(e.getMember().getAsMention()+" 你沒有遊戲能夠關閉");
        }
    }
}
