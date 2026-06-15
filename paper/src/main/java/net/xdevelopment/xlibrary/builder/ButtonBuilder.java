package net.xdevelopment.xlibrary.builder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ButtonBuilder {

    @NonFinal Component button;

    private ButtonBuilder(@NotNull Component button) {
        this.button = button;
    }

    public static ButtonBuilder of(@NotNull String text) {
        return new ButtonBuilder(Component.text(text));
    }

    public ButtonBuilder hover(@NotNull String text) {
        this.button = button.hoverEvent(HoverEvent.showText(Component.text(text)));
        return this;
    }

    public ButtonBuilder command(@NotNull String command) {
        this.button = button.clickEvent(ClickEvent.runCommand(command));
        return this;
    }

    public ButtonBuilder suggest(@NotNull String text) {
        this.button = button.clickEvent(ClickEvent.suggestCommand(text));
        return this;
    }

    public Component build() {
        return button;
    }
}
