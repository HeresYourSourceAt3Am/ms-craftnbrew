package fuckyour.code.craftnbrewms.recipes;

import fuckyour.code.craftnbrewms.CraftNBrewMS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Mash {
    private final static ItemStack MASH;

    static {
        ItemStack itemStack = ItemBuilder.start(Material.POTION)
                .name("&fMash")
                .lore("&f&oUsed to brew Moonshine")
                .build();
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setBasePotionData(new PotionData(PotionType.MUNDANE));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);
        MASH = itemStack;
    }

    public static ItemStack getMash() {
        return MASH;
    }

    public Mash(CraftNBrewMS plugin) {
        // Add recipe for mash in crafting tables
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "mash");
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, getMash());
        recipe.shape(" S ", " W ", " B ");
        ItemStack waterBottle = ItemBuilder.start(Material.POTION).build();
        PotionMeta meta = (PotionMeta) waterBottle.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottle.setItemMeta(meta);
        recipe.setIngredient('S', Material.SUGAR);
        recipe.setIngredient('W', Material.WHEAT);
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(waterBottle));
        plugin.getServer().addRecipe(recipe);
    }



}
