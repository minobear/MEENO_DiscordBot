package me.mino.games;

import me.mino.games.tictactoe.TicTacToeGame;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

public abstract class Game {
    public static HashMap<String, Game> games = new HashMap<>();

    private final String gameName;
    private final TextChannel channel;
    private final Message gameMessage;
    private final String gameMessageID;
    private final Member creatorMember;

    public Game(String gameName, Member creatorMember, Message gameMessage) {
        this.gameName = gameName;
        this.gameMessage = gameMessage;
        this.gameMessageID = gameMessage.getId();
        this.creatorMember = creatorMember;
        this.channel = gameMessage.getTextChannel();
    }

    public String getGameName() {
        return gameName;
    }

    public Message getGameMessage() {
        return gameMessage;
    }

    public String getGameMessageID(){
        return gameMessageID;
    }

    public TextChannel getChannel(){
        return channel;
    }

    public Member getCreatorMember() {
        return creatorMember;
    }

    public static boolean isGameExist(Game game){
        return games.containsValue(game);
    }

    public static boolean isMemberInGame(Member member){
        return getGameByMember(member) != null;
    }

    public static Game getGameByMember(Member member){
        for (Game game : games.values()){
            if (game.getCreatorMember().equals(member)) return game;

            switch (game.getGameName()){
                case "TicTacToe":
                    TicTacToeGame ticTacToeGame = (TicTacToeGame) game;
                    if (ticTacToeGame.isGameRunning()){
                        if (ticTacToeGame.getPlayer1().equals(member) || ticTacToeGame.getPlayer2().equals(member)){
                            return game;
                        }
                    }
                    break;
            }
        }
        return null;
    }

    public static Game getGameByMessageID(String gameMessageID){
        return games.get(gameMessageID);
    }

    public abstract void start(Member player);

    public abstract void start(Member player1, Member player2);

    public abstract void end();

    public abstract void close();
}
