package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end tests for {@link Main}: simulated user input (including invalid data,
 * e.g. strings instead of numbers) and verification that errors are reported
 * clearly and that the {@code finally} block always prints the closing message.
 */
class MainTest {

    private static final String PROCESSED = "The request has been processed.";

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    /** Runs Main.main() with the given input typed by the "user" and returns its output. */
    private String runMain(String input) {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        try {
            Main.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return captured.toString(StandardCharsets.UTF_8);
    }

    // ---------------------------------------------------------------
    // Valid input
    // ---------------------------------------------------------------

    @Test
    @DisplayName("10 + 3 -> correct result and closing message")
    void validAddition() {
        String output = runMain("10\n3\n+\n");

        assertTrue(output.contains("10.0 + 3.0 = 13.0"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    @Test
    @DisplayName("16 sqrt -> correct result")
    void validSquareRoot() {
        String output = runMain("16\n0\nsqrt\n");

        assertTrue(output.contains("sqrt(16.0) = 4.0"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    // ---------------------------------------------------------------
    // Division by zero
    // ---------------------------------------------------------------

    @Test
    @DisplayName("10 / 0 -> clear message about division by zero, finally still runs")
    void divisionByZero() {
        String output = runMain("10\n0\n/\n");

        assertTrue(output.toLowerCase().contains("division by zero"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    // ---------------------------------------------------------------
    // Invalid input data: strings instead of numbers
    // ---------------------------------------------------------------

    @Test
    @DisplayName("'abc' instead of the first number -> NumberFormatException is handled")
    void stringInsteadOfFirstNumber() {
        String output = runMain("abc\n3\n+\n");

        assertTrue(output.contains("Error:"), output);
        assertTrue(output.contains("is not a valid number"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    @Test
    @DisplayName("'five' instead of the second number -> error message, no crash")
    void stringInsteadOfSecondNumber() {
        String output = runMain("5\nfive\n+\n");

        assertTrue(output.contains("Error:"), output);
        assertTrue(output.contains("is not a valid number"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    @Test
    @DisplayName("Empty line instead of a number -> handled gracefully")
    void emptyInput() {
        String output = runMain("\n\n\n");

        assertTrue(output.contains("Error:"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    // ---------------------------------------------------------------
    // Cases that raise the custom InvalidInputException
    // ---------------------------------------------------------------

    @Test
    @DisplayName("sqrt(-4) -> own InvalidInputException is caught and reported")
    void negativeSquareRoot() {
        String output = runMain("-4\n0\nsqrt\n");

        assertTrue(output.contains("not defined for negative"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    @Test
    @DisplayName("unknown operation '%' -> own InvalidInputException is caught and reported")
    void unknownOperation() {
        String output = runMain("5\n5\n%\n");

        assertTrue(output.contains("unknown operation"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    @Test
    @DisplayName("NaN as input -> own InvalidInputException is caught and reported")
    void nonFiniteInput() {
        String output = runMain("NaN\n1\n+\n");

        assertTrue(output.contains("finite number"), output);
        assertTrue(output.contains(PROCESSED), output);
    }

    // ---------------------------------------------------------------
    // The finally block
    // ---------------------------------------------------------------

    @Test
    @DisplayName("finally prints the closing message on success and on every error")
    void finallyAlwaysPrintsProcessedMessage() {
        assertTrue(runMain("1\n1\n+\n").contains(PROCESSED));
        assertTrue(runMain("x\nx\nx\n").contains(PROCESSED));     // bad numbers
        assertTrue(runMain("1\n0\n/\n").contains(PROCESSED));     // division by zero
        assertTrue(runMain("-1\n0\nsqrt\n").contains(PROCESSED)); // custom exception
    }
}
