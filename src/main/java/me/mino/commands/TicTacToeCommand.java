package me.mino.commands;

import me.mino.DiscordBot;
import me.mino.events.CommandEvent;
import me.mino.games.Game;
import me.mino.games.tictactoe.TicTacToeGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TicTacToeCommand extends Command {
    private final DiscordBot plugin = DiscordBot.getInstance();
    public TicTacToeCommand(String name, Permission permission) {
        super(name, permission);
    }

    @Override
    public void execute(CommandEvent e) {
        if (!Game.isMemberInGame(e.getMember())) {
            if (e.getArgs().length == 1) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("你想和誰遊玩圈圈叉叉呢?");
                embed.setDescription("點擊 :robot: 和我遊玩\n點擊 :people_wrestling: 與其他玩家遊玩\n點擊 :no_entry: 關閉遊戲");
                embed.setFooter(TicTacToeGame.gameName);
                Message message = e.sendMessageComplete("遊戲創建者: " + e.getMember().getAsMention(), embed.build());
                message.addReaction("U+1f916").queue();
                message.addReaction("U+1f93c").queue();
                message.addReaction("U+26d4").queue();
                e.deleteMessage();
                new TicTacToeGame(e.getMember(), message);
            } else if (e.getArgs().length == 2) {
                List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
                if (mentionedMembers.size() == 1) {
                    if (mentionedMembers.get(0) != e.getMember()){
                        Member invitee = mentionedMembers.get(0);
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("遊戲邀請");
                        embed.setDescription(e.getMember().getAsMention() + " 邀請你與他一起遊玩圈圈叉叉遊戲");
                        embed.addField("接受:", "✅", true);
                        embed.addField("拒絕:", "❌", true);
                        embed.setFooter(TicTacToeGame.gameName);
                        Message message = e.sendMessageComplete("受邀者: "+invitee.getAsMention(), embed.build());
                        message.addReaction("✅").queue();
                        message.addReaction("❌").queue();
                        message.addReaction("U+26d4").queue();
                        e.deleteMessage();
                        if (mentionedMembers.get(0).getUser() != plugin.jda.getSelfUser()){
                            new TicTacToeGame(e.getMember(), message).setInvitee(invitee);
                        }else{
                            TicTacToeGame game = new TicTacToeGame(e.getMember(), message);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (Game.isGameExist(game)){
                                        game.start(e.getMember());
                                    }else{
                                        this.cancel();
                                    }
                                }
                            }, new Random().nextInt(2000)+1000);
                        }
                    }else{
                        e.sendMessageQueue(e.getMember().getAsMention() + " 你無法對自己發送遊戲邀請!");
                    }
                } else {
                    e.sendMessageQueue(e.getMember().getAsMention() + " 請Tag一位你想發送遊戲邀請的人: ``=" + e.getCommandName() + " <tag>``");
                }
            }
        } else {
            e.sendMessageQueue(e.getMember().getAsMention() + " 你已經正在一個遊戲中了，無法創建新的遊戲");
        }
    }
}
