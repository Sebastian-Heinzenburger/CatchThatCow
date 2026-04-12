package de.heinzenburger.speed;

public abstract class Speed implements Comparable<Speed> {
    protected double meterPerSecond;

    public Speed(double meterPerSecond) {
        this.meterPerSecond = meterPerSecond;
    }

    public abstract double value();

    @Override
    public int compareTo(Speed o) {
        return Double.compare(meterPerSecond, o.meterPerSecond);
    }
}
