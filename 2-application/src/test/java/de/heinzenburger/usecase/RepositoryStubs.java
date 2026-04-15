package de.heinzenburger.usecase;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.PlayerRepository;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.world.World;
import de.heinzenburger.world.WorldRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory stub implementations of repository interfaces for testing.
 * These stubs provide simple implementations without actual persistence.
 */
public class RepositoryStubs {

    /**
     * In-memory implementation of PlayerRepository.
     */
    public static class InMemoryPlayerRepository implements PlayerRepository {
        private Player savedPlayer;

        @Override
        public void save(Player player) {
            this.savedPlayer = player;
        }

        @Override
        public Optional<Player> load() {
            return Optional.ofNullable(savedPlayer);
        }

        public void clear() {
            this.savedPlayer = null;
        }
    }

    /**
     * In-memory implementation of WorldRepository.
     */
    public static class InMemoryWorldRepository implements WorldRepository {
        private World savedWorld;

        @Override
        public void save(World world) {
            this.savedWorld = world;
        }

        @Override
        public Optional<World> load() {
            return Optional.ofNullable(savedWorld);
        }

        public void clear() {
            this.savedWorld = null;
        }
    }

    /**
     * In-memory implementation of AnimalSpeciesRepository.
     */
    public static class InMemoryAnimalSpeciesRepository implements AnimalSpeciesRepository {
        private final List<AnimalSpecies> species;

        public InMemoryAnimalSpeciesRepository() {
            this.species = new ArrayList<>();
        }

        public InMemoryAnimalSpeciesRepository(List<AnimalSpecies> species) {
            this.species = new ArrayList<>(species);
        }

        @Override
        public List<AnimalSpecies> findAll() {
            return new ArrayList<>(species);
        }

        @Override
        public List<AnimalSpecies> findByLevel(int level) {
            return species.stream()
                    .filter(s -> s.getLevel() == level)
                    .collect(Collectors.toList());
        }

        @Override
        public List<AnimalSpecies> findByHabitat(BiomeType biomeType) {
            return species.stream()
                    .filter(s -> s.getHabitat() == biomeType)
                    .collect(Collectors.toList());
        }

        public void add(AnimalSpecies animalSpecies) {
            this.species.add(animalSpecies);
        }

        public void clear() {
            this.species.clear();
        }
    }
}
