package de.heinzenburger;

public class TerminalGamePresenter implements GamePresenter {
    private final TextPresenter textPresenter;
    private final UserInput userInput;

    public TerminalGamePresenter(TextPresenter textPresenter, UserInput userInput) {
        this.textPresenter = textPresenter;
        this.userInput = userInput;
    }

    @Override
    public void showWelcome() {
        textPresenter.print("");
        textPresenter.print("╔═══════════════════════════════════════╗");
        textPresenter.print("║                                       ║");
        textPresenter.print("║        CATCH THAT COW!                ║");
        textPresenter.print("║                                       ║");
        textPresenter.print("║   Fange wilde Tiere in epischen       ║");
        textPresenter.print("║   Stat-Battles!                       ║");
        textPresenter.print("║                                       ║");
        textPresenter.print("╚═══════════════════════════════════════╝");
        textPresenter.print("");
    }

    @Override
    public void showMainMenu() {
        textPresenter.print("");
        textPresenter.print("--- HAUPTMENÜ ---");
        textPresenter.print("1. Erkunden (Tier begegnen)");
        textPresenter.print("2. Bewegen (anderes Biom)");
        textPresenter.print("3. Inventar anzeigen");
        textPresenter.print("4. Beenden");
        textPresenter.print("");
        textPresenter.print("Deine Wahl:");
    }

    @Override
    public GameAction getUserAction() {
        int choice = userInput.readInt(1, 4);

        switch (choice) {
            case 1:
                return GameAction.EXPLORE;
            case 2:
                return GameAction.MOVE;
            case 3:
                return GameAction.VIEW_INVENTORY;
            case 4:
                return GameAction.QUIT;
            default:
                textPresenter.print("Ungültige Eingabe!");
                return getUserAction();
        }
    }

    @Override
    public void showGameOver(String reason) {
        textPresenter.print("");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("         GAME OVER");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print(reason);
        textPresenter.print("");
        textPresenter.print("Danke fürs Spielen!");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("");
    }
}
