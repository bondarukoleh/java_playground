## JUnit

### Annotations and Lifecycle Methods
JUnit 5 provides several annotations to define and manage the lifecycle of your tests. 

`@Test`, `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`, `@DisplayName`, `@Disabled`, `@Tag` is used to filter,
`@Nested` is used to create nested test classes. `@Timeout` if tests exceeds the time specified in the tag - it fails.

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

### Extensions in JUnit 5
Extensions can be used to extend the functionality of JUnit in various ways, such as modifying test lifecycle events,
providing additional annotations, or integrating with external systems.

Writing an Extension \
To create a custom extension, you need to implement one or more interfaces from the _org.junit.jupiter.api.extension_
package. \
You can plug in extension with `@ExtendWith` or `@RegisterExtension` annotation.

`@ExtendWith`:
- @ExtendWith is straightforward and easy to use when you want to apply an extension to an entire test class
- It applies the extension to all test methods within a class if you want this
- It offers a clear, declarative way to specify extensions, making the test class easier to read.

`@RegisterExtension`:
- Allows for detailed configuration and initialization of the extension instance, suitable for more complex scenarios.
- Can be used as either a static or non-static field, providing flexibility in how the extension is applied and whether
it should be shared across all tests or instantiated a new for each test.
- Ideal for extensions that need to maintain state or require dynamic configuration.

Common Types of Extensions:
- Lifecycle Callbacks: Custom logic to be executed before and after tests or test classes. _BeforeAllCallback_, 
_AfterAllCallback_, _BeforeEachCallback_, _AfterEachCallback_ interfaces.
- Test Instance Post-Processing: Modifying test instances after they are created. _TestInstancePostProcessor_ interface.
- Conditional Test Execution: Custom conditions to enable or disable tests. _ExecutionCondition_ interface.
- Parameter Resolution: Supplying custom parameters to test methods. _ParameterResolver_ interface.
- Exception Handling: Custom logic to handle exceptions thrown during tests. _TestExecutionExceptionHandler_ interface.

Parameter Resolver
```java
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class StringParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == String.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return "Injected String";
    }
}

// ...
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(StringParameterResolver.class)
public class ParameterResolverTest {

    @Test
    void testWithInjectedParameter(String injectedString) {
        assertEquals("Injected String", injectedString);
    }
}
```

### Dynamic Tests
Dynamic tests in JUnit 5 provide a way to define and execute tests at runtime. Unlike static tests that are defined at
compile time using the @Test annotation, dynamic tests are generated programmatically and can be useful for scenarios
where the test cases depend on external data or complex logic.

- DynamicTest: Represents a single dynamic test. It can be created using the _DynamicTest.dynamicTest_ factory method.
- `@TestFactory`: Marks a method as a factory for dynamic tests. The method must return a Stream, Collection, Iterable,
or Iterator of DynamicTest instances.
- Dynamic Container: Represents a group of dynamic tests and containers, allowing for nested test structures.

```java
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DynamicTestsExample {

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStream() {
        return Stream.of("A", "B", "C")
                .map(str -> dynamicTest("Test: " + str,
                        () -> assertTrue(str.matches("[A-Z]"))));
    }

    @TestFactory
    Stream<DynamicContainer> dynamicTestContainers() {
        return Stream.of("A", "B", "C")
                .map(str -> dynamicContainer("Container: " + str, Stream.of(
                        dynamicTest("Test 1 for " + str, () -> assertTrue(str.matches("[A-Z]"))),
                        dynamicTest("Test 2 for " + str, () -> assertTrue(str.length() == 1))
                )));
    }
}
```

Use Cases for Dynamic Tests
- Data-Driven Testing: Generate tests based on external data sources such as databases, files, or APIs.
- Complex Test Scenarios: Create tests for scenarios that cannot be determined at compile time.
- Parameterized Testing: When @ParameterizedTest is insufficient or less flexible.

### Integration with maven
Yes, in a Maven project, JUnit tests are typically executed using the _maven-surefire-plugin_
```xml
<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
                <configuration>
                    <includes>
                        <include>**/*Tests.java</include>
                        <include>**/*Test.java</include>
                    </includes>
                    <!-- after Java 9 module-path is default instead of class-path. This turns it off-->
                    <useModulePath>false</useModulePath>
                </configuration>
            </plugin>
```

### Reporting
You can add JUnit report, generated by surefire or failsafe plugins to _target/-reports_, to Jenkins, GitLab CI, and
GitHub Actions. For code coverage - Use JaCoCo.

You can generate the html report with:
```shell
 mvn surefire-report:report;
```

### Maven Surefire Plugin
The maven-surefire-plugin is a Maven plugin that is used to run unit tests and generate reports during the build lifecycle.
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
<!--    scope fo testing-->
    <scope>test</scope>
    <configuration>
<!--        By default, Surefire will run tests in classes that match **/Test*.java -->
        <includes>
            <include>**/*Tests.java</include>
            <include>**/*Test.java</include>
        </includes>
        <excludes>
            <exclude>**/IntegrationTest.java</exclude>
        </excludes>
        
<!--        Pass system properties to the test JVMs.-->
<!--        JVM specific, configuration of java programs, you can manage them via System.getProperty, or $>java -DpropertyName=value-->
        <systemPropertyVariables>
            <property1>value1</property1>
            <property2>value2</property2>
        </systemPropertyVariables>
        
<!--        Set environment variables for the test JVM.-->
<!--        OS specific, via System.getenv or $>export ENV_VAR=value-->
        <environmentVariables>
            <ENV_VAR>value</ENV_VAR>
        </environmentVariables>
        
<!--        Run tests in parallel to speed up the test execution.-->
        <parallel>methods</parallel>
        <threadCount>4</threadCount>

<!--        Forking isolates ensuring that memory usage and state changes do not affect other tests.-->
<!--        Environment variables, system properties, and classpath configurations are isolated per JVM-->
<!--        This improves stability and helps with debugging -->
        <forkCount>2</forkCount> <!-- Fork Count: Number of JVMs to fork. Can be a number or a ratio of available CPUs. -->
        <reuseForks>true</reuseForks> <!-- Reuse Forks: Whether to reuse forks between test classes. -->
        
        <failIfNoTests>true</failIfNoTests> <!--       Fail the build if no tests are found. -->
        <testFailureIgnore>false</testFailureIgnore>  <!--      Ignore test failures and continue the build -->
        
<!--        Customize the format and location of test reports.-->
        <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
        <reportFormat>brief</reportFormat> <!-- Can be brief, plain, or xml -->
        <useFile>true</useFile> <!-- Write reports to a file -->
        
<!--        Attach listeners for test events.-->
<!--        logging, communication with external systems, debugging, profiling, you name it-->
        <properties>
            <property>
                <name>listener</name>
                <value>com.example.MyTestListener</value>
            </property>
        </properties>
        
<!--        Re-run failing tests a specified number of times before considering them as failures.-->
        <rerunFailingTestsCount>2</rerunFailingTestsCount>
        <runOrder>alphabetical</runOrder>
    </configuration>
</plugin>
```
