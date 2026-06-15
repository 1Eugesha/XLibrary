package net.xdevelopment.xlibrary.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.text.Component;
import net.xdevelopment.xlibrary.core.Identifiable;
import net.xdevelopment.xlibrary.core.collection.ExpiringSet;
import net.xdevelopment.xlibrary.gui.click.CloseHandler;
import net.xdevelopment.xlibrary.utility.ColorUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Menu implements InventoryHolder, Identifiable {

    static long DEFAULT_THROTTLE_MILLIS = 100L;

    String menuId;
    UUID uniqueId = generateUniqueId();
    @Setter
    @NonFinal Component title;
    Inventory inventory;
    Int2ObjectMap<MenuItem> items = new Int2ObjectOpenHashMap<>();
    @Setter
    @NonFinal boolean interactDisabled = true;
    @Setter
    @NonFinal CloseHandler closeHandler;
    @NonFinal long throttleMillis = DEFAULT_THROTTLE_MILLIS;
    @NonFinal ExpiringSet<UUID> throttle = new ExpiringSet<>(DEFAULT_THROTTLE_MILLIS);

    public Menu(String id, String title, int rows) {
        this(id, Component.text(title), rows);
    }

    public Menu(String id, Component title, int rows) {
        this.menuId = id;
        this.title = title;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public Menu(String id, Component title, InventoryType type) {
        this.menuId = id;
        this.title = title;
        this.inventory = Bukkit.createInventory(this, type, title);
    }

    public static Builder builder() {
        return new Builder();
    }

    public MenuItem getItem(int position) {
        return items.get(position);
    }

    public Menu setItem(int position, MenuItem item) {
        item.setPosition(position);
        items.put(position, item);
        inventory.setItem(position, item.getItem());
        return this;
    }

    public Menu removeItem(int position) {
        items.remove(position);
        inventory.clear(position);
        return this;
    }

    public Menu fill(MenuItem item) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            setItem(slot, item);
        }
        return this;
    }

    public Menu fillEmpty(MenuItem item) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (!hasItem(slot)) {
                setItem(slot, item);
            }
        }
        return this;
    }

    public Menu border(MenuItem item) {
        final int size = inventory.getSize();
        final int rows = size / 9;
        for (int column = 0; column < 9; column++) {
            setItem(column, item);
            setItem(size - 9 + column, item);
        }
        for (int row = 0; row < rows; row++) {
            setItem(row * 9, item);
            setItem(row * 9 + 8, item);
        }
        return this;
    }

    public Menu pattern(List<String> pattern, Map<Character, MenuItem> legend) {
        for (int row = 0; row < pattern.size(); row++) {
            final String line = pattern.get(row);
            for (int column = 0; column < line.length() && column < 9; column++) {
                final MenuItem item = legend.get(line.charAt(column));
                if (item != null) {
                    setItem(row * 9 + column, item);
                }
            }
        }
        return this;
    }

    public Menu refreshItems() {
        inventory.clear();
        for (Int2ObjectMap.Entry<MenuItem> entry : items.int2ObjectEntrySet()) {
            inventory.setItem(entry.getIntKey(), entry.getValue().getItem());
        }
        updateViewers();
        return this;
    }

    public Menu refreshItem(int position) {
        final MenuItem item = items.get(position);
        if (item == null) {
            inventory.clear(position);
        } else {
            inventory.setItem(position, item.getItem());
        }
        updateViewers();
        return this;
    }

    public int getItemPosition(MenuItem item) {
        for (Int2ObjectMap.Entry<MenuItem> entry : items.int2ObjectEntrySet()) {
            if (entry.getValue().equals(item)) {
                return entry.getIntKey();
            }
        }
        return -1;
    }

    public boolean hasItem(int position) {
        return items.containsKey(position);
    }

    public boolean hasCloseHandler() {
        return closeHandler != null;
    }

    public void setThrottleMillis(long throttleMillis) {
        this.throttleMillis = throttleMillis;
        this.throttle = new ExpiringSet<>(Math.max(1L, throttleMillis));
    }

    private void updateViewers() {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                player.updateInventory();
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public @NotNull String getName() {
        return menuId;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static final class Builder {

        String id = "";
        @NonFinal Component title = Component.empty();
        @NonFinal int rows = 1;
        @NonFinal InventoryType type;
        @NonFinal boolean interactDisabled = true;
        @NonFinal long throttleMillis = DEFAULT_THROTTLE_MILLIS;
        @NonFinal CloseHandler closeHandler;
        final List<Consumer<Menu>> operations = new ArrayList<>();

        public Builder id(@NotNull String id) {
            this.id = id;
            return this;
        }

        public Builder title(@NotNull String title) {
            this.title = ColorUtility.colorize(title);
            return this;
        }

        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder type(@NotNull InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder interactDisabled(boolean interactDisabled) {
            this.interactDisabled = interactDisabled;
            return this;
        }

        public Builder throttle(long millis) {
            this.throttleMillis = millis;
            return this;
        }

        public Builder closeHandler(@NotNull CloseHandler closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        public Builder item(int position, @NotNull MenuItem item) {
            operations.add(menu -> menu.setItem(position, item));
            return this;
        }

        public Builder fill(@NotNull MenuItem item) {
            operations.add(menu -> menu.fill(item));
            return this;
        }

        public Builder fillEmpty(@NotNull MenuItem item) {
            operations.add(menu -> menu.fillEmpty(item));
            return this;
        }

        public Builder border(@NotNull MenuItem item) {
            operations.add(menu -> menu.border(item));
            return this;
        }

        public Builder pattern(@NotNull List<String> pattern, @NotNull Map<Character, MenuItem> legend) {
            operations.add(menu -> menu.pattern(pattern, legend));
            return this;
        }

        public Menu build() {
            final Menu menu = type != null
                    ? new Menu(id, title, type)
                    : new Menu(id, title, rows);
            menu.setInteractDisabled(interactDisabled);
            menu.setThrottleMillis(throttleMillis);
            if (closeHandler != null) {
                menu.setCloseHandler(closeHandler);
            }
            for (Consumer<Menu> operation : operations) {
                operation.accept(menu);
            }
            return menu;
        }
    }
}
