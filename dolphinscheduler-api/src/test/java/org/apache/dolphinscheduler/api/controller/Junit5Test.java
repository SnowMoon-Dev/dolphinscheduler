package org.apache.dolphinscheduler.api.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.RepeatedTest.LONG_DISPLAY_NAME;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test Junt5")
@ExtendWith(RandomParametersExtension.class)
@ExtendWith(TimingExtension.class)
public class Junit5Test {

    static final Logger logger = Logger.getLogger(Junit5Test.class.getName());

    @BeforeAll
    static void beforeAllTests() {
        logger.info("Before all tests");
    }

    @AfterAll
    static void afterAllTests() {
        logger.info("After all tests");
    }

    //    @BeforeEach
//    void beforeEachTest(TestInfo testInfo) {
//        logger.info(() -> String.format("About to execute [%s]",
//                testInfo.getDisplayName()));
//    }
//    @BeforeEach
//    void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
//        int currentRepetition = repetitionInfo.getCurrentRepetition();
//        int totalRepetitions = repetitionInfo.getTotalRepetitions();
//        String methodName = testInfo.getTestMethod().get().getName();
//        logger.info(String.format("About to execute repetition %d of %d for %s", //
//                currentRepetition, totalRepetitions, methodName));
//    }


    @DisplayName("A negative value for year is not supported by the leap year computation.")
    @ParameterizedTest(name = "For example, year {0} is not supported.")
    @ValueSource(strings = {"racecar", "radar", "able was I ere I saw elba"})
    @NullSource
    @Order(1)
    void palindromes(String candidate) {
//        if (candidate == null) {
//            Assertions.assertFalse(!StringUtils.isEmpty(candidate));
//        }else {
//            Assertions.assertTrue(!StringUtils.isEmpty(candidate));
//        }
        assertAll("person",
                () -> assertNull(candidate, () -> "Assertion messages can be lazily evaluated -- "
                        + "to avoid constructing complex messages unnecessarily.")
//                () -> assertEquals(!StringUtils.isEmpty(candidate) , candidate)
        );

    }

    @Test
    @DisplayName("1 + 1 = 2")
    @Order(2)
    void addsTwoNumbers() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0,    1,   1",
            "1,    2,   3",
            "49,  51, 100",
            "1,  100, 101"
    })
    void add(int first, int second, int expectedResult) {
//        assumingThat("CI".equals(System.getenv("ENV")),
//                () -> {
//                    // perform these assertions only on the CI server
//                    assertEquals(2, calculator.divide(4, 2));
//                });
        Calculator calculator = new Calculator();
        assertEquals(expectedResult, calculator.add(first, second),
                () -> first + " + " + second + " should equal " + expectedResult);
    }


    @Disabled("Disabled until bug #42 has been resolved")
    @Test
    void testWillBeSkipped() {
    }


    @Test
    void reportSingleValue(TestReporter testReporter) {
        testReporter.publishEntry("a status message");
    }

    @Test
    void reportKeyValuePair(TestReporter testReporter) {
        testReporter.publishEntry("a key", "a value");
    }

    @Test
    void reportMultipleKeyValuePairs(TestReporter testReporter) {
        Map<String, String> values = new HashMap<>();
        values.put("user name", "dk38");
        values.put("award year", "1974");

        testReporter.publishEntry(values);
    }

    @Test
    void injectsInteger(@RandomParametersExtension.Random int i, @RandomParametersExtension.Random int j) {
        assertNotEquals(i, j);
    }


    @RepeatedTest(value = 10, name = LONG_DISPLAY_NAME)
    void injectsDouble(@RandomParametersExtension.Random double d) {
        assertEquals(0.0, d, 1.0);
    }

    @ParameterizedTest
    @EnumSource(names = { "DAYS", "HOURS" })
    void testWithEnumSourceInclude(ChronoUnit unit) {
        assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = { "ERAS", "FOREVER" })
    void testWithEnumSourceExclude(ChronoUnit unit) {
        assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
    void testWithEnumSourceRegex(ChronoUnit unit) {
        assertTrue(unit.name().endsWith("DAYS"));
    }


    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithExplicitLocalMethodSource(String argument) {
        assertNotNull(argument);
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana");
    }

    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument) {
        assertNotEquals(9, argument);
    }

    @Test
    void testAssertJ() {
        assertThat("Frodo").isEqualTo("Frodo");
    }

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }


    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    void testWithMultiArgMethodSource(String str, int num, List<String> list) {
        assertEquals(5, str.length());
        assertTrue(num >=1 && num <=2);
        assertEquals(2, list.size());
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("apple", 1, Arrays.asList("a", "b")),
                arguments("lemon", 2, Arrays.asList("x", "y"))
        );
    }
}
