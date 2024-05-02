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

If _Child_ (e.g. _Manager_) chooses to override `compareTo`, it must be prepared to compare Child to Parent.
It can’t simply cast a Parent to a Child.

### Properties of Interfaces

You can never use the `new` operator to instantiate an interface. But you can **declare** interface variables.
An interface variable must refer to an object of a class that implements the interface. You can use `instanceof` to check
whether an object implements an interface. You can `extend` the interface. \
Although you cannot put instance fields in an interface, you can supply constants in them.
```java
public interface Powered extends Moveable {
    double milesPerGallon();
    double SPEED_LIMIT = 95; // a public static final constant
}
```
Just as **methods** in an interface are automatically **public**, **fields** are always **public static final**. \
While each class can have only one superclass, classes can implement multiple interfaces. Interfaces can be sealed.

> Records and enumeration classes cannot extend other classes, however, can implement interfaces

### Interfaces and Abstract Classes

Why can’t Comparable simply be an abstract class? The main reason - a class only extend a single class. Interfaces afford
most of the benefits of multiple inheritance while avoiding the complexities and inefficiencies.

### Static and Private Methods
You are allowed to add static methods to interfaces. \
When you implement your own interfaces, there is no longer a reason to provide a separate companion class for utility
methods. Methods in an interface can be private. A private method can be static or an instance method. Since private
methods can only be used in the methods of the interface itself, their use is limited to being helper methods for the
other methods of the interface.

### Default Methods
You can supply a default implementation for any interface method, with the `default` modifier. A default method can call
other methods. Adding a nondefault method to an interface is not source-compatible.

### Resolving Default Method Conflicts
What happens if the exact same method is defined as a default method in one interface and then again as a method of a
superclass or another interface?
1. **Superclasses win**. If a superclass provides a concrete method, `default` methods with the same name and
parameter types are simply ignored. 
2. **Interfaces clash**. If an interface provides a default method, and another interface contains a method with the
same name and parameter types (default or not), then you must resolve the conflict by overriding that method. It doesn’t
matter how two interfaces conflict. The Java compiler reports an error and leaves it up to the programmer to implement
the method he wants to use. 
```java
class Student implements Person, Named {
    public String getName() { return Person.super.getName(); }
}
```
> You can never make a _default_ method that redefines one of the methods in the Object class. As a consequence of the
> “class wins” rule.

### Interfaces and Callbacks
The _callback pattern_ - you specify the action that should occur whenever a particular event happens.

### The _Comparator_ Interface
Suppose we want to sort strings by increasing length, not in dictionary order. There is a second version of the _Arrays.sort_
method whose parameters are an _array_ and a _comparator_ — an instance of a class that implements the _Comparator_ interface.

### Object Cloning

