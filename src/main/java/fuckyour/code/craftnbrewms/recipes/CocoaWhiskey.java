package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.brewing.BrewAction;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CocoaWhiskey extends BrewAction {
    private static final ItemStack COCOA_WHISKEY;

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&9Cocoa Whiskey &4(48% vol)")
                .lore("&f&oIt's all natural, handcrafted, and copper distilled")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(PotionEffectType.SLOW_FALLING.getColor());
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*5, 1), true);
        itemStack.setItemMeta(meta);

        COCOA_WHISKEY = itemStack;
    }

    public static ItemStack getCocoaWhiskey() {
        return COCOA_WHISKEY;
    }

    @Override
    public void brew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if(empty != -1) {
            // Create new stack for white rum
            ItemStack potion = getCocoaWhiskey().clone();
            potion.setAmount(1);
            inventory.setItem(empty, potion);
        } else {
            // Error, do something
            for (ItemStack itemStack : inventory) {
                if(itemStack.isSimilar(getCocoaWhiskey())) {
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
                if(itemStack.isSimilar(getCocoaWhiskey()) && itemStack.getAmount() + 1 <= 64) return true;
            }
            return false;
        }
    }


}
