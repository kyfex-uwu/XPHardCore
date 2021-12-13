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
    public static int maxXP;
    public static boolean maxXPEnabled;

    @Override
    public void onEnable() {
        System.out.println("Kyfex's XPHardCore enabled! :3");
        config.addDefault("maxXP",30);
        config.addDefault("maxXPEnabled",true);
        maxXP = config.getInt("maxXP");
        maxXPEnabled = config.getBoolean("maxXPEnabled");
        saveDefaultConfig();
        this.getCommand("xphc").setExecutor(new XPCommands());
        this.getCommand("xphc").setTabCompleter(new XPTabCompleter());
        getServer().getPluginManager().registerEvents(new XPEvents(), this);
    }

    @Override
    public void onDisable() {
        config.set("maxXP", maxXP);
        config.set("maxXPEnabled", maxXPEnabled);
        this.saveConfig();
        System.out.println("Kyfex's XPHardCore disabled! ;w;");
    }

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