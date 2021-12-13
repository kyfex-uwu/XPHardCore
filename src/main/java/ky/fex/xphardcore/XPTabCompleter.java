package ky.fex.xphardcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class XPTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, String[] args) {
        List<String> argsToReturn = new ArrayList<>();
        switch(args.length){
            case 1:
                argsToReturn.add("setMaxXP");
                argsToReturn.add("maxXPEnabled");
                break;
            case 2:
                if(!"maxXPEnabled".equals(args[0]))
                    break;
                argsToReturn.add("true");
                argsToReturn.add("false");
                break;
            default: break;
        }
        String targetArg = args[args.length - 1];
        for (String i : argsToReturn) {
            if (i.startsWith(targetArg))
                continue;
            argsToReturn.remove(i);
        } // Using this we can make sure that if we type "sta" it will return "start" and not "end" << Just an example
        return argsToReturn;
    }

}