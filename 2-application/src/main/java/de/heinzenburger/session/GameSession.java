package de.heinzenburger.session;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.player.Player;
import de.heinzenburger.session.state.EncounterPendingState;
import de.heinzenburger.session.state.ExploringState;
import de.heinzenburger.session.state.InBattleState;
import de.heinzenburger.session.state.SessionState;
import de.heinzenburger.world.World;


/**
 * Holds transient runtime game state using State pattern for game phases.
 * This is not persisted - only Player and World are saved.
 */
public class GameSession {

    private final Player player;
    private final World world;
    private SessionState state;

    public GameSession(Player player, World world) {
        this.player = player;
        this.world = world;
        this.state = new ExploringState();
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Battle getCurrentBattle() {
        if (state instanceof InBattleState battleState) {
            return battleState.getBattle();
        }
        return null;
    }

    public Animal getEncounteredAnimal() {
        if (state instanceof EncounterPendingState encounterState) {
            return encounterState.getEncounteredAnimal();
        }
        return null;
    }

    public GamePhase getPhase() {
        return state.getPhase();
    }

    public boolean canMove() {
        return state.canMove();
    }

    public boolean canStartBattle() {
        return state.canStartBattle();
    }

    public boolean canFlee() {
        return state.canFlee();
    }

    public void startBattle(Battle battle) {
        this.state = state.transitionToBattle(battle);
    }

    public void endBattle() {
        this.state = state.transitionToExploring();
    }

    public void setEncounter(Animal animal) {
        this.state = state.transitionToEncounter(animal);
    }

    public void clearEncounter() {
        this.state = state.transitionToExploring();
    }
}
