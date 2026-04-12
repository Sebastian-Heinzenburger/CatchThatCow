package de.heinzenburger;

import de.heinzenburger.length.Centimeter;
import de.heinzenburger.length.Length;
import de.heinzenburger.length.Meter;
import de.heinzenburger.presenter.GamePresenter;

public class Main {
    public static void main(String[] args) {
        Length a = new Meter(8);
        Length b = new Centimeter(210);
        System.out.println(a + " vs " + b);
        System.out.println("a.compareTo(b) = " + a.compareTo(b));

        TextInput textInput = new SystemInScannerInput();
        TextPresenter textPresenter = new SystemOutPrintlnPresenter();
        Random random = new JavaRandom();

        GamePresenter gamePresenter = new TerminalGamePresenter(textPresenter, textInput);

        PlayGameUseCase playGameUseCase = new PlayGameUseCase(gamePresenter, random);
        playGameUseCase.start();
    }
}