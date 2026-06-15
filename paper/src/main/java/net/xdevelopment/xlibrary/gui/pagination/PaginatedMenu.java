package net.xdevelopment.xlibrary.gui.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.xdevelopment.xlibrary.core.utility.Paginator;
import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import net.xdevelopment.xlibrary.gui.Menus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PaginatedMenu {

    Menu menu;
    List<Integer> contentSlots;
    @NonFinal Paginator<MenuItem> paginator;
    @NonFinal int page = 1;

    public PaginatedMenu(@NotNull Menu menu, @NotNull List<Integer> contentSlots, @NotNull List<MenuItem> content) {
        if (contentSlots.isEmpty()) {
            throw new IllegalArgumentException("contentSlots must not be empty");
        }
        this.menu = menu;
        this.contentSlots = contentSlots;
        this.paginator = new Paginator<>(content, contentSlots.size());
    }

    public int totalPages() {
        return paginator.getTotalPages();
    }

    public boolean hasNext() {
        return page < totalPages();
    }

    public boolean hasPrevious() {
        return page > 1;
    }

    public PaginatedMenu setContent(@NotNull List<MenuItem> content) {
        this.paginator = new Paginator<>(content, contentSlots.size());
        this.page = clamp(page);
        return this;
    }

    public PaginatedMenu page(int page) {
        this.page = clamp(page);
        return render();
    }

    public PaginatedMenu next() {
        return page(page + 1);
    }

    public PaginatedMenu previous() {
        return page(page - 1);
    }

    public PaginatedMenu navigation(int previousSlot, @NotNull MenuItem previousItem, int nextSlot, @NotNull MenuItem nextItem) {
        menu.setItem(previousSlot, previousItem.onClick(context -> previous()));
        menu.setItem(nextSlot, nextItem.onClick(context -> next()));
        return this;
    }

    public PaginatedMenu render() {
        for (int slot : contentSlots) {
            menu.removeItem(slot);
        }
        final List<MenuItem> current = paginator.getPage(page);
        for (int i = 0; i < current.size(); i++) {
            menu.setItem(contentSlots.get(i), current.get(i));
        }
        menu.refreshItems();
        return this;
    }

    public void open(@NotNull Player player) {
        render();
        Menus.open(player, menu);
    }

    private int clamp(int page) {
        return Math.max(1, Math.min(page, Math.max(1, totalPages())));
    }
}
