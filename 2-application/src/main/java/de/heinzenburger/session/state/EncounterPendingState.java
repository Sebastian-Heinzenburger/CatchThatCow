package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.session.GamePhase;

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
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canStartBattle() {
        return true;
    }

    @Override
    public boolean canFlee() {
        return encounteredAnimal.isPrey();
    }

    public Animal getEncounteredAnimal() {
        return encounteredAnimal;
    }

    @Override
    public SessionState transitionToEncounter(Animal animal) {
        throw new IllegalStateException("Already in encounter pending state");
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
