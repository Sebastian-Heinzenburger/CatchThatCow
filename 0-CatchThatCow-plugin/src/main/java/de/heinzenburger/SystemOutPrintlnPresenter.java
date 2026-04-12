package de.heinzenburger;

import java.util.List;

public class SystemOutPrintlnPresenter implements TextPresenter {

    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public void printNumberedList(String title, List<String> items) {
        print(""); // newline
        print(title);
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            print((i + 1) + ". " + item);
        }
    }
}