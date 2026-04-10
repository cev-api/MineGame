package dev.minegame.mines;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class MinegamesJoinListener implements Listener {
    private final JoinGiftManager joinGiftManager;

    public MinegamesJoinListener(JoinGiftManager joinGiftManager) {
        this.joinGiftManager = joinGiftManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        joinGiftManager.handleJoin(event.getPlayer());
    }
}
