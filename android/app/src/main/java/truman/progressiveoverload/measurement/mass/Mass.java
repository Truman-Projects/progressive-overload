package truman.progressiveoverload.measurement.mass;

import truman.progressiveoverload.measurement.MagnitudeOutOfRangeException;

public class Mass {

    private final long milligrams_;

    private Mass(long milligrams) {
        milligrams_ = milligrams;
    }

    public long toMilligrams() {
        return milligrams_;
    }

    public static Mass fromMilligrams(long milligrams) {
        return new Mass(milligrams);
    }

    public double toGrams() {
        return (double) milligrams_ * MILLIGRAMS_TO_GRAMS;
    }

    public static Mass fromGrams(double grams) throws MagnitudeOutOfRangeException {
        if (grams < Mass.MIN_VALUE_GRAMS || grams > Mass.MAX_VALUE_GRAMS) {
            throw new MagnitudeOutOfRangeException("Attempting to instantiate Mass with too many grams");
        }
        double milligrams = grams / MILLIGRAMS_TO_GRAMS;
        return Mass.fromMilligrams((long) milligrams);
    }

    public double toKilograms() {
        return (double) milligrams_ * MILLIGRAMS_TO_KILOGRAMS;
    }

    public static Mass fromKilograms(double kilograms) throws MagnitudeOutOfRangeException {
        if (kilograms < Mass.MIN_VALUE_KILOGRAMS || kilograms > Mass.MAX_VALUE_KILOGRAMS) {
            throw new MagnitudeOutOfRangeException("Attempting to instantiate Mass with too many kilograms");
        }
        double milligrams = kilograms / MILLIGRAMS_TO_KILOGRAMS;
        return Mass.fromMilligrams((long) milligrams);
    }

    public double toPounds() {
        return (double) milligrams_ * MILLIGRAMS_TO_POUNDS;
    }

    public static Mass fromPounds(double pounds) throws MagnitudeOutOfRangeException {
        if (pounds < Mass.MIN_VALUE_POUNDS || pounds > Mass.MAX_VALUE_POUNDS) {
            throw new MagnitudeOutOfRangeException("Attempting to instantiate Mass with too many pounds");
        }
        double milligrams = pounds / MILLIGRAMS_TO_POUNDS;
        return Mass.fromMilligrams((long) milligrams);
    }

    public Mass plus(Mass other) throws MagnitudeOutOfRangeException {
        if (this.milligrams_ > 0) {
            long maxMilligramsForOtherBeforeOverflow = Mass.MAX_VALUE_MILLIGRAMS - this.milligrams_;
            if (other.toMilligrams() > maxMilligramsForOtherBeforeOverflow) {
                throw new MagnitudeOutOfRangeException("Result of adding masses is too big");
            }
        } else {
            long minMilligramsForOtherBeforeUnderflow = Mass.MIN_VALUE_MILLIGRAMS - this.milligrams_;
            if (other.toMilligrams() < minMilligramsForOtherBeforeUnderflow) {
                throw new MagnitudeOutOfRangeException("Result of adding masses is too big (negative)");
            }
        }

        long combinedMilligrams = this.milligrams_ + other.toMilligrams();
        return Mass.fromMilligrams(combinedMilligrams);
    }

    public Mass minus(Mass other) {
        long subtractedMilligrams = this.milligrams_ - other.toMilligrams();
        return Mass.fromMilligrams(subtractedMilligrams);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Mass)) {
            return false;
        }
        return (this.milligrams_ == ((Mass) other).toMilligrams());
    }

    private static final double MILLIGRAMS_TO_GRAMS = (double) 1e-3;
    private static final double MILLIGRAMS_TO_KILOGRAMS = (double) 1e-6;
    private static final double MILLIGRAMS_TO_POUNDS = 2.20462e-6;

    public static final long MIN_VALUE_MILLIGRAMS = Long.MIN_VALUE;
    public static final double MIN_VALUE_GRAMS = (double) MIN_VALUE_MILLIGRAMS * MILLIGRAMS_TO_GRAMS;
    public static final double MIN_VALUE_KILOGRAMS = (double) MIN_VALUE_MILLIGRAMS * MILLIGRAMS_TO_KILOGRAMS;
    public static final double MIN_VALUE_POUNDS = (double) MIN_VALUE_MILLIGRAMS * MILLIGRAMS_TO_POUNDS;

    public static final long MAX_VALUE_MILLIGRAMS = Long.MAX_VALUE;
    public static final double MAX_VALUE_GRAMS = (double) MAX_VALUE_MILLIGRAMS * MILLIGRAMS_TO_GRAMS;
    public static final double MAX_VALUE_KILOGRAMS = (double) MAX_VALUE_MILLIGRAMS * MILLIGRAMS_TO_KILOGRAMS;
    public static final double MAX_VALUE_POUNDS = (double) MAX_VALUE_MILLIGRAMS * MILLIGRAMS_TO_POUNDS;

}
