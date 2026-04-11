package de.heinzenburger;

public class PlayGameUseCase {
    private final Player player;
    private final WorldMap worldMap;
    private final GamePresenter gamePresenter;
    private final EncounterAnimalUseCase encounterUseCase;
    private final MovePlayerUseCase moveUseCase;
    private final ShowInventoryUseCase inventoryUseCase;

    public PlayGameUseCase(Player player,
                          WorldMap worldMap,
                          GamePresenter gamePresenter,
                          EncounterAnimalUseCase encounterUseCase,
                          MovePlayerUseCase moveUseCase,
                          ShowInventoryUseCase inventoryUseCase) {
        this.player = player;
        this.worldMap = worldMap;
        this.gamePresenter = gamePresenter;
        this.encounterUseCase = encounterUseCase;
        this.moveUseCase = moveUseCase;
        this.inventoryUseCase = inventoryUseCase;
    }

    public void start() {
        gamePresenter.showWelcome();

        // Gib dem Spieler 3 Starttiere
        initializePlayerInventory();

        boolean running = true;

        while (running) {
            try {
                gamePresenter.showMainMenu();
                GameAction action = gamePresenter.getUserAction();

                switch (action) {
                    case EXPLORE:
                        Biome currentBiome = worldMap.getBiomeAt(player.getPosition());
                        encounterUseCase.encounter(player, currentBiome);
                        break;

                    case MOVE:
                        moveUseCase.showNavigationAndMove(player);
                        break;

                    case VIEW_INVENTORY:
                        inventoryUseCase.showInventory(player);
                        break;

                    case QUIT:
                        running = false;
                        break;
                }
            } catch (IllegalStateException e) {
                // Spieler hat keine Tiere mehr
                gamePresenter.showGameOver(e.getMessage());
                running = false;
            } catch (Exception e) {
                // Andere Fehler
                gamePresenter.showGameOver("Ein Fehler ist aufgetreten: " + e.getMessage());
                running = false;
            }
        }
    }

    private void initializePlayerInventory() {
        // Gib dem Spieler 3 Starttiere aus dem Startbiom
        Biome startBiome = worldMap.getBiomeAt(player.getPosition());

        for (int i = 0; i < 3; i++) {
            Animal startAnimal = startBiome.spawnRandomAnimal();
            player.addAnimal(startAnimal);
        }
    }
}
