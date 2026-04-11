package de.heinzenburger;

public interface FightPresenter {
    void showEncounter(Animal wildAnimal);
    Animal selectAnimal(java.util.List<Animal> availableAnimals);
    StatCategory selectCategory();
    void showRoundResult(FightResult result);
    void showFightOutcome(FightOutcome outcome, Animal caughtOrLostAnimal);
}
