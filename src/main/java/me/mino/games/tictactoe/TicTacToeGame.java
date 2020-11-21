package me.mino.games.tictactoe;

import me.mino.games.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TicTacToeGame extends Game {
    public static final String gameName = "TicTacToe";
    private final Member[][] gamePanel = {
            {null, null, null},
            {null, null, null},
            {null, null, null}
    };
    public Member invitee;
    private Member player1;
    private Member player2;
    private boolean isPlayWithBot;
    private Member currentPlayer;
    private boolean gameRunning;
    private boolean gameInterrupted;

    private final static String O = ":regional_indicator_o:";
    private final static String X = ":regional_indicator_x:";
    private final static String redO = ":o:";
    private final static String redX = ":x:";

    public TicTacToeGame(Member creator, Message gameMessage) {
        super(gameName, creator, gameMessage);
        Game.games.put(getGameMessageID(), this);
    }

    public Member getPlayer1() {
        return player1;
    }

    public Member getPlayer2() {
        return player2;
    }

    public void setInvitee(Member invitee) {
        this.invitee = invitee;
    }

    public Member getInvitee() {
        return invitee;
    }

    public boolean isPlayWithBot() {
        return isPlayWithBot;
    }

    public Member getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isGameInterrupted() {
        return gameInterrupted;
    }

    public void setGameInterrupted(boolean gameEnd) {
        gameInterrupted = gameEnd;
    }

    private void turnPlayer() {
        this.currentPlayer = this.currentPlayer.equals(player1) ? player2 : player1;
        if (isPlayWithBot() && getCurrentPlayer().equals(player2)){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Random random = new Random();
                    int ranGrid = random.nextInt(9) + 1;
                    while (getGridStatus(ranGrid) != null) {
                        ranGrid = random.nextInt(9) + 1;
                    }
                    setGridStatus(ranGrid, player2);
                }
            }, new Random().nextInt(2000)+1000);
        }
    }

    public void setGridStatus(int grid, Member member) {
        switch (grid) {
            case 1:
                gamePanel[0][0] = member;
                break;
            case 2:
                gamePanel[0][1] = member;
                break;
            case 3:
                gamePanel[0][2] = member;
                break;
            case 4:
                gamePanel[1][0] = member;
                break;
            case 5:
                gamePanel[1][1] = member;
                break;
            case 6:
                gamePanel[1][2] = member;
                break;
            case 7:
                gamePanel[2][0] = member;
                break;
            case 8:
                gamePanel[2][1] = member;
                break;
            case 9:
                gamePanel[2][2] = member;
                break;
        }

        if (getWinLine() == -1) {
            turnPlayer();
        } else {
            end();
        }

        refreshPanel();
    }

    public Member getGridStatus(int grid) {
        switch (grid) {
            case 1:
                return gamePanel[0][0];
            case 2:
                return gamePanel[0][1];
            case 3:
                return gamePanel[0][2];
            case 4:
                return gamePanel[1][0];
            case 5:
                return gamePanel[1][1];
            case 6:
                return gamePanel[1][2];
            case 7:
                return gamePanel[2][0];
            case 8:
                return gamePanel[2][1];
            case 9:
                return gamePanel[2][2];
        }

        return null;
    }

    private int getWinLine() {
        if (gamePanel[0][0] != null && gamePanel[0][1] != null && gamePanel[0][2] != null && gamePanel[0][0].equals(gamePanel[0][1]) && gamePanel[0][0].equals(gamePanel[0][2]))
            return 1;
        if (gamePanel[1][0] != null && gamePanel[1][1] != null && gamePanel[1][2] != null && gamePanel[1][0].equals(gamePanel[1][1]) && gamePanel[1][0].equals(gamePanel[1][2]))
            return 2;
        if (gamePanel[2][0] != null && gamePanel[2][1] != null && gamePanel[2][2] != null && gamePanel[2][0].equals(gamePanel[2][1]) && gamePanel[2][0].equals(gamePanel[2][2]))
            return 3;
        if (gamePanel[1][0] != null && gamePanel[0][0] != null && gamePanel[2][0] != null && gamePanel[0][0].equals(gamePanel[1][0]) && gamePanel[0][0].equals(gamePanel[2][0]))
            return 4;
        if (gamePanel[0][1] != null && gamePanel[1][1] != null && gamePanel[2][1] != null && gamePanel[0][1].equals(gamePanel[1][1]) && gamePanel[0][1].equals(gamePanel[2][1]))
            return 5;
        if (gamePanel[1][2] != null && gamePanel[0][2] != null && gamePanel[2][2] != null && gamePanel[0][2].equals(gamePanel[1][2]) && gamePanel[0][2].equals(gamePanel[2][2]))
            return 6;
        if (gamePanel[0][0] != null && gamePanel[1][1] != null && gamePanel[2][2] != null && gamePanel[0][0].equals(gamePanel[1][1]) && gamePanel[0][0].equals(gamePanel[2][2]))
            return 7;
        if (gamePanel[0][2] != null && gamePanel[1][1] != null && gamePanel[2][0] != null && gamePanel[0][2].equals(gamePanel[1][1]) && gamePanel[0][2].equals(gamePanel[2][0]))
            return 8;

        for (Member[] raw : gamePanel) {
            for (Member grid : raw)
                if (grid == null) return -1;
        }

        return 0;
    }

    private void refreshPanel() {
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder panelBuilder = new StringBuilder();
        int i = 1;

        embed.setTitle("圈圈叉叉遊戲");
        for(int j=0; j<3; j++){
            panelBuilder.append("  ——  ");
        }
        panelBuilder.append("\n");
        for (Member[] raw : gamePanel) {
            panelBuilder.append(" | ");
            for (Member grid : raw) {
                if (grid != null) {
                    if (getWinLine() == -1 || getWinLine() == 0) {
                        if (grid.equals(player1)) {
                            panelBuilder.append(X);
                        } else {
                            panelBuilder.append(O);
                        }
                    } else {
                        switch (getWinLine()) {
                            case 1:
                                if (i <= 3) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 2:
                                if (i > 3 && i <= 6) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 3:
                                if (i > 5 && i <= 9) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 4:
                                if (i == 1 || i == 4 || i == 7) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 5:
                                if (i == 2 || i == 5 || i == 8) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 6:
                                if (i == 3 || i == 6 || i == 9) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 7:
                                if (i == 1 || i == 5 || i == 9) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                            case 8:
                                if (i == 3 || i == 5 || i == 7) {
                                    if (grid.equals(player1) && currentPlayer.equals(player1)) {
                                        panelBuilder.append(redX);
                                    } else {
                                        panelBuilder.append(redO);
                                    }
                                } else if (grid.equals(player1)) {
                                    panelBuilder.append(X);
                                } else {
                                    panelBuilder.append(O);
                                }
                                break;
                        }
                    }

                } else {
                    switch (i) {
                        case 1:
                            panelBuilder.append(":one:");
                            break;
                        case 2:
                            panelBuilder.append(":two:");
                            break;
                        case 3:
                            panelBuilder.append(":three:");
                            break;
                        case 4:
                            panelBuilder.append(":four:");
                            break;
                        case 5:
                            panelBuilder.append(":five:");
                            break;
                        case 6:
                            panelBuilder.append(":six:");
                            break;
                        case 7:
                            panelBuilder.append(":seven:");
                            break;
                        case 8:
                            panelBuilder.append(":eight:");
                            break;
                        case 9:
                            panelBuilder.append(":nine:");
                            break;
                    }
                }
                i++;
                panelBuilder.append(" | ");
            }

            panelBuilder.append("\n");
            for(int j=0; j<3; j++){
                panelBuilder.append("  ——  ");
            }
            panelBuilder.append("\n");
        }

        embed.setDescription(panelBuilder);
        if (isGameInterrupted()) {
            embed.addField(":no_entry: 遊戲中斷", "", true);
            embed.addField(TicTacToeGame.X + " 玩家:", player1.getAsMention(), false);
            embed.addField(TicTacToeGame.O + " 玩家:", player2.getAsMention(), false);
        }else if (getWinLine() == -1) {
            embed.addField("", "玩家回合:", false);
            if (currentPlayer.equals(player1)) {
                embed.addField(TicTacToeGame.X+" 玩家:", player1.getAsMention() + "  :arrow_left:", true);
                embed.addField(TicTacToeGame.O+" 玩家:", player2.getAsMention(), false);
            } else {
                embed.addField(TicTacToeGame.X+" 玩家:", player1.getAsMention(), false);
                embed.addField(TicTacToeGame.O+" 玩家:", player2.getAsMention() + "  :arrow_left:", true);
            }
        } else if (getWinLine() == 0) {
            embed.addField(":handshake: 雙方平手!", "", true);
            embed.addField(TicTacToeGame.X+" 玩家:", player1.getAsMention(), false);
            embed.addField(TicTacToeGame.O+" 玩家:", player2.getAsMention(), false);
        } else if (getWinLine() > 0){
            embed.addField(":tada: 贏家: ", this.currentPlayer.getAsMention(), true);
            embed.addField(":skull: 輸家: ", this.currentPlayer.equals(player1) ? player2.getAsMention() : player1.getAsMention(), true);
        }
        embed.setFooter(TicTacToeGame.gameName);

        getGameMessage().editMessage(embed.build()).queue();
    }

    @Override
    public void start(Member player) {
        this.player1 = player;
        this.player2 = getChannel().getGuild().getMemberById(getChannel().getJDA().getSelfUser().getId()); // bot
        this.isPlayWithBot = true;
        this.currentPlayer = this.player1;
        this.gameRunning = true;
        getGameMessage().editMessage(player1.getAsMention()+" :vs: "+player2.getAsMention()).queue();
        getGameMessage().clearReactions().complete();
        getGameMessage().addReaction("U+31U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+32U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+33U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+34U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+35U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+36U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+37U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+38U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+39U+fe0fU+20e3").queue();
        refreshPanel();
    }

    @Override
    public void start(Member player1, Member player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.isPlayWithBot = false;
        this.currentPlayer = this.player1;
        this.gameRunning = true;
        getGameMessage().editMessage("遊戲開始: "+player1.getAsMention()+"  :vs:  "+player2.getAsMention()).queue();
        getGameMessage().clearReactions().complete();
        getGameMessage().addReaction("U+31U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+32U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+33U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+34U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+35U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+36U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+37U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+38U+fe0fU+20e3").queue();
        getGameMessage().addReaction("U+39U+fe0fU+20e3").queue();
        refreshPanel();
    }

    @Override
    public void end() {
        this.gameRunning = false;
        getGameMessage().clearReactions().queue();
        close();
    }
    
    @Override
    public void close(){
        Game.games.remove(getGameMessageID());
        if (isGameRunning()){
            setGameInterrupted(true);
            refreshPanel();
            end();
        }else if (getCurrentPlayer() == null){
            getGameMessage().delete().queue();
        }
    }
}
