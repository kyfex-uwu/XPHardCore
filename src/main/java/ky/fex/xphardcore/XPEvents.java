package ky.fex.xphardcore;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class XPEvents implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player eventPlayer = event.getPlayer();
        int currLevel = eventPlayer.getLevel();
        int newXP = 0;

        if (currLevel - 1 <= 15) {
            newXP = eventPlayer.getTotalExperience() - 2 * currLevel - 7;
            event.setNewExp(newXP);
        } else if (currLevel - 1 <= 30) {
            newXP = eventPlayer.getTotalExperience() - 5 * currLevel + 38;
            event.setNewExp(newXP);
        } else {
            newXP = eventPlayer.getTotalExperience() - 9 * currLevel + 158;
            event.setNewExp(newXP);
        }
        event.setDroppedExp(0);
        if (newXP > 0)
            return;
        eventPlayer.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onReceiveXP(PlayerExpChangeEvent event) {
        if (!XPHardCore.maxXPEnabled)
            return;
        Player eventPlayer = event.getPlayer();
        event.setAmount(Integer.min(
                event.getAmount(),
                XPHardCore.levelToXP(XPHardCore.maxXP) - eventPlayer.getTotalExperience() + 1
        ));
    }
}