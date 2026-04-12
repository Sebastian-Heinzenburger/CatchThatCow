package de.heinzenburger.speed;

public final class MeterPerSecond extends Speed {
    public MeterPerSecond(double meterPerSecond) {
        super(meterPerSecond);
    }

    @Override
    public double value() {
        return meterPerSecond;
    }
}
