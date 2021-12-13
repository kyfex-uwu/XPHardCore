package ky.fex.xphardcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class XPCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String[] args) {
        if(args.length < 1)
            return false; // Doing a quick return will make it easier to read it as it prevents nesting
        switch (args[0].toLowerCase(Locale.ROOT)) {
            // I'm not sure if it already ignores case but if it doesn't there you go
            case "setmaxxp":
                if(args.length < 2)
                    break;
                setMaxXP(sender, Integer.parseInt(args[1]));
                break;
            case "maxxpenabled":
                decideEnabled(sender, args[1]);
                break;
            default:
                break;
        }
        return true; // Returning true usually means that everything went well and there were no errors
    }

    private void setMaxXP(CommandSender sender, int newMaxXP) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Max XP was level &a" + XPHardCore.maxXP + "&7 it's now set to &a" + newMaxXP));
        XPHardCore.maxXP = newMaxXP;
    }
    private void decideEnabled(CommandSender sender, String str) {
        switch (str) {
            case "true":
                XPHardCore.maxXPEnabled = true;
                sender.sendMessage("Max XP enabled");
                return;
            case "false":
                XPHardCore.maxXPEnabled=false;
                sender.sendMessage("Max XP disabled");
                return;
            default:
                String isEnabled = XPHardCore.maxXPEnabled ? "enabled" : "disabled";
                // This ? statement is basically a compact if/else statement, the first value
                // will be the value that will apply if it's true and the second is if it's false
                // Not usually recommended since it decreases readability it but oh well
                sender.sendMessage("Max XP is currently "+isEnabled);
        }
    }
}
