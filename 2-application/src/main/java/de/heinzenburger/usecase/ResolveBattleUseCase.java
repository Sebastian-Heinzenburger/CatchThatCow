package de.heinzenburger.usecase;

import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundWinner;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;

/**
 * Resolves a finished battle and applies consequences.
 * - If player won: catch the opponent animal
 * - If player lost: lose a random animal
 */
public class ResolveBattleUseCase {

    private final GameSessionManager sessionManager;

    public ResolveBattleUseCase(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Resolves the current battle and applies results.
     *
     * @return the result containing the winner and any caught/lost animal
     * @throws GameNotStartedException   if no active game session
     * @throws InvalidGamePhaseException if not in IN_BATTLE phase or battle not finished
     */
    public BattleOutcome execute() throws GameNotStartedException, InvalidGamePhaseException {
        GameSession session = sessionManager.getCurrentSession();

        if (session.getPhase() != GamePhase.IN_BATTLE)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.IN_BATTLE);

        Battle battle = session.getCurrentBattle();

        // Validate battle is finished
        if (!battle.isFinished())
            throw new InvalidGamePhaseException(session.getPhase(), "resolve an unfinished battle");

        Player player = session.getPlayer();
        RoundWinner winner = battle.getWinner();
        BattleOutcome outcome;

        if (winner == RoundWinner.PLAYER) {
            // Player wins - catch the opponent animal!
            player.addAnimal(battle.getOpponentAnimal());
            outcome = new BattleOutcome(winner, battle.getOpponentAnimal(), null);
        } else {
            // Player loses - lose a random animal
            try {
                var lostAnimal = player.loseRandomAnimal();
                outcome = new BattleOutcome(winner, null, lostAnimal);
            } catch (InsufficientAnimalsException e) {
                // Player has no animals left - still create outcome
                outcome = new BattleOutcome(winner, null, null);
            }
        }

        // End the battle and return to exploring
        session.endBattle();

        return outcome;
    }

    /**
     * The outcome of a resolved battle.
     */
    public record BattleOutcome(RoundWinner winner, de.heinzenburger.animal.Animal caughtAnimal,
                                de.heinzenburger.animal.Animal lostAnimal) {
        public boolean playerWon() {
            return winner == RoundWinner.PLAYER;
        }
    }
}
