package net.xdevelopment.xlibrary.gui.listener;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("UnstableApiUsage")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuListener implements Listener {

    InteractionGuard guard = new InteractionGuard();
    ClickDispatcher dispatcher = new ClickDispatcher();

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder(false) instanceof Menu menu)) {
            return;
        }

        guard.onMenuClick(event, menu, top);

        if (event.getAction() == InventoryAction.NOTHING || event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory() != top) {
            return;
        }

        final MenuItem item = menu.getItem(event.getSlot());
        if (item == null) {
            return;
        }

        guard.onItemClick(event, item);
        dispatcher.dispatch(event, menu, item, player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder(false) instanceof Menu menu) {
            guard.onDrag(event, menu);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder(false) instanceof Menu menu
                && menu.hasCloseHandler()
                && event.getPlayer() instanceof Player player) {
            menu.getCloseHandler().onClose(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent || event instanceof InventoryDragEvent) {
            return;
        }
        if (event.getView().getTopInventory().getHolder(false) instanceof Menu menu) {
            guard.onInteract(event, menu);
        }
    }
}
