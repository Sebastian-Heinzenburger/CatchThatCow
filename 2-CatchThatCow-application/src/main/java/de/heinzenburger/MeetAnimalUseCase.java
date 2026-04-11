package de.heinzenburger;

public class MeetAnimalUseCase {
    UiAnzeiger uiAnzeiger;

    public MeetAnimalUseCase(UiAnzeiger uiAnzeiger) {
        this.uiAnzeiger = uiAnzeiger;
    }

    public void sayHi() {
        Animal animal = new Animal();
        uiAnzeiger.printsomething(animal.getNoise());
    }
}
