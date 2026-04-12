package de.heinzenburger;

public class Main {
    public static void main(String[] args) {
        TextInput textInput = new SystemInScannerInput();
        TextPresenter textPresenter = new SystemOutPrintlnPresenter();

        GamePresenter gamePresenter = new TerminalGamePresenter(textPresenter, textInput);

        PlayGameUseCase playGameUseCase = new PlayGameUseCase(gamePresenter);
        playGameUseCase.start();
    }
}