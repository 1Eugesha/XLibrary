# Getting Started

Welcome to XLibrary — a powerful framework for developing Paper plugins!

XLibrary is built to replace the standard approach to creating plugins. The library allows you to easily manage configurations, build multi-level commands with TabCompletion, interact with GUI menus cleanly without inventory event spaghetti, and manipulate schematics through a lightweight FastAsyncWorldEdit helper.

## Installation

To use XLibrary in your plugin, add it to your dependencies in `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    // Specify your local/private repository here if XLibrary is hosted there.
}

dependencies {
    compileOnly("net.xdevelopment:xlibrary-paper:1.0.0-SNAPSHOT")
}
```

Do not forget to specify `depend` in your plugin's `plugin.yml`:
```yaml
name: MyAwesomePlugin
version: 1.0.0
main: com.example.plugin.MyPlugin
api-version: 1.21
depend: [XLibrary]
```

## Quick Example

Here is an example of how easily you can create a graphical menu:

```java
import net.xdevelopment.xlibrary.gui.Menu;
import net.xdevelopment.xlibrary.gui.MenuItem;
import org.bukkit.Material;

Menu myMenu = Menu.builder()
    .id("my_menu")
    .title("Super Menu")
    .rows(3)
    .item(13, MenuItem.builder()
        .material(Material.DIAMOND)
        .display("<green>Click me</green>")
        .onClick(context -> context.player().sendMessage("You clicked!"))
        .build())
    .build();

player.openInventory(myMenu.getInventory());
```
