package de.heinzenburger;

import java.util.List;

public class TerminalInventoryPresenter implements InventoryPresenter {
    private final TextPresenter textPresenter;

    public TerminalInventoryPresenter(TextPresenter textPresenter) {
        this.textPresenter = textPresenter;
    }

    @Override
    public void displayInventory(List<Animal> animals) {
        textPresenter.print("");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("         DEIN INVENTAR");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("Anzahl Tiere: " + animals.size());
        textPresenter.print("");

        if (animals.isEmpty()) {
            textPresenter.print("  (leer)");
        } else {
            for (int i = 0; i < animals.size(); i++) {
                Animal animal = animals.get(i);
                textPresenter.print((i + 1) + ". " + animal.getSpecies() + " (Level " + animal.getLevel() + ")");
                textPresenter.print("   Geschwindigkeit: " + animal.getStatValue(StatCategory.SPEED));
                textPresenter.print("   Länge: " + animal.getStatValue(StatCategory.LENGTH));
                textPresenter.print("   Gewicht: " + animal.getStatValue(StatCategory.WEIGHT));
                textPresenter.print("   Stärke: " + animal.getStatValue(StatCategory.STRENGTH));
                textPresenter.print("");
            }
        }

        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("");
    }
}
