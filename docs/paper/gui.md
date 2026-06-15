# GUI

Building graphical interfaces without XLibrary typically involves creating custom `InventoryHolder` objects, listening heavily to `InventoryClickEvent`, executing checks like `if (e.getInventory() == menu)`, and manually canceling clicks.

With the classes in the `net.xdevelopment.xlibrary.gui` package, this process shrinks to just a few neat lines of code.

Register the built-in `MenuListener` once on startup and everything below just works.

## Menu

Use the fluent `Menu.builder()`:

```java
Menu menu = Menu.builder()
        .id("shop")
        .title("<gold>Shop")
        .rows(6)
        .throttle(150)
        .item(13, someItem)
        .closeHandler(player -> player.sendMessage("Menu closed!"))
        .build();
```

By default the menu cancels all interaction clicks (`interactDisabled = true`). Call `.interactDisabled(false)` to allow item movement.

The classic constructors (`new Menu(id, title, rows)`) and `Menus.create(...)` / `Menus.open(player, menu)` are still available.

## MenuItem

`MenuItem` encapsulates the item and its click logic. Build it via `MenuItem.builder()`:

```java
MenuItem item = MenuItem.builder()
        .material(Material.GOLD_INGOT)
        .display("<yellow>Gold")
        .lore(List.of("<gray>Click me!"))
        .amount(5)
        .enchanted(true)
        .onClick(context -> context.player().sendMessage("Clicked!"))
        .build();
```

The base is set with `.material(...)`, `.item(ItemStack)`, `.head(key)` or `.displayable(...)`. The old fluent style (`new MenuItem(Material.GOLD_INGOT).display(...)`) still works too.

## Click handling (ClickContext)

`ClickHandler` is a functional interface, so handlers are plain lambdas receiving a `ClickContext` (`player`, `clickType`, `menu`, `item`, `slot`):

```java
item.onClick(context -> context.player().sendMessage("Any click!"));   // every click
item.onLeft(context -> ...);                                           // left only
item.onRight(context -> ...);                                          // right only
item.onShiftLeft(context -> ...);
```

`onClick` runs on every click; the typed handlers run additionally for their specific click type. The `MenuListener` has a configurable per-menu anti-spam throttle (`.throttle(millis)`, default 100ms) so players cannot crash the menu by spamming clicks.

## Layout helpers

```java
Menu.builder()
        .id("menu").rows(6)
        .border(grayPane)          // outer frame
        .fillEmpty(blackPane)      // fill remaining empty slots
        .pattern(
            List.of(
                "XXXXXXXXX",
                "X       X",
                "XXXXXXXXX"
            ),
            Map.of('X', grayPane)
        )
        .build();
```

The same helpers exist on a live `Menu`: `fill`, `fillEmpty`, `border`, `pattern`.

## Pagination

`PaginatedMenu` (in `net.xdevelopment.xlibrary.gui.pagination`) reuses the core `Paginator` to slice a list into pages and render them into the chosen content slots:

```java
Menu frame = Menu.builder().id("list").rows(6).border(grayPane).build();

List<Integer> contentSlots = List.of(10, 11, 12, 13, 14, 15, 16); // where items appear
PaginatedMenu paginated = new PaginatedMenu(frame, contentSlots, allItems);

paginated.navigation(
        48, MenuItem.builder().material(Material.ARROW).display("<gray>Back").build(),
        50, MenuItem.builder().material(Material.ARROW).display("<gray>Next").build()
);

paginated.open(player);
```

Navigation buttons call `previous()` / `next()` automatically. Pages are 1-based; `totalPages()`, `hasNext()`, `hasPrevious()` and `setContent(...)` are available for custom controls.
