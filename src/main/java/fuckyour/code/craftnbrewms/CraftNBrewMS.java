package fuckyour.code.craftnbrewms;

import com.google.common.collect.ImmutableList;
import fuckyour.code.craftnbrewms.brewing.BrewEventClick;
import fuckyour.code.craftnbrewms.brewing.BrewingRecipe;
import fuckyour.code.craftnbrewms.brewing.NoBrewListener;
import fuckyour.code.craftnbrewms.recipes.*;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class CraftNBrewMS extends JavaPlugin {
    private final static List<BrewingRecipe> BREWING_RECIPES;
    private static CraftNBrewMS instance;

    static {

        BREWING_RECIPES = ImmutableList.of(
                new BrewingRecipe(Arrays.asList(Mash.getMash(), ItemBuilder.start(Material.BIRCH_LOG).build()),
                        ItemBuilder.start(Material.COAL).build(), new WhiteRum(), true, 100, 100,
                        "craftnbrew.vip1", "craftnbrew.lvl1"),
                new BrewingRecipe(Arrays.asList(Mash.getMash(), ItemBuilder.start(Material.DARK_OAK_LOG).build()),
                        ItemBuilder.start(Material.COAL).build(), new DarkRum(), true, 100, 100,
                        "craftnbrew.vip2", "craftnbrew.lvl2"),
                new BrewingRecipe(Arrays.asList(Mash.getMash(), ItemBuilder.start(Material.COCOA_BEANS).build()),
                        ItemBuilder.start(Material.COAL).build(), new CocoaWhiskey(), true, 100, 100,
                        "craftnbrew.vip3", "craftnbrew.lvl3"),
                new BrewingRecipe(Arrays.asList(Mash.getMash(), ItemBuilder.start(Material.MELON_SLICE).build()),
                        ItemBuilder.start(Material.COAL).build(), new Champagne(), true, 100, 100,
                        "craftnbrew.vip4", "craftnbrew.lvl4"),
                new BrewingRecipe(Arrays.asList(Champagne.getChampagne(), ItemBuilder.start(Material.GOLDEN_APPLE).build()),
                        ItemBuilder.start(Material.COAL).build(), new GoldChampagne(), true, 100, 100,
                        "craftnbrew.vip5", "craftnbrew.lvl5")
        );
    }

    public static List<BrewingRecipe> getBrewingRecipes() {
        return CraftNBrewMS.BREWING_RECIPES;
    }

    public static CraftNBrewMS getInstance() {
        return CraftNBrewMS.instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new BrewEventClick(), this);
        getServer().getPluginManager().registerEvents(new NoBrewListener(), this);
        getCommand("cnbversion").setExecutor(new VersionCommand(this));

        // Register Mash recipe
        new Mash(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
