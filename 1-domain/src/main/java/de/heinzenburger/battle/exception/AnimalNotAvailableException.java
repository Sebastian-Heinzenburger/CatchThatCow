package de.heinzenburger.battle.exception;

import de.heinzenburger.animal.Animal;

public class AnimalNotAvailableException extends BattleException {

    public AnimalNotAvailableException() {
        super("Selected animal is not available for battle");
    }

    public AnimalNotAvailableException(Animal animal) {
        super("Animal '" + animal + "' is not available for battle");
    }

}

