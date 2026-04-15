package de.heinzenburger.session;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.player.Player;
import de.heinzenburger.world.World;


/**
 * TODO: Candidate of a State pattern for game phases (exploring, in battle, encounter pending).
 * Holds transient runtime game state.
 * This is not persisted - only Player and World are saved.
 */
public class GameSession {

    private final Player player;
    private final World world;
    private Battle currentBattle;
    private Animal encounteredAnimal;
    private GamePhase phase;

    public GameSession(Player player, World world) {
        this.player = player;
        this.world = world;
        this.currentBattle = null;
        this.encounteredAnimal = null;
        this.phase = GamePhase.EXPLORING;
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Battle getCurrentBattle() {
        return currentBattle;
    }

    public Animal getEncounteredAnimal() {
        return encounteredAnimal;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void startBattle(Battle battle) {
        this.currentBattle = battle;
        this.encounteredAnimal = null;
        this.phase = GamePhase.IN_BATTLE;
    }

    public void endBattle() {
        this.currentBattle = null;
        this.phase = GamePhase.EXPLORING;
    }

    public void setEncounter(Animal animal) {
        this.encounteredAnimal = animal;
        this.phase = GamePhase.ENCOUNTER_PENDING;
    }

    public void clearEncounter() {
        this.encounteredAnimal = null;
        this.phase = GamePhase.EXPLORING;
    }
}
