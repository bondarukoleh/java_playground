# Exceptions, Assertion, and Logging

## Dealing with Errors
The mission of exception handling is to transfer control from where the error occurred to an error handler that can deal
with the situation.

Java allows every method an alternative exit path if it is unable to complete its task in the normal way. It throws an
object that encapsulates the error information, method exits immediately. The exception-handling mechanism begins its
search for an exception handler that can deal with this particular error condition.

### The Classification of Exceptions
An exception object is always an instance of a class derived from _Throwable_. But the hierarchy immediately splits into
two branches: _Error_ and _Exception_.

![exceptions](/info/Java_Core_Volume_I/info/media/exceptions/exceptions.PNG)

The _Error_ hierarchy describes internal errors and resource exhaustion situations inside the Java runtime system.
**You should not throw** an object of **this type**.

When doing Java programming, **focus on** the _Exception_ hierarchy. The Exception hierarchy also splits into two
branches: exceptions that derive from RuntimeException and those that do not. The general rule is this: A RuntimeException
happens because you made a programming error. Any other exception occurs because a bad thing, such as an I/O error, etc.

The Java Language Specification calls any exception that derives from the class Error or the class RuntimeException an
_unchecked_ exception. All other exceptions are called _checked_ exceptions.

### Declaring Checked Exceptions
A method will not only tell the Java compiler what values it can return, it is also going to tell the compiler what can
go wrong. In Java you declare that your method may throw an exception with an exception specification in the method header. \
However, you do not need to advertise internal Java errors — that is, exceptions inheriting from Error, you  also should
not advertise unchecked exceptions inheriting from.

In summary, a method must declare all the checked exceptions that it might throw. Unchecked exceptions are either beyond
your control (Error) or result from conditions that you should not have allowed in the first place (RuntimeException).

> If you override a method from a superclass, the checked exceptions that the subclass method declares cannot be more
> general than those of the superclass method. (It is OK to throw more specific exceptions, or not to throw any exceptions
> in the subclass method.) In particular, if the superclass method throws no checked exception at all, neither can the subclass.

When a method declares that it throws an exception, it may throw  an exception of that class or of any of its subclasses.
e.g. FileInputStream could declare that it throws an IOException. In case, you know what kind of IOException it is - it
could throw any of the various IOException subclasses, such as FileNotFoundException.

### Creating Exception Classes
Your code may run into a problem which is not adequately described by any of the standard exception classes. In this case,
it is easy enough to create your own exception class. Just derive it from Exception, or from a child class of Exception
such as IOException.

## Catching Exceptions
Remember, the compiler strictly enforces the throws specifiers. If you call a method that throws a checked exception,
you must either handle it or pass it on. Which of the two is better? As a general rule, you **should catch** those **exceptions** 
**you can handle** and **propagate those** that **you do not know how to handle**. \
There is one exception to this rule. If you are writing a method that overrides a superclass method which throws no
exceptions, then you must not throw anything from override method. You catch everything there. You are not allowed to
add more `throws` to a subclass method than are present in the superclass method.

### Catching Multiple Exceptions
Use a separate catch clause for each type
```java
try {
    code that might throw exceptions
}
catch (FileNotFoundException e) {
    emergency action for missing files
}
catch (UnknownHostException e)  {
    emergency action for unknown hosts
}
catch (FileNotFoundException | UnknownHostException e){
    emergency action for missing files and unknown hosts
}
```
You can catch multiple exception types in the same catch clause. This feature is only needed when catching exception 
types that are not subclasses of one another.

### Rethrowing and Chaining Exceptions
You can throw an exception in a catch clause. Typically, you do this when you want to change the exception type.
However, it is a better idea to set the original exception as the “cause” of the new exception:
```java
try {
    access the database
}
catch (SQLException original) {
    var e = new ServletException("database error");
    e.initCause(original);
    throw e;
}
```

### The _finally_ Clause
The code in the _finally_ clause executes whether or not an exception was caught. You can use the finally clause without
a catch clause. \
> (!) Suppose you exit the middle of a try block with a `return` statement. Before the method returns, the _finally_ block
> is executed. If the _finally_ block also contains a `return` statement, then it **masks the original return value**.
> The body of the _finally_ clause is intended for cleaning up resources. Don’t put statements that change the control
> flow (`return, throw, break, continue`) inside a _finally_ clause.

### The try-with-Resources Statement
A simple usage of try statement.
```java
open a resource
try {
    work with the resource
}
finally {
    close the resource
}
```
In its simplest variant, the _try-with-resources_ statement has the form:
```java
try (Resource res = . . .) {
    work with res
}
```
When the _try_ block exits, then `res.close()` is called automatically.

You can specify multiple resources:
```java
try (
        var in = new Scanner(Path.of("in.txt"), StandardCharsets.UTF_8);
        var out = new PrintWriter("out.txt", StandardCharsets.UTF_8)
     ) {
        while (in.hasNext())
        out.println(in.next().toUpperCase());
        }
```
No matter how the block exits, both _in_ and _out_ are closed. \
In try catch you can provide previously declared effectively final variables. \
A difficulty arises when the try block throws an exception and the close method also throws an exception. The 
try-with-resources statement handles this like this: The original exception is rethrown, and any exceptions thrown by
close methods are considered “suppressed.” They are automatically caught and added to the original exception with the
_addSuppressed_ method. If you are interested in them, call the _getSuppressed_ method which yields an array of the
suppressed expressions from close methods. Use the _try-with-resources_ statement whenever you need to _close_ a resource.

> A _try-with-resources_ statement can itself have _catch_ clauses and even a _finally_ clause. These are executed after
> closing the resources.

### Analyzing Stack Trace Elements
A stack trace is a listing of all pending method calls at a particular point in the execution of a program. \
You can access the text description of a stack trace by calling the _printStackTrace_ method. \
A more flexible approach is the _StackWalker_ class.

## Tips for Using Exceptions
1. Exception handling is not supposed to replace a simple test.
2. Do not micromanage exceptions.
3. Make good use of the exception hierarchy.
4. Do not silent exceptions.
5. When you detect an error, “tough love” works better than indulgence.
6. Propagating exceptions is not a sign of shame.
7. Use standard methods for reporting null-pointer and out-of-bounds exceptions.
   The Objects class has methods:
   - requireNonNull
   - checkIndex
   - checkFromToIndex
   - checkFromIndexSize
     If the method is called with an invalid index or a null argument, an exception is thrown.
> Rules 5 and 6 can be summarized as “**throw early, catch late**.”

## Using Assertions
### The Assertion Concept
The assertion mechanism allows you to put in checks during testing and to have them **automatically removed** in the
production code. The Java language has a keyword `assert`. There are two forms:
```java
assert condition;
assert condition : expression;
```
Both statements evaluate the condition and throw an AssertionError if it is false. In the second statement, the
expression is passed to the constructor of the AssertionError object and turned into a message string.

### Assertion Enabling and Disabling
By default, assertions are disabled. Enable them by running the program with the _-enableassertions_ or _-ea_ option. \
You can even turn on assertions in specific classes or in entire packages. \
You can also disable assertions in certain classes and packages with the -disableassertions or -da option: \
```shell
java -enableassertions MyApp;
java -ea:MyClass -ea:com.mycompany.mylib MyApp
java -ea:... -da:MyClass MyApp
```

### Using Assertions for Parameter Checking
The Java language gives you three mechanisms to deal with system failures:
- Throwing an exception 
- Logging 
- Using assertions

When should you choose assertions? Keep these points in mind:
- Assertion failures are intended to be fatal, unrecoverable errors. 
- Assertion checks are turned on only during development and testing (they won't help on PROD).

Suppose the method contract had been slightly different:
```java
@param a the array to be sorted (must not be null).
```
Now, since the callers of the method have been put on notice that it is illegal to call the method with a `null` array. \
The method may start with the assertion:
```java
assert a != null;
```
Computer scientists call this kind of contract a _precondition_.

### Using Assertions for Documenting Assumptions
**Assertions** are a tactical tool **for testing and debugging**. In contrast, **logging** is a strategic tool for **the
entire lifecycle** of a program.

## Logging
Here are the principal advantages of the logging API:
- It is easy to suppress all log records or just those below a certain level
- Suppressed logs are very cheap, a minimal penalty for leaving the logging code in your application.
- Different output - displaying in the console, writing to a file, and so on.
- Both loggers and handlers can filter records using any criteria supplied by the filter implementor.
- Log has formats, in plain text or XML.
- Applications can use multiple
- The logging configuration is controlled by a configuration file

Simple:
```java
Logger.getGlobal().info("File->Open menu item selected");
```

### Advanced Logging
Call the getLogger method to create or retrieve a logger:
```java
private static final Logger myLogger = Logger.getLogger("com.mycompany.myapp");
```
Similar to package names, logger names are hierarchical. If you set the log level on the logger "com.mycompany", then
the child loggers inherit that level.

There are seven logging levels:
- SEVERE 
- WARNING 
- INFO 
- CONFIG 
- FINE 
- FINER 
- FINEST

You can set `logger.setLevel(Level.FINE);` Now `FINE` and all levels above it are logged. \ 
You can also use `Level.ALL` to turn on logging for all levels or `Level.OFF` to turn all logging off. \
You can use the `logp` method to give the precise location of the calling class and method. \
A common use for logging is to log _unexpected exceptions_.

### Changing the Log Manager Configuration
The _loggers_ don’t actually send the messages to the console — that is the job of the _handlers_. _Handlers_ also have
levels. To see FINE messages on the console, you also need to set
```shell
java.util.logging.ConsoleHandler.level=FINE
```
The log manager is initialized during VM startup, before main executes.

> The settings in the log manager configuration are not system properties. Starting a program with 
> `-Dcom.mycompany.myapp.level=FINE` does not have any effect on the logger.

You can instead update the logging configuration by calling
```java
LogManager.getLogManager().updateConfiguration(mapper);
```
A new configuration is read from the location specified by the _java.util.logging.config.file_ system property.

### Handlers
By default, loggers send records to a ConsoleHandler that prints them to the _System.err_ stream. \
For a record to be logged, its logging level must be above the threshold of _both_ the logger and the handler. \
The log manager configuration file sets the logging level. Default is INFO. \
By default, a logger sends records both to its own handlers and to the handlers of the parent. \
To send log records elsewhere, add another handler. The logging API provides two useful handlers for this purpose:
a _FileHandler_ and a _SocketHandler_.

### A Logging Recipe
1. For a simple application, choose a single logger. It is a good idea to give the logger the same name as your main
application package.
2. It is a good idea to install a more reasonable default in your application.
3. The level _FINE_ is a good choice for logging messages that are intended for programmers.

> Whenever you are tempted to call System.out.println, emit a log message instead: logger.fine("File open dialog canceled");

## Debugging Tips
1. You can print or log the value of any variable with code like this `System.out.println("x=" + x);` 
or `Logger.getGlobal().info("x=" + x);`
2. One seemingly little-known but very useful trick is putting **a separate** `main` method **in each class**. Inside it,
you can put a unit test stub that lets you test the class in isolation. You can leave all these `main` methods in place
and launch the JVM separately on each of the files to run the tests. When you run an app, the JVM calls only the `main`
method of the startup class. (This is basically what JUnit is doing with tests)
3. A _logging proxy_ is an object of a subclass that intercepts method calls, logs them, and then calls the superclass.
4. You don’t even need to catch an exception to generate a stack trace. Simply insert the statement: `Thread.dumpStack();`
   anywhere into your code to get a stack trace.
5. Normally, the stack trace is displayed on `System.err`. If you want to log or display the stack trace, here is how
you can capture it into a string:
```java
var out = new StringWriter();
new Throwable().printStackTrace(new PrintWriter(out));
String description = out.toString();
```
6. It is often handy to trap program errors in a file:
```shell
# capture the error stream asser
java MyProgram 2> errors.txt
# To capture both System.err and System.out in the same file, use
java MyProgram 1> errors.txt 2>&1
```
7. You can provide a separate handler (e.g. for uncaught exceptions) and make it write only them into file.
8. To watch class loading, launch the Java virtual machine with the _-verbose_ flag.
9. The _-Xlint_ option tells the compiler to spot common code problems (e.g. missing `break` statements in `switch`)
10. The JVM has support for monitoring and management of Java applications. Memory consumption, thread usage, class
loading, and so on. Start your program, then start _jconsole_ and pick your program from the list of running Java programs.
11. Java Mission Control is a professional-level profiling and diagnostics tool.