package net.xdevelopment.xlibrary.gui.listener;

import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

final class InteractionGuard {

    void onMenuClick(@NotNull InventoryClickEvent event, @NotNull Menu menu, @NotNull Inventory top) {
        if (!menu.isInteractDisabled()) {
            return;
        }
        final boolean topClicked = event.getClickedInventory() == top;
        final boolean shiftFromBottom = event.getClickedInventory() == event.getView().getBottomInventory() && event.isShiftClick();
        final boolean collect = event.getAction() == InventoryAction.COLLECT_TO_CURSOR;
        if (topClicked || shiftFromBottom || collect) {
            event.setCancelled(true);
        }
    }

    void onItemClick(@NotNull InventoryClickEvent event, @NotNull MenuItem item) {
        if (item.isInteractDisabled()) {
            event.setCancelled(true);
        }
    }

    void onDrag(@NotNull InventoryDragEvent event, @NotNull Menu menu) {
        if (!menu.isInteractDisabled()) {
            return;
        }
        final int topSize = event.getView().getTopInventory().getSize();
        if (event.getRawSlots().stream().anyMatch(slot -> slot < topSize)) {
            event.setCancelled(true);
        }
    }

    void onInteract(@NotNull InventoryInteractEvent event, @NotNull Menu menu) {
        if (menu.isInteractDisabled()) {
            event.setCancelled(true);
        }
    }
}
