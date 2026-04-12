package de.heinzenburger.speed;

public final class KilometerPerHour extends Speed {

    public KilometerPerHour(double meterPerSecond) {
        super(meterPerSecond * 3.6);
    }

    @Override
    public double value() {
        return meterPerSecond / 3.6;
    }

}
