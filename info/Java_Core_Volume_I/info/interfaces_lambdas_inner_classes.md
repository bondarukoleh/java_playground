# Interfaces, Lambda Expressions, and Inner Classes

_Interfaces_ is a way of describing _what_ classes should do, without specifying _how_ they should do it. \
_Lambda expressions_ - you can express code that uses callbacks or variable behavior in an elegant and concise fashion. \
_Inner classes_ are useful when you design collections of cooperating classes.

## Interfaces
An _interface_ is not a class but a set of requirements for the classes that want to conform to the interface.
```java
public interface Comparable<T> {
    int compareTo(T other); // parameter has type T
}
```
In the interface, the `compareTo` method is abstract — it has no implementation. A class that implements the Comparable
interface needs to implement a `compareTo` method. Otherwise, the class is also abstract. \
All methods of an interface are automatically public. \
`compareTo` method is supposed to return:
- a negative number if `x` is smaller than `y`
- zero if they are equal
- and a positive number otherwise

Interfaces can also define constants but interfaces never have instance fields. \
Suppose we want to use the `sort` method of the Arrays class to sort an array of Employee objects. Then the Employee
class must implement the Comparable interface, `sort` will be performed based on implemented `compareTo` method.

### Properties of Interfaces




