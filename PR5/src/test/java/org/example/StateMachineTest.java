package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateMachineTest {

    // ---------- Завдання 2: функціональні кейси ----------

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "abcTESTabc, FINISH",   // слово посередині
            "TEST,       FINISH",   // тільки слово
            "TESTabc,    FINISH",   // слово на початку
            "abcTEST,    FINISH",   // слово в кінці
            "TESTTEST,   FINISH",   // слово двічі
            "abcTES,     THIRD",    // приклад з умови
            "abcTE,      SECOND",
            "abcT,       FIRST",
            "abc,        START",    // слова немає
            "TEXT,       START",    // схоже, але ні
            "tEST,       START",    // регістр важливий
            "TES T,      START",    // пробіл розриває слово
            "'',         START"     // порожній рядок
    })
    void shouldReturnExpectedFinalState(String input, State expected) {
        assertEquals(expected, new StateMachine().processString(input));
    }

    @ParameterizedTest(name = "{0} + ''{1}'' -> {2}")
    @CsvSource({
            "START,  T, FIRST",
            "START,  x, START",
            "FIRST,  E, SECOND",
            "FIRST,  x, START",
            "SECOND, S, THIRD",
            "SECOND, x, START",
            "THIRD,  T, FINISH",
            "THIRD,  x, START",
            "FINISH, x, FINISH",    // FINISH — поглинаючий стан
            "FINISH, T, FINISH"
    })
    void shouldFollowTransitionTable(State from, char symbol, State expected) {
        assertEquals(expected, StateMachine.next(from, symbol));
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
