package org.maiminhdung.customenderchest.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Handles Vault economy integration for retrieval fees.
 * Thread-safe: all economy calls must be made on the main server thread.
 */
public class VaultHandler {

    private Economy economy = null;
    private boolean enabled = false;

    /**
     * Attempt to hook into Vault's Economy provider.
     * Call this during plugin enable (main thread).
     *
     * @return true if Vault economy was found and hooked
     */
    public boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        enabled = economy != null;
        return enabled;
    }

    public boolean isEnabled() {
        return enabled && economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    /**
     * Check if a player can afford a fee.
     * MUST be called on the main thread.
     *
     * @param player The player to check
     * @param amount The fee amount
     * @return true if the player has enough balance
     */
    public boolean canAfford(OfflinePlayer player, double amount) {
        if (!isEnabled()) return false;
        return economy.has(player, amount);
    }

    /**
     * Withdraw a fee from a player.
     * MUST be called on the main thread.
     *
     * @param player  The player to charge
     * @param amount  The fee amount
     * @param reason  Description for transaction log
     * @return true if the withdrawal was successful
     */
    public boolean withdraw(OfflinePlayer player, double amount, String reason) {
        if (!isEnabled()) return false;
        if (amount <= 0) return true; // Free
        EconomyResponse response = economy.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    /**
     * Get a player's current balance.
     * MUST be called on the main thread.
     *
     * @param player The player
     * @return The balance, or 0 if economy is not available
     */
    public double getBalance(OfflinePlayer player) {
        if (!isEnabled()) return 0;
        return economy.getBalance(player);
    }

    /**
     * Format an amount for display using the economy's currency name.
     *
     * @param amount The amount to format
     * @return Formatted string (e.g. "$100.00")
     */
    public String format(double amount) {
        if (!isEnabled()) return String.valueOf(amount);
        return economy.format(amount);
    }
}
