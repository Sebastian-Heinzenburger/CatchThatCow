package de.heinzenburger;

public class Animal implements InventoryItem {
    String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return name;
    }

    public String getDescription() {
        return "TODO: Hier die Stats anzeigen";
    }
}
