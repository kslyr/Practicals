package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Calculator}: normal operations, division by zero
 * and invalid input data that must raise {@link InvalidInputException}.
 */
class CalculatorTest {

    private static final double DELTA = 1e-9;

    private final Calculator calculator = new Calculator();

    // ---------------------------------------------------------------
    // Normal cases
    // ---------------------------------------------------------------

    @Test
    @DisplayName("add(2.5, 3.5) = 6.0")
    void addTwoNumbers() throws InvalidInputException {
        assertEquals(6.0, calculator.add(2.5, 3.5), DELTA);
    }

    @Test
    @DisplayName("subtract(10.0, 4.25) = 5.75")
    void subtractTwoNumbers() throws InvalidInputException {
        assertEquals(5.75, calculator.subtract(10.0, 4.25), DELTA);
    }

    @Test
    @DisplayName("multiply(3.0, 1.5) = 4.5")
    void multiplyTwoNumbers() throws InvalidInputException {
        assertEquals(4.5, calculator.multiply(3.0, 1.5), DELTA);
    }

    @Test
    @DisplayName("divide(10.0, 4.0) = 2.5")
    void divideNormalValues() throws InvalidInputException {
        assertEquals(2.5, calculator.divide(10.0, 4.0), DELTA);
    }

    @Test
    @DisplayName("sqrt(16.0) = 4.0")
    void squareRootOfPositiveNumber() throws InvalidInputException {
        assertEquals(4.0, calculator.sqrt(16.0), DELTA);
    }

    @Test
    @DisplayName("sqrt(0.0) = 0.0 - boundary value is allowed")
    void squareRootOfZero() throws InvalidInputException {
        assertEquals(0.0, calculator.sqrt(0.0), DELTA);
    }

    // ---------------------------------------------------------------
    // Division by zero -> ArithmeticException (wrapped with its cause)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("divide(10.0, 0.0) reports division by zero")
    void divideByZero() {
        InvalidInputException exception =
                assertThrows(InvalidInputException.class, () -> calculator.divide(10.0, 0.0));

        // the original ArithmeticException must be kept as the cause
        assertInstanceOf(ArithmeticException.class, exception.getCause());
        assertTrue(exception.getCause().getMessage().contains("Division by zero"));
        assertTrue(exception.getMessage().contains("division by zero"));
    }

    @Test
    @DisplayName("divide(-7.0, 0.0) - zero divisor is rejected for negative dividend too")
    void divideNegativeByZero() {
        InvalidInputException exception =
                assertThrows(InvalidInputException.class, () -> calculator.divide(-7.0, 0.0));
        assertInstanceOf(ArithmeticException.class, exception.getCause());
    }

    // ---------------------------------------------------------------
    // Invalid input data -> own InvalidInputException
    // ---------------------------------------------------------------

    @Test
    @DisplayName("divide with a zero divisor raises InvalidInputException (own exception)")
    void divideByZeroRaisesOwnException() {
        assertThrows(InvalidInputException.class, () -> calculator.divide(1.0, 0.0));
    }

    @Test
    @DisplayName("divide(1.0, NaN) raises InvalidInputException")
    void divideWithNaN() {
        InvalidInputException exception =
                assertThrows(InvalidInputException.class, () -> calculator.divide(1.0, Double.NaN));
        assertTrue(exception.getMessage().contains("finite number"));
    }

    @Test
    @DisplayName("divide(1.0, Infinity) raises InvalidInputException")
    void divideWithInfinity() {
        assertThrows(InvalidInputException.class,
                () -> calculator.divide(1.0, Double.POSITIVE_INFINITY));
    }

    @Test
    @DisplayName("add(NaN, 1.0) raises InvalidInputException")
    void addWithNaN() {
        InvalidInputException exception =
                assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 1.0));
        assertTrue(exception.getMessage().contains("a must be a finite number"));
    }

    @Test
    @DisplayName("multiply(Infinity, 2.0) raises InvalidInputException")
    void multiplyWithInfinity() {
        assertThrows(InvalidInputException.class,
                () -> calculator.multiply(Double.NEGATIVE_INFINITY, 2.0));
    }

    @Test
    @DisplayName("subtract(5.0, NaN) raises InvalidInputException")
    void subtractWithNaN() {
        assertThrows(InvalidInputException.class,
                () -> calculator.subtract(5.0, Double.NaN));
    }

    // ---------------------------------------------------------------
    // Square root validation -> own InvalidInputException
    // ---------------------------------------------------------------

    @Test
    @DisplayName("sqrt(-4.0) raises InvalidInputException with a clear message")
    void squareRootOfNegativeNumber() {
        InvalidInputException exception =
                assertThrows(InvalidInputException.class, () -> calculator.sqrt(-4.0));

        assertTrue(exception.getMessage().contains("negative"));
        assertTrue(exception.getMessage().contains("-4.0"));
    }

    @Test
    @DisplayName("sqrt(-0.000001) raises InvalidInputException (boundary just below zero)")
    void squareRootJustBelowZero() {
        assertThrows(InvalidInputException.class, () -> calculator.sqrt(-0.000001));
    }

    @Test
    @DisplayName("sqrt(NaN) raises InvalidInputException")
    void squareRootOfNaN() {
        assertThrows(InvalidInputException.class, () -> calculator.sqrt(Double.NaN));
    }

    // ---------------------------------------------------------------
    // The exception object itself
    // ---------------------------------------------------------------

    @Test
    @DisplayName("InvalidInputException keeps the message")
    void exceptionKeepsMessage() {
        InvalidInputException exception = new InvalidInputException("bad input");
        assertEquals("bad input", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    @DisplayName("InvalidInputException keeps the original cause")
    void exceptionKeepsCause() {
        ArithmeticException cause = new ArithmeticException("Division by zero");
        InvalidInputException exception = new InvalidInputException("wrapped", cause);

        assertSameCause(cause, exception);
    }

    private void assertSameCause(Throwable cause, InvalidInputException exception) {
        assertEquals(cause, exception.getCause());
    }
}
