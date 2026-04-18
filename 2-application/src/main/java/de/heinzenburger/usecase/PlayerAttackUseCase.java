package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.BattleNotInProgressException;
import de.heinzenburger.battle.exception.NotPlayersTurnException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.StatCategory;

/**
 * Handles the player's attack turn in a battle.
 */
public class PlayerAttackUseCase {

    private final GameSessionManager sessionManager;

    public PlayerAttackUseCase(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Executes the player's attack with the selected animal and stat category.
     *
     * @param selectedAnimal the animal to use for this round
     * @param category       the stat category to compete with
     * @return the result of the round
     * @throws GameNotStartedException     if no active game session
     * @throws InvalidGamePhaseException   if not in IN_BATTLE phase or not player's turn
     * @throws AnimalNotAvailableException if the selected animal is not available
     */
    public RoundResult execute(Animal selectedAnimal, StatCategory category) throws GameNotStartedException, InvalidGamePhaseException, AnimalNotAvailableException {
        GameSession session = sessionManager.getCurrentSession();

        if (session.getPhase() != GamePhase.IN_BATTLE)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.IN_BATTLE);

        Battle battle = session.getCurrentBattle();

        if (!battle.isPlayerTurn())
            throw new InvalidGamePhaseException(session.getPhase(), "attack when it's not your turn");

        try {
            return battle.playerAttack(selectedAnimal, category);
        } catch (BattleNotInProgressException | NotPlayersTurnException e) {
           throw new InvalidGamePhaseException(e.getMessage(), e);
        }
    }
}
