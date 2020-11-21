package me.mino.listener;

import me.mino.games.Game;
import me.mino.games.tictactoe.TicTacToeGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class TicTacToeListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        if (Objects.requireNonNull(event.getUser()).isBot()) return;

        TextChannel channel = event.getTextChannel();
        Message message = channel.retrieveMessageById(event.getMessageId()).complete();
        Member member = event.getMember();

        if (message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {  // if the message sender is bot self
            List<MessageEmbed> embedList = message.getEmbeds();
            if (message.getEmbeds().size() == 0 || embedList.get(0).getFooter() == null || !Objects.equals(Objects.requireNonNull(embedList.get(0).getFooter()).getText(), TicTacToeGame.gameName)) return;

            TicTacToeGame game = (TicTacToeGame) Game.getGameByMessageID(message.getId());
            if (game == null){
                message.delete().queue();
                channel.sendMessage(MessageFormat.format("{0} 這個遊戲已被關閉，請嘗試重新創建一個遊戲!", Objects.requireNonNull(member).getAsMention())).queue();
            }else{
                Member creatorMember = game.getCreatorMember();
                event.getReaction().removeReaction(Objects.requireNonNull(user)).complete();
                switch (event.getReactionEmote().toString()) {
                    case "RE:U+1f916":  // play with bot
                        if (Objects.requireNonNull(member).equals(creatorMember)) {
                            game.start(member);
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                    case "RE:U+1f93c": // play with people
                        if (member == creatorMember) {
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("尋找對手中...");
                            embed.setDescription(MessageFormat.format("{0} 開啟了圈圈叉叉遊戲, 可按下 :raised_hands: 與他一同遊玩!", Objects.requireNonNull(member).getAsMention()));
                            embed.setFooter(TicTacToeGame.gameName);
                            message.clearReactions().complete();
                            message.editMessage(embed.build()).queue();
                            message.addReaction("U+1f64c").queue(); // join
                            message.addReaction("U+26d4").queue(); // close
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                    case "RE:U+1f64c": // player2 join the game
                        if (!Objects.requireNonNull(member).equals(creatorMember)) {
                            if (!Game.isMemberInGame(member)) {
                                game.start(creatorMember, member);
                            } else {
                                channel.sendMessage(MessageFormat.format("{0} 你目前已經有一個遊戲被創建，無法加入他人的遊戲!\n可輸入 ``{1}`` 關閉當前創建的遊戲", member.getAsMention(), "=close")).queue();
                            }
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                    case "RE:U+26d4": // close the game
                        if (Objects.requireNonNull(member).equals(creatorMember)) {
                            game.close();
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                    case "RE:U+2705": // accept the game invite
                        if (Objects.requireNonNull(member).equals(game.getInvitee())) {
                            game.start(creatorMember, member);
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                    case "RE:U+274c": // reject the game invite
                        if (Objects.requireNonNull(member).equals(game.getInvitee())) {
                            game.close();
                            channel.sendMessage(creatorMember.getAsMention() + " 對方拒絕了你的遊戲邀請").queue();
                        } else {
                            event.getReaction().removeReaction(user).queue();
                        }
                        break;
                }

                // game grid set listener
                if (game.isGameRunning()){
                    if (game.getCurrentPlayer().equals(member)) {
                        switch (event.getReactionEmote().toString()) {
                            case "RE:U+31U+fe0fU+20e3":
                                if (game.getGridStatus(1) == null)
                                    game.setGridStatus(1, member);
                                break;
                            case "RE:U+32U+fe0fU+20e3":
                                if (game.getGridStatus(2) == null)
                                    game.setGridStatus(2, member);
                                break;
                            case "RE:U+33U+fe0fU+20e3":
                                if (game.getGridStatus(3) == null)
                                    game.setGridStatus(3, member);
                                break;
                            case "RE:U+34U+fe0fU+20e3":
                                if (game.getGridStatus(4) == null)
                                    game.setGridStatus(4, member);
                                break;
                            case "RE:U+35U+fe0fU+20e3":
                                if (game.getGridStatus(5) == null)
                                    game.setGridStatus(5, member);
                                break;
                            case "RE:U+36U+fe0fU+20e3":
                                if (game.getGridStatus(6) == null)
                                    game.setGridStatus(6, member);
                                break;
                            case "RE:U+37U+fe0fU+20e3":
                                if (game.getGridStatus(7) == null)
                                    game.setGridStatus(7, member);
                                break;
                            case "RE:U+38U+fe0fU+20e3":
                                if (game.getGridStatus(8) == null)
                                    game.setGridStatus(8, member);
                                break;
                            case "RE:U+39U+fe0fU+20e3":
                                if (game.getGridStatus(9) == null)
                                    game.setGridStatus(9, member);
                                break;
                        }
                    }
                }
            }
        }
    }
}
