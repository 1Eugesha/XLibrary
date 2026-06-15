package net.xdevelopment.xlibrary.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.xdevelopment.xlibrary.core.utility.EnumUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class SkullBuilder {

    private static final String TEXTURE_PREFIX = "PLAYER_HEAD;";

    ItemStack item;

    private SkullBuilder(@NotNull ItemStack item) {
        this.item = item;
    }

    public static SkullBuilder create() {
        return new SkullBuilder(ItemStack.of(Material.PLAYER_HEAD));
    }

    public static SkullBuilder material(@NotNull Material material) {
        return new SkullBuilder(ItemStack.of(material));
    }

    public static SkullBuilder from(@NotNull String key) {
        if (key.startsWith(TEXTURE_PREFIX)) {
            return create().texture(key.substring(TEXTURE_PREFIX.length()));
        }
        final Material material = EnumUtility.get(key, Material.class);
        return material(material != null ? material : Material.BARRIER);
    }

    public SkullBuilder texture(@NotNull String base64) {
        item.editMeta(meta -> {
            if (meta instanceof SkullMeta skull) {
                final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", base64));
                skull.setPlayerProfile(profile);
            }
        });
        return this;
    }

    public SkullBuilder owner(@NotNull OfflinePlayer player) {
        item.editMeta(meta -> {
            if (meta instanceof SkullMeta skull) {
                skull.setOwningPlayer(player);
            }
        });
        return this;
    }

    public SkullBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
