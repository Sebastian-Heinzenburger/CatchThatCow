package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.player.Inventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.services.AnimalEncounterService;
import de.heinzenburger.services.WorldGenerator;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.List;

/**
 * Initializes a new game with world generation, player creation, and starter animals.
 */
public class StartNewGameUseCase {

    private static final int STARTER_ANIMALS_COUNT = 3;

    private final WorldGenerator worldGenerator;
    private final AnimalSpeciesRepository animalSpeciesRepository;
    private final AnimalEncounterService animalEncounterService;
    private final GameSessionManager sessionManager;
    private final RandomNumberGenerator random;

    public StartNewGameUseCase(
            WorldGenerator worldGenerator,
            AnimalSpeciesRepository animalSpeciesRepository,
            AnimalEncounterService animalEncounterService,
            GameSessionManager sessionManager,
            RandomNumberGenerator random) {
        this.worldGenerator = worldGenerator;
        this.animalSpeciesRepository = animalSpeciesRepository;
        this.animalEncounterService = animalEncounterService;
        this.sessionManager = sessionManager;
        this.random = random;
    }

    /**
     * Starts a new game.
     *
     * @param worldSize the size parameter for world generation (creates a (2n+1)x(2n+1) grid)
     * @return the initialized game session
     */
    public GameSession execute(int worldSize) {
        // End any existing session
        sessionManager.endSession();

        // Generate the world
        World world = worldGenerator.generateWorld(worldSize);

        // Create player with empty inventory at world's start position
        Inventory inventory = new Inventory(random);
        Player player = new Player(inventory, world.getStartPosition());

        // Give player starter animals from the start biome (level 1)
        Biome startBiome = world.getBiomeAt(world.getStartPosition());
        List<AnimalSpecies> allSpecies = animalSpeciesRepository.findAll();

        for (int i = 0; i < STARTER_ANIMALS_COUNT; i++) {
            Animal starterAnimal = animalEncounterService.encounterAnimal(startBiome, allSpecies);
            player.addAnimal(starterAnimal);
        }

        // Initialize and return the session
        return sessionManager.startSession(player, world);
    }
}
