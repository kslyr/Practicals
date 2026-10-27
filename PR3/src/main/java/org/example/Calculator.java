package org.example;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Calculator {

    /**
     * Adds two numbers.
     *
     * @throws InvalidInputException if either argument is not a finite number (NaN / infinity)
     */
    public double add(double a, double b) throws InvalidInputException {
        validateFinite(a, "a");
        validateFinite(b, "b");
        return a + b;
    }

    /**
     * Subtracts the second number from the first.
     *
     * @throws InvalidInputException if either argument is not a finite number (NaN / infinity)
     */
    public double subtract(double a, double b) throws InvalidInputException {
        validateFinite(a, "a");
        validateFinite(b, "b");
        return a - b;
    }

    /**
     * Multiplies two numbers.
     *
     * @throws InvalidInputException if either argument is not a finite number (NaN / infinity)
     */
    public double multiply(double a, double b) throws InvalidInputException {
        validateFinite(a, "a");
        validateFinite(b, "b");
        return a * b;
    }

    /**
     * Divides the first number by the second.
     *
     * <p>An attempt to divide by zero is signalled with {@link ArithmeticException} inside the
     * method and is handled here: instead of leaking the raw arithmetic error it is reported to
     * the caller as an {@link InvalidInputException} that keeps the original error as its cause.</p>
     *
     * @throws InvalidInputException if an argument is not a finite number, or if {@code b == 0}
     *                               (cause: {@link ArithmeticException})
     */
    public double divide(double a, double b) throws InvalidInputException {
        validateFinite(a, "a (dividend)");
        validateFinite(b, "b (divisor)");
        try {
            return divideChecked(a, b);
        } catch (ArithmeticException e) {
            throw new InvalidInputException("Invalid input: division by zero (b must not be 0)", e);
        }
    }

    /**
     * Returns the square root of a non-negative number.
     *
     * @throws InvalidInputException if {@code a} is negative or not a finite number
     */
    public double sqrt(double a) throws InvalidInputException {
        validateFinite(a, "a");
        if (a < 0) {
            throw new InvalidInputException(
                    "Invalid input: square root is not defined for negative numbers (a = " + a + ")");
        }
        return Math.sqrt(a);
    }

    /** Performs the division, signalling a zero divisor with {@link ArithmeticException}. */
    private double divideChecked(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    /** Rejects values that are not usable in arithmetic calculations. */
    private void validateFinite(double value, String name) throws InvalidInputException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new InvalidInputException(
                    "Invalid input: " + name + " must be a finite number, but was " + value);
        }
    }
}
