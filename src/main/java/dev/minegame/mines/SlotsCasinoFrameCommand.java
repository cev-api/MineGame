package dev.minegame.mines;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class SlotsCasinoFrameCommand {
    private final SlotsManager slotsManager;

    public SlotsCasinoFrameCommand(SlotsManager slotsManager) {
        this.slotsManager = slotsManager;
    }

    public boolean execute(Player player, String[] args) {
        if (!player.hasPermission("slots.admin")) {
            player.sendMessage(slotsManager.colorize(slotsManager.text(
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
            slotsManager.disableFrameAnimation(player, applyAll);
            return true;
        }

        if (args.length == offset + 1 && args[offset].equalsIgnoreCase("reset")) {
            slotsManager.resetFrameAnimationOverrides(player, applyAll);
            return true;
        }

        if (args.length == offset + 2 && args[offset].equalsIgnoreCase("mode")) {
            slotsManager.setFrameAnimationMode(player, args[offset + 1], applyAll);
            return true;
        }

        if (args.length < offset + 2) {
            sendUsage(player);
            return true;
        }

        Material block = Material.matchMaterial(args[offset]);
        if (block == null || !block.isBlock()) {
            player.sendMessage(slotsManager.colorize(slotsManager.text(
                    "messages.shared.invalid-block-material",
                    "&cInvalid block material."
            )));
            return true;
        }

        int pattern;
        try {
            pattern = Integer.parseInt(args[offset + 1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(slotsManager.colorize(slotsManager.text(
                    "messages.shared.pattern-not-number",
                    "&cPattern must be a number (1-10)."
            )));
            return true;
        }

        if (pattern < 1 || pattern > 10) {
            player.sendMessage(slotsManager.colorize(slotsManager.text(
                    "messages.shared.pattern-out-of-range",
                    "&cPattern must be between 1 and 10."
            )));
            return true;
        }

        slotsManager.setFrameAnimation(player, block.name(), pattern, applyAll);
        return true;
    }

    private void sendUsage(Player player) {
        for (String line : slotsManager.lines("messages.slots.command.casino-frame-usage-lines", java.util.List.of(
                "&eUsage: /slotsadmin casinoframe [all] <block> <pattern 1-10>",
                "&eUsage: /slotsadmin casinoframe [all] mode <idle_only|always>",
                "&eUsage: /slotsadmin casinoframe [all] <off|reset>"
        ))) {
            player.sendMessage(slotsManager.colorize(line));
        }
    }
}
