package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateMachineTest {


    void shouldReturnExpectedFinalState(String input, State expected) {
        assertEquals(expected, new StateMachine().processString(input));
    }

    // Тести таблиці переходів — по одному на кожну гілку switch у StateMachine.next()

    // case START -> restart(c); case FIRST -> 'E'; case SECOND -> 'S'; case THIRD -> 'T'
    @ParameterizedTest(name = "{0} + ''{1}'' -> {2} (правильний символ)")
    @CsvSource({
            "START,  T, FIRST",
            "FIRST,  E, SECOND",
            "SECOND, S, THIRD",
            "THIRD,  T, FINISH"
    })
    void shouldAdvanceOnExpectedSymbol(State from, char symbol, State expected) {
        assertEquals(expected, StateMachine.next(from, symbol));
    }

    // restart(c): будь-який інший символ (не 'T') повертає в START
    @ParameterizedTest(name = "{0} + ''{1}'' -> START (чужий символ)")
    @CsvSource({
            "START,  x",
            "FIRST,  x",
            "FIRST,  S",   // літера зі слова, але не в тому порядку
            "SECOND, x",
            "SECOND, E",
            "THIRD,  x",
            "THIRD,  S"
    })
    void shouldRestartOnUnexpectedSymbol(State from, char symbol) {
        assertEquals(State.START, StateMachine.next(from, symbol));
    }

    // restart(c): невдалий символ 'T' — це початок нового слова, тому FIRST
    @ParameterizedTest(name = "{0} + ''T'' -> FIRST (T як новий початок)")
    @CsvSource({
            "FIRST",    // "TT"
            "SECOND"    // "TET"
    })
    void shouldStartOverOnTInsteadOfRestart(State from) {
        assertEquals(State.FIRST, StateMachine.next(from, 'T'));
    }

    // case FINISH -> FINISH: кінцевий стан не змінюється від жодного символу
    @ParameterizedTest(name = "FINISH + ''{0}'' -> FINISH")
    @ValueSource(chars = {'T', 'E', 'S', 'x', ' '})
    void shouldStayInFinish(char symbol) {
        assertEquals(State.FINISH, StateMachine.next(State.FINISH, symbol));
    }

    @Test
    void shouldBeReusableBetweenCalls() {
        StateMachine machine = new StateMachine();
        assertEquals(State.FINISH, machine.processString("TEST"));
        assertEquals(State.START, machine.processString("abc")); // стан не "залипає" у FINISH
    }

    @Test
    void shouldThrowOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new StateMachine().processString(null));
    }

    // ---------- Завдання 3*: рядки, на яких наївна схема помилялась ----------

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "TTEST,   FINISH",   // зайва T перед словом
            "TTTEST,  FINISH",   // кілька зайвих T
            "TETEST,  FINISH",   // "TE" + одразу нове "TEST"
            "TESTEST, FINISH",
            "TTE,     SECOND",   // після зайвої T автомат має рахувати "TE"
            "TET,     FIRST",    // T після "TE" — початок нового слова
            "TESTT,   FINISH"
    })
    void shouldFindWordAfterFalseStart(String input, State expected) {
        assertEquals(expected, new StateMachine().processString(input));
    }
}
