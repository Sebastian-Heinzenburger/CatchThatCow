package de.heinzenburger;

public class TerminalPresenter implements UiAnzeiger {

    TextPresenter textPresenter;

    public TerminalPresenter(TextPresenter textPresenter) {
        this.textPresenter = textPresenter;
    }

    @Override
    public void printsomething(String something) {
        textPresenter.print(something);
    }
}
