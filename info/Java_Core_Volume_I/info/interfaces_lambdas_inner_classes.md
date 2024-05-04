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
**Lambda expressions are closures**, just like JS. A lambda expression has three ingredients:
1. A block of code 
2. Parameters 
3. Values for the _free variables_ (captured) — that is, the variables that are not parameters and not defined inside the
code, but came from outside the lambda.

The data structure representing the lambda expression must store the values for the _free variables_. We say that such
values have been _captured_ by the lambda expression.

>**In a lambda** expression, you can only **reference variables** whose value **doesn’t change**. \
> You **can't mutate captured** variable.

Mutating variables in a lambda expression is not safe when multiple actions are executed concurrently. It is also illegal
to refer, in a lambda expression, to a variable that is mutated outside.

**Any captured variable** in a lambda expression **must be effectively final**. An effectively final variable is a
variable that is never assigned a new value after it has been initialized.

The body of a lambda expression has the same scope as a nested block. The same rules for name conflicts and shadowing apply.

When you use the `this` keyword in a lambda expression, you refer to the `this` parameter of the method that **creates** the lambda.

### Processing Lambda Expressions
The point of using lambdas is _deferred execution_. \
There are many reasons for executing code later, such as - Running the code in a separate thread, multiple times, at the
right point in an algorithm, on some specific event, only when necessary, etc. \
To accept the lambda, we need to pick (or, in rare cases, provide) a functional interface. (Runnable, Supplier, Predicate, etc.) \
It is more efficient to use these specializations than the generic interfaces:
```java
public interface IntConsumer {
    void accept(int value);
}

public static void repeat(int n, IntConsumer action) {
   for (int i = 0; i < n; i++) action.accept(i);
}
```
If you design your own interface with a single abstract method, you can tag it with the _@FunctionalInterface_ annotation.

Some programmers love chains of method calls
```java
String input = " 618970019642690137449562111 ";
boolean isPrime = input.strip().transform(BigInteger::new).isProbablePrime(20);
```

The _transform_ method of the String class applies a Function to the string and yields the result.

### More about Comparators
The _Comparator_ interface has a number of convenient static methods for creating comparators. These methods are intended
to be used with _lambda expressions_ or _method references_. \
The static _comparing_ method takes a “key extractor” - function that maps a type `T` to a comparable type (e.g. String).

```java
Arrays.sort(people, Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName));
```
If two people have the same last name, then the second comparator is used. \
You can specify a _comparator_ to be used for the keys that the _comparing_ and _thenComparing_ methods extract.
```java
Arrays.sort(people, Comparator.comparing(Person::getName, (s, t) -> Integer.compare(s.length(), t.length())));
```
If your key function can return `null`, you could use _nullsFirst_ and _nullsLast_ adapters. These static methods take
an existing comparator and modify it so that it doesn’t throw an exception when encountering `null` values but ranks
them as smaller or larger than regular values. \
The _nullsFirst_ method needs a comparator, the `naturalOrder` method makes a comparator for any class implementing _Comparable_.
Comparator.<String>naturalOrder() is what needed here.
```java
Arrays.sort(people, comparing(Person::getMiddleName, nullsFirst(naturalOrder())));
```

### Inner Classes
An _inner class_ is a class that is defined inside another class. There are two reasons:
- Inner classes can be hidden from other classes in the same package. 
- Inner class methods can access the data from the scope in which they are defined — including the data that would
otherwise be private.
- A lot of things require some objects/classes that implements some Interface. Instead of doing it somewhere else, you
can create those instance internally, and have muc more compact and hidden implementation.

### Use of an Inner Class to Access Object State
An inner class method gets to access both its own instance fields and those of the outer object creating it. \
The **outer** class **reference** is **set in** the **constructor**. The compiler modifies all inner class constructors,
adding a parameter for the outer class reference.

> Only **inner classes** can be **private**. **Regular classes** always have either **package or public access**.

### Special Syntax Rules for Inner Classes
`OuterClass.this` - denotes the outer class reference. \
You can write the inner object constructor more explicitly, using the syntax:
```java
// outerObject.new InnerClass(construction parameters)
ActionListener listener = this.new TimePrinter();
```
Here, the outer class reference of the newly constructed TimePrinter object is set to the `this` reference of the
method that creates the inner class object. \
This is the most common case. As always, the `this` qualifier is redundant. However, it is also possible to set the
outer class reference to another object by explicitly naming it. For example, since TimePrinter is a public inner
class, you can construct a TimePrinter for any talking clock:
```java
var jabberer = new TalkingClock(1000, true);
TalkingClock.TimePrinter listener = jabberer.new TimePrinter();
```
> **Regular inner classes cannot have `static` fields**. They belong to the instance of the parent, so they cannot
> access enclosed static parent fields.

### Local Inner Classes
You **can define the class** locally **in** a single **method**:
```java
public void start()  {
    class TimePrinter implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is " + Instant.ofEpochMilli(event.getWhen()));
            if (beep) Toolkit.getDefaultToolkit().beep() 
        }
    }
    var listener = new TimePrinter();
    var timer = new Timer(interval, listener);
    timer.start();
}
```
Local classes are never declared with an access specifier. Their scope is always restricted to the block in which they
are declared. \
Local classes have one great advantage: They are completely hidden from the outside world—not even other code in the
TalkingClock class can access them. No method except start has any knowledge of the TimePrinter class.

### Accessing Variables from Outer Methods
Local classes even access and enclose local (method arguments) variables! However, those local variables must be
effectively final.

### Anonymous Inner Classes
If you want to make only a single object of this class, you don’t even need to give the class a name. Such a class is
called an _anonymous inner class_.
```java
public void start(int interval, boolean beep) {
    var listener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is " + Instant.ofEpochMilli(event.getWhen()));
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    };
    var timer = new Timer(interval, listener);
    timer.start();
}
```
What it means is this: Create a new object of a class that implements the _ActionListener_ interface.
```text
new SuperType(construction parameters) {
        inner class methods and data
    }
```
_SuperType_ can be an interface, such as ActionListener, or one that implements that interface. SuperType can also be
a class or inner class extends that class. \
An anonymous inner class cannot have constructors because the name of a constructor must be the same as the name of a
class, and the class has no name. Instead, the construction parameters are given to the superclass constructor. In
particular, whenever an inner class implements an interface, it cannot have any construction parameters. Nevertheless,
you must supply a set of parentheses

Even though an anonymous class cannot have constructors, you can provide an object initialization block.
```java
var count = new Person("Dracula") {
    { initialization }
    . . .
};
```

The object constructed with `new Object() { String name = "Bob"; }` has type “Object with a Sting name field”. This
is a _non-denotable type_ — a type that you cannot express with Java syntax. Nevertheless, the compiler understands the type.

The following trick, called double brace initialization:
```java
// instead of
var friends = new ArrayList<String>();
friends.add("Harry");
friends.add("Tony");
invite(friends);
// you can
invite(new ArrayList<String>() {{ add("Harry"); add("Tony");}});
```
Note the double braces. The outer braces make an anonymous subclass of ArrayList. The inner braces are an object 
initialization block.

When you produce logging or debugging messages you often want to include the name of the current class:
```java
System.err.println("Something awful happened in " + getClass());
```
But that fails in the `static` methods. Use this:
```java
new Object(){}.getClass().getEnclosingClass() // gets class of static method
```
`new Object(){}` makes an anonymous object inside the desired class, and you can get his parent.

### Static Inner Classes
If you need an _inner class_ — but you don’t need it to have a reference to the parent object? You can suppress the
generation of that reference by declaring the inner _class static_. Some programmers use the term _nested class_.
It will belong to the parent class itself, not to his object.

> Only **inner classes can be** declared **_static_**. A static inner class is exactly like any other inner class,
> except that an **object** of a static inner class does not have a reference to the outer parent that generated it.

> **Unlike regular** inner classes, **static inner classes** can **have static fields and methods**.

> Classes that are declared inside an interface are automatically static and public. This helps to organize code better.

> Interfaces, records, and enumerations that are declared inside a class are automatically static. For encapsulation,
> and code organization.

### Service Loaders
Sometimes, you develop an application with a service architecture. There are platforms that encourage this approach,
such as OSGi (http://osgi.org), the JDK also offers a simple mechanism for loading services, supported by Java Platform
Module System. The _ServiceLoader_ class makes it easy to load services that conform to a common interface.

The implementing Service Interface classes can be in any package.

## Proxies
You can use a proxy to create, at runtime, new classes that implement a given set of interfaces. Proxies are only 
necessary when you don’t yet know at compile time which interfaces you need to implement.

Proxy is good for:
- Remote Communication: enable communication between distributed components or systems. For example, in a client-server
architecture, a proxy can act as an intermediary between the client and the server, handling communication details.
- Security: Proxies can enforce access control and security policies by intercepting requests to access sensitive resources.
For example, a security proxy can authenticate users and enforce authorization rules.
- Logging and Monitoring: Proxies can be used to log method invocations or monitor the behavior of objects at runtime.
- Lazy Initialization: Proxies can defer the creation or initialization of expensive objects until they are actually needed.
- Aspect-Oriented Programming (AOP): Proxies are a fundamental building block in AOP frameworks.