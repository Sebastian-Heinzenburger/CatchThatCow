package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.exception.*;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.shared.StatCategory;

import java.util.Collections;
import java.util.List;

// TODO: Refactor this god class
public class Battle {
    private final Animal opponentAnimal;
    private final BattleInventory playerBattleInventory;
    private final RandomNumberGenerator random;
    private Score playerScore;
    private Score opponentScore;
    private BattleProgressState state;
    private boolean playerTurn;
    private StatCategory opponentSelectedCategory;

    public Battle(Animal opponentAnimal, BattleInventory playerBattleInventory, RandomNumberGenerator random) {
        if (opponentAnimal == null) throw new IllegalArgumentException("Opponent animal cannot be null");
        if (playerBattleInventory == null) throw new IllegalArgumentException("Player battle inventory cannot be null");
        if (random == null) throw new IllegalArgumentException("Random cannot be null");

        this.opponentAnimal = opponentAnimal;
        this.playerBattleInventory = playerBattleInventory;
        this.playerTurn = opponentAnimal.isPrey();
        this.random = random;
        this.playerScore = new Score();
        this.opponentScore = new Score();
        this.state = BattleProgressState.NOT_STARTED;
    }

    public void startBattle() throws BattleAlreadyStartedException {
        if (state != BattleProgressState.NOT_STARTED) throw new BattleAlreadyStartedException();
        this.state = BattleProgressState.IN_PROGRESS;
    }

    public RoundResult playerAttack(Animal selectedAnimal, StatCategory category) throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException {
        if (selectedAnimal == null) throw new IllegalArgumentException("Selected animal cannot be null");
        if (category == null) throw new IllegalArgumentException("Category cannot be null");

        if (state != BattleProgressState.IN_PROGRESS) throw new BattleNotInProgressException();
        if (!playerTurn) throw new NotPlayersTurnException();
        validateAnimalAvailable(selectedAnimal);

        RoundResult result = executeRound(selectedAnimal, category);
        updateStateAfterRound(false);

        return result;
    }

    private void clearOpponentSelectedCategory() {
        opponentSelectedCategory = null;
    }

    public RoundResult opponentAttack(Animal selectedAnimal) throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException {
        if (state != BattleProgressState.IN_PROGRESS) throw new BattleNotInProgressException();
        if (playerTurn) throw new NotPlayersTurnException("It is not the opponent's turn");
        if (selectedAnimal == null) throw new IllegalArgumentException("Selected animal cannot be null");
        if (!playerBattleInventory.hasAvailableAnimals()) throw new NoMoreAnimalsAvailableException();
        validateAnimalAvailable(selectedAnimal);

        StatCategory category = getOpponentSelectedCategoryInternal();
        RoundResult result = executeRound(selectedAnimal, category);
        updateStateAfterRound(true);

        return result;
    }

    private void validateAnimalAvailable(Animal animal) throws AnimalNotAvailableException {
        if (!playerBattleInventory.getAvailableAnimals().contains(animal)) {
            throw new AnimalNotAvailableException(animal);
        }
    }

    private RoundResult executeRound(Animal selectedAnimal, StatCategory category) {
        playerBattleInventory.markAsUsed(selectedAnimal);

        int playerStatValue = selectedAnimal.getStat(category);
        int opponentStatValue = opponentAnimal.getStat(category);

        RoundWinner winner;
        if (playerStatValue > opponentStatValue) {
            winner = RoundWinner.PLAYER;
            playerScore = playerScore.increment();
        } else {
            winner = RoundWinner.OPPONENT;
            opponentScore = opponentScore.increment();
        }

        return new RoundResult(selectedAnimal, opponentAnimal, category, winner, playerStatValue, opponentStatValue);
    }

    private void updateStateAfterRound(boolean switchToPlayerTurn) {
        if (playerScore.hasWon() || opponentScore.hasWon()) {
            state = BattleProgressState.FINISHED;
        } else {
            playerTurn = switchToPlayerTurn;
        }
        clearOpponentSelectedCategory();
    }

    public StatCategory getOpponentSelectedCategory() throws BattleNotInProgressException, NotPlayersTurnException {
        if (state != BattleProgressState.IN_PROGRESS) throw new BattleNotInProgressException();
        if (playerTurn) throw new NotPlayersTurnException("It is the player's turn, not the opponent's");
        return getOpponentSelectedCategoryInternal();
    }

    private StatCategory getOpponentSelectedCategoryInternal() {
        if (opponentSelectedCategory == null) {
            // Generate category on first access during opponent's turn
            StatCategory[] categories = StatCategory.values();
            opponentSelectedCategory = categories[random.nextInt(categories.length)];
        }
        return opponentSelectedCategory;
    }

    public boolean isFinished() {
        return state == BattleProgressState.FINISHED;
    }

    public RoundWinner getWinner() {
        if (!isFinished()) return null;
        return playerScore.hasWon() ? RoundWinner.PLAYER : RoundWinner.OPPONENT;
    }

    public boolean canFlee() {
        return playerTurn && opponentAnimal.isPrey() && state == BattleProgressState.IN_PROGRESS;
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    /**
     * Returns an unmodifiable list of animals available for the player to use in battle.
     *
     * @return unmodifiable list of available animals
     */
    public List<Animal> getAvailableAnimals() {
        return Collections.unmodifiableList(playerBattleInventory.getAvailableAnimals());
    }

    /**
     * Returns an unmodifiable list of all animals in the battle inventory.
     *
     * @return unmodifiable list of all battle animals
     */
    public List<Animal> getAllBattleAnimals() {
        return Collections.unmodifiableList(playerBattleInventory.getAllAnimals());
    }

    public int getPlayerScore() {
        return playerScore.getValue();
    }

    public int getOpponentScore() {
        return opponentScore.getValue();
    }

    public BattleProgressState getState() {
        return state;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    @Override
    public String toString() {
        return "Battle{" + "opponent=" + opponentAnimal + ", score=" + playerScore + "-" + opponentScore + ", state=" + state + '}';
    }
}
