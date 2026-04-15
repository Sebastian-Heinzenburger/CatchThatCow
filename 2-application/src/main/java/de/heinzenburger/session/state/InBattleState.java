package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.session.GamePhase;

import java.util.Optional;

/**
 * Player is currently in battle.
 * Can only transition back to EXPLORING when battle ends.
 */
public class InBattleState implements SessionState {

    private final Battle battle;

    public InBattleState(Battle battle) {
        if (battle == null) {
            throw new IllegalArgumentException("Battle cannot be null");
        }
        this.battle = battle;
    }

    @Override
    public GamePhase getPhase() {
        return GamePhase.IN_BATTLE;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canStartBattle() {
        return false;
    }

    @Override
    public boolean canFlee() {
        return battle.canFlee();
    }

    @Override
    public Optional<Battle> getCurrentBattle() {
        return Optional.of(battle);
    }

    @Override
    public SessionState transitionToEncounter(Animal animal) {
        throw new IllegalStateException("Cannot encounter animal while in battle");
    }

    @Override
    public SessionState transitionToBattle(Battle battle) {
        throw new IllegalStateException("Already in battle");
    }

    @Override
    public SessionState transitionToExploring() {
        return new ExploringState();
    }
}
