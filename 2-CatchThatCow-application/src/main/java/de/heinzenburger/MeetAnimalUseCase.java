package de.heinzenburger;

import java.util.HashMap;
import java.util.Map;

public class MeetAnimalUseCase {
    UiAnzeiger uiAnzeiger;

    public MeetAnimalUseCase(UiAnzeiger uiAnzeiger) {
        this.uiAnzeiger = uiAnzeiger;
    }

    public void sayHi() {
        // Beispiel-Tier für Tests
        Map<StatCategory, Integer> stats = new HashMap<>();
        stats.put(StatCategory.SPEED, 50);
        stats.put(StatCategory.LENGTH, 100);
        stats.put(StatCategory.WEIGHT, 200);
        stats.put(StatCategory.STRENGTH, 75);

        Animal animal = new Animal("Schwein", "Oink", 1, AnimalType.PREY, stats);
        uiAnzeiger.printsomething(animal.getNoise());
    }
}
