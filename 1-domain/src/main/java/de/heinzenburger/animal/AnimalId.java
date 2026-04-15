package de.heinzenburger.animal;

import de.heinzenburger.shared.EntityId;

import java.util.UUID;

public final class AnimalId extends EntityId {
    public AnimalId() {
        super();
    }

    public AnimalId(UUID value) {
        super(value);
    }
}
