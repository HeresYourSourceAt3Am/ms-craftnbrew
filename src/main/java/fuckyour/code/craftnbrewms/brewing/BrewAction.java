package fuckyour.code.craftnbrewms.brewing;

import org.bukkit.inventory.BrewerInventory;

/**
 * Brewing API shamelessly reused from and modified by me
 * @author  https://www.spigotmc.org/threads/updated-creating-custom-brewing-recipes.472074/
 */
public abstract class BrewAction {
    public abstract void brew(BrewerInventory inventory, BrewingRecipe recipe);
    public abstract boolean canBrew(BrewerInventory inventory, BrewingRecipe recipe);
}