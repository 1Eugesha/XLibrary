package net.xdevelopment.xlibrary.gui.click;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickHandler {

    void handle(@NotNull ClickContext context);
}
