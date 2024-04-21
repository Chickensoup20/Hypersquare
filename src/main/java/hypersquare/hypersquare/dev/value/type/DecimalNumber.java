package hypersquare.hypersquare.dev.value.type;

public class DecimalNumber {
    public static final long SCALE = 1000000L;
    private final long value;

    public DecimalNumber(long whole, long decimalPart) {
        long temp = 0L;
        try {
            temp = Math.addExact(Math.multiplyExact(whole, SCALE), decimalPart);
        } catch (ArithmeticException _) {
        }
        this.value = temp;
    }

    public DecimalNumber(long whole) {
        this(whole, 0);
    }

    public DecimalNumber(double value) {
        this((long) value, (long) (value * SCALE) % SCALE);
    }

    public DecimalNumber(float value) {
        this((long) value, (long) (value * SCALE) % SCALE);
    }

    public DecimalNumber add(DecimalNumber other) {
        try {
            return new DecimalNumber(0, Math.addExact(this.value, other.value));
        } catch (ArithmeticException ignored) {
            return new DecimalNumber(0, 0);
        }
    }

    public DecimalNumber subtract(DecimalNumber other) {
        try {
            return new DecimalNumber(0, Math.subtractExact(this.value, other.value));
        } catch (ArithmeticException ignored) {
            return new DecimalNumber(0, 0);
        }
    }

    public DecimalNumber multiply(DecimalNumber other) {
        try {
            return new DecimalNumber(0, Math.multiplyExact(this.value, other.value) / SCALE);
        } catch (ArithmeticException ignored) {
            return new DecimalNumber(0, 0);
        }
    }

    public DecimalNumber divide(DecimalNumber other) {
        // TODO: Error handling
        if (other.value == 0) return new DecimalNumber(0, 0);

        try {
            return new DecimalNumber(0, Math.multiplyExact(this.value, SCALE) / other.value);
        } catch (ArithmeticException ignored) {
            return new DecimalNumber(0, 0);
        }
    }

    public long toLong() {
        return value / SCALE;
    }

    public int toInt() {
        return (int) (value / SCALE);
    }

    public float toFloat() {
        return (float) value / SCALE;
    }

    public double toDouble() {
        return (double) value / SCALE;
    }

    @Override
    public String toString() {
        long wholePart = Math.abs(this.value / SCALE);
        long decimalPart = Math.abs(this.value % SCALE);
        String str = value < 0 ? "-" : "";
        str += String.format("%d.%06d", wholePart, decimalPart);
        while (str.endsWith("0")) str = str.substring(0, str.length() - 1);
        if (str.endsWith(".")) str = str.substring(0, str.length() - 1);
        return str;
    }

    public Long rawData() {
        return value;
    }
}
