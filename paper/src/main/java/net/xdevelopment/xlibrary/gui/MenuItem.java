package net.xdevelopment.xlibrary.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.text.Component;
import net.xdevelopment.xlibrary.gui.click.ClickContext;
import net.xdevelopment.xlibrary.gui.click.ClickHandler;
import net.xdevelopment.xlibrary.utility.Displayable;
import net.xdevelopment.xlibrary.builder.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(chain = true, makeFinal = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SuppressWarnings("UnstableApiUsage")
public final class MenuItem {

    @NonFinal ItemStack item;
    @NonFinal ClickHandler anyHandler;
    Map<ClickType, ClickHandler> handlers = new EnumMap<>(ClickType.class);
    @NonFinal boolean interactDisabled;
    @Setter
    @NonFinal int position;
    @NonFinal boolean staticItem;

    public MenuItem(@NotNull ItemStack item) {
        this.item = item;
    }

    public MenuItem(@NotNull Material material) {
        this.item = ItemStack.of(material);
    }

    public MenuItem(@NotNull String materialKey) {
        this.item = SkullBuilder.from(materialKey).build();
    }

    public MenuItem(@NotNull Displayable displayable) {
        this.item = ItemStack.of(displayable.getIcon());
        this.item.setData(DataComponentTypes.CUSTOM_NAME, Component.text(displayable.getDisplayName()));
    }

    public MenuItem(@NotNull Material material, @NotNull String display, @NotNull List<String> lore) {
        this.item = ItemStack.of(material);
        this.item.setData(DataComponentTypes.CUSTOM_NAME, Component.text(display));
        this.item.setData(DataComponentTypes.LORE, ItemLore.lore()
                .addLines(lore.stream().map(Component::text).toList())
                .build());
        this.item.editMeta(meta -> meta.setHideTooltip(true));
    }

    public static Builder builder() {
        return new Builder();
    }

    public MenuItem setItem(@NotNull ItemStack item) {
        this.item = item;
        return this;
    }

    public MenuItem display(@NotNull String display) {
        item.setData(DataComponentTypes.CUSTOM_NAME, Component.text(display));
        return this;
    }

    public MenuItem lore(@NotNull List<String> lore) {
        item.setData(DataComponentTypes.LORE, ItemLore.lore()
                .addLines(lore.stream().map(Component::text).toList())
                .build());
        return this;
    }

    public MenuItem amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public MenuItem hideAttributes(boolean hide) {
        item.editMeta(meta -> meta.setHideTooltip(hide));
        return this;
    }

    public MenuItem enchanted(boolean enchanted) {
        item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchanted);
        return this;
    }

    public MenuItem disableInteract(boolean disable) {
        this.interactDisabled = disable;
        return this;
    }

    public MenuItem headOwner(@NotNull OfflinePlayer player) {
        item.editMeta(meta -> {
            if (meta instanceof SkullMeta skull) {
                skull.setOwningPlayer(player);
            }
        });
        return this;
    }

    public MenuItem headTexture(@NotNull String value) {
        this.item = SkullBuilder.create().texture(value).build();
        return this;
    }

    public MenuItem staticItem(boolean staticItem) {
        this.staticItem = staticItem;
        return this;
    }

    public boolean isStatic() {
        return staticItem;
    }

    public MenuItem onClick(@NotNull ClickHandler handler) {
        this.anyHandler = handler;
        return this;
    }

    public MenuItem onLeft(@NotNull ClickHandler handler) {
        handlers.put(ClickType.LEFT, handler);
        return this;
    }

    public MenuItem onRight(@NotNull ClickHandler handler) {
        handlers.put(ClickType.RIGHT, handler);
        return this;
    }

    public MenuItem onMiddle(@NotNull ClickHandler handler) {
        handlers.put(ClickType.MIDDLE, handler);
        return this;
    }

    public MenuItem onShiftLeft(@NotNull ClickHandler handler) {
        handlers.put(ClickType.SHIFT_LEFT, handler);
        return this;
    }

    public MenuItem onShiftRight(@NotNull ClickHandler handler) {
        handlers.put(ClickType.SHIFT_RIGHT, handler);
        return this;
    }

    public boolean hasClickHandler() {
        return anyHandler != null || !handlers.isEmpty();
    }

    public void handle(@NotNull ClickContext context) {
        if (anyHandler != null) {
            anyHandler.handle(context);
        }
        final ClickHandler typed = handlers.get(context.clickType());
        if (typed != null) {
            typed.handle(context);
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static final class Builder {

        @NonFinal MenuItem target;

        public Builder item(@NotNull ItemStack item) {
            this.target = new MenuItem(item);
            return this;
        }

        public Builder material(@NotNull Material material) {
            this.target = new MenuItem(material);
            return this;
        }

        public Builder head(@NotNull String materialKey) {
            this.target = new MenuItem(materialKey);
            return this;
        }

        public Builder displayable(@NotNull Displayable displayable) {
            this.target = new MenuItem(displayable);
            return this;
        }

        public Builder display(@NotNull String display) {
            return apply(item -> item.display(display));
        }

        public Builder lore(@NotNull List<String> lore) {
            return apply(item -> item.lore(lore));
        }

        public Builder amount(int amount) {
            return apply(item -> item.amount(amount));
        }

        public Builder enchanted(boolean enchanted) {
            return apply(item -> item.enchanted(enchanted));
        }

        public Builder hideAttributes(boolean hide) {
            return apply(item -> item.hideAttributes(hide));
        }

        public Builder disableInteract(boolean disable) {
            return apply(item -> item.disableInteract(disable));
        }

        public Builder staticItem(boolean staticItem) {
            return apply(item -> item.staticItem(staticItem));
        }

        public Builder headOwner(@NotNull OfflinePlayer player) {
            return apply(item -> item.headOwner(player));
        }

        public Builder headTexture(@NotNull String value) {
            return apply(item -> item.headTexture(value));
        }

        public Builder onClick(@NotNull ClickHandler handler) {
            return apply(item -> item.onClick(handler));
        }

        public Builder onLeft(@NotNull ClickHandler handler) {
            return apply(item -> item.onLeft(handler));
        }

        public Builder onRight(@NotNull ClickHandler handler) {
            return apply(item -> item.onRight(handler));
        }

        public Builder onMiddle(@NotNull ClickHandler handler) {
            return apply(item -> item.onMiddle(handler));
        }

        public Builder onShiftLeft(@NotNull ClickHandler handler) {
            return apply(item -> item.onShiftLeft(handler));
        }

        public Builder onShiftRight(@NotNull ClickHandler handler) {
            return apply(item -> item.onShiftRight(handler));
        }

        public MenuItem build() {
            require();
            return target;
        }

        private Builder apply(@NotNull java.util.function.Consumer<MenuItem> action) {
            require();
            action.accept(target);
            return this;
        }

        private void require() {
            if (target == null) {
                throw new IllegalStateException("Define a base via material/item/head/displayable first");
            }
        }
    }
}
