package de.heinzenburger.ui.view;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.RoundWinner;
import de.heinzenburger.shared.StatCategory;
import de.heinzenburger.usecase.ResolveBattleUseCase.BattleOutcome;

import java.util.List;

/**
 * Renders battle-related displays.
 */
public class BattleRenderer {

    private final InventoryRenderer inventoryRenderer = new InventoryRenderer();

    public String renderBattleStatus(Battle battle) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║                         BATTLE                               ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        // Score
        sb.append(String.format("║ Score: YOU %d - %d OPPONENT                                   ║\n",
                battle.getPlayerScore(), battle.getOpponentScore()));

        // Opponent info
        Animal opponent = battle.getOpponentAnimal();
        sb.append(String.format("║ Opponent: %-15s (Lvl %d, %-8s)                 ║\n",
                opponent.getSpecies().name(),
                opponent.getLevel(),
                opponent.isPredator() ? "PREDATOR" : "PREY"));

        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        // Your animals
        sb.append("║ Your available animals:                                      ║\n");
        List<Animal> available = battle.getAvailableAnimals();
        if (available.isEmpty()) {
            sb.append("║   (No animals remaining)                                     ║\n");
        } else {
            for (int i = 0; i < available.size(); i++) {
                Animal a = available.get(i);
                sb.append(String.format("║   %d. %-12s SPD:%-3d LEN:%-4d WGT:%-4d LIFE:%-3d OFF:%-3d   ║\n",
                        i + 1,
                        a.getSpecies().name(),
                        a.getStat(StatCategory.SPEED),
                        a.getStat(StatCategory.LENGTH),
                        a.getStat(StatCategory.WEIGHT),
                        a.getStat(StatCategory.LIFESPAN),
                        a.getStat(StatCategory.OFFSPRING)));
            }
        }

        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        // Opponent stats
        sb.append("║ Opponent stats:                                              ║\n");
        sb.append(String.format("║   SPD:%-3d LEN:%-4d WGT:%-4d LIFE:%-3d OFF:%-3d                  ║\n",
                opponent.getStat(StatCategory.SPEED),
                opponent.getStat(StatCategory.LENGTH),
                opponent.getStat(StatCategory.WEIGHT),
                opponent.getStat(StatCategory.LIFESPAN),
                opponent.getStat(StatCategory.OFFSPRING)));

        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        // Turn indicator
        if (battle.isPlayerTurn()) {
            sb.append("║ YOUR TURN: attack <animal#> <stat>                           ║\n");
        } else {
            sb.append("║ OPPONENT'S TURN: defend <animal#>                            ║\n");
        }

        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    public String renderRoundResult(RoundResult result, boolean battleFinished) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n═══════════════ ROUND RESULT ═══════════════\n");
        sb.append(String.format("Category: %s\n", result.category()));

        if (result.playerAnimal() != null) {
            sb.append(String.format("Your %s: %d\n", result.playerAnimal().getSpecies().name(), result.playerStatValue()));
        } else {
            sb.append(String.format("Your stat: %d\n", result.playerStatValue()));
        }
        sb.append(String.format("Opponent's %s: %d\n", result.opponentAnimal().getSpecies().name(), result.opponentStatValue()));

        if (result.winner() == RoundWinner.PLAYER) {
            sb.append(">>> YOU WIN THIS ROUND! <<<\n");
        } else {
            sb.append(">>> OPPONENT WINS THIS ROUND <<<\n");
        }

        sb.append("═════════════════════════════════════════════\n");

        return sb.toString();
    }

    public String renderBattleOutcome(BattleOutcome outcome) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");

        if (outcome.playerWon()) {
            sb.append("║             *** VICTORY! ***                                 ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════╣\n");
            if (outcome.caughtAnimal() != null) {
                sb.append(String.format("║ You caught: %-20s                         ║\n",
                        outcome.caughtAnimal().getSpecies().name()));
                sb.append("║ The animal has been added to your inventory!                ║\n");
            }
        } else {
            sb.append("║             *** DEFEAT ***                                   ║\n");
            sb.append("╠══════════════════════════════════════════════════════════════╣\n");
            if (outcome.lostAnimal() != null) {
                sb.append(String.format("║ You lost: %-22s                         ║\n",
                        outcome.lostAnimal().getSpecies().name()));
            } else {
                sb.append("║ Better luck next time!                                       ║\n");
            }
        }

        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    public String renderEncounteredAnimal(Animal animal) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║               A WILD ANIMAL APPEARS!                         ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Name: %-20s                                  ║\n", animal.getSpecies().name()));
        sb.append(String.format("║ Level: %-2d    Type: %-8s                                ║\n",
                animal.getLevel(),
                animal.isPredator() ? "PREDATOR" : "PREY"));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append("║ Stats:                                                       ║\n");
        sb.append(String.format("║   Speed: %-5d  Length: %-5d  Weight: %-5d                 ║\n",
                animal.getStat(StatCategory.SPEED),
                animal.getStat(StatCategory.LENGTH),
                animal.getStat(StatCategory.WEIGHT)));
        sb.append(String.format("║   Lifespan: %-4d  Offspring: %-4d                            ║\n",
                animal.getStat(StatCategory.LIFESPAN),
                animal.getStat(StatCategory.OFFSPRING)));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        if (animal.isPredator()) {
            sb.append("║ WARNING: This is a PREDATOR - you cannot flee!               ║\n");
        } else {
            sb.append("║ This is prey - you can fight or flee!                        ║\n");
        }

        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }
}
