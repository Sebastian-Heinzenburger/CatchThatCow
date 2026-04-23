package de.heinzenburger.ui.dto;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.Biome;

/**
 * Presentation DTO for displaying movement results in the UI.
 * Decouples the view layer from domain entities.
 */
public record MovementResult(
        Position position,
        BiomeType biomeType,
        int animalLevel
) {
    public static MovementResult from(Position position, Biome biome) {
        return new MovementResult(position, biome.type(), biome.getAnimalLevel());
    }
}
