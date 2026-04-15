package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.services.AnimalEncounterService;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.List;

/**
 * Triggers a wild animal encounter at the player's current position.
 */
public class TriggerEncounterUseCase {

    private final GameSessionManager sessionManager;
    private final AnimalEncounterService encounterService;
    private final AnimalSpeciesRepository speciesRepository;

    public TriggerEncounterUseCase(
            GameSessionManager sessionManager,
            AnimalEncounterService encounterService,
            AnimalSpeciesRepository speciesRepository) {
        this.sessionManager = sessionManager;
        this.encounterService = encounterService;
        this.speciesRepository = speciesRepository;
    }

    /**
     * Triggers an encounter with a wild animal at the player's current position.
     *
     * @return the encountered animal
     * @throws GameNotStartedException if no active game session
     * @throws InvalidGamePhaseException if not in EXPLORING phase
     */
    public Animal execute() throws GameNotStartedException, InvalidGamePhaseException {
        GameSession session = sessionManager.getCurrentSession();

        // Validate phase
        if (session.getPhase() != GamePhase.EXPLORING) {
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.EXPLORING);
        }

        Player player = session.getPlayer();
        World world = session.getWorld();

        // Get the biome at player's current position
        Biome currentBiome = world.getBiomeAt(player.getCurrentPosition());

        // Get all available species
        List<AnimalSpecies> allSpecies = speciesRepository.findAll();

        // Generate a random encounter
        Animal encounteredAnimal = encounterService.encounterAnimal(currentBiome, allSpecies);

        // Store in session and update phase
        session.setEncounter(encounteredAnimal);

        return encounteredAnimal;
    }
}
