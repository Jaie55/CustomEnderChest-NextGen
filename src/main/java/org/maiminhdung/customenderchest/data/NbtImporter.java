package org.maiminhdung.customenderchest.data;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.maiminhdung.customenderchest.EnderChest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Reads offline players' enderchest data from .dat files using NBTAPI.
 * This class is only loaded when NBTAPI is confirmed present on the server.
 */
class NbtImporter {

    /**
     * Load offline players' enderchest data from world/playerdata .dat files.
     *
     * @param plugin The plugin instance
     * @return List of PlayerData for offline players with enderchest items
     */
    static List<BulkImporter.PlayerData> loadOfflinePlayers(EnderChest plugin) {
        List<BulkImporter.PlayerData> result = new ArrayList<>();

        File playerDataFolder = getPlayerDataFolder();
        if (playerDataFolder == null || !playerDataFolder.exists()) {
            plugin.getDebugLogger().log("Playerdata folder not found, skipping offline import");
            return result;
        }

        File[] datFiles = playerDataFolder.listFiles((dir, name) -> name.endsWith(".dat"));
        if (datFiles == null || datFiles.length == 0) {
            return result;
        }

        plugin.getDebugLogger().log("Scanning " + datFiles.length + " .dat files for offline players...");

        for (File datFile : datFiles) {
            try {
                String fileName = datFile.getName();
                String uuidStr = fileName.substring(0, fileName.length() - 4);

                UUID playerUUID;
                try {
                    playerUUID = UUID.fromString(uuidStr);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                // Skip online players
                if (Bukkit.getPlayer(playerUUID) != null) {
                    continue;
                }

                ItemStack[] enderItems = readEnderChest(datFile, playerUUID, plugin);
                if (enderItems == null || !hasAnyItems(enderItems)) {
                    continue;
                }

                String playerName = getPlayerName(playerUUID);
                result.add(new BulkImporter.PlayerData(playerUUID, playerName, enderItems, 27));

            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error reading .dat file: " + datFile.getName(), e);
            }
        }

        plugin.getDebugLogger().log("Found " + result.size() + " offline players with enderchest data");
        return result;
    }

    private static File getPlayerDataFolder() {
        List<World> worlds = Bukkit.getWorlds();
        if (worlds.isEmpty()) return null;
        return new File(worlds.get(0).getWorldFolder(), "playerdata");
    }

    private static ItemStack[] readEnderChest(File datFile, UUID playerUUID, EnderChest plugin) {
        try {
            ReadWriteNBT nbt;
            try (FileInputStream fis = new FileInputStream(datFile)) {
                nbt = NBT.readNBT(fis);
            }

            if (nbt == null) return null;

            ReadWriteNBTCompoundList enderItemsList = nbt.getCompoundList("EnderItems");
            if (enderItemsList == null || enderItemsList.size() == 0) return null;

            ItemStack[] items = new ItemStack[27];

            for (ReadWriteNBT itemNbt : enderItemsList) {
                int slot = itemNbt.getByte("Slot") & 255;
                if (slot >= items.length) continue;

                try {
                    ItemStack item = NBTItem.convertNBTtoItem((NBTCompound) itemNbt);
                    if (item != null && item.getType() != Material.AIR) {
                        items[slot] = item;
                    }
                } catch (Exception e) {
                    plugin.getDebugLogger().log("Failed to convert item in slot " + slot + " for " + playerUUID);
                }
            }

            return items;

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to read .dat file for " + playerUUID, e);
            return null;
        }
    }

    private static boolean hasAnyItems(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) return true;
        }
        return false;
    }

    private static String getPlayerName(UUID playerUUID) {
        try {
            org.bukkit.OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            String name = offlinePlayer.getName();
            return name != null ? name : playerUUID.toString();
        } catch (Exception e) {
            return playerUUID.toString();
        }
    }
}
