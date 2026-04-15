package de.heinzenburger.services;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.world.Biome;

import java.util.List;

public class AnimalEncounterService {
    private final RandomNumberGenerator random;

    public AnimalEncounterService(RandomNumberGenerator random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        this.random = random;
    }

    public Animal encounterAnimal(Biome biome, List<AnimalSpecies> availableSpecies) {
        if (biome == null) throw new IllegalArgumentException("Biome cannot be null");
        if (availableSpecies == null || availableSpecies.isEmpty())
            throw new IllegalArgumentException("Available species cannot be null or empty");

        List<AnimalSpecies> compatibleSpecies = availableSpecies.stream().filter(biome::canContainSpecies).toList();

        if (compatibleSpecies.isEmpty())
            throw new IllegalStateException("No compatible species found for biome: " + biome);

        // Randomly select a species
        AnimalSpecies selectedSpecies = compatibleSpecies.get(random.nextInt(compatibleSpecies.size()));

        return selectedSpecies.generateAnimalWithSlightStatVariation(random);
    }
}
