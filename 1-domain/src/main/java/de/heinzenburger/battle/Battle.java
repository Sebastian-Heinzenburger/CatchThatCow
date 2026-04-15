package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.shared.StatCategory;

public class Battle {
    private final Animal opponentAnimal;
    private final BattleInventory playerBattleInventory;
    private int playerScore;
    private int opponentScore;
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
        this.playerScore = 0;
        this.opponentScore = 0;
        this.state = BattleState.NOT_STARTED;
    }

    public void startBattle() {
        if (state != BattleState.NOT_STARTED) {
            throw new IllegalStateException("Battle has already been started");
        }
        this.state = BattleState.IN_PROGRESS;
    }

    public RoundResult playerAttack(Animal selectedAnimal, StatCategory category) {
        if (state != BattleState.IN_PROGRESS) {
            throw new IllegalStateException("Battle is not in progress");
        }
        if (!playerTurn) {
            throw new IllegalStateException("It is not the player's turn");
        }
        if (selectedAnimal == null) {
            throw new IllegalArgumentException("Selected animal cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (!playerBattleInventory.getAvailableAnimals().contains(selectedAnimal)) {
            throw new IllegalArgumentException("Selected animal is not available for battle");
        }

        playerBattleInventory.use(selectedAnimal);

        int playerStatValue = selectedAnimal.getStat(category);
        int opponentStatValue = opponentAnimal.getStat(category);

        RoundWinner winner;
        if (playerStatValue > opponentStatValue) {
            winner = RoundWinner.PLAYER;
            playerScore++;
        } else {
            winner = RoundWinner.OPPONENT;
            opponentScore++;
        }

        RoundResult result = new RoundResult(selectedAnimal, opponentAnimal, category,
                winner, playerStatValue, opponentStatValue);

        if (playerScore >= 3 || opponentScore >= 3) {
            state = BattleState.FINISHED;
        } else {
            playerTurn = false;
            opponentSelectedCategory = null; // Clear for next opponent turn
        }

        return result;
    }

    public RoundResult opponentAttack(Animal selectedAnimal) {
        if (state != BattleState.IN_PROGRESS) {
            throw new IllegalStateException("Battle is not in progress");
        }
        if (playerTurn) {
            throw new IllegalStateException("It is not the opponent's turn");
        }
        if (selectedAnimal == null) {
            throw new IllegalArgumentException("Selected animal cannot be null");
        }
        if (!playerBattleInventory.getAvailableAnimals().contains(selectedAnimal)) {
            throw new IllegalArgumentException("Selected animal is not available for battle");
        }
        if (!playerBattleInventory.hasAvailableAnimals()) {
            throw new IllegalStateException("Player has no available animals for opponent attack");
        }

        // Use the category that was selected (and shown to player)
        StatCategory category = getOpponentSelectedCategory();

        // Player has chosen the animal to defend with
        playerBattleInventory.use(selectedAnimal);

        int playerStatValue = selectedAnimal.getStat(category);
        int opponentStatValue = opponentAnimal.getStat(category);

        RoundWinner winner;
        if (playerStatValue > opponentStatValue) {
            winner = RoundWinner.PLAYER;
            playerScore++;
        } else {
            winner = RoundWinner.OPPONENT;
            opponentScore++;
        }

        RoundResult result = new RoundResult(selectedAnimal, opponentAnimal, category,
                winner, playerStatValue, opponentStatValue);

        if (playerScore >= 3 || opponentScore >= 3) {
            state = BattleState.FINISHED;
        } else {
            playerTurn = true;
        }

        // Clear the opponent's selected category for the next round
        opponentSelectedCategory = null;

        return result;
    }

    public StatCategory getOpponentSelectedCategory() {
        if (state != BattleState.IN_PROGRESS) {
            throw new IllegalStateException("Battle is not in progress");
        }
        if (playerTurn) {
            throw new IllegalStateException("It is the player's turn, not the opponent's");
        }
        if (opponentSelectedCategory == null) {
            // Generate category on first access during opponent's turn
            // TODO: shouldnt this change every opponent turn?
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
        return playerScore >= 3 ? RoundWinner.PLAYER : RoundWinner.OPPONENT;
    }

    public boolean canFlee() {
        return playerTurn && opponentAnimal.isPrey() && state == BattleState.IN_PROGRESS;
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    public BattleInventory getPlayerBattleInventory() {
        return playerBattleInventory;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getOpponentScore() {
        return opponentScore;
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
