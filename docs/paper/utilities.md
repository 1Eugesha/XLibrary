# Paper Utilities

The `net.xdevelopment.xlibrary.utility` package provides additional helper classes specific to the Bukkit/Paper API.

## ColorUtility (MiniMessage)

Gone are the days of `ChatColor.translateAlternateColorCodes('&', message)`! XLibrary actively promotes modern Adventure API `MiniMessage` standards.

With `ColorUtility`, you can format your chat strings using HTML-like tags, gradients, and seamlessly parse placeholders:

```java
import net.xdevelopment.xlibrary.utility.ColorUtility;

// Basic text formatting
Component text = ColorUtility.colorize("<gradient:red:blue>Hello World</gradient>");

// Using Placeholders
Map<String, Object> replacements = Map.of(
    "player", player.getName(),
    "money", 500
);

Component msg = ColorUtility.colorize("You gave <money> coins to <player>!", replacements);
player.sendMessage(msg);
```

You can pass `String`, integers, or even other `Component` objects via the Map into `ColorUtility` — it will automatically bind the correct tag resolvers!

## SkullBuilder

Fluently build Player Head `ItemStack` objects without tedious Reflection. Lives in `net.xdevelopment.xlibrary.builder`.

```java
import net.xdevelopment.xlibrary.builder.SkullBuilder;

// From a Base64 texture
ItemStack customHead = SkullBuilder.create()
        .texture("eyJ0ZXh0dXJlcyI...")
        .amount(1)
        .build();

// From an owning player
ItemStack playerHead = SkullBuilder.create().owner(somePlayer).build();

// Parse a material name or a "PLAYER_HEAD;<texture>" key (unknown names fall back to BARRIER)
ItemStack parsed = SkullBuilder.from("DIAMOND").build();

// It is also supported natively inside MenuItems:
myMenu.setItem(10, new MenuItem("PLAYER_HEAD;eyJ0ZXh..."));
```

## ButtonBuilder

Fluently build clickable/hoverable chat `Component` buttons. Lives in `net.xdevelopment.xlibrary.builder`.

```java
import net.xdevelopment.xlibrary.builder.ButtonBuilder;

Component button = ButtonBuilder.of("<green>[Confirm]")
        .hover("Click to confirm")
        .command("/confirm")
        .build();

player.sendMessage(button);
```
