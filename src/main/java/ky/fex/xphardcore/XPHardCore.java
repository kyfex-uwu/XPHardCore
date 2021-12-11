package ky.fex.xphardcore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class XPHardCore extends JavaPlugin {

    public FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        System.out.println("Kyfex's XPHardCore enabled! :3");
        config.addDefault("maxXP",30);
        config.addDefault("maxXPEnabled",true);
        maxXP = config.getInt("maxXP");
        maxXPEnabled = config.getBoolean("maxXPEnabled");

        this.getCommand("xphc").setExecutor(new XPCommands());
        this.getCommand("xphc").setTabCompleter(new XPTabCompleter());
        getServer().getPluginManager().registerEvents(new XPEvents(), this);
    }

    @Override
    public void onDisable() {
        config.set("maxXP",maxXP);
        config.set("maxXPEnabled",maxXPEnabled);
        this.saveConfig();
        System.out.println("Kyfex's XPHardCore disabled! ;w;");
    }

    public static int maxXP;
    public static boolean maxXPEnabled;

    public static int levelToXP(int level){
        if(level<=16){
            return (int) (Math.pow(level,2)+6*level);
        }else if(level<=31){
            return (int) (2.5*Math.pow(level,2)-40.5*level+360);
        }else{
            return (int) (4.5*Math.pow(level,2)-162.5*level+2220);
        }
    }
}

class XPCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argsLength=args.length;
        if(argsLength>=1) {
            switch (args[0]) {
                case "setMaxXP":
                    if(argsLength>=2) {
                        try {
                            int newMaxXP = Integer.parseInt(args[1]);
                            XPHardCore.maxXP=newMaxXP;
                            sender.sendMessage("Max XP set to level "+args[1]);
                        } catch (NumberFormatException nfe) {
                            return false;
                        }
                    }else{
                        sender.sendMessage("Max XP is currently level "+Integer.toString(XPHardCore.maxXP));
                    }
                    return false;
                case "maxXPEnabled":
                    if(argsLength>=2) {
                        if(args[1].equals("false")){
                            XPHardCore.maxXPEnabled=false;
                            sender.sendMessage("Max XP disabled");
                        }else if(args[1].equals("true")){
                            XPHardCore.maxXPEnabled=true;
                            sender.sendMessage("Max XP enabled");
                        }
                    }else{
                        String isEnabled = "disabled";
                        if(XPHardCore.maxXPEnabled){
                            isEnabled = "enabled";
                        }
                        sender.sendMessage("Max XP is currently "+isEnabled);
                    }
                    return false;
            }
        }
        return false;
    }
}

class XPTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> argsToReturn = new ArrayList<String>();

        switch(args.length){
            case 1:
                //if (sender.hasPermission("myplugin.set.health")){
                argsToReturn.add("setMaxXP");
                argsToReturn.add("maxXPEnabled");
                //}
                return StringUtil.copyPartialMatches(args[0], argsToReturn, new ArrayList<String>());
            case 2:
                if(args[0].equals("maxXPEnabled")){
                    argsToReturn.add("true");
                    argsToReturn.add("false");
                }
                return StringUtil.copyPartialMatches(args[1], argsToReturn, new ArrayList<String>());
            default:
                return null;
        }
    }
}

class XPEvents implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player eventPlayer = event.getPlayer();
        int currLevel = eventPlayer.getLevel();
        int newXP=0;

        if(currLevel-1<=15){
            newXP=eventPlayer.getTotalExperience()-2*currLevel-7;
            event.setNewExp(newXP);
        }else if(currLevel-1<=30){
            newXP=eventPlayer.getTotalExperience()-5*currLevel+38;
            event.setNewExp(newXP);
        }else{
            newXP=eventPlayer.getTotalExperience()-9*currLevel+158;
            event.setNewExp(newXP);
        }
        event.setDroppedExp(0);

        if(newXP<=0) {
            eventPlayer.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onReceiveXP(PlayerExpChangeEvent event){
        Player eventPlayer = event.getPlayer();
        if(XPHardCore.maxXPEnabled){
            event.setAmount(Integer.min(
                    event.getAmount(),
                    XPHardCore.levelToXP(XPHardCore.maxXP)-eventPlayer.getTotalExperience()+1
            ));
        }
    }
}
