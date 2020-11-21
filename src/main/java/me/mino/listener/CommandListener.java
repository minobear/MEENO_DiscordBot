package me.mino.listener;

import me.mino.commands.Command;
import me.mino.events.CommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.getMessage().getContentDisplay().startsWith(Command.prefix)) return;

        String commandName = e.getMessage().getContentDisplay().trim().split("\\s+")[0].replace(Command.prefix, "");
        String[] args = e.getMessage().getContentDisplay().split("\\s+");
        Command command = Command.commands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(commandName)).findFirst().orElse(null);
        if (command != null){
            if (command.getPermissionNeed() != null){
                if (!e.getMember().getPermissions().contains(command.getPermissionNeed())){
                    e.getTextChannel().sendMessage(e.getMember().getAsMention()+" 你沒有權限使用此指令!").queue();
                    return;
                }
            }

            command.execute(new CommandEvent(e, commandName, args));
        }
    }
}
