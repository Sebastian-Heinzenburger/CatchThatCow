package de.heinzenburger.ui.view;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.world.Biome;

import java.util.Scanner;

/**
 * Console-based implementation of GameView.
 * Outputs to System.out and reads from System.in.
 */
public class ConsoleGameView implements GameView {

    private final Scanner scanner;
    private final WorldRenderer worldRenderer;
    private final InventoryRenderer inventoryRenderer;
    private final BattleRenderer battleRenderer;

    public ConsoleGameView() {
        this.scanner = new Scanner(System.in);
        this.worldRenderer = new WorldRenderer();
        this.inventoryRenderer = new InventoryRenderer();
        this.battleRenderer = new BattleRenderer();
    }

    @Override
    public void showWelcome() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║              CATCH THAT COW!                                 ║");
        System.out.println("║                                                              ║");
        System.out.println("║          A Clean Architecture Terminal Game                  ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    @Override
    public void showMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("  1. New Game");
        System.out.println("  2. Load Game");
        System.out.println("  3. Quit");
        System.out.println();
    }

    @Override
    public void showPhasePrompt(GameSession session) {
        GamePhase phase = session.getPhase();
        var player = session.getPlayer();
        var pos = player.getCurrentPosition();

        System.out.println();
        switch (phase) {
            case EXPLORING -> {
                Biome biome = session.getWorld().getBiomeAt(pos);
                System.out.printf("[EXPLORING] Position: %s | Biome: %s (Lvl %d) | Animals: %d%n",
                        pos, biome.type(), biome.getAnimalLevel(), player.getInventory().size());
            }
            case ENCOUNTER_PENDING -> {
                Animal animal = session.getEncounteredAnimal();
                System.out.printf("[ENCOUNTER] Facing: %s (Lvl %d, %s)%n",
                        animal.getSpecies().name(),
                        animal.getLevel(),
                        animal.isPredator() ? "PREDATOR" : "PREY");
            }
            case IN_BATTLE -> {
                var battle = session.getCurrentBattle();
                System.out.printf("[BATTLE] Score: YOU %d - %d OPPONENT | %s%n",
                        battle.getPlayerScore(),
                        battle.getOpponentScore(),
                        battle.isPlayerTurn() ? "Your turn" : "Defend!");
            }
        }
        System.out.print("> ");
    }

    @Override
    public void showResult(CommandResult result) {
        // Using instanceof for Java 17 compatibility (pattern matching in switch is Java 21+)
        if (result instanceof CommandResult.Success success) {
            System.out.println(success.message());
        } else if (result instanceof CommandResult.Error error) {
            System.out.println("ERROR: " + error.message());
        } else if (result instanceof CommandResult.Help help) {
            System.out.println("\nAvailable commands:");
            help.commands().forEach(cmd -> System.out.println("  " + cmd));
        } else if (result instanceof CommandResult.Quit) {
            System.out.println("Goodbye! Thanks for playing!");
        } else if (result instanceof CommandResult.MovedTo movedTo) {
            System.out.printf("Moved to %s - %s (Level %d)%n",
                    movedTo.position(), movedTo.biome().type(), movedTo.biome().getAnimalLevel());
        } else if (result instanceof CommandResult.EncounterStarted encounter) {
            System.out.print(battleRenderer.renderEncounteredAnimal(encounter.animal()));
        } else if (result instanceof CommandResult.BattleStarted battleStarted) {
            System.out.println("\n*** BATTLE BEGINS! ***");
            System.out.print(battleRenderer.renderBattleStatus(battleStarted.battle()));
        } else if (result instanceof CommandResult.RoundOutcome roundOutcome) {
            System.out.print(battleRenderer.renderRoundResult(roundOutcome.result(), roundOutcome.battleFinished()));
        } else if (result instanceof CommandResult.BattleEnded battleEnded) {
            System.out.print(battleRenderer.renderBattleOutcome(battleEnded.outcome()));
        } else if (result instanceof CommandResult.FledEncounter) {
            System.out.println("You fled successfully! Returning to exploration.");
        } else if (result instanceof CommandResult.GameSaved) {
            System.out.println("Game saved successfully!");
        } else if (result instanceof CommandResult.ShowInventory showInventory) {
            System.out.print(inventoryRenderer.render(showInventory.animals()));
        } else if (result instanceof CommandResult.ShowMap showMap) {
            System.out.print(worldRenderer.render(showMap.world(), showMap.playerPosition()));
        } else if (result instanceof CommandResult.ShowBattleStatus showStatus) {
            System.out.print(battleRenderer.renderBattleStatus(showStatus.battle()));
        }
    }

    @Override
    public String readInput() {
        if (!scanner.hasNextLine()) {
            return "quit"; // EOF - gracefully exit
        }
        return scanner.nextLine().trim();
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }
}
