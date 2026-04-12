package de.heinzenburger;

import de.heinzenburger.length.Centimeter;
import de.heinzenburger.length.Length;
import de.heinzenburger.length.Meter;

public class Animal implements InventoryItem {
    String name;
    Length length;

    static Animal cow = new Animal("Kuh", new Meter(6.4));
    static Animal ant = new Animal("Ameise", new Centimeter(0.5));

    public Animal(String name) {
        this.name = name;
    }

    public Animal(String name, Length length) {
        this.name = name;
        this.length = length;
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
