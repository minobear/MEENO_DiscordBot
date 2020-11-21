package me.mino.commands;

import me.mino.events.CommandEvent;
import net.dv8tion.jda.api.Permission;

import java.util.HashSet;

public abstract class Command {
    public static final HashSet<Command> commands = new HashSet<>();
    public static final String prefix = "=";

    private final String name;
    private final Permission permissionNeed;

    public Command(String name, Permission permission) {
        this.name = name;
        this.permissionNeed = permission;
    }

    public String getName() {
        return name;
    }

    public Permission getPermissionNeed() {
        return permissionNeed;
    }

    public abstract void execute(CommandEvent e);
}
