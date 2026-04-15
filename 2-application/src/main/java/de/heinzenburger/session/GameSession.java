package de.heinzenburger.session;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.player.Player;
import de.heinzenburger.session.state.ExploringState;
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
        return state.getCurrentBattle().orElse(null);
    }

    public Animal getEncounteredAnimal() {
        return state.getEncounteredAnimal().orElse(null);
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
