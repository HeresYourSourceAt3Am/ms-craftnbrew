package fuckyour.code.craftnbrewms.brewing;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BrewEventClick implements Listener {

    @EventHandler
    public void customPotionItemStackClick(InventoryClickEvent event) {

        Inventory inv = event.getClickedInventory();

        if (inv == null || ((event.getView().getTopInventory().getType() != InventoryType.BREWING)
                && !(event.getView().getTopInventory().getHolder() instanceof BrewingStand))) {
            return;
        }

        if (!(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT ||
                (event.getClick() == ClickType.SHIFT_LEFT && event.getClickedInventory().getType() == InventoryType.PLAYER))) {
            return;
        }

        ItemStack is = event.getCurrentItem(); // GETS ITEMSTACK THAT IS BEING CLICKED
        ItemStack is2 = event.getCursor(); // GETS CURRENT ITEMSTACK HELD ON MOUSE

        if (event.getClick() == ClickType.RIGHT && is.isSimilar(is2)) {
            return;
        }

        event.setCancelled(true);

        Player p = (Player) (event.getView().getPlayer());

        boolean compare = is.isSimilar(is2);
        ClickType type = event.getClick();

        int firstAmount = is.getAmount();
        int secondAmount = is2.getAmount();

        int stack = is.getMaxStackSize();
        int half = firstAmount / 2;

        int clickedSlot = event.getSlot();


        if (type == ClickType.SHIFT_LEFT && is != null) {
            BrewerInventory inventory = (BrewerInventory) event.getView().getTopInventory();
            if (inventory.getFuel() != null && inventory.getFuel().isSimilar(is)) {
                int diff = 64 - inventory.getFuel().getAmount();
                if (is.getAmount() > diff) {
                    is.setAmount(is.getAmount() - diff);
                    inventory.getFuel().setAmount(inventory.getFuel().getAmount() + diff);
                } else if (is.getAmount() == diff) {
                    inv.setItem(clickedSlot, null);
                    inventory.getFuel().setAmount(inventory.getFuel().getAmount() + diff);
                } else if (is.getAmount() < diff) {
                    int actual = is.getAmount();
                    inv.setItem(clickedSlot, null);
                    inventory.getFuel().setAmount(inventory.getFuel().getAmount() + actual);
                }
            } else if (inventory.getFuel() == null) {
                inventory.setFuel(is);
                inv.setItem(clickedSlot, null);
            } else {
                for (int i = 0; i < 4; i++) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (itemStack == null) {
                        inventory.setItem(i, is);
                        inv.setItem(clickedSlot, null);
                        break;
                    } else if (is.isSimilar(itemStack)) {
                        int diff = 64 - itemStack.getAmount();
                        int actual = is.getAmount();
                        if (diff >= actual) {
                            itemStack.setAmount(itemStack.getAmount() + actual);
                            inv.setItem(clickedSlot, null);
                            break;
                        } else {
                            itemStack.setAmount(itemStack.getAmount() + diff);
                            is.setAmount(is.getAmount() - diff);
                        }
                    }
                }
            }
        }


        if (type == ClickType.LEFT) {

            if (is == null || (is != null && is.getType() == Material.AIR)) {

                p.setItemOnCursor(is);
                inv.setItem(clickedSlot, is2);

            } else if (compare) {

                int used = stack - firstAmount;
                if (secondAmount <= used) {

                    is.setAmount(firstAmount + secondAmount);
                    p.setItemOnCursor(null);

                } else {

                    is2.setAmount(secondAmount - used);
                    is.setAmount(firstAmount + used);
                    p.setItemOnCursor(is2);

                }

            } else if (!compare) {

                inv.setItem(clickedSlot, is2);
                p.setItemOnCursor(is);

            }

        } else if (type == ClickType.RIGHT) {

            if (is == null || (is != null && is.getType() == Material.AIR)) {


                ItemStack clone = is2.clone();
                clone.setAmount(1);

                is2.setAmount(is2.getAmount()-1);

                p.setItemOnCursor(is2);
                inv.setItem(clickedSlot, clone);

            } else if ((is != null && is.getType() != Material.AIR) &&
                    (is2 == null || (is2 != null && is2.getType() == Material.AIR))) {

                ItemStack isClone = is.clone();
                isClone.setAmount(is.getAmount() % 2 == 0 ? firstAmount - half : firstAmount - half - 1);
                p.setItemOnCursor(isClone);

                is.setAmount(firstAmount - half);

            } else if (compare) {

                if ((firstAmount + 1) <= stack) {

                    is2.setAmount(secondAmount - 1);
                    is.setAmount(firstAmount + 1);

                }

            } else if (!compare) {

                inv.setItem(clickedSlot, is2);
                p.setItemOnCursor(is);
            }

        }

        BrewingRecipe recipe = BrewingRecipe.getRecipe((BrewerInventory) event.getView().getTopInventory());

        if (recipe == null) {
            return;
        }

        if (!p.hasPermission(recipe.getLevelPermission())) {
            return;
        }


        // todo: make the items stackable
        // todo: fix shift clicking of items

        if (recipe.getAction().canBrew((BrewerInventory) event.getView().getTopInventory(), recipe) && (event.getClickedInventory().getType() ==
                InventoryType.BREWING))
            recipe.startBrewing((BrewerInventory) event.getView().getTopInventory(), p);

    }
}
