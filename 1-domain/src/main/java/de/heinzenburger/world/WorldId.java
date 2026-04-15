package de.heinzenburger.world;

import de.heinzenburger.shared.EntityId;

import java.util.UUID;

public final class WorldId extends EntityId {
    public WorldId() {
        super();
    }

    public WorldId(UUID value) {
        super(value);
    }
}
