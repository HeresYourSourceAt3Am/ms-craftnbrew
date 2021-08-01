package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.brewing.BrewAction;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Champagne extends BrewAction {
    private static final ItemStack CHAMPAGNE;

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&bMelon Champagne &4(13% vol)")
                .lore("&f&oFinest melon champagne ever made only for the fine people")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(PotionEffectType.SPEED.getColor());
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 1), true);

        itemStack.setItemMeta(meta);

        CHAMPAGNE = itemStack;
    }

    public static ItemStack getChampagne() {
        return CHAMPAGNE;
    }

    @Override
    public void brew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if (empty != -1) {
            // Create new stack for white rum
            ItemStack potion = getChampagne().clone();
            potion.setAmount(1);
            inventory.setItem(empty, potion);
        } else {
            // Error, do something
            for (ItemStack itemStack : inventory) {
                if (itemStack.isSimilar(getChampagne())) {
                    // Add 1 white rum
                    itemStack.setAmount(itemStack.getAmount() + 1);
                    return;
                }
            }
        }
    }

    @Override
    public boolean canBrew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if (empty != -1) {
            // Has room, can brew
            return true;
        } else {
            // No room, must have similar potion existing already
            for (ItemStack itemStack : inventory) {
                if (itemStack.isSimilar(getChampagne()) && itemStack.getAmount() + 1 <= 64) return true;
            }
            return false;
        }
    }
}
