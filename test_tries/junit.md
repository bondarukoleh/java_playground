## JUnit

### Annotations and Lifecycle Methods
JUnit 5 provides several annotations to define and manage the lifecycle of your tests. 

`@Test`, `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`, `@DisplayName`, `@Disabled`, `@Tag` is used to filter,
`@Nested` is used to create nested test classes. 

### Assertions
```java
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);
assertTrue(condition); //  assertTrue(3 < 5);
assertFalse(condition);
assertNull(object);
assertNotNull(object);
assertSame(expected, actual): // Verifies that two references point to the same object.
assertNotSame();
assertThrows(expectedType, executable); //Verifies that the executable throws the expected exception.
assertThrows(ArithmeticException.class, () -> {int res = 1 / 0;});
assertAll(executables) // Allows grouping multiple assertions into a single test.
assertAll("grouped assertions",
  () -> assertEquals(2, 1 + 1),
  () -> assertTrue("JUnit".startsWith("J"))
);
assertArrayEquals(expectedArray, actualArray); // Verifies that two arrays are equal.
```

### Assumptions
Assumptions are used to control the execution of tests based on certain conditions. If the assumption fails, the test is
aborted and marked as "skipped." This is useful for conditional test execution based on runtime conditions or external
factors.

```java
assumeTrue(condition)/assumeFalse(condition); // Aborts the test if the condition is false/true.
// test will only execute if assumption is true
assumeTrue("CI".equals(System.getenv("ENV")));

assumingThat(condition, executable); // Executes the given executable only if the condition is true, otherwise the test continues.
assumingThat("CI".equals(System.getenv("ENV")), () -> {
    // this assertion only executes if the condition is true
    assertEquals(2, 1 + 1);
});
```

#### Use Cases for Assumptions
1. Platform-Specific Tests: When certain tests should only run on specific platforms (e.g., only on Windows or Linux).
```java
 assumeTrue(System.getProperty("os.name").startsWith("Windows"));
```
2. Environment-Specific Tests: When tests should only run in certain environments (e.g., only on CI servers).
```java
assumeTrue("CI".equals(System.getenv("ENV")));
```
3. Configuration-Specific Tests: When tests depend on specific configurations or external conditions.
```java
 assumeTrue("true".equals(System.getProperty("config.enabled")));
```

### Parameterized Tests
Parameterized tests enable you to run the same test multiple times with different arguments. This is useful when you
want to test a method with various input values and expected results.

`@ParameterizedTest` annotation is used in conjunction with various `@Source` annotations to provide the parameters.
```java
@ParameterizedTest
@ValueSource(strings = {"racecar", "radar", "able was I ere I saw elba"})
void testPalindrome(String candidate) {
    assertTrue(isPalindrome(candidate));
}
```

#### Parameter Sources
`@ValueSource` Provides a simple array of literal values.
```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 4, 5})
void testWithValueSource(int argument) {
    assertTrue(argument > 0);
}
```
`@EnumSource` Provides values from an enum.
```java
enum Color { RED, GREEN, BLUE }

@ParameterizedTest
@EnumSource(Color.class)
void testWithEnumSource(Color color) {
  assertNotNull(color);
}
```
`@MethodSource`: Provides values from a static method within the test class or an external class
```java
@ParameterizedTest
@MethodSource("stringProvider")
void testWithMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> stringProvider() {
    return Stream.of("apple", "banana", "cherry");
}
```
`@CsvSource` Provides values from a CSV-formatted string.
```java
    @ParameterizedTest
    @CsvSource({
        "apple,         1",
        "banana,        2",
        "cherry,        3"
    })
    void testWithCsvSource(String fruit, int rank) {
        assertNotNull(fruit);
        assertTrue(rank > 0);
    }

    //    `@CsvFileSource`: Provides values from a CSV file.
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
```
`@ArgumentsSource`: Provides custom arguments by implementing ArgumentsProvider. You need some class that impelements
_ArgumentsProvider_. The benefit is you can get the data dynamically, let's say from DB, or from some distant source,
if you need to do some work before get this data. 
```java
    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        assertNotNull(argument);
    }

    static class MyArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of("apple", "banana", "cherry").map(Arguments::of);
        }
    }
```

### Test Execution Order
By default, JUnit 5 executes test methods in a deterministic but arbitrary order. If want to control the execution order
there is a `@TestMethodOrder` annotation. is used at the class level to specify the order in which test methods are
executed. It works in conjunction with the _MethodOrderer_ interface, which defines the ordering strategy.

Here are some commonly used MethodOrderer implementations:
- `@Order`: Specify an explicit order for each test method.
- MethodOrderer.OrderAnnotation: Orders test methods based on the @Order annotation.
- MethodOrderer.MethodName: Orders test methods alphabetically by method name.
- MethodOrderer.DisplayName: Orders test methods by the value of their @DisplayName annotations.
- MethodOrderer.Random: Orders test methods randomly.
- Custom Method Order. You can create a custom _MethodOrderer_ by implementing the _MethodOrderer_ interface.
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.MethodOrderer.DisplayName;

@TestMethodOrder(OrderAnnotation.class)
public class OrderedTest {
    @Test
    @Order(1)
    void testA(){}
    @Test
    @Order(2)
    void testB() {}
}


@TestMethodOrder(DisplayName.class)
public class DisplayNameOrderTest {

    @Test
    @DisplayName("C Test")
    void testC() {}
```

#### Nested Tests and @TestInstance
By default, JUnit creates a new test instance for each test method, which is why @BeforeAll must be static. \
If you want to use **non-static** `@BeforeAll` and `@AfterAll` methods, you can change the test instance lifecycle
using the `@TestInstance` annotation.
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NonStaticLifecycleTest {

    @BeforeAll
    void setUpAll() {
        System.out.println("BeforeAll: Set up resources once (non-static)");
    }
```

### Conditional Test Execution
JUnit 5 allows you to conditionally enable or disable tests based on various conditions. This can be useful for skipping
tests in certain environments, on specific platforms, or under particular configurations.

Basic Annotations for Conditional Test Execution:
- @EnabledOnOs/@DisabledOnOs: Enables/Disables a test only on specified operating systems.
- @EnabledOnJre/@DisabledOnJre: Enables/Disables a test only on specified Java Runtime Environments (JREs).
- @EnabledIf/@DisabledIf: Enables/Disables a test if the given condition is met.
- @EnabledIfSystemProperty/@DisabledIfSystemProperty: enable or disable tests based on system properties.
- @EnabledIfEnvironmentVariable/@DisabledIfEnvironmentVariable  enable or disable tests based on environment variables.

```java
@EnabledOnOs(OS.WINDOWS)
@DisabledOnOs({OS.LINUX, OS.MAC})
@EnabledOnJre(JRE.JAVA_8)

@EnabledIf("isFeatureEnabled")
void testIfFeatureEnabled() {}
boolean isFeatureEnabled() {
    return true;
}

@EnabledIfSystemProperty(named = "env", matches = "ci")
void testIfCiEnvironment() {
    System.out.println("This test runs if the system property 'env' is 'ci'");
}

@EnabledIfEnvironmentVariable(named = "ENV", matches = "PROD")
void testIfProdEnvironment() {
    System.out.println("This test runs if the environment variable 'ENV' is 'PROD'");
}
```