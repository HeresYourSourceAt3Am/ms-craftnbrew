package fuckyour.code.craftnbrewms.shop;

import fuckyour.code.craftnbrewms.CraftNBrewMS;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

import java.util.logging.Level;

public class CitizensHook {
    public CitizensHook(CraftNBrewMS plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Citizens") == null || !plugin.getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            plugin.getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ShopTrait.class).withName("cnbshoptrait"));
    }
}
