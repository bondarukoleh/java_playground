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
