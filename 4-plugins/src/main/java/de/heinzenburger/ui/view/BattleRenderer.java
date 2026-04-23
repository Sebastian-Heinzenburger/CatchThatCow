package de.heinzenburger.ui.view;

import de.heinzenburger.shared.StatCategory;
import de.heinzenburger.ui.dto.AnimalSummary;
import de.heinzenburger.ui.dto.BattleOutcomeSummary;
import de.heinzenburger.ui.dto.BattleSummary;
import de.heinzenburger.ui.dto.RoundOutcomeSummary;

import java.util.List;

import static de.heinzenburger.ui.view.RenderConstants.*;

/**
 * Renders battle-related displays.
 */
public class BattleRenderer {

    private final InventoryRenderer inventoryRenderer;

    public BattleRenderer() {
        this.inventoryRenderer = new InventoryRenderer();
    }

    public BattleRenderer(InventoryRenderer inventoryRenderer) {
        this.inventoryRenderer = inventoryRenderer;
    }

    public String renderBattleStatus(BattleSummary battle) {
        StringBuilder sb = new StringBuilder();

        renderBattleHeader(sb);
        renderScore(sb, battle);
        renderOpponentInfo(sb, battle.opponent());
        sb.append(SEPARATOR);
        renderAvailableAnimals(sb, battle.availableAnimals());
        sb.append(SEPARATOR);
        renderOpponentStats(sb, battle.opponent());
        sb.append(SEPARATOR);
        renderTurnIndicator(sb, battle.isPlayerTurn());
        sb.append(BOTTOM_BORDER);

        return sb.toString();
    }

    private void renderBattleHeader(StringBuilder sb) {
        sb.append(TOP_BORDER);
        sb.append("║                         BATTLE                               ║\n");
        sb.append(SEPARATOR);
    }

    private void renderScore(StringBuilder sb, BattleSummary battle) {
        sb.append(String.format("║ Score: YOU %d - %d OPPONENT                                   ║\n",
                battle.playerScore(), battle.opponentScore()));
    }

    private void renderOpponentInfo(StringBuilder sb, AnimalSummary opponent) {
        sb.append(String.format("║ Opponent: %-15s (Lvl %d, %-8s)                 ║\n",
                opponent.name(),
                opponent.level(),
                opponent.typeLabel()));
    }

    private void renderAvailableAnimals(StringBuilder sb, List<AnimalSummary> available) {
        sb.append("║ Your available animals:                                      ║\n");
        if (available.isEmpty()) {
            sb.append("║   (No animals remaining)                                     ║\n");
        } else {
            for (int i = 0; i < available.size(); i++) {
                AnimalSummary a = available.get(i);
                sb.append(String.format("║   %d. %-12s SPD:%-3d LEN:%-4d WGT:%-4d LIFE:%-3d OFF:%-3d   ║\n",
                        i + 1,
                        a.name(),
                        a.getStat(StatCategory.SPEED),
                        a.getStat(StatCategory.LENGTH),
                        a.getStat(StatCategory.WEIGHT),
                        a.getStat(StatCategory.LIFESPAN),
                        a.getStat(StatCategory.OFFSPRING)));
            }
        }
    }

    private void renderOpponentStats(StringBuilder sb, AnimalSummary opponent) {
        sb.append("║ Opponent stats:                                              ║\n");
        sb.append(String.format("║   SPD:%-3d LEN:%-4d WGT:%-4d LIFE:%-3d OFF:%-3d                  ║\n",
                opponent.getStat(StatCategory.SPEED),
                opponent.getStat(StatCategory.LENGTH),
                opponent.getStat(StatCategory.WEIGHT),
                opponent.getStat(StatCategory.LIFESPAN),
                opponent.getStat(StatCategory.OFFSPRING)));
    }

    private void renderTurnIndicator(StringBuilder sb, boolean isPlayerTurn) {
        if (isPlayerTurn) {
            sb.append("║ YOUR TURN: attack <animal#> <stat>                           ║\n");
        } else {
            sb.append("║ OPPONENT'S TURN: defend <animal#>                            ║\n");
        }
    }

    public String renderRoundResult(RoundOutcomeSummary result, boolean battleFinished) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n═══════════════ ROUND RESULT ═══════════════\n");
        sb.append(String.format("Category: %s\n", result.category()));

        if (result.playerAnimalName() != null) {
            sb.append(String.format("Your %s: %d\n", result.playerAnimalName(), result.playerStatValue()));
        } else {
            sb.append(String.format("Your stat: %d\n", result.playerStatValue()));
        }
        sb.append(String.format("Opponent's %s: %d\n", result.opponentAnimalName(), result.opponentStatValue()));

        if (result.playerWon()) {
            sb.append(">>> YOU WIN THIS ROUND! <<<\n");
        } else {
            sb.append(">>> OPPONENT WINS THIS ROUND <<<\n");
        }

        sb.append("═════════════════════════════════════════════\n");

        return sb.toString();
    }

    public String renderBattleOutcome(BattleOutcomeSummary outcome) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(TOP_BORDER);

        if (outcome.playerWon()) {
            sb.append("║             *** VICTORY! ***                                 ║\n");
            sb.append(SEPARATOR);
            if (outcome.caughtAnimalName() != null) {
                sb.append(String.format("║ You caught: %-20s                         ║\n",
                        outcome.caughtAnimalName()));
                sb.append("║ The animal has been added to your inventory!                ║\n");
            }
        } else {
            sb.append("║             *** DEFEAT ***                                   ║\n");
            sb.append(SEPARATOR);
            if (outcome.lostAnimalName() != null) {
                sb.append(String.format("║ You lost: %-22s                         ║\n",
                        outcome.lostAnimalName()));
            } else {
                sb.append("║ Better luck next time!                                       ║\n");
            }
        }

        sb.append(BOTTOM_BORDER);

        return sb.toString();
    }

    public String renderEncounteredAnimal(AnimalSummary animal) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(TOP_BORDER);
        sb.append("║               A WILD ANIMAL APPEARS!                         ║\n");
        sb.append(SEPARATOR);
        sb.append(String.format("║ Name: %-20s                                  ║\n", animal.name()));
        sb.append(String.format("║ Level: %-2d    Type: %-8s                                ║\n",
                animal.level(),
                animal.typeLabel()));
        sb.append(SEPARATOR);
        renderAnimalStats(sb, animal);
        sb.append(SEPARATOR);

        if (animal.isPredator()) {
            sb.append("║ WARNING: This is a PREDATOR - you cannot flee!               ║\n");
        } else {
            sb.append("║ This is prey - you can fight or flee!                        ║\n");
        }

        sb.append(BOTTOM_BORDER);

        return sb.toString();
    }

    private void renderAnimalStats(StringBuilder sb, AnimalSummary animal) {
        sb.append("║ Stats:                                                       ║\n");
        sb.append(String.format("║   Speed: %-5d  Length: %-5d  Weight: %-5d                 ║\n",
                animal.getStat(StatCategory.SPEED),
                animal.getStat(StatCategory.LENGTH),
                animal.getStat(StatCategory.WEIGHT)));
        sb.append(String.format("║   Lifespan: %-4d  Offspring: %-4d                            ║\n",
                animal.getStat(StatCategory.LIFESPAN),
                animal.getStat(StatCategory.OFFSPRING)));
    }
}
