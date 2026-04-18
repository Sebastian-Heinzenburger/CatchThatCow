package de.heinzenburger.ui.view;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.StatCategory;

import java.util.List;

/**
 * Renders the player's inventory as an ASCII table.
 */
public class InventoryRenderer {

    public String render(List<Animal> animals) {
        if (animals.isEmpty()) {
            return "Your inventory is empty!";
        }

        StringBuilder sb = new StringBuilder();

        // Table header
        sb.append("╔════╤════════════════╤═════╤══════════╤═══════╤════════╤════════╤══════════╤═══════════╗\n");
        sb.append("║  # │ Name           │ Lvl │ Type     │ Speed │ Length │ Weight │ Lifespan │ Offspring ║\n");
        sb.append("╠════╪════════════════╪═════╪══════════╪═══════╪════════╪════════╪══════════╪═══════════╣\n");

        // Table rows
        int index = 1;
        for (Animal animal : animals) {
            sb.append(String.format("║ %2d │ %-14s │ %3d │ %-8s │ %5d │ %6d │ %6d │ %8d │ %9d ║\n",
                    index++,
                    truncate(animal.getSpecies().name(), 14),
                    animal.getLevel(),
                    animal.isPredator() ? "PREDATOR" : "PREY",
                    animal.getStat(StatCategory.SPEED),
                    animal.getStat(StatCategory.LENGTH),
                    animal.getStat(StatCategory.WEIGHT),
                    animal.getStat(StatCategory.LIFESPAN),
                    animal.getStat(StatCategory.OFFSPRING)
            ));
        }

        // Table footer
        sb.append("╚════╧════════════════╧═════╧══════════╧═══════╧════════╧════════╧══════════╧═══════════╝\n");
        sb.append("Total: ").append(animals.size()).append(" animal(s)\n");

        return sb.toString();
    }

    public String renderCompact(List<Animal> animals) {
        if (animals.isEmpty()) {
            return "No animals available";
        }

        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Animal animal : animals) {
            sb.append(String.format("%d. %s (Lvl %d, %s) - SPD:%d LEN:%d WGT:%d LIFE:%d OFF:%d\n",
                    index++,
                    animal.getSpecies().name(),
                    animal.getLevel(),
                    animal.isPredator() ? "PRED" : "PREY",
                    animal.getStat(StatCategory.SPEED),
                    animal.getStat(StatCategory.LENGTH),
                    animal.getStat(StatCategory.WEIGHT),
                    animal.getStat(StatCategory.LIFESPAN),
                    animal.getStat(StatCategory.OFFSPRING)
            ));
        }
        return sb.toString();
    }

    private String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 2) + "..";
    }
}
