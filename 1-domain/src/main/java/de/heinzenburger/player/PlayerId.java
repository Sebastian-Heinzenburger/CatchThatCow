package de.heinzenburger.player;

import de.heinzenburger.shared.EntityId;

import java.util.UUID;

public final class PlayerId extends EntityId {
    public PlayerId() {
        super();
    }

    public PlayerId(UUID value) {
        super(value);
    }
}
