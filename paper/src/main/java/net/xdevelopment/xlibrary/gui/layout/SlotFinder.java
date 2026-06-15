package net.xdevelopment.xlibrary.gui.layout;

import lombok.experimental.UtilityClass;
import net.xdevelopment.xlibrary.gui.Menu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class SlotFinder {

    public int firstEmptySlot(Menu menu, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            final ItemStack item = menu.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                return i;
            }
        }
        return -1;
    }
}
