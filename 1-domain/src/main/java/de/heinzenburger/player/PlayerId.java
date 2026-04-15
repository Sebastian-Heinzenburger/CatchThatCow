package de.heinzenburger.player;

import java.util.Objects;
import java.util.UUID;

public final class PlayerId {
    private final UUID value;

    public PlayerId() {
        this.value = UUID.randomUUID();
    }

    public PlayerId(UUID value) {
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
        PlayerId playerId = (PlayerId) o;
        return Objects.equals(value, playerId.value);
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
