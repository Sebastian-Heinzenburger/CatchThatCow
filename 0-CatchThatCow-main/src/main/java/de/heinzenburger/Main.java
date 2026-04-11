package de.heinzenburger;

public class Main {
    public static void main(String[] args) {
        MeetAnimalUseCase meetAnimalUseCase = new MeetAnimalUseCase(new TerminalPresenter(new SystemOutPrintlnPresenter()));
        meetAnimalUseCase.sayHi();
    }
}