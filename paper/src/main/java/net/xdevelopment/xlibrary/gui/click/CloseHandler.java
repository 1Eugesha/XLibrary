package net.xdevelopment.xlibrary.gui.click;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CloseHandler {

    void onClose(@NotNull Player player);
}
