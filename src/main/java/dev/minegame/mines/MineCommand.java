package dev.minegame.mines;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MineCommand implements CommandExecutor {
    private final MinesManager minesManager;

    public MineCommand(MinesManager minesManager) {
        this.minesManager = minesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(minesManager.colorize(minesManager.text(
                    "messages.shared.only-players",
                    "Only players can use this command."
            )));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("cashout")) {
            minesManager.cashout(player);
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(minesManager.colorize(minesManager.text(
                    "messages.minegame.command.usage",
                    "Usage: /minegame <mines 1-24> <wager> or /minegame cashout"
            )));
            return true;
        }

        int mines;
        double wager;
        try {
            mines = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            player.sendMessage(minesManager.colorize(minesManager.text(
                    "messages.minegame.command.mines-not-number",
                    "Mines must be a number."
            )));
            return true;
        }
        try {
            wager = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(minesManager.colorize(minesManager.text(
                    "messages.minegame.command.wager-not-number",
                    "Wager must be a number."
            )));
            return true;
        }

        minesManager.startGame(player, mines, wager);
        return true;
    }
}
