package de.heinzenburger.length;

public final class Meter extends Length {
    public Meter(double value) {
        super(value * 1000);
    }

    @Override
    double value() {
        return millimeter / 1000;
    }

    @Override
    public String toString() {
        return value() + "m";
    }
}
