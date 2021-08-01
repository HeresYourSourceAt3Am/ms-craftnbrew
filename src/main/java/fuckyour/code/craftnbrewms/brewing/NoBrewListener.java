package fuckyour.code.craftnbrewms.brewing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;

public class NoBrewListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBrew(BrewEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBrewingStandFuel(BrewingStandFuelEvent event) {
        event.setCancelled(true);
    }
}
