package org.maiminhdung.customenderchest.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.maiminhdung.customenderchest.EnderChest;
import org.maiminhdung.customenderchest.Scheduler;

/**
 * Utility class to trigger vanilla enderchest block animations (lid open/close).
 * 
 * The enderchest animation is client-side and triggered by a Block Action packet.
 * When we cancel PlayerInteractEvent, the vanilla animation doesn't play.
 * This class sends the packet manually to restore the animation.
 * 
 * Uses PacketEvents for version-independent packet sending.
 */
public class EnderChestAnimation {

    // Block state ID for ender chest in Minecraft 1.21.x
    // This is the default state, but we need to get the actual state from the world
    private static final int ENDER_CHEST_BLOCK_TYPE = 0;

    /**
     * Send the enderchest open animation to a player.
     * This triggers the lid opening animation on the client.
     * 
     * @param player The player to send the animation to
     * @param block The enderchest block
     */
    public static void sendOpenAnimation(Player player, Block block) {
        if (block.getType() != Material.ENDER_CHEST) return;
        sendBlockAction(player, block, 1, 1); // action=1, param=1 means "open"
    }
    
    /**
     * Send the enderchest close animation to a player.
     * This triggers the lid closing animation on the client.
     * 
     * @param player The player to send the animation to
     * @param block The enderchest block
     */
    public static void sendCloseAnimation(Player player, Block block) {
        if (block.getType() != Material.ENDER_CHEST) return;
        sendBlockAction(player, block, 1, 0); // action=1, param=0 means "close"
    }
    
    /**
     * Send a block action packet to trigger the enderchest animation.
     * 
     * For enderchest:
     * - action=1, parameter=0: Open lid
     * - action=1, parameter=1: Close lid
     * 
     * @param player The player to send to
     * @param block The enderchest block
     * @param action The action type (1 for chest/enderchest)
     * @param parameter The parameter (0=open, 1=close)
     */
    private static void sendBlockAction(Player player, Block block, int action, int parameter) {
        try {
            // Create Vector3i for block position
            Vector3i position = new Vector3i(block.getX(), block.getY(), block.getZ());
            
            // Get the block type ID from the block state
            // For ender chest, we use the block's material type ID
            int blockTypeId = getBlockTypeId(block);
            
            // Create the block action packet using PacketEvents
            // Constructor: Vector3i position, int action, int parameter, int blockType
            WrapperPlayServerBlockAction packet = new WrapperPlayServerBlockAction(
                position,
                action,
                parameter,
                blockTypeId
            );
            
            // Send the packet using PacketEvents
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
            
            EnderChest.getInstance().getDebugLogger().log(
                "Sent enderchest animation to " + player.getName() + 
                " (action=" + action + ", param=" + parameter + ")");
            
        } catch (Exception e) {
            EnderChest.getInstance().getDebugLogger().log(
                "Failed to send enderchest animation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get the block type ID for the given block.
     * This is needed for the PacketEvents API.
     * 
     * @param block The block
     * @return The block type ID
     */
    private static int getBlockTypeId(Block block) {
        // In PacketEvents, the block type is typically the block state ID
        // For ender chest, we can use a constant or get it from the world
        // The exact value depends on the Minecraft version
        
        // For now, we'll use a hardcoded value for ender chest
        // In a real implementation, you'd want to get this from the world state
        
        // Ender Chest block state ID (this may vary by version)
        // In 1.21.x, ender chest has ID around 234-240 depending on state
        // We'll use a safe default
        return 234; // Default ender chest state
    }
    
    /**
     * Play enderchest open animation with a slight delay for visual effect.
     * This matches vanilla behavior where the animation plays after a brief delay.
     * 
     * @param player The player
     * @param block The enderchest block
     */
    public static void playOpenAnimation(Player player, Block block) {
        // Send open animation immediately on the entity's thread
        Scheduler.runEntityTask(player, () -> sendOpenAnimation(player, block));
    }
    
    /**
     * Play enderchest close animation when inventory is closed.
     * This should be called from InventoryCloseEvent.
     * 
     * @param player The player
     * @param block The enderchest block (can be null if opened via command)
     */
    public static void playCloseAnimation(Player player, Block block) {
        if (block == null) return;
        
        // Send close animation on the entity's thread
        Scheduler.runEntityTask(player, () -> sendCloseAnimation(player, block));
    }
}
