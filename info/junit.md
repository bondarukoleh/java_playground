Unit (Module) tests - testing of minimal functionality (method/class/module). Sometimes you can test only method, 
because it's not dependent on something else. But when method, or class depend on other classes, and change their state
with some hidden private methods or fields - these bunch of classes is a minimal functionality. You cannot check 
something that depends on another thing separately, it won't give you confident about its correctness.\

JUnit 4
in org.junit.Test.
Framework for testing. All frameworks has some amound of abstractions that you can use.
JUnit has runner to run the tests.
Since Java (jvm) knows how to run only main() method - and you want to run test methods somewhere else, this Runner has
his main, and JVM will run his main(), from what there will be invocations of your test methods.
So when you are running from IDE - IDE runs the JUnit, JUnit runs his (or your own, if you setup) runner, runner runs 
methods tells JUnit, JUnit tells IDE, IDE show the results.
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

Tests are independent in JUnit, one fail doesn't stop the run. For each test method JUnit creates separate instance of 
the application (so far I get);

If you trying by TDD write tests first and understand that it's too hard to write them on your functionality, it's a 
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
