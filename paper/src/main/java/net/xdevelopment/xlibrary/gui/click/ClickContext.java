package net.xdevelopment.xlibrary.gui.click;

import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public record ClickContext(@NotNull Player player,
                           @NotNull ClickType clickType,
                           @NotNull Menu menu,
                           @NotNull MenuItem item,
                           int slot) {
}
