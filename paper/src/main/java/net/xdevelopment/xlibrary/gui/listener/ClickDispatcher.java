package net.xdevelopment.xlibrary.gui.listener;

import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import net.xdevelopment.xlibrary.gui.click.ClickContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

final class ClickDispatcher {

    void dispatch(@NotNull InventoryClickEvent event, @NotNull Menu menu, @NotNull MenuItem item, @NotNull Player player) {
        if (!item.hasClickHandler()) {
            return;
        }
        if (isThrottled(menu, player)) {
            event.setCancelled(true);
            return;
        }
        item.handle(new ClickContext(player, event.getClick(), menu, item, event.getSlot()));
    }

    private boolean isThrottled(@NotNull Menu menu, @NotNull Player player) {
        return menu.getThrottleMillis() > 0 && !menu.getThrottle().add(player.getUniqueId());
    }
}
