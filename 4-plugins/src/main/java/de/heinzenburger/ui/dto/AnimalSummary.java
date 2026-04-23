package de.heinzenburger.ui.dto;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.StatCategory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Presentation DTO for displaying animal information in the UI.
 * Decouples the view layer from domain entities.
 */
public record AnimalSummary(
        String name,
        int level,
        boolean isPredator,
        Map<StatCategory, Integer> stats
) {
    public static AnimalSummary from(Animal animal) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        for (StatCategory cat : StatCategory.values()) {
            stats.put(cat, animal.getStat(cat));
        }
        return new AnimalSummary(
                animal.getSpecies().name(),
                animal.getLevel(),
                animal.isPredator(),
                stats
        );
    }

    public int getStat(StatCategory category) {
        return stats.getOrDefault(category, 0);
    }

    public String typeLabel() {
        return isPredator ? "PREDATOR" : "PREY";
    }
}
