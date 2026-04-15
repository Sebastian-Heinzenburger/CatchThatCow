package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.BattleNotInProgressException;
import de.heinzenburger.battle.exception.NoMoreAnimalsAvailableException;
import de.heinzenburger.battle.exception.NotPlayersTurnException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.StatCategory;

/**
 * Handles defending against the opponent's attack in a battle.
 * Player selects which animal to use to defend.
 */
public class DefendAgainstAttackUseCase {

    private final GameSessionManager sessionManager;

    public DefendAgainstAttackUseCase(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Gets the stat category the opponent has selected for their attack.
     * Call this before execute() to know which stat will be compared.
     *
     * @return the opponent's selected stat category
     * @throws GameNotStartedException   if no active game session
     * @throws InvalidGamePhaseException if not in IN_BATTLE phase or it's player's turn
     */
    public StatCategory getOpponentSelectedCategory() throws GameNotStartedException, InvalidGamePhaseException {
        GameSession session = sessionManager.getCurrentSession();

        if (session.getPhase() != GamePhase.IN_BATTLE)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.IN_BATTLE);

        Battle battle = session.getCurrentBattle();

        if (battle.isPlayerTurn())
            throw new InvalidGamePhaseException(session.getPhase(), "see opponent's category when it's your turn");

        try {
            // TODO: Violating the Law of Demeter here - see LawOfDemeterViolation.md
            return battle.getOpponentSelectedCategory();
        } catch (BattleNotInProgressException | NotPlayersTurnException e) {
            throw new InvalidGamePhaseException(e.getMessage(), e);
        }
    }

    /**
     * Defends against the opponent's attack with the selected animal.
     *
     * @param selectedAnimal the animal to use for defense
     * @return the result of the round
     * @throws GameNotStartedException         if no active game session
     * @throws InvalidGamePhaseException       if not in IN_BATTLE phase or it's player's turn
     * @throws AnimalNotAvailableException     if the selected animal is not available
     * @throws NoMoreAnimalsAvailableException if no more animals are available
     */
    public RoundResult execute(Animal selectedAnimal) throws GameNotStartedException, InvalidGamePhaseException, AnimalNotAvailableException, NoMoreAnimalsAvailableException {
        GameSession session = sessionManager.getCurrentSession();

        if (session.getPhase() != GamePhase.IN_BATTLE)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.IN_BATTLE);

        Battle battle = session.getCurrentBattle();

        if (battle.isPlayerTurn())
            throw new InvalidGamePhaseException(session.getPhase(), "defend when it's your turn to attack");

        try {
            return battle.opponentAttack(selectedAnimal);
        } catch (BattleNotInProgressException | NotPlayersTurnException e) {
            throw new InvalidGamePhaseException(e.getMessage(), e);
        }
    }
}
