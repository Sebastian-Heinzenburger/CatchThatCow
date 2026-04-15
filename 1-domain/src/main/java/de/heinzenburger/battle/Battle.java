package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.BattleAlreadyStartedException;
import de.heinzenburger.battle.exception.BattleNotInProgressException;
import de.heinzenburger.battle.exception.NotPlayersTurnException;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.shared.StatCategory;

import java.util.Collections;
import java.util.List;

public class Battle {
    private final Animal opponentAnimal;
    private final BattleInventory playerBattleInventory;
    private Score playerScore;
    private Score opponentScore;
    private BattleState state;
    private boolean playerTurn;
    private final RandomNumberGenerator random;
    private StatCategory opponentSelectedCategory;

    public Battle(Animal opponentAnimal, BattleInventory playerBattleInventory, RandomNumberGenerator random) {
        if (opponentAnimal == null) {
            throw new IllegalArgumentException("Opponent animal cannot be null");
        }
        if (playerBattleInventory == null) {
            throw new IllegalArgumentException("Player battle inventory cannot be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }

        this.opponentAnimal = opponentAnimal;
        this.playerBattleInventory = playerBattleInventory;
        this.playerTurn = opponentAnimal.isPrey();
        this.random = random;
        this.playerScore = new Score();
        this.opponentScore = new Score();
        this.state = BattleState.NOT_STARTED;
    }

    public void startBattle() throws BattleAlreadyStartedException {
        if (state != BattleState.NOT_STARTED) {
            throw new BattleAlreadyStartedException();
        }
        this.state = BattleState.IN_PROGRESS;
    }

    public RoundResult playerAttack(Animal selectedAnimal, StatCategory category)
            throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException {
        if (state != BattleState.IN_PROGRESS) {
            throw new BattleNotInProgressException();
        }
        if (!playerTurn) {
            throw new NotPlayersTurnException();
        }
        if (selectedAnimal == null) {
            throw new IllegalArgumentException("Selected animal cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (!playerBattleInventory.getAvailableAnimals().contains(selectedAnimal)) {
            throw new AnimalNotAvailableException(selectedAnimal);
        }

        playerBattleInventory.use(selectedAnimal);

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

        RoundResult result = new RoundResult(selectedAnimal, opponentAnimal, category,
                winner, playerStatValue, opponentStatValue);

        if (playerScore.hasWon() || opponentScore.hasWon()) {
            state = BattleState.FINISHED;
        } else {
            playerTurn = false;
            opponentSelectedCategory = null; // Clear for next opponent turn
        }

        return result;
    }

    public RoundResult opponentAttack(Animal selectedAnimal)
            throws BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException {
        if (state != BattleState.IN_PROGRESS) {
            throw new BattleNotInProgressException();
        }
        if (playerTurn) {
            throw new NotPlayersTurnException("It is not the opponent's turn");
        }
        if (selectedAnimal == null) {
            throw new IllegalArgumentException("Selected animal cannot be null");
        }
        if (!playerBattleInventory.getAvailableAnimals().contains(selectedAnimal)) {
            throw new AnimalNotAvailableException(selectedAnimal);
        }
        if (!playerBattleInventory.hasAvailableAnimals()) {
            throw new AnimalNotAvailableException();
        }

        // Use the category that was selected (and shown to player)
        StatCategory category = getOpponentSelectedCategoryInternal();

        // Player has chosen the animal to defend with
        playerBattleInventory.use(selectedAnimal);

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

        RoundResult result = new RoundResult(selectedAnimal, opponentAnimal, category,
                winner, playerStatValue, opponentStatValue);

        if (playerScore.hasWon() || opponentScore.hasWon()) {
            state = BattleState.FINISHED;
        } else {
            playerTurn = true;
        }

        // Clear the opponent's selected category for the next round
        opponentSelectedCategory = null;

        return result;
    }

    public StatCategory getOpponentSelectedCategory()
            throws BattleNotInProgressException, NotPlayersTurnException {
        if (state != BattleState.IN_PROGRESS) {
            throw new BattleNotInProgressException();
        }
        if (playerTurn) {
            throw new NotPlayersTurnException("It is the player's turn, not the opponent's");
        }
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
        return state == BattleState.FINISHED;
    }

    public RoundWinner getWinner() {
        if (!isFinished()) {
            return null;
        }
        return playerScore.hasWon() ? RoundWinner.PLAYER : RoundWinner.OPPONENT;
    }

    public boolean canFlee() {
        return playerTurn && opponentAnimal.isPrey() && state == BattleState.IN_PROGRESS;
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    /**
     * Returns an unmodifiable list of animals available for the player to use in battle.
     * @return unmodifiable list of available animals
     */
    public List<Animal> getAvailableAnimals() {
        return Collections.unmodifiableList(playerBattleInventory.getAvailableAnimals());
    }

    /**
     * Returns an unmodifiable list of all animals in the battle inventory.
     * @return unmodifiable list of all battle animals
     */
    public List<Animal> getAllBattleAnimals() {
        return Collections.unmodifiableList(playerBattleInventory.getAllAnimals());
    }

    /**
     * @deprecated Use {@link #getAvailableAnimals()} or {@link #getAllBattleAnimals()} instead.
     * This method exposes internal state and will be removed in a future version.
     */
    @Deprecated
    public BattleInventory getPlayerBattleInventory() {
        return playerBattleInventory;
    }

    public int getPlayerScore() {
        return playerScore.getValue();
    }

    public int getOpponentScore() {
        return opponentScore.getValue();
    }

    public BattleState getState() {
        return state;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    @Override
    public String toString() {
        return "Battle{" +
                "opponent=" + opponentAnimal +
                ", score=" + playerScore + "-" + opponentScore +
                ", state=" + state +
                '}';
    }
}
