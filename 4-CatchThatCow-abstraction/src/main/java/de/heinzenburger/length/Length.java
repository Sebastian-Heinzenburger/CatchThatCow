package de.heinzenburger.length;

import java.util.Objects;

public abstract class Length implements Comparable<Length> {
    protected final double millimeter;

    public Length(double millimeter) {
        this.millimeter = millimeter;
    }

    abstract double value();

    @Override
    public int compareTo(Length o) {
        return Double.compare(millimeter, o.millimeter);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Length other)) return false;
        return Double.compare(millimeter, other.millimeter) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(millimeter);
    }
}
