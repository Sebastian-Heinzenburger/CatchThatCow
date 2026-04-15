package de.heinzenburger.battle;

/**
 * Sealed interface representing battle state with associated data.
 * Models the state machine: NotStarted -> InProgress -> Finished
 * <p>
 * Similar to Rust's enum with associated data:
 * enum BattleState { NotStarted, InProgress(Score, Score), Finished(Score, Score, RoundWinner) }
 */
public sealed interface BattleState permits BattleState.NotStarted, BattleState.InProgress, BattleState.Finished {

    record NotStarted() implements BattleState {
    }

    record InProgress(Score playerScore, Score opponentScore) implements BattleState {
        public InProgress {
            if (playerScore == null) throw new IllegalArgumentException("Player score cannot be null");
            if (opponentScore == null) throw new IllegalArgumentException("Opponent score cannot be null");
        }

        public InProgress() {
            this(new Score(), new Score());
        }

        public BattleState incrementPlayerScore() {
            Score newPlayerScore = playerScore.increment();
            if (newPlayerScore.hasWon())
                return new Finished(newPlayerScore, opponentScore, RoundWinner.PLAYER);
            return new InProgress(playerScore.increment(), opponentScore);
        }

        public BattleState incrementOpponentScore() {
            Score newOpponentScore = opponentScore.increment();
            if (newOpponentScore.hasWon())
                return new Finished(playerScore, newOpponentScore, RoundWinner.OPPONENT);
            return new InProgress(playerScore, opponentScore.increment());
        }
    }

    record Finished(Score playerScore, Score opponentScore, RoundWinner winner) implements BattleState {
        public Finished {
            if (playerScore == null) throw new IllegalArgumentException("Player score cannot be null");
            if (opponentScore == null) throw new IllegalArgumentException("Opponent score cannot be null");
            if (winner == null) throw new IllegalArgumentException("Winner cannot be null");
        }
    }
}
