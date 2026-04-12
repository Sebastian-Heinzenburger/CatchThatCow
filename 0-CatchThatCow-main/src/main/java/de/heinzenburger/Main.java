package de.heinzenburger;

import de.heinzenburger.presenter.GamePresenter;

public class Main {
    public static void main(String[] args) {
        TextInput textInput = new SystemInScannerInput();
        TextPresenter textPresenter = new SystemOutPrintlnPresenter();
        Random random = new JavaRandom();

        GamePresenter gamePresenter = new TerminalGamePresenter(textPresenter, textInput);

        PlayGameUseCase playGameUseCase = new PlayGameUseCase(gamePresenter, random);
        playGameUseCase.start();
    }
}