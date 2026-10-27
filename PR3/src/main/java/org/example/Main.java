package org.example;

import java.util.Scanner;
import lombok.Cleanup;

public class Main {

    public static void main(String[] args) {
        @Cleanup Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        double a = 0;
        double b = 0;
        String operation = "";

        try {
            System.out.print("Enter the first number: ");
            a = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter the second number: ");
            b = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter the operation (+, -, *, / or sqrt): ");
            operation = scanner.nextLine().trim();

            double result;
            switch (operation) {
                case "+":
                    result = calculator.add(a, b);
                    break;
                case "-":
                    result = calculator.subtract(a, b);
                    break;
                case "*":
                    result = calculator.multiply(a, b);
                    break;
                case "/":
                    result = calculator.divide(a, b);
                    break;
                case "sqrt":
                    result = calculator.sqrt(a);
                    break;
                default:
                    throw new InvalidInputException(
                            "unknown operation \"" + operation + "\". Use +, -, *, / or sqrt");
            }

            if (operation.equals("sqrt")) {
                System.out.println("sqrt(" + a + ") = " + result);
            } else {
                System.out.println(a + " " + operation + " " + b + " = " + result);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + e.getMessage() + "\" is not a valid number.");
        } catch (ArithmeticException e) {
            // Division by zero raised by the division method.
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            // Invalid arguments (negative square root, unknown operation, non-finite values,
            // division by zero wrapped with its ArithmeticException cause).
            if (e.getCause() instanceof ArithmeticException) {
                System.out.println("Error: division by zero - " + e.getMessage());
            } else {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            System.out.println("The request has been processed.");
        }
    }
}
