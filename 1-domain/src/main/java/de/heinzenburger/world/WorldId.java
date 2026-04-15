package de.heinzenburger.world;

import java.util.Objects;
import java.util.UUID;

public final class WorldId {
    private final UUID value;

    public WorldId() {
        this.value = UUID.randomUUID();
    }

    public WorldId(UUID value) {
        if (value == null) throw new IllegalArgumentException("ID value cannot be null");
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldId worldId = (WorldId) o;
        return Objects.equals(value, worldId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
