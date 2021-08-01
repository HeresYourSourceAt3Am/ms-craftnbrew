package fuckyour.code.craftnbrewms.shop;

import fuckyour.code.craftnbrewms.CraftNBrewMS;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class VaultHook {
    private Economy economy;

    public VaultHook(CraftNBrewMS plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null || !plugin.getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            plugin.getLogger().log(Level.SEVERE, "Vault not found or not enabled");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().log(Level.SEVERE, "Vault not found or not enabled");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        economy = rsp.getProvider();
    }

    public Economy getEconomy() {
        return economy;
    }
}
