package dev.minegame.mines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;

public final class JoinGiftStorage {
    private final MinegamePlugin plugin;
    private final File file;
    private final Map<UUID, SeenPlayer> seenPlayers = new LinkedHashMap<>();

    public JoinGiftStorage(MinegamePlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "join_rewards.yml");
    }

    public void load() {
        seenPlayers.clear();
        if (!file.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> raw = yaml.getMapList("players");
        for (Map<?, ?> entry : raw) {
            UUID uuid;
            try {
                uuid = UUID.fromString(String.valueOf(entry.get("uuid")));
            } catch (IllegalArgumentException ex) {
                continue;
            }
            String name = entry.containsKey("name") ? String.valueOf(entry.get("name")) : "Unknown";
            long firstSeen = parseLong(entry.get("firstSeen"), System.currentTimeMillis());
            long lastSeen = parseLong(entry.get("lastSeen"), firstSeen);
            seenPlayers.put(uuid, new SeenPlayer(uuid, name, firstSeen, lastSeen));
        }
    }

    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        List<Map<String, Object>> raw = new ArrayList<>();
        for (SeenPlayer player : seenPlayers.values()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("uuid", player.uuid().toString());
            entry.put("name", player.name());
            entry.put("firstSeen", player.firstSeen());
            entry.put("lastSeen", player.lastSeen());
            raw.add(entry);
        }
        yaml.set("players", raw);
        try {
            yaml.save(file);
        } catch (IOException ex) {
            plugin.getLogger().warning("Failed to save join_rewards.yml: " + ex.getMessage());
        }
    }

    public boolean markSeen(UUID uuid, String name) {
        long now = System.currentTimeMillis();
        SeenPlayer current = seenPlayers.get(uuid);
        if (current == null) {
            seenPlayers.put(uuid, new SeenPlayer(uuid, name, now, now));
            save();
            return true;
        }
        seenPlayers.put(uuid, new SeenPlayer(uuid, name, current.firstSeen(), now));
        save();
        return false;
    }

    public boolean hasSeen(UUID uuid) {
        return seenPlayers.containsKey(uuid);
    }

    public int seenCount() {
        return seenPlayers.size();
    }

    public List<String> seenNames() {
        return seenPlayers.values().stream()
                .map(SeenPlayer::name)
                .distinct()
                .toList();
    }

    private long parseLong(Object raw, long fallback) {
        if (raw == null) {
            return fallback;
        }
        try {
            return Long.parseLong(String.valueOf(raw));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private record SeenPlayer(UUID uuid, String name, long firstSeen, long lastSeen) {}
}
