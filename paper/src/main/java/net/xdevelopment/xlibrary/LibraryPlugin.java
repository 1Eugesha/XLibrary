package net.xdevelopment.xlibrary;

import net.xdevelopment.xlibrary.gui.listener.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LibraryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }
}
