package truman.progressiveoverload.measurement;

public class Mass {
    final static double MILLIGRAMS_TO_GRAMS = (double) 1e-3;
    final static double MILLIGRAMS_TO_KILOGRAMS = (double) 1e-6;
    final static double MILLIGRAMS_TO_POUNDS = 2.20462e-6;

    private final long milligrams_;

    public Mass(long milligrams) {
        milligrams_ = milligrams;
    }

    public long toMilligrams() {
        return milligrams_;
    }

    public double toGrams() {
        return (double) milligrams_ * MILLIGRAMS_TO_GRAMS;
    }

    public static Mass fromGrams(double grams) {
        double milligrams = grams / MILLIGRAMS_TO_GRAMS;
        return new Mass((long) milligrams);
    }

    public double toKilograms() {
        return (double) milligrams_ * MILLIGRAMS_TO_KILOGRAMS;
    }

    public static Mass fromKilograms(double kilograms) {
        double milligrams = kilograms / MILLIGRAMS_TO_KILOGRAMS;
        return new Mass((long) milligrams);
    }

    public double toPounds() {
        return (double) milligrams_ * MILLIGRAMS_TO_POUNDS;
    }

    public static Mass fromPounds(double pounds) {
        double milligrams = pounds / MILLIGRAMS_TO_POUNDS;
        return new Mass((long) milligrams);
    }

    public Mass plus(Mass other) {
        long combinedMilligrams = this.milligrams_ + other.toMilligrams();
        return new Mass(combinedMilligrams);
    }

    public Mass minus(Mass other) {
        long subtractedMilligrams = this.milligrams_ - other.toMilligrams();
        return new Mass(subtractedMilligrams);
    }

}
