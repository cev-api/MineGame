package dev.minegame.mines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public record SlotStationData(
        String worldName,
        int x,
        int y,
        int z,
        BlockFace facing,
        int reelCount,
        int rowCount,
        String outerFrameBlock,
        String innerFrameBlock,
        String winningBlock,
        Boolean frameAnimEnabled,
        String frameAnimBlock,
        Integer frameAnimPattern,
        String frameAnimMode,
        Integer boardSize,
        Double costPerSpin
) {
    public SlotStationData {
        if (outerFrameBlock != null) {
            outerFrameBlock = outerFrameBlock.toUpperCase();
        }
        if (innerFrameBlock != null) {
            innerFrameBlock = innerFrameBlock.toUpperCase();
        }
        if (winningBlock != null) {
            winningBlock = winningBlock.toUpperCase();
        }
        if (frameAnimBlock != null) {
            frameAnimBlock = frameAnimBlock.toUpperCase();
        }
        if (frameAnimMode != null) {
            frameAnimMode = frameAnimMode.toLowerCase();
        }
        reelCount = Math.max(3, Math.min(8, reelCount));
        rowCount = Math.max(1, Math.min(2, rowCount));
        if (boardSize != null) {
            boardSize = Math.max(2, boardSize);
        }
        if (costPerSpin != null) {
            costPerSpin = Math.max(0.0, costPerSpin);
        }
    }

    public SlotStationData(String worldName, int x, int y, int z, BlockFace facing, int reelCount) {
        this(worldName, x, y, z, facing, reelCount, 1, null, null, null, null, null, null, null, null, null);
    }

    public String key() {
        return worldName + ":" + x + ":" + y + ":" + z;
    }

    public Location originLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new Location(world, x, y, z);
    }

    public SlotStationData withBoardMaterials(String outerFrame, String innerFrame, String winning) {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                outerFrame,
                innerFrame,
                winning,
                frameAnimEnabled,
                frameAnimBlock,
                frameAnimPattern,
                frameAnimMode,
                boardSize,
                costPerSpin
        );
    }

    public SlotStationData clearBoardMaterialOverrides() {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                null,
                null,
                null,
                frameAnimEnabled,
                frameAnimBlock,
                frameAnimPattern,
                frameAnimMode,
                boardSize,
                costPerSpin
        );
    }

    public SlotStationData withFrameAnimation(Boolean enabled, String block, Integer pattern, String mode) {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                outerFrameBlock,
                innerFrameBlock,
                winningBlock,
                enabled,
                block,
                pattern,
                mode,
                boardSize,
                costPerSpin
        );
    }

    public SlotStationData clearFrameAnimationOverrides() {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                outerFrameBlock,
                innerFrameBlock,
                winningBlock,
                null,
                null,
                null,
                null,
                boardSize,
                costPerSpin
        );
    }

    public SlotStationData withBoardSize(Integer size) {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                outerFrameBlock,
                innerFrameBlock,
                winningBlock,
                frameAnimEnabled,
                frameAnimBlock,
                frameAnimPattern,
                frameAnimMode,
                size,
                costPerSpin
        );
    }

    public SlotStationData clearBoardSizeOverride() {
        return withBoardSize(null);
    }

    public SlotStationData withCostPerSpin(Double price) {
        return new SlotStationData(
                worldName,
                x,
                y,
                z,
                facing,
                reelCount,
                rowCount,
                outerFrameBlock,
                innerFrameBlock,
                winningBlock,
                frameAnimEnabled,
                frameAnimBlock,
                frameAnimPattern,
                frameAnimMode,
                boardSize,
                price
        );
    }

    public SlotStationData clearCostPerSpinOverride() {
        return withCostPerSpin(null);
    }
}
