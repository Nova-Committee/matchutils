package committee.nova.matchutils.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A utility class which provides more convenient pattern matching methods.
 * <p>
 * To match all the entries, use {@link Match#matchAll()}.
 * <p>
 * To keep matching the entries until a successful match, use {@link Match#matchFirst()}.
 *
 * @param <T> the type of this match
 * @author pkstDev
 */
public class Match<T> {
    private final T object;
    private final Map<Predicate<T>, Consumer<T>> entries;
    private Consumer<T> defaultEntry;
    private boolean isDefaultAdded;

    /**
     * Constructs a new match.
     *
     * @param object the object to be matched
     */
    public Match(T object) {
        this.isDefaultAdded = false;
        this.object = object;
        this.entries = new HashMap<>();
        this.defaultEntry = null;
    }

    /**
     * Adds the default entry which will be run if no match is successful.
     *
     * @param defaultEntry the default entry of this match
     * @return this match
     */
    public Match<T> addDefault(Consumer<T> defaultEntry) {
        if (isDefaultAdded) throw new IllegalStateException("Default branch is already added");
        this.defaultEntry = defaultEntry;
        this.isDefaultAdded = true;
        return this;
    }

    /**
     * Adds a match entry.
     *
     * @param predicate the predicate of this entry
     * @param func the function executed if match of this entry is successful
     * @return this match
     */
    public Match<T> addEntry(Predicate<T> predicate, Consumer<T> func) {
        entries.put(predicate, func);
        return this;
    }

    /**
     * Match the object with all the entries and execute functions of all the successful match (excluding the default entry).
     */
    public void matchAll() {
        AtomicBoolean isConsumed = new AtomicBoolean(false);
        entries.forEach((predicate, func) -> {
            if (predicate.test(object)) {
                func.accept(object);
                isConsumed.set(true);
            }
        });
        if (!isConsumed.get() && defaultEntry != null) {
            defaultEntry.accept(object);
        }
    }

    /**
     * Match the object with all the entries and execute the function of the first successful match (excluding the default entry).
     */
    public void matchFirst() {
        AtomicBoolean isConsumed = new AtomicBoolean(false);
        for (Map.Entry<Predicate<T>, Consumer<T>> entry : entries.entrySet()) {
            if (entry.getKey().test(object)) {
                entry.getValue().accept(object);
                isConsumed.set(true);
                break;
            }
        }
        if (!isConsumed.get() && defaultEntry != null) {
            defaultEntry.accept(object);
        }
    }
}
