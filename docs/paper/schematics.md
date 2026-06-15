# Schematics (FAWE)

XLibrary ships a single, lightweight schematic helper — `FaweSchematics` — built on top of
[FastAsyncWorldEdit](https://www.spigotmc.org/resources/fastasyncworldedit.13932/). It exposes
asynchronous **save**, **paste** and **undo** operations over the standard Sponge `.schem` format.

> **Note:** The previous custom, FAWE-independent **native** engine (`.json` format) has been removed.
> Only the FAWE-based helper is currently shipped — the native engine will return in a future release.

## Setup

`FastAsyncWorldEdit` must be present and enabled on the server. The constructor throws an
`IllegalStateException` otherwise.

```java
import net.xdevelopment.xlibrary.schematic.FaweSchematics;

FaweSchematics schematics = new FaweSchematics();
```

## Operations

Every operation runs off the main thread and returns a `CompletableFuture`.

### Saving

```java
File targetFile = new File(plugin.getDataFolder(), "my_schematic.schem");

// The final boolean flag (true) ignores air blocks while copying the region.
schematics.save(targetFile, minLocation, maxLocation, true).thenAccept(file -> {
    player.sendMessage("Schematic fully saved to disk!");
});
```

### Pasting

```java
schematics.paste(targetFile, pasteLocation).thenAccept(session -> {
    // The session is an EditSession dedicated to this paste.
    // Cache it if you intend to undo this action later.
    lastSessions.put(player.getUniqueId(), session);
});
```

### Undoing

```java
EditSession session = lastSessions.get(player.getUniqueId());
if (session != null) {
    schematics.undo(session);
}
```
