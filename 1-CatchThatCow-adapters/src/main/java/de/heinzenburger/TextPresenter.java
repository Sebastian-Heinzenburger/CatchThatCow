package de.heinzenburger;

import java.util.List;

public interface TextPresenter {
    void print(String text);
    void printNumberedList(String title, List<String> items);
}
