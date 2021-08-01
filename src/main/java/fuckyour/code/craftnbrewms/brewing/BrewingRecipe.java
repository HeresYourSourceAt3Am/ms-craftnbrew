package fuckyour.code.craftnbrewms.brewing;

import fuckyour.code.craftnbrewms.CraftNBrewMS;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class BrewingRecipe {

    private final List<ItemStack> ingredients;
    private final ItemStack fuel;
    private final String vipPermission;
    private final String levelPermission;
    private int fuelSet;
    private int fuelCharge;
    private BrewAction action;
    private BrewClock clock;
    private boolean perfect;

    public BrewingRecipe(List<ItemStack> ingredients, ItemStack fuel, BrewAction action, boolean perfect, int fuelSet,
                         int fuelCharge, String vipPermission, String levelPermission) {
        this.ingredients = ingredients;
        this.fuel = (fuel == null ? new ItemStack(Material.AIR) : fuel);
        this.setFuelSet(fuelSet);
        this.setFuelCharge(fuelCharge);
        this.action = action;
        this.perfect = perfect;
        this.vipPermission = vipPermission;
        this.levelPermission = levelPermission;
    }

    public static BrewingRecipe getRecipe(BrewerInventory inventory) {
        for (BrewingRecipe recipe : CraftNBrewMS.getBrewingRecipes()) {
            if (inventory.getFuel() == null) {
                if (!recipe.isPerfect() && matchIngredients(recipe, inventory)) {
                    return recipe;
                }
                if (recipe.isPerfect() && matchIngredients(recipe, inventory)) {
                    return recipe;
                }
            } else {
                if (!recipe.isPerfect() && matchIngredients(recipe, inventory) &&
                        inventory.getFuel().getType() == recipe.getFuel().getType()) {
                    return recipe;
                }
                if (recipe.isPerfect() && matchIngredients(recipe, inventory) &&
                        inventory.getFuel().isSimilar(recipe.getFuel())) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private static boolean matchIngredients(BrewingRecipe recipe, BrewerInventory inventory) {
        if (recipe.isPerfect()) {
            for (ItemStack ingredient : recipe.getIngredient()) {
                // we only go to three and not 4 to ignore the fuel slot.
                boolean found = false;
                for (int i = 0; i < 4; i++) {
                    if (inventory.getItem(i) != null &&
                            Objects.requireNonNull(inventory.getItem(i)).isSimilar(ingredient)) found = true;
                }
                if (!found) return false;
            }
        } else {
            for (ItemStack ingredient : recipe.getIngredient()) {
                if (!inventory.contains(ingredient.getType())) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getVipPermission() {
        return vipPermission;
    }

    public String getLevelPermission() {
        return levelPermission;
    }

    public List<ItemStack> getIngredient() {
        return ingredients;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public BrewAction getAction() {
        return action;
    }

    public void setAction(BrewAction action) {
        this.action = action;
    }

    public BrewClock getClock() {
        return clock;
    }

    public void setClock(BrewClock clock) {
        this.clock = clock;
    }

    public boolean isPerfect() {
        return perfect;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
    }

    public void startBrewing(BrewerInventory inventory, Player p) {
        clock = new BrewClock(this, inventory, 400, p);
    }

    public int getFuelSet() {
        return fuelSet;
    }

    public void setFuelSet(int fuelSet) {
        this.fuelSet = fuelSet;
    }

    public int getFuelCharge() {
        return fuelCharge;
    }

    public void setFuelCharge(int fuelCharge) {
        this.fuelCharge = fuelCharge;
    }

    /*
     * Slot 0: 3 Potion Slot Far Left
     * Slot 1: 3 Potion Slot Middle
     * Slot 2: 3 Potion Slot Far Right
     * Slot 3: Ingredient Slot
     * Slot 4: Fuel
     */
    public class BrewClock extends BukkitRunnable {
        private BrewerInventory inventory;
        private BrewingRecipe recipe;
        private ItemStack[] before;
        private BrewingStand stand;
        private int current;
        private Player player;

        public BrewClock(BrewingRecipe recipe, BrewerInventory inventory, int time, Player p) {
            this.recipe = recipe;
            this.inventory = inventory;
            this.stand = inventory.getHolder();
            this.before = inventory.getContents();
            this.current = time;
            this.player = p;
            runTaskTimer(CraftNBrewMS.getInstance(), 0L, 1L);
        }

        @Override
        public void run() {
            if (current == 0) {

                // Set ingredient to 1 less than the current. Otherwise set to air
                boolean match = matchIngredients(recipe, inventory);
                for (ItemStack itemStack : recipe.getIngredient()) {
                    for (int i = 0; i < 4; i++) {
                        if (inventory.getItem(i) != null && (perfect ? inventory.getItem(i).isSimilar(itemStack) :
                                inventory.getItem(i).getType() == itemStack.getType())) {
                            if (inventory.getItem(i).getAmount() < 2) {
                                inventory.setItem(i, new ItemStack(Material.AIR));
                            } else {
                                inventory.getItem(i).setAmount(inventory.getItem(i).getAmount() - 1);
                            }
                        }
                    }
                }
                // Check the fuel in the recipe is more than 0, and exists
                ItemStack newFuel = recipe.getFuel();
                if (recipe.getFuel() != null && recipe.getFuel().getType() != Material.AIR &&
                        recipe.getFuel().getAmount() > 0) {
                    /*
                     * We count how much fuel should be taken away in order to fill
                     * the whole fuel bar
                     */
                    int count = 0;
                    while (inventory.getFuel().getAmount() > 0 && stand.getFuelLevel() + recipe.fuelCharge <= 100) {
                        stand.setFuelLevel(stand.getFuelLevel() + recipe.fuelSet);
                        count++;
                    }
                    // If the fuel in the inventory is 0, set it to air.
                    if (inventory.getFuel().getAmount() == 0) {
                        newFuel = new ItemStack(Material.AIR);
                    } else {
                        /* Otherwise, set the percent of fuel level to 100 and update the
                         *  count of the fuel
                         */
                        stand.setFuelLevel(100);
                        newFuel.setAmount(inventory.getFuel().getAmount() - count);
                    }
                } else {
                    newFuel = new ItemStack(Material.AIR);
                }
                inventory.setFuel(newFuel);
                // Brew recipe for each item put in
                if (match) {
                    // Recipe is correct

                    recipe.getAction().brew(inventory, recipe);
                } else {
                    // Recipe incorrect, something went wrong somewhere
                    cancel();
                    return;
                }
                // Set the fuel level
                stand.setFuelLevel(stand.getFuelLevel() - recipe.fuelCharge);
                BrewingRecipe recipe = getRecipe(inventory);
                if (recipe != null) {
                    if (player.hasPermission(recipe.getLevelPermission())) {
                        recipe.startBrewing(inventory, player);
                    }
                }
                cancel();
                return;
            }
            // If a player drags an item, fuel, or any contents, reset it
            if (searchChanged(before, inventory.getContents(), perfect)) {
                cancel();
                return;
            }


            if(stand.getInventory().getFuel() == null) {
                cancel();
                return;
            }
            if(stand.getInventory().getFuel().getAmount() < 1) {
                cancel();
                return;
            }

            // Decrement, set the brewing time, and update the stand
            current--;
            stand.setBrewingTime(current);
            stand.update(true);
        }

        // Check if any slots were changed
        public boolean searchChanged(ItemStack[] before, ItemStack[] after, boolean perfect) {
            for (int i = 0; i < before.length; i++) {
                if (before[i] != null && after[i] == null) return true;
                if (before[i] == null && after[i] != null) return true;
                if (before[i] != null && after[i] != null) {
                    if (perfect) {
                        if (!before[i].isSimilar(after[i])) return true;
                        if(before[i].getAmount() != after[i].getAmount()) return true;
                    } else {
                        if (!(before[i].getType() == after[i].getType())) return true;
                    }
                }
            }
            return false;
        }
    }

}