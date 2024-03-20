Unit (Module) tests - testing of minimal functionality (method/class/module). Sometimes you can test only method, 
because it's not dependent on something else. But when method, or class depend on other classes, and change their state
with some hidden private methods or fields - these bunch of classes is a minimal functionality. You cannot check 
something that depends on another thing separately, it won't give you confident about its correctness.\

JUnit 4
in org.junit.Test.
Framework for testing. All frameworks have some amount of abstractions that you can use.
JUnit has a `runner` to run the tests.
Since Java (jvm) knows how to run only main() method - and you want to run test methods somewhere else, this Runner has
his main, and JVM will run his main(), from what there will be invocations of your test methods.
So when you are running from IDE - IDE runs the JUnit, JUnit runs his (or your own, if you setup) runner, runner runs 
methods tells JUnit, JUnit tells IDE, IDE show the results. IDE somehow listen the JUnit.
So when you need to add, or use your own runner with some specific fail conditions, or maybe that will run in 10000 
threads, you can extend JUnit runner, or find something in internet and run JUnit with your own runner.

Test naming
```java
class WhatIAmTestingTest {
    testObject_testActiononObject(){}
}

class LoginPageTest {
    loginField_enterValidLoginTest(){}   
    testLoginField_enterInvalidLogin(){}
}
```

JUnit using reflection finds *Test classes and @Test annotated methods and run it, and it looks like we have a lot of 
enter points in our program. 
When you are running tests from IDE - it has it's own runner that stands between you and JUnit. IDE understands where
and what you want to run (test method(s), class, package), and runs only them + it's generate test result much more 
clear and fancy than JUnit.

On CI server should understand junit outcome, and fail or pass the build.

Tests are independent in JUnit, one fail doesn't stop the run.
For each test method JUnit creates separate instance of the Testing class;

JUnit single run
@BeforeClass (once by class, all methods should be static)
TestClass constructor()
@Before (all methods, for each @Test annotated)
@Test (all methods)
@After
@AfterClass

If you try by TDD write tests first and understand that it's too hard to write them on your functionality, it's a 
bell that you have too complicated and confusing API of your functionality.

JUnit think that test is broken if there is an unexpected exception during the run or timeout is out.
If you want to fail the test with some condition - throw AssertionError.
```java
// timeouts
@Test(timeout = 1000) // 1 second
// or default timeout
@Rule
public Timeout globalTimeout = new Timeout(3000); // 10 seconds max per test method
// Rule will override the annotation argument. Rule applies to all test cases in the test class
```

Design by contract, means we are adding assertions inside the code, and they check all main point in our program. Like 
incoming arguments, and outgoing data. When we want to check system - we turn on the flag to run with assertions and
system will check itself. If unit test tests from outside the method, design by contract asserts will check the code from
the inside, which is cool. 

Imperative style, where you strictly say what are you doing.  
Declarative - it's a describing the situations rather strict orders.
DSL - domain specific language.
 External DSL - when you are using different from your base project language to solve some of your business needs,
 e.g. in Java app use Python to compute something.
 Internal DSL - when you creating your own specific language for your needs. Not mandatory to create your own language
 and compiler, but you can some how create your own style of writing that will be different from common. 
 
JUnit Matcher - it's a predicate, function that expects argument but returns boolean.
Matcher - it's a collection of predicates that you want to run on your data, and depend on what you choose ther could
be allOf() means concatenated with && sign, or anyOf() via ||, either, both, hasItem, equalTo, not, startsWith, etc. 
Matcher has method matches, and expected value as a state. 
JUnit will use matcher.match(actual) and pass actual value in it.
Hamcrest good that he has a lot of matchers, so you can use predefined.
You can create own matchers that is applicable for your needs. e.g. BasketShopMatcher.isEmpty()

In Java, if something has utilities, it will be in class with "S" ending. Array, utilities for Array  in Arrays.

Parametrised test
If you have same test with a lot of different arguments. Then you need to create method that will return Iterable
and get the data from it in test.
```java
@RunWith(Parameterized.class) // Means do not run with default @RunWith(JUnit4.class), but with another runner
/* We need to pass class, because we have to use metadata about the class, because of reflection */
public class ParameterizedTest {
    private final int actual;
    private final int expected;

    public ParameterizedTest(int actual, int expected){
        this.actual = actual;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{{3, 4}, {2, 5}});
    }

    @Test
    public void test() {
        assertThat(actual, not(expected));
    }
}
```
@RunWith(Parameterized.class) runner can use a diff arguments with one method @RunWith(Theory.class) runner lets you
use diff arguments with diff methods.

Good test framework have to use declarative style, if it uses imperative, it will be a lot of code and logic pollution
in the tests. They will be more difficult to understand.

All JUnit's annotations can be replaced with classes/Interfaces Test, Assert and so on, but annotations is easier.   

You can gather test and testSuites in more suites. In Suite classes there no need to test something, you can setup here
something and teardown
```java
@RunWith(Suite.class)
@Suite.SuiteClasses({UserSuiteTest.class, LoginSuiteTest.class})
public class AllSuiteTest {
    @Before
    public void setup(){}
    @After
    public void teardown(){}
}
```

JMock, Mockito let you create mock/spy objects, that will give you all information about interoperation with your test 
object, where, what, how many, with what it is invoked.

##### Rest assured.
```java
// Can be written in BDD style
when().get("http://something.com/1").then().bondy("property_name", is("property_value"));
// Or TDD
RequestSpecBuilder builder = new RequestSpecBuilder();
builder.setBaseUrl("http://something.com");
assertThay(RestAssured.given().spec(builder.build()).get("/1").getBody().jsonPath().get("property_name"), is("property_value"))
```