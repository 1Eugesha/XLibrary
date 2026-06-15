package net.xdevelopment.xlibrary.gui;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.xdevelopment.xlibrary.utility.ColorUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@UtilityClass
@SuppressWarnings("UnstableApiUsage")
public class Menus {

    public Menu create(String id, String title, int rows) {
        return new Menu(id, ColorUtility.colorize(title), rows);
    }

    public Menu create(String id, Component title, int rows) {
        return new Menu(id, title, rows);
    }

    public Menu create(String id, String title, InventoryType type) {
        return new Menu(id, ColorUtility.colorize(title), type);
    }

    public Menu from(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder(false);
        if (holder instanceof Menu menu) {
            return menu;
        }
        return null;
    }

    public void open(Player player, Menu menu) {
        player.openInventory(menu.getInventory());
    }

    public void reopen(Player player) {
        final Inventory top = player.getOpenInventory().getTopInventory();
        if (top.getHolder(false) instanceof Menu menu) {
            player.openInventory(menu.getInventory());
        }
    }
}
