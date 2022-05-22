package org.apache.dolphinscheduler.api.controller.test;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.util.Named;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;


public class ParameterTest {

    private static final Logger logger = LoggerFactory.getLogger(ParameterTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"racecar", "radar", "able was I ere I saw elba"})
    void palindromes(String candidate) {
        assertTrue(StringUtils.isNotEmpty(candidate));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testWithValueSource(int argument) {
        assertTrue(argument > 0 && argument < 4);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullEmptyAndBlankStrings(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    /**
     * Both variants of the nullEmptyAndBlankStrings(String) parameterized test method result in six invocations: 1
     * for null, 1 for the empty string, and 4 for the explicit blank strings supplied via @ValueSource.
     *
     * @param text
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullEmptyAndBlankStrings2(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }


    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithEnumSource(TemporalUnit unit) {
        assertNotNull(unit);
    }

    /**
     * 注释提供了一个可选names属性，可让您指定应使用哪些常量，如下例所示。如果省略，将使用所有常量。
     *
     * @param unit
     */
    @ParameterizedTest
    @EnumSource
    void testWithEnumSourceWithAutoDetection(ChronoUnit unit) {
        assertNotNull(unit);
    }

    @ParameterizedTest
    @EnumSource(names = {"DAYS", "HOURS"})
    void testWithEnumSourceInclude(ChronoUnit unit) {
        assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"ERAS", "FOREVER"})
    void testWithEnumSourceExclude(ChronoUnit unit) {
        assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
    }


    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithExplicitLocalMethodSource(String argument) {
        logger.info(argument);
        assertNotNull(argument);
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana");
    }

    /**
     * 如果您没有通过 显式提供工厂方法名称@MethodSource，JUnit Jupiter 将按照约定搜索与当前方法同名 的工厂@ParameterizedTest方法
     *
     * @param argument
     */
    @ParameterizedTest
    @MethodSource
    void testWithDefaultLocalMethodSource(String argument) {
        assertNotNull(argument);
    }

    static Stream<String> testWithDefaultLocalMethodSource() {
        return Stream.of("apple", "banana");
    }


    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument) {
        assertNotEquals(9, argument);
    }

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    void testWithMultiArgMethodSource(String str, int num, List<String> list) {
        assertEquals(5, str.length());
        assertTrue(num >= 1 && num <= 2);
        assertEquals(2, list.size());
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("apple", 1, Arrays.asList("a", "b")),
                arguments("lemon", 2, Arrays.asList("x", "y"))
        );
    }

    /**
     * @param fruit
     * @param rank
     * @CsvSource({ "apple, banana" })
     * <p>
     * "apple","banana"
     * @CsvSource({ "apple, 'lemon, lime'" })
     * <p>
     * "apple","lemon, lime"
     * @CsvSource({ "apple, ''" })
     * <p>
     * "apple",""
     * @CsvSource({ "apple, " })
     * <p>
     * "apple",null
     * @CsvSource(value = { "apple, banana, NIL" }, nullValues = "NIL")
     * <p>
     * "apple", "banana",null
     * @CsvSource(value = { " apple , banana" }, ignoreLeadingAndTrailingWhitespace = false)
     * <p>
     * " apple "," banana"
     */

    @ParameterizedTest
    @CsvSource({
            "apple,         1",
            "banana,        2",
            "'lemon, lime', 0xF1",
            "strawberry,    700000"
    })
    void testWithCsvSource(String fruit, int rank) {
        logger.info(fruit + "  " + rank);
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }


    /**
     * 可以通过提供其完全限定的方法名称来引用外部static 工厂方法，
     */
//    @ParameterizedTest
//    @MethodSource("example.StringsProviders#tinyStrings")
//    void testWithExternalMethodSource(String tinyString) {
//        // test with tiny string
//    }
//
//    class StringsProviders {
//
//        static Stream<String> tinyStrings() {
//            return Stream.of(".", "oo", "OOO");
//        }
//    }


//@ParameterizedTest
//@CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
//void testWithCsvFileSourceFromClasspath(String country, int reference) {
//    assertNotNull(country);
//    assertNotEquals(0, reference);
//}
//
//    @ParameterizedTest
//    @CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
//    void testWithCsvFileSourceFromFile(String country, int reference) {
//        assertNotNull(country);
//        assertNotEquals(0, reference);
//    }
//
//    @ParameterizedTest(name = "[{index}] {arguments}")
//    @CsvFileSource(resources = "/two-column.csv", useHeadersInDisplayName = true)
//    void testWithCsvFileSourceAndHeaders(String country, int reference) {
//        assertNotNull(country);
//        assertNotEquals(0, reference);
//    }
    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        assertNotNull(argument);
    }

    @ParameterizedTest
    @ValueSource(strings = "SECONDS")
    void testWithImplicitArgumentConversion(ChronoUnit argument) {
        assertNotNull(argument.name());
    }

    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithExplicitArgumentConversion(
            @ConvertWith(ToStringArgumentConverter.class) String argument) {

        assertNotNull(ChronoUnit.valueOf(argument));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01.01.2017", "31.12.2017"})
    void testWithExplicitJavaTimeConverter(
            @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {

        assertEquals(2017, argument.getYear());
    }

    /**
     * 默认情况下，提供给方法的每个参数@ParameterizedTest对应于单个方法参数。因此，预期会提供大量参数的参数源可能会导致较大的方法签名。
     */
//    @ParameterizedTest
//    @CsvSource({
//            "Jane, Doe, F, 1990-05-20",
//            "John, Doe, M, 1990-10-22"
//    })
//    void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
//        Person person = new Person(arguments.getString(0),
//                arguments.getString(1),
//                arguments.get(2, Gender.class),
//                arguments.get(3, LocalDate.class));
//
//        if (person.getFirstName().equals("Jane")) {
//            assertEquals(Gender.F, person.getGender());
//        }
//        else {
//            assertEquals(Gender.M, person.getGender());
//        }
//        assertEquals("Doe", person.getLastName());
//        assertEquals(1990, person.getDateOfBirth().getYear());
//    }

//@ParameterizedTest
//@CsvSource({
//        "Jane, Doe, F, 1990-05-20",
//        "John, Doe, M, 1990-10-22"
//})
//void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
//    // perform assertions against person
//}

//@ParameterizedTest
//@CsvSource({
//        "Jane, Doe, F, 1990-05-20",
//        "John, Doe, M, 1990-10-22"
//})
//void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
//    // perform assertions against person
//}

    /**
     * Display name of container ✔
     * ├─ 1 ==> the rank of 'apple' is 1 ✔
     * ├─ 2 ==> the rank of 'banana' is 2 ✔
     * └─ 3 ==> the rank of 'lemon, lime' is 3 ✔
     *
     * {displayName}
     *
     * the display name of the method
     *
     * {index}
     *
     * the current invocation index (1-based)
     *
     * {arguments}
     *
     * the complete, comma-separated arguments list
     *
     * {argumentsWithNames}
     *
     * the complete, comma-separated arguments list with parameter names
     *
     * {0}, {1}, …​
     *
     * an individual argument
     * @param fruit
     * @param rank
     */
    @DisplayName("Display name of container")
    @ParameterizedTest(name = "{index} ==> the rank of ''{0}'' is {1}")
    @CsvSource({"apple, 1", "banana, 2", "'lemon, lime', 3"})
    void testWithCustomDisplayNames(String fruit, int rank) {
    }

//    @DisplayName("A parameterized test with named arguments")
//    @ParameterizedTest(name = "{index}: {0}")
//    @MethodSource("namedArguments")
//    void testWithNamedArguments(File file) {
//    }
//
//    static Stream<Arguments> namedArguments() {
//        return Stream.of(arguments(Named.of("An important file", new File("path1"))),
//                arguments(Named.of("Another file", new File("path2"))));
//    }
}
