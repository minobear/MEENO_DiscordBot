package me.mino.listener;

import me.mino.VisionAPI;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith("https://cdn.discordapp.com")){
            try {
                VisionAPI.isImageContainsBear(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (new VisionAPI().isImageContainsBear(message)){
//                event.getMessage().delete().queue();
//                event.getChannel().sendMessage(event.getMember().getAsMention()+" 這張圖片內包含 \"**熊**\"，所以被我刪除掉了!!!!").queue();
//            }
        }
    }
}
