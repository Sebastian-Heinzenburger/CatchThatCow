package de.heinzenburger;

import java.util.Map;

public class TerminalNavigationPresenter implements NavigationPresenter {
    private final TextPresenter textPresenter;
    private final UserInput userInput;

    public TerminalNavigationPresenter(TextPresenter textPresenter, UserInput userInput) {
        this.textPresenter = textPresenter;
        this.userInput = userInput;
    }

    @Override
    public void showCurrentBiome(Biome biome) {
        textPresenter.print("");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("  Aktuelles Biom: " + biome.getType().getDisplayName());
        textPresenter.print("  Level: " + biome.getLevel());
        textPresenter.print("  Position: " + biome.getPosition());
        textPresenter.print("═══════════════════════════════════════");
    }

    @Override
    public void showNavigationMenu(Map<Direction, Biome> surroundingBiomes) {
        textPresenter.print("");
        textPresenter.print("Wohin möchtest du gehen?");

        for (Map.Entry<Direction, Biome> entry : surroundingBiomes.entrySet()) {
            Direction dir = entry.getKey();
            Biome biome = entry.getValue();
            textPresenter.print("  " + dir.name().charAt(0) + " - " + dir.getDisplayName() +
                    ": " + biome.getType().getDisplayName() + " (Level " + biome.getLevel() + ")");
        }

        textPresenter.print("  S - Hier bleiben");
        textPresenter.print("");
    }

    @Override
    public Direction getUserDirection() {
        String input = userInput.readLine().toUpperCase();

        switch (input) {
            case "N":
                return Direction.NORTH;
            case "S":
                return Direction.SOUTH;
            case "E":
                return Direction.EAST;
            case "W":
                return Direction.WEST;
            default:
                textPresenter.print("Ungültige Eingabe! Bitte N, S, E oder W eingeben.");
                return getUserDirection();
        }
    }

    @Override
    public boolean wantsToStay() {
        String input = userInput.readLine().toUpperCase();
        return input.equals("S");
    }
}
