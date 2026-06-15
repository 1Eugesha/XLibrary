// Enum methods adapted and improved from: https://github.com/nulli0n/nightcore-spigot
package net.xdevelopment.xlibrary.core.utility;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class EnumUtility {

    @NotNull
    public <T extends Enum<T>> Optional<T> parse(String str, @NotNull Class<T> clazz) {
        try {
            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(clazz, str.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    @Nullable
    public <T extends Enum<T>> T get(String str, @NotNull Class<T> clazz) {
        return parse(str, clazz).orElse(null);
    }

    @NotNull
    public String inline(@NotNull Class<? extends Enum<?>> clazz) {
        return inline(clazz, ", ");
    }

    @NotNull
    public String inline(@NotNull Class<? extends Enum<?>> clazz, @NotNull String delimiter) {
        return String.join(delimiter, getNames(clazz));
    }

    @NotNull
    public List<String> getNames(@NotNull Class<? extends Enum<?>> clazz) {
        return Stream.of(clazz.getEnumConstants()).map(Enum::name).toList();
    }

    @NotNull
    public <T extends Enum<T>> T next(@NotNull Enum<T> numeration) {
        return shifted(numeration, 1);
    }

    @NotNull
    public <T extends Enum<T>> T next(@NotNull Enum<T> numeration, @NotNull Predicate<T> predicate) {
        return shifted(numeration, 1, predicate);
    }

    @NotNull
    public <T extends Enum<T>> T previous(@NotNull Enum<T> numeration) {
        return shifted(numeration, -1);
    }

    @NotNull
    public <T extends Enum<T>> T previous(@NotNull Enum<T> numeration, @NotNull Predicate<T> predicate) {
        return shifted(numeration, -1, predicate);
    }

    @NotNull
    public <T extends Enum<T>> T shifted(@NotNull Enum<T> numeration, int shift) {
        return shifted(numeration, shift, null);
    }

    @NotNull
    private <T extends Enum<T>> T shifted(@NotNull Enum<T> numeration, int shift, @Nullable Predicate<T> predicate) {
        T[] values = numeration.getDeclaringClass().getEnumConstants();
        return shifted(values, numeration, shift, predicate);
    }

    @NotNull
    private <T extends Enum<T>> T shifted(T[] values, @NotNull Enum<T> origin, int shift, @Nullable Predicate<T> predicate) {
        if (predicate != null) {
            T source = origin.getDeclaringClass().cast(origin);
            List<T> filtered = new ArrayList<>(Arrays.asList(values));
            filtered.removeIf(num -> !predicate.test(num) && num != source);

            int currentIndex = filtered.indexOf(source);
            if (currentIndex < 0 || filtered.isEmpty()) return source;

            return shifted(filtered, currentIndex, shift);
        }
        return shifted(values, origin.ordinal(), shift);
    }

    @NotNull
    public <T> T shifted(T[] values, int currentIndex, int shift) {
        int index = currentIndex + shift;
        return values[index >= values.length || index < 0 ? 0 : index];
    }

    @NotNull
    public <T> T shifted(@NotNull List<T> values, int currentIndex, int shift) {
        if (values.isEmpty()) throw new IllegalArgumentException("List is empty");
        int index = currentIndex + shift;
        if (index < 0) return values.get(values.size() - 1);
        return values.get(index >= values.size() ? 0 : index);
    }
}
