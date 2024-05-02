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
_Cloneable_ interface that indicates that a class has provided a safe `clone` method. \
If you would like _copy_ to be a new object that begins its life being identical to original but whose state can diverge
over time, use the `clone` method.
```java
var original = new Employee("John Public", 50000);
Employee copy = original.clone();
copy.raiseSalary(10); // OK--original unchanged
```
But default `clone` copies only values. If all instance fields are primitives - `clone` is just fine. But if the object
contains references to subobjects, then copying the field gives you another reference to the same subobject.

Does it matter if the copy is shallow? If the subobject shared between the _original_ and the _shallow clone_ is
_immutable_, then the sharing is safe. This certainly happens if the subobject belongs to an immutable class, such as
String. Alternatively, the subobject may simply remain constant.

Quite frequently, however, subobjects are mutable, and you must **redefine** the `clone` method to make a **deep copy** that
clones the subobjects as well.

For every class, you need to decide whether:
1. The default clone method is good enough;
2. The default clone method can be patched up by calling clone on the mutable subobjects; or
3. clone should not be attempted.

The third option is actually the default. To choose either the first or the second option, a class must:
1. Implement the _Cloneable_ interface; and
2. Redefine the `clone` method with the `public` access modifier.

> A subclass can call a protected clone method only to clone its own objects. You must redefine clone to be public
> to allow objects to be cloned by any method.

The appearance of the _Cloneable_ interface has nothing to do with the normal use of interfaces. In particular, it does
not specify the clone method — that method is inherited from the Object class. **The interface merely serves as a tag**,
indicating that the class designer understands the cloning process. Objects are so paranoid about cloning that they
generate a checked exception.

> The _Cloneable_ interface is one of **tagging** interfaces that Java provides. (called them _marker_ interfaces.)

Be careful about cloning of subclasses, anyone can use it to clone Manager objects. There is no guarantee that the
implementor of the subclass has fixed clone to do the right thing. For that reason, the clone method is declared as
protected in the Object class.


## Lambda Expressions
A _lambda expression_ is a block of code that you can pass around so it can be executed later, once or multiple times.

### The Syntax of Lambda Expressions
If a method has a single parameter with inferred type, you can even omit the parentheses:
```java
ActionListener listener = event -> System.out.println("The time is " + Instant.ofEpochMilli(event.getWhen()));
  // instead of (event) -> . . .
  // or (ActionEvent event) -> . . .
```
You **never specify** the result **type of a lambda** expression. It is always inferred from context.

> It is illegal for a lambda expression to return a value in some branches but not in others. For example: \
> `(int x) -> { if (x >= 0) return 1; }` is invalid.

### Functional Interfaces

You can **supply a lambda** expression **whenever** an object of an interface with **a single abstract method is
expected**. Such an interface is called a _functional interface_.

To demonstrate the conversion to a functional interface, consider `Arrays.sort` method, it's second parameter requires
an instance of _Comparator_.
```java
Arrays.sort(words, (first, second) -> first.length() - second.length());
```
Behind the scenes, the `Arrays.sort` method receives an _object_ of some class that implements _Comparator<String>_.
**Invoking** the `compare` **method** on that object **executes the body of the lambda** expression. It is best to
think of a lambda expression as a function, not an object, and to accept that it can be passed to a functional interface. \
In fact, conversion to a functional interface is the only thing that you can do with a lambda expression in Java.

The _ArrayList_ class has a `removeIf` method whose parameter is a _Predicate_. It is specifically designed to pass a lambda
expression. \
Another useful functional interface is _Supplier<T>_, yields a value of type _T_ when it is called. Suppliers are used 
for _lazy evaluation_, like:
```java
LocalDate hireDay = Objects.requireNonNullElseGet(day, () -> new LocalDate.of(1970, 1, 1));
```
The `requireNonNullElseGet` method only calls the supplier when the value is needed (because the `day` was `null`).

### Method References
Sometimes a lambda expression involves a single method.
```java
var timer = new Timer(1000, event -> System.out.println(event));
// or you can pass a method reference
var timer = new Timer(1000, System.out::println);
```
The expression _System.out::println_ is a method reference. It directs the compiler to produce an instance of a functional
interface, overriding the single abstract method of the interface to call the given method.

> Like a lambda expression, **a method reference is not an object**. It produces a Functional Interface Object. \

The :: operator separates the method name from the name of an object or class
1. object::instanceMethod
   The **method** reference is equivalent to a lambda expression whose **parameters are passed** to the method
2. Class::instanceMethod
   The **first parameter becomes the implicit parameter** of the method
3. Class::staticMethod
   All **parameters are passed** to the static method.

Note that a **lambda** expression **can only be** rewritten as a **method reference if** the body of the **lambda**
expression **calls a single method** and doesn’t do anything else.

You can capture the _this_ parameter in a method reference. For example, `this::equals` is the same as `x -> this.equals(x)`.
It is also valid to use `super`. `super::instanceMethod`

### Constructor References
Constructor references are just like method references, except that the name of the method is `new`. \
```java
Stream<Person> stream = names.stream().map(Person::new);
```
You can form constructor references with array types. Array constructor references are useful to overcome a limitation 
of Java. The _Stream_ interface has a `toArray` method that returns an _Object_ array.
```java
Object[] people = stream.toArray();
// but more sufficient
Person[] people = stream.toArray(Person[]::new);
```

### Variable Scope
