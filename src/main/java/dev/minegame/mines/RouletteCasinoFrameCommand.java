package dev.minegame.mines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class RouletteCasinoFrameCommand {
    private final RouletteManager rouletteManager;

    public RouletteCasinoFrameCommand(RouletteManager rouletteManager) {
        this.rouletteManager = rouletteManager;
    }

    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission("roulette.admin")) {
            player.sendMessage(rouletteManager.colorize(rouletteManager.text(
                    "messages.shared.no-permission",
                    "&cNo permission."
            )));
            return true;
        }

        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        boolean applyAll = args.length > 0 && args[0].equalsIgnoreCase("all");
        int offset = applyAll ? 1 : 0;

        if (args.length == offset + 1 && args[offset].equalsIgnoreCase("off")) {
            rouletteManager.disableFrameAnimation(player, applyAll);
            return true;
        }

        if (args.length == offset + 1 && args[offset].equalsIgnoreCase("reset")) {
            rouletteManager.resetFrameAnimationOverrides(player, applyAll);
            return true;
        }

        if (args.length == offset + 2 && args[offset].equalsIgnoreCase("mode")) {
            rouletteManager.setFrameAnimationMode(player, args[offset + 1], applyAll);
            return true;
        }

        if (args.length < offset + 2) {
            sendUsage(player);
            return true;
        }

        Material block = Material.matchMaterial(args[offset]);
        if (block == null || !block.isBlock()) {
            player.sendMessage(rouletteManager.colorize(rouletteManager.text(
                    "messages.shared.invalid-block-material",
                    "&cInvalid block material."
            )));
            return true;
        }
        int pattern;
        try {
            pattern = Integer.parseInt(args[offset + 1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(rouletteManager.colorize(rouletteManager.text(
                    "messages.shared.pattern-not-number",
                    "&cPattern must be a number (1-10)."
            )));
            return true;
        }
        if (pattern < 1 || pattern > 10) {
            player.sendMessage(rouletteManager.colorize(rouletteManager.text(
                    "messages.shared.pattern-out-of-range",
                    "&cPattern must be between 1 and 10."
            )));
            return true;
        }
        rouletteManager.setFrameAnimation(player, block.name(), pattern, applyAll);
        return true;
    }

    private void sendUsage(Player player) {
        for (String line : rouletteManager.lines("messages.roulette.command.casino-frame-usage-lines", java.util.List.of(
                "&eUsage: /rouletteadmin casinoframe [all] <block> <pattern 1-10>",
                "&eUsage: /rouletteadmin casinoframe [all] mode <always|betting_only>",
                "&eUsage: /rouletteadmin casinoframe [all] <off|reset>"
        ))) {
            player.sendMessage(rouletteManager.colorize(line));
        }
    }
}
