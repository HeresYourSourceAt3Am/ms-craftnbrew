package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.brewing.BrewAction;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldChampagne extends BrewAction {
    private static final ItemStack GOLD_CHAMPAGNE;

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&bCristal Champagne &4(17% vol)")
                .lore("&f&oWealth in bottle for only the richest of rich, but also known to be illegal to brew and sell")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(PotionEffectType.INCREASE_DAMAGE.getColor());
        meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*10, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 1), true);


        itemStack.setItemMeta(meta);


        GOLD_CHAMPAGNE = itemStack;
    }

    public static ItemStack getGoldChampagne() {
        return GOLD_CHAMPAGNE;
    }

    @Override
    public void brew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if(empty != -1) {
            // Create new stack for white rum
            ItemStack potion = getGoldChampagne().clone();
            potion.setAmount(1);
            inventory.setItem(empty, potion);
        } else {
            // Error, do something
            for (ItemStack itemStack : inventory) {
                if(itemStack.isSimilar(getGoldChampagne())) {
                    // Add 1 white rum
                    itemStack.setAmount(itemStack.getAmount()+1);
                    return;
                }
            }
        }
    }


    @Override
    public boolean canBrew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if(empty != -1) {
            // Has room, can brew
            return true;
        } else {
            // No room, must have similar potion existing already
            for (ItemStack itemStack : inventory) {
                if(itemStack.isSimilar(getGoldChampagne()) && itemStack.getAmount() + 1 <= 64) return true;
            }
            return false;
        }
    }
}
