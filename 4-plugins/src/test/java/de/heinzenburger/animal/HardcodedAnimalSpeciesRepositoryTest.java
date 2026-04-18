package de.heinzenburger.animal;

import de.heinzenburger.shared.BiomeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HardcodedAnimalSpeciesRepositoryTest {

    private HardcodedAnimalSpeciesRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HardcodedAnimalSpeciesRepository();
    }

    @Test
    void findAll_returns36Species() {
        List<AnimalSpecies> species = repository.findAll();

        assertEquals(36, species.size(), "Should have 36 species (6 biomes * 3 levels * 2 types)");
    }

    @Test
    void findAll_returnsNewListEachTime() {
        List<AnimalSpecies> first = repository.findAll();
        List<AnimalSpecies> second = repository.findAll();

        assertNotSame(first, second);
        assertEquals(first, second);
    }

    @Test
    void findByLevel_level1Returns12Species() {
        List<AnimalSpecies> species = repository.findByLevel(1);

        assertEquals(12, species.size(), "Should have 12 level-1 species (6 biomes * 2 types)");
        assertTrue(species.stream().allMatch(s -> s.level() == 1));
    }

    @Test
    void findByLevel_level2Returns12Species() {
        List<AnimalSpecies> species = repository.findByLevel(2);

        assertEquals(12, species.size());
        assertTrue(species.stream().allMatch(s -> s.level() == 2));
    }

    @Test
    void findByLevel_level3Returns12Species() {
        List<AnimalSpecies> species = repository.findByLevel(3);

        assertEquals(12, species.size());
        assertTrue(species.stream().allMatch(s -> s.level() == 3));
    }

    @Test
    void findByHabitat_eachBiomeHas6Species() {
        for (BiomeType biome : BiomeType.values()) {
            List<AnimalSpecies> species = repository.findByHabitat(biome);

            assertEquals(6, species.size(), "Biome " + biome + " should have 6 species (3 levels * 2 types)");
            assertTrue(species.stream().allMatch(s -> s.habitat() == biome));
        }
    }

    @Test
    void findByHabitat_grasslandContainsExpectedSpecies() {
        List<AnimalSpecies> species = repository.findByHabitat(BiomeType.GRASSLAND);
        List<String> names = species.stream().map(AnimalSpecies::name).toList();

        assertTrue(names.contains("Rabbit"));
        assertTrue(names.contains("Fox"));
        assertTrue(names.contains("Deer"));
        assertTrue(names.contains("Wolf"));
        assertTrue(names.contains("Bison"));
        assertTrue(names.contains("Lion"));
    }

    @Test
    void allSpecies_havePredatorAndPreyAtEachLevelPerBiome() {
        for (BiomeType biome : BiomeType.values()) {
            for (int level = 1; level <= 3; level++) {
                int finalLevel = level;
                List<AnimalSpecies> speciesAtLevel = repository.findByHabitat(biome).stream()
                        .filter(s -> s.level() == finalLevel)
                        .toList();

                assertEquals(2, speciesAtLevel.size(),
                        "Should have 2 species at level " + level + " in " + biome);

                long predatorCount = speciesAtLevel.stream()
                        .filter(s -> s.type() == de.heinzenburger.shared.AnimalType.PREDATOR)
                        .count();
                long preyCount = speciesAtLevel.stream()
                        .filter(s -> s.type() == de.heinzenburger.shared.AnimalType.PREY)
                        .count();

                assertEquals(1, predatorCount, "Should have 1 predator at level " + level + " in " + biome);
                assertEquals(1, preyCount, "Should have 1 prey at level " + level + " in " + biome);
            }
        }
    }

    @Test
    void allSpecies_haveValidStats() {
        for (AnimalSpecies species : repository.findAll()) {
            assertNotNull(species.baseStats());
            for (de.heinzenburger.shared.StatCategory category : de.heinzenburger.shared.StatCategory.values()) {
                int stat = species.baseStats().getStat(category);
                assertTrue(stat > 0, "Stat " + category + " should be positive for " + species.name());
            }
        }
    }
}
