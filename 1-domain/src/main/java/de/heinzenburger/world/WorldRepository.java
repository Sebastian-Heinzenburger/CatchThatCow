package de.heinzenburger.world;

import java.util.Optional;

public interface WorldRepository {
    void save(World world);
    Optional<World> load();
}
