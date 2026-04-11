package de.heinzenburger;

import java.util.Map;

public interface NavigationPresenter {
    void showCurrentBiome(Biome biome);
    void showNavigationMenu(Map<Direction, Biome> surroundingBiomes);
    Direction getUserDirection();
    boolean wantsToStay();
}
