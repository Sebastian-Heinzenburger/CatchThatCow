package de.heinzenburger;

public class EncounterAnimalUseCase {
    private final FightUseCase fightUseCase;

    public EncounterAnimalUseCase(FightUseCase fightUseCase) {
        this.fightUseCase = fightUseCase;
    }

    public void encounter(Player player, Biome biome) {
        Animal wildAnimal = biome.spawnRandomAnimal();

        // Bei Fluchttier könnte man hier die Möglichkeit zur Flucht implementieren
        // Für jetzt: Direkt zum Kampf

        fightUseCase.executeFight(player, wildAnimal);
    }
}
