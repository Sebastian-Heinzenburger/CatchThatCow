package de.heinzenburger.length;

public final class Centimeter extends Length {

    public Centimeter(double value) {
        super(value * 10);
    }

    @Override
    double value() {
        return millimeter / 10;
    }

    @Override
    public String toString() {
        return value() + "cm";
    }
}
