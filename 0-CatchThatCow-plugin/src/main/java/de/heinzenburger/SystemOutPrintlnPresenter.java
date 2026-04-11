package de.heinzenburger;

public class SystemOutPrintlnPresenter implements TextPresenter {

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}