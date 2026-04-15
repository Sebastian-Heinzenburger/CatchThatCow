package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.exception.*;
import de.heinzenburger.battle.opponentstrategy.OpponentStrategy;
import de.heinzenburger.battle.opponentstrategy.RandomOpponentStrategy;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.shared.StatCategory;

import java.util.Collections;
import java.util.List;

public class Battle {

    private final TurnManager turnManager;
    private final BattleInventory playerBattleInventory;
    private final Animal opponentAnimal;
    private final OpponentStrategy opponentStrategy;
    private BattleState state;

    public Battle(Animal opponentAnimal, BattleInventory playerBattleInventory, RandomNumberGenerator random) {
        if (opponentAnimal == null) throw new IllegalArgumentException("Opponent animal cannot be null");
        if (playerBattleInventory == null) throw new IllegalArgumentException("Player battle inventory cannot be null");
        if (random == null) throw new IllegalArgumentException("Random cannot be null");

        this.state = new BattleState.NotStarted();
        this.turnManager = new TurnManager(opponentAnimal.isPrey());
        this.playerBattleInventory = playerBattleInventory;
        this.opponentAnimal = opponentAnimal;
        this.opponentStrategy = new RandomOpponentStrategy(random);
    }

    public void startBattle() throws BattleAlreadyStartedException {
        if (!(state instanceof BattleState.NotStarted)) throw new BattleAlreadyStartedException();
        this.state = new BattleState.InProgress();
    }

    public RoundResult playerAttack(Animal selectedAnimal, StatCategory category) throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException {
        if (!(state instanceof BattleState.InProgress inProgress)) throw new BattleNotInProgressException();
        if (!turnManager.isPlayerTurn()) throw new NotPlayersTurnException();
        if (category == null) throw new IllegalArgumentException("Category cannot be null");

        ensureAnimalIsAvailable(selectedAnimal);
        return executeRound(selectedAnimal, category, inProgress);
    }

    public RoundResult opponentAttack(Animal selectedAnimal) throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException {
        if (!(state instanceof BattleState.InProgress inProgress)) throw new BattleNotInProgressException();
        if (turnManager.isPlayerTurn()) throw new NotPlayersTurnException("It is not the opponent's turn");
        if (!playerBattleInventory.hasAvailableAnimals()) throw new NoMoreAnimalsAvailableException();

        ensureAnimalIsAvailable(selectedAnimal);
        RoundResult result = executeRound(selectedAnimal, opponentStrategy.selectCategory(), inProgress);
        opponentStrategy.reset();
        return result;
    }

    private void ensureAnimalIsAvailable(Animal selectedAnimal) throws AnimalNotAvailableException {
        if (selectedAnimal == null) throw new IllegalArgumentException("Selected animal cannot be null");
        if (!playerBattleInventory.getAvailableAnimals().contains(selectedAnimal))
            throw new AnimalNotAvailableException(selectedAnimal);
    }


    private RoundResult executeRound(Animal selectedAnimal, StatCategory category, BattleState.InProgress currentState) {
        playerBattleInventory.markAsUsed(selectedAnimal);

        int statValuePlayer = selectedAnimal.getStat(category);
        int statValueOpponent = opponentAnimal.getStat(category);

        RoundWinner winner = statValuePlayer > statValueOpponent ? RoundWinner.PLAYER : RoundWinner.OPPONENT;
        this.state = switch (winner) {
            case PLAYER -> currentState.incrementPlayerScore();
            case OPPONENT -> currentState.incrementOpponentScore();
        };

        turnManager.switchTurn();
        return new RoundResult(selectedAnimal, opponentAnimal, winner, category, statValuePlayer, statValueOpponent);
    }

    public StatCategory getOpponentSelectedCategory() throws BattleNotInProgressException, NotPlayersTurnException {
        if (!(state instanceof BattleState.InProgress)) throw new BattleNotInProgressException();
        if (turnManager.isPlayerTurn())
            throw new NotPlayersTurnException("It is the player's turn, not the opponent's");
        return opponentStrategy.selectCategory();
    }

    public boolean isFinished() {
        return state instanceof BattleState.Finished;
    }

    public RoundWinner getWinner() {
        if (!(state instanceof BattleState.Finished finished))
            throw new IllegalStateException("Battle is not finished yet");
        return finished.winner();
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    public List<Animal> getAvailableAnimals() {
        return Collections.unmodifiableList(playerBattleInventory.getAvailableAnimals());
    }

    public List<Animal> getAllBattleAnimals() {
        return playerBattleInventory.getAllAnimals();
    }

    public int getPlayerScore() {
        return switch (state) {
            case BattleState.NotStarted() -> 0;
            case BattleState.InProgress(var ps, var os) -> ps.getValue();
            case BattleState.Finished(var ps, var os, var w) -> ps.getValue();
        };
    }

    public int getOpponentScore() {
        return switch (state) {
            case BattleState.NotStarted() -> 0;
            case BattleState.InProgress(var ps, var os) -> os.getValue();
            case BattleState.Finished(var ps, var os, var w) -> os.getValue();
        };
    }

    public BattleState getState() {
        return state;
    }

    public boolean isPlayerTurn() {
        return turnManager.isPlayerTurn();
    }

    @Override
    public String toString() {
        return "Battle{" + "opponent=" + opponentAnimal + ", score=" + getPlayerScore() + "-" + getOpponentScore() + ", state=" + state + '}';
    }
}
