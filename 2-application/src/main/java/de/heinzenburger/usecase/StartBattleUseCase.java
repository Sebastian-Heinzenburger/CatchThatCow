package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.exception.BattleAlreadyStartedException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.services.BattleFactory;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;

/**
 * Starts a battle from a pending encounter.
 */
public class StartBattleUseCase {

    private final GameSessionManager sessionManager;
    private final BattleFactory battleFactory;

    public StartBattleUseCase(GameSessionManager sessionManager, BattleFactory battleFactory) {
        this.sessionManager = sessionManager;
        this.battleFactory = battleFactory;
    }

    /**
     * Starts a battle with the currently encountered animal.
     *
     * @return the created and started battle
     * @throws GameNotStartedException      if no active game session
     * @throws InvalidGamePhaseException    if not in ENCOUNTER_PENDING phase
     * @throws InsufficientAnimalsException if player doesn't have enough animals
     */
    public Battle execute() throws GameNotStartedException, InvalidGamePhaseException, InsufficientAnimalsException {
        GameSession session = sessionManager.getCurrentSession();

        if (session.getPhase() != GamePhase.ENCOUNTER_PENDING)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.ENCOUNTER_PENDING);

        // Create battle using the encountered animal
        Battle battle = battleFactory.createBattle(session.getPlayer(), session.getEncounteredAnimal());

        // Start the battle
        try {
            battle.startBattle();
        } catch (BattleAlreadyStartedException e) {
            // This should never happen since we just created the battle
            throw new InvalidGamePhaseException("Battle already started", e);
        }

        // Update session
        session.startBattle(battle);

        return battle;
    }
}
