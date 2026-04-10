package dev.minegame.mines;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MinegamesJoinCommand implements CommandExecutor {
    private final JoinGiftManager joinGiftManager;

    public MinegamesJoinCommand(JoinGiftManager joinGiftManager) {
        this.joinGiftManager = joinGiftManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(joinGiftManager.color(joinGiftManager.text(
                    "messages.shared.only-players",
                    "Only players can use this command."
            )));
            return true;
        }
        if (!player.hasPermission("minegamesjoin.admin")) {
            player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                    "messages.shared.no-permission",
                    "&cNo permission."
            )));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                    "messages.join-gift.command.usage",
                    "&eUsage: /minegamesjoin <true|false|set <amount>>"
            )
                    .replace("%enabled%", String.valueOf(joinGiftManager.isEnabled()))
                    .replace("%amount%", String.valueOf(joinGiftManager.amount()))
                    .replace("%seen%", String.valueOf(joinGiftManager.seenCount()))));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                        "messages.join-gift.command.set-usage",
                        "&eUsage: /minegamesjoin set <amount>"
                )));
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                        "messages.join-gift.command.amount-not-number",
                        "&cAmount must be a number."
                )));
                return true;
            }
            if (amount < 0.0) {
                player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                        "messages.shared.amount-must-be-greater-than-zero",
                        "&cAmount must be greater than 0."
                )));
                return true;
            }
            joinGiftManager.setAmount(amount);
            player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                    "messages.join-gift.command.amount-set",
                    "&aJoin gift amount set to &6$%amount%&a."
            ).replace("%amount%", String.valueOf(amount))));
            return true;
        }

        Boolean enabled = parseBoolean(args[0]);
        if (enabled == null) {
            player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                    "messages.join-gift.command.usage",
                    "&eUsage: /minegamesjoin <true|false|set <amount>>"
            )));
            return true;
        }
        joinGiftManager.setEnabled(enabled);
        player.sendMessage(joinGiftManager.color(joinGiftManager.text(
                enabled ? "messages.join-gift.command.enabled" : "messages.join-gift.command.disabled",
                enabled ? "&aJoin gift enabled." : "&eJoin gift disabled."
        )));
        return true;
    }

    private Boolean parseBoolean(String raw) {
        if (raw == null) {
            return null;
        }
        return switch (raw.toLowerCase()) {
            case "true", "on", "enable", "enabled" -> Boolean.TRUE;
            case "false", "off", "disable", "disabled" -> Boolean.FALSE;
            default -> null;
        };
    }
}
