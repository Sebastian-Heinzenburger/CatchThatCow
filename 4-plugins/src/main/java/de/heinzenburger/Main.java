package de.heinzenburger;

import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.animal.HardcodedAnimalSpeciesRepository;
import de.heinzenburger.game.FileGameStateRepository;
import de.heinzenburger.game.GameStateRepository;
import de.heinzenburger.services.AnimalEncounterService;
import de.heinzenburger.services.BattleFactory;
import de.heinzenburger.services.WorldGenerator;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.JavaRandomNumberGenerator;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.ui.GameController;
import de.heinzenburger.ui.handler.BattleHandler;
import de.heinzenburger.ui.handler.EncounterHandler;
import de.heinzenburger.ui.handler.ExploringHandler;
import de.heinzenburger.ui.handler.PhaseHandler;
import de.heinzenburger.ui.view.ConsoleGameView;
import de.heinzenburger.ui.view.GameView;
import de.heinzenburger.usecase.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Main entry point for the Catch That Cow! game.
 * Wires all dependencies according to Clean Architecture principles.
 * This is the composition root - the only place where dependencies are created.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // === Infrastructure Layer ===
            RandomNumberGenerator random = new JavaRandomNumberGenerator();
            AnimalSpeciesRepository speciesRepo = new HardcodedAnimalSpeciesRepository();

            // Set up save directory
            Path saveDir = Paths.get(System.getProperty("user.home"), ".catchthatcow");
            Files.createDirectories(saveDir);
            GameStateRepository gameStateRepo = new FileGameStateRepository(saveDir, random, speciesRepo);

            // === Domain Services ===
            WorldGenerator worldGenerator = new WorldGenerator(random);
            AnimalEncounterService encounterService = new AnimalEncounterService(random);
            BattleFactory battleFactory = new BattleFactory(random);

            // === Application Layer - Session Manager (shared state) ===
            GameSessionManager sessionManager = new GameSessionManager();

            // === Application Layer - Use Cases ===
            StartNewGameUseCase startNewGame = new StartNewGameUseCase(
                    worldGenerator, speciesRepo, encounterService, sessionManager, random);

            LoadGameUseCase loadGame = new LoadGameUseCase(sessionManager, gameStateRepo);

            SaveGameUseCase saveGame = new SaveGameUseCase(sessionManager, gameStateRepo);

            MovePlayerUseCase movePlayer = new MovePlayerUseCase(sessionManager);

            TriggerEncounterUseCase triggerEncounter = new TriggerEncounterUseCase(
                    sessionManager, encounterService, speciesRepo);

            StartBattleUseCase startBattle = new StartBattleUseCase(sessionManager, battleFactory);

            FleeEncounterUseCase fleeEncounter = new FleeEncounterUseCase(sessionManager);

            PlayerAttackUseCase playerAttack = new PlayerAttackUseCase(sessionManager);

            DefendAgainstAttackUseCase defendAgainstAttack = new DefendAgainstAttackUseCase(sessionManager);

            ResolveBattleUseCase resolveBattle = new ResolveBattleUseCase(sessionManager);

            // === Plugins Layer - UI ===
            GameView view = new ConsoleGameView();

            // Phase Handlers (Strategy Pattern)
            Map<GamePhase, PhaseHandler> handlers = Map.of(
                    GamePhase.EXPLORING, new ExploringHandler(movePlayer, triggerEncounter, saveGame),
                    GamePhase.ENCOUNTER_PENDING, new EncounterHandler(startBattle, fleeEncounter),
                    GamePhase.IN_BATTLE, new BattleHandler(playerAttack, defendAgainstAttack, resolveBattle)
            );

            // === Start the Game ===
            GameController controller = new GameController(
                    sessionManager, view, handlers, startNewGame, loadGame);

            controller.run();

        } catch (IOException e) {
            System.err.println("Failed to initialize game: " + e.getMessage());
            System.exit(1);
        }
    }
}
