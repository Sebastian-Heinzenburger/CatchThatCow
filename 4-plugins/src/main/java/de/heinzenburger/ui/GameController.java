package de.heinzenburger.ui;

import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.ui.handler.PhaseHandler;
import de.heinzenburger.ui.view.GameView;
import de.heinzenburger.usecase.LoadGameUseCase;
import de.heinzenburger.usecase.StartNewGameUseCase;

import java.util.Map;

/**
 * Main game controller that orchestrates the game loop.
 * Uses Strategy pattern for phase-specific command handling.
 */
public class GameController {

    private static final int DEFAULT_WORLD_SIZE = 2; // Creates 5x5 world

    private final GameSessionManager sessionManager;
    private final GameView view;
    private final Map<GamePhase, PhaseHandler> handlers;
    private final StartNewGameUseCase startNewGameUseCase;
    private final LoadGameUseCase loadGameUseCase;

    private boolean running = true;

    public GameController(
            GameSessionManager sessionManager,
            GameView view,
            Map<GamePhase, PhaseHandler> handlers,
            StartNewGameUseCase startNewGameUseCase,
            LoadGameUseCase loadGameUseCase) {
        this.sessionManager = sessionManager;
        this.view = view;
        this.handlers = handlers;
        this.startNewGameUseCase = startNewGameUseCase;
        this.loadGameUseCase = loadGameUseCase;
    }

    public void run() {
        view.showWelcome();

        while (running) {
            if (!sessionManager.hasActiveSession()) {
                if (!showMainMenu()) {
                    break;
                }
                continue;
            }

            gameLoop();
        }
    }

    private boolean showMainMenu() {
        view.showMainMenu();
        view.showPrompt("Enter choice: ");
        String choice = view.readInput();

        switch (choice) {
            case "1", "new", "n" -> {
                view.showMessage("\nStarting new game...");
                view.showMessage("Enter world size (1=3x3, 2=5x5, 3=7x7) or press Enter for default (5x5):");
                view.showPrompt("> ");
                String sizeInput = view.readInput();

                int worldSize = DEFAULT_WORLD_SIZE;
                if (!sizeInput.isEmpty()) {
                    try {
                        worldSize = Integer.parseInt(sizeInput);
                        if (worldSize < 1 || worldSize > 5) {
                            view.showError("Invalid size. Using default (5x5).");
                            worldSize = DEFAULT_WORLD_SIZE;
                        }
                    } catch (NumberFormatException e) {
                        view.showError("Invalid input. Using default (5x5).");
                    }
                }

                GameSession session = startNewGameUseCase.execute(worldSize);
                view.showMessage("\nGame started! You have " + session.getPlayer().getInventory().size() + " starter animals.");
                view.showMessage("Type 'help' for available commands, or 'map' to see the world.\n");
                return true;
            }
            case "2", "load", "l" -> {
                try {
                    GameSession session = loadGameUseCase.execute();
                    view.showMessage("\nGame loaded successfully!");
                    view.showMessage("Position: " + session.getPlayer().getCurrentPosition());
                    view.showMessage("Animals: " + session.getPlayer().getInventory().size());
                    view.showMessage("Type 'help' for available commands.\n");
                    return true;
                } catch (LoadGameUseCase.NoSavedGameException e) {
                    view.showError("No saved game found. Please start a new game.");
                    return true;
                }
            }
            case "3", "quit", "q", "exit" -> {
                view.showMessage("Goodbye!");
                running = false;
                return false;
            }
            default -> {
                view.showError("Invalid choice. Please enter 1, 2, or 3.");
                return true;
            }
        }
    }

    private void gameLoop() {
        while (running && sessionManager.hasActiveSession()) {
            try {
                GameSession session = sessionManager.getCurrentSession();
                view.showPhasePrompt(session);

                String input = view.readInput();
                if (input.isEmpty()) {
                    continue;
                }

                PhaseHandler handler = handlers.get(session.getPhase());
                if (handler == null) {
                    view.showError("No handler for phase: " + session.getPhase());
                    continue;
                }

                CommandResult result = handler.handle(input, session);
                view.showResult(result);

                if (result.shouldQuit()) {
                    running = false;
                }

            } catch (Exception e) {
                view.showError("Unexpected error: " + e.getMessage());
            }
        }
    }
}
