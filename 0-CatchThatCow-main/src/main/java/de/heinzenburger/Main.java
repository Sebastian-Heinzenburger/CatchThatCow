package de.heinzenburger;

public class Main {
    public static void main(String[] args) {
        // === PLUGIN LAYER ===
        TextPresenter textPresenter = new SystemOutPrintlnPresenter();

        // === ADAPTER LAYER ===
        UserInput userInput = new ScannerUserInput();

        FightPresenter fightPresenter = new TerminalFightPresenter(textPresenter, userInput);
        NavigationPresenter navigationPresenter = new TerminalNavigationPresenter(textPresenter, userInput);
        InventoryPresenter inventoryPresenter = new TerminalInventoryPresenter(textPresenter);
        GamePresenter gamePresenter = new TerminalGamePresenter(textPresenter, userInput);

        // === DOMAIN LAYER ===
        Position startPosition = new Position(0, 0);
        Player player = new Player(startPosition);
        WorldMap worldMap = new WorldMap(5); // (2*5+1)^2 = 11x11 Biome

        // === APPLICATION LAYER (USE CASES) ===
        FightUseCase fightUseCase = new FightUseCase(fightPresenter);
        EncounterAnimalUseCase encounterUseCase = new EncounterAnimalUseCase(fightUseCase);
        MovePlayerUseCase moveUseCase = new MovePlayerUseCase(worldMap, navigationPresenter);
        ShowInventoryUseCase inventoryUseCase = new ShowInventoryUseCase(inventoryPresenter);

        PlayGameUseCase playGameUseCase = new PlayGameUseCase(
                player,
                worldMap,
                gamePresenter,
                encounterUseCase,
                moveUseCase,
                inventoryUseCase
        );

        // === START GAME ===
        playGameUseCase.start();
    }
}