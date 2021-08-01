package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.brewing.BrewAction;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WhiteRum extends BrewAction {
    private static final ItemStack WHITE_RUM;

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&2Birch Rum &4(24% vol)")
                .lore("&f&oTriple distilled for maximum taste and clearness")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(PotionEffectType.SLOW.getColor());
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0), true);
        itemStack.setItemMeta(meta);
        WHITE_RUM = itemStack;
    }

    public static ItemStack getWhiteRum() {
        return WHITE_RUM;
    }

    @Override
    public void brew(BrewerInventory inventory, BrewingRecipe recipe) {
        int empty = inventory.firstEmpty();
        if (empty != -1) {
            // Create new stack for white rum
            ItemStack potion = getWhiteRum().clone();
            potion.setAmount(1);
            inventory.setItem(empty, potion);
        } else {
            // Error, do something
            for (ItemStack itemStack : inventory) {
                if (itemStack.isSimilar(getWhiteRum())) {
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
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack.isSimilar(getWhiteRum()) && itemStack.getAmount() + 1 <= 64) return true;
            }
            return false;
        }
    }
}
