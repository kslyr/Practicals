package org.example;

import static org.example.State.FINISH;
import static org.example.State.FIRST;
import static org.example.State.SECOND;
import static org.example.State.START;
import static org.example.State.THIRD;

/**
 * Скінчений автомат, що шукає у рядку підрядок "TEST".
 * FINISH означає, що "TEST" знайдено.
 */
public class StateMachine {
    private State state;

    public StateMachine() {
        this.state = START;
    }

    /**
     * Проходить рядок символ за символом і повертає кінцевий стан.
     * Щоразу починає з START, тому один екземпляр можна використовувати багаторазово.
     */
    public State processString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Вхідний рядок не може бути null");
        }

        state = START;
        for (int i = 0; i < input.length(); i++) {
            switchState(input.charAt(i));
            if (state == FINISH) {
                break; // FINISH — кінцевий стан, далі дивитись немає сенсу
            }
        }
        return state;
    }

    /** Приймає один символ і змінює поточний стан автомата. */
    public void switchState(char c) {
        state = next(state, c);
    }

    public State getState() {
        return state;
    }

    /** Таблиця переходів (package-private, щоб її можна було тестувати напряму). */
    static State next(State current, char c) {
        return switch (current) {
            case START -> restart(c);
            case FIRST -> c == 'E' ? SECOND : restart(c);
            case SECOND -> c == 'S' ? THIRD : restart(c);
            case THIRD -> c == 'T' ? FINISH : restart(c);
            case FINISH -> FINISH;
        };
    }

    /**
     * Невдалий символ: якщо це 'T' — він може бути початком нового "TEST",
     * тому йдемо в FIRST, інакше повертаємось у START.
     */
    private static State restart(char c) {
        return c == 'T' ? FIRST : START;
    }
}
