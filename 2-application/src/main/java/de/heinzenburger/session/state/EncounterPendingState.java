package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.session.GamePhase;

import java.util.Optional;

/**
 * Player has encountered an animal and must decide to fight or flee.
 * Can transition to IN_BATTLE or back to EXPLORING.
 */
public class EncounterPendingState implements SessionState {

    private final Animal encounteredAnimal;

    public EncounterPendingState(Animal encounteredAnimal) {
        if (encounteredAnimal == null) {
            throw new IllegalArgumentException("Encountered animal cannot be null");
        }
        this.encounteredAnimal = encounteredAnimal;
    }

    @Override
    public GamePhase getPhase() {
        return GamePhase.ENCOUNTER_PENDING;
    }

    @Override
    public boolean canStartBattle() {
        return true;
    }

    @Override
    public boolean canFlee() {
        return encounteredAnimal.isPrey();
    }

    @Override
    public Optional<Animal> getEncounteredAnimal() {
        return Optional.of(encounteredAnimal);
    }

    @Override
    public SessionState transitionToBattle(Battle battle) {
        if (battle == null) {
            throw new IllegalArgumentException("Battle cannot be null");
        }
        return new InBattleState(battle);
    }

    @Override
    public SessionState transitionToExploring() {
        return new ExploringState();
    }
}
