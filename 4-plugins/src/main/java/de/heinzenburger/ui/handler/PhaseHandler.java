package de.heinzenburger.ui.handler;

import de.heinzenburger.session.GameSession;
import de.heinzenburger.ui.command.CommandResult;

import java.util.List;

/**
 * Strategy interface for phase-specific command handling.
 * Each game phase has its own handler that knows which commands are valid
 * and how to execute them.
 *
 * Design Pattern: Strategy
 * - Separates phase-specific logic (OCP)
 * - Each handler has single responsibility (SRP)
 * - Enables isolated testing
 */
public interface PhaseHandler {

    /**
     * Handles user input for the current phase.
     *
     * @param input   the raw user input string
     * @param session the current game session
     * @return the result of command execution
     */
    CommandResult handle(String input, GameSession session);

    /**
     * Returns the list of available commands for this phase.
     * Used for help display.
     *
     * @return list of command descriptions
     */
    List<String> getAvailableCommands();
}
