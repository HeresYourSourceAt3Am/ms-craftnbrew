package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.brewing.BrewAction;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarkRum extends BrewAction {
    private static final ItemStack DARK_RUM;

    @Override
    public boolean canBrew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if(empty != -1) {
            // Has room, can brew
            return true;
        } else {
            // No room, must have similar potion existing already
            for (ItemStack itemStack : inventory) {
                if(itemStack.isSimilar(getDarkRum()) && itemStack.getAmount() + 1 <= 64) return true;
            }
            return false;
        }
    }

    @Override
    public void brew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if(empty != -1) {
            // Create new stack for white rum
            ItemStack potion = getDarkRum().clone();
            potion.setAmount(1);
            inventory.setItem(empty, potion);
        } else {
            // Error, do something
            for (ItemStack itemStack : inventory) {
                if(itemStack.isSimilar(getDarkRum())) {
                    // Add 1 white rum
                    itemStack.setAmount(itemStack.getAmount()+1);
                    return;
                }
            }
        }
    }

    public static ItemStack getDarkRum() {
        return DARK_RUM;
    }

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&2Oak Rum &4(33% vol)")
                .lore("&f&oDarkest moonshine ever brewed known to man")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(PotionEffectType.WEAKNESS.getColor());
        meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*5, 1), true);
        itemStack.setItemMeta(meta);


        DARK_RUM = itemStack;
    }
}
