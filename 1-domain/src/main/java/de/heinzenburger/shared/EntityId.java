package de.heinzenburger.shared;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for type-safe entity identifiers.
 * Uses getClass() check in equals() to ensure PlayerId != AnimalId even with same UUID.
 */
public abstract class EntityId {
    private final UUID value;

    protected EntityId() {
        this.value = UUID.randomUUID();
    }

    protected EntityId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ID value cannot be null");
        }
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return Objects.equals(value, entityId.value);
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
