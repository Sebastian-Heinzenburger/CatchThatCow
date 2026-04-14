package de.heinzenburger.position;

import java.util.Objects;

public final class MapUnit {
    private final int value;

    public MapUnit(int value) {
        this.value = value;
    }

    public int toInt() {
        return this.value;
    }

    public MapUnit increment() {
        return new MapUnit(this.value + 1);
    }

    public MapUnit decrement() {
        return new MapUnit(this.value - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MapUnit mapUnit = (MapUnit) o;
        return value == mapUnit.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
