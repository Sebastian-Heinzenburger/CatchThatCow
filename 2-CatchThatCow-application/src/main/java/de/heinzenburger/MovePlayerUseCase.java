package de.heinzenburger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovePlayerUseCase {
    private final WorldMap worldMap;
    private final NavigationPresenter presenter;

    public MovePlayerUseCase(WorldMap worldMap, NavigationPresenter presenter) {
        this.worldMap = worldMap;
        this.presenter = presenter;
    }

    public void showNavigationAndMove(Player player) {
        Position currentPosition = player.getPosition();
        Biome currentBiome = worldMap.getBiomeAt(currentPosition);

        presenter.showCurrentBiome(currentBiome);

        // Zeige verfügbare Richtungen
        Map<Direction, Biome> surroundingBiomes = getSurroundingBiomes(currentPosition);
        presenter.showNavigationMenu(surroundingBiomes);

        // Prüfe ob Spieler bleiben möchte
        if (presenter.wantsToStay()) {
            return;
        }

        Direction direction = presenter.getUserDirection();
        move(player, direction);
    }

    private void move(Player player, Direction direction) {
        Position newPosition = player.getPosition().move(direction);

        if (!worldMap.isValidPosition(newPosition)) {
            throw new IllegalArgumentException("Ungültige Position!");
        }

        player.setPosition(newPosition);
    }

    private Map<Direction, Biome> getSurroundingBiomes(Position position) {
        Map<Direction, Biome> result = new HashMap<>();
        List<Direction> availableDirections = worldMap.getAvailableDirections(position);

        for (Direction dir : availableDirections) {
            Position newPos = position.move(dir);
            result.put(dir, worldMap.getBiomeAt(newPos));
        }

        return result;
    }
}
