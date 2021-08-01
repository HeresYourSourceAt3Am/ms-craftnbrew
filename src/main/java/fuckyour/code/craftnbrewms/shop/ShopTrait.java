package fuckyour.code.craftnbrewms.shop;

import fuckyour.code.craftnbrewms.CraftNBrewMS;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;

public class ShopTrait extends Trait {
    private final CraftNBrewMS instance;

    public ShopTrait() {
        super("cnbshoptrait");
        instance = CraftNBrewMS.getInstance();
    }

    @EventHandler
    public void onClick(NPCClickEvent event) {
        // todo: open the shop GUI, and check if vip or normal shop
    }


}
