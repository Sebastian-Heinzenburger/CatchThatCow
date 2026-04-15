package de.heinzenburger.shared;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class AnimalStats {
    private final Map<StatCategory, Integer> stats;

    public AnimalStats(Map<StatCategory, Integer> stats) {
        if (stats == null || stats.isEmpty())
            throw new IllegalArgumentException("Stats cannot be null or empty: " + stats);

        for (StatCategory category : StatCategory.values()) {
            if (!stats.containsKey(category))
                throw new IllegalArgumentException("Missing stat for category: " + category);

            if (stats.get(category) < 0)
                throw new IllegalArgumentException("Stat value cannot be negative for category: " + category);
        }

        this.stats = new EnumMap<>(stats);
    }

    public int getStat(StatCategory category) {
        return stats.get(category);
    }

    public Map<StatCategory, Integer> getAllStats() {
        return Collections.unmodifiableMap(stats);
    }

    public int compareTo(AnimalStats other, StatCategory category) {
        return Integer.compare(this.getStat(category), other.getStat(category));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalStats that = (AnimalStats) o;
        return Objects.equals(stats, that.stats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stats);
    }

    @Override
    public String toString() {
        return "AnimalStats" + stats;
    }
}
