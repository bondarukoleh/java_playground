# Inheritance

## Classes, Superclasses, and Subclasses
The existing class is called the _superclass, base class, or parent class_. The new class is called the _subclass,
derived class, or child class_.

### Overriding Methods
If you define a method in a subclass that has the same signature as a superclass method, you **override** the 
superclass method. If you need to call superclass' method from subclass - `super` keyword.
```java
super.getSalary();
```
`super` is not a reference to an object, it is a special keyword that directs the compiler to invoke the superclass method.
> The return type is not part of the signature. However, **when you override** a method, you **need to keep the
> return type** compatible. A subclass may change the return type to a subtype of the original type.

> When you override a method, the subclass method could have more visibility not less. parent is package-private child
> must be the same or public.

### Subclass Constructors
Here, the keyword `super` has a different meaning:
```java
class Manager extends Employee {
    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        bonus = 0;
    }
}
```
is shorthand for “call the constructor of the Employee superclass with n, s, year, month, and day as parameters.”.
The call using `super` **must be the first statement** in the constructor for the subclass.

```java
var staff = new Employee[2];
staff[0] = new Manager();
staff[1] = new Employee();

for (Employee e : staff)
        System.out.println(e.getName() + " " + e.getSalary());
```

The fact that an object variable (such as `e`) can refer to multiple actual types is called _polymorphism_. Automatically
selecting the appropriate method at runtime is called _dynamic binding_.

### Inheritance Hierarchies
The collection of all classes extending a common superclass is called an _inheritance hierarchy_. The path from a
particular class to its ancestors in the inheritance hierarchy is its _inheritance chain_.
Hierarchy shown more like a root, the superclass at the top, and subclasses are below.  

### Polymorphism
A simple rule can help you decide whether or not inheritance is the right design for your data. The **“is–a”** rule states
that every object of the subclass is an object of the superclass.

Another way of formulating the “is–a” rule is the _substitution principle_. You should **be able to use a subclass**
object **whenever** the program **expects a superclass object**. \
In the Java programming language, object variables are **polymorphic**. A variable of type _Employee_ can refer to an
object of type Employee or any of his subclasses.

> (!) In Java, arrays of subclass references can be converted to arrays of superclass references without a cast. \
> **All arrays remember the element type** with which they **were created**. Attempt to store not appropriate type 
> causes an ArrayStoreException

### Understanding Method Calls
Let’s say we call `x.f(args)`
1. The compiler looks at the declared type of the object and the method name. The compiler enumerates all methods called
`f` in the class `X` and all accessible methods called `f` in the superclasses of `X`.
2. Next, the compiler determines the types of the arguments supplied in the method call. If there is a unique method
whose parameter types are a best match that method is chosen to be called. This process is called _overloading resolution_.
Now the compiler knows the name and parameter types of the method that needs to be called.
3. If the method is _private, static, final,_ or a _constructor_, then the compiler knows exactly which method to call.
Because they are only in one place. This is called _static binding_. Otherwise, the method to be called depends on the
actual type of the _implicit_ parameter, and _dynamic binding_ must be used at runtime.
4. When the program runs and uses _dynamic binding_ to call a method, the VM must call the version of the method that
is appropriate for the _actual type_. It would be time-consuming to carry out this search every time, instead VM
precomputes a _method table_ for each class. When a method is actually called, the virtual machine simply makes a table
lookup.

_Dynamic binding_ - is a key mechanism that enables polymorphism in object-oriented programming. It makes programs
extensible without the need for modifying existing code. Reusability, flexibility, and extensibility.

### Preventing Inheritance: Final Classes and Methods
Classes that cannot be extended are called _final classes_, and you use the `final` modifier in the definition of the
class. \
You can also make a specific method in a class `final` - no subclass can override that method.
> Fields can also be declared as `final` - cannot be changed after the object has been constructed.
> However, if a class is declared `final`, only the methods, not the fields, are automatically `final`.

There is only one good reason to make a method or class _final_: to make sure its semantics cannot be changed in a
subclass. For example, the _getTime_ and _setTime_ methods of the Calendar class are final. String class is final.
**Enumerations** and **records** are **always final** — you cannot extend them.

If a method is not overridden, and it is short, then a compiler can optimize the method call away — a process
called _inlining_ - put the body of the small method directly to the code, instead of calling the method, JIT is also
doing it.

### Casting
A conversion from one type to another is called _casting_. Occasionally you need to convert a floating-point number to an
integer, you also may need to convert an object reference from one class to another. You can cast only within an
inheritance hierarchy.

There is only one reason why you would want to make a cast — to use an object in its full capacity after its actual type
has been temporarily forgotten.

What happens if you try to cast down an inheritance chain and are “lying” about what an object contains?
```java
Manager boss = (Manager) staff[1]; // ERROR
```
When the program runs, the Java runtime system notices the broken promise and generates a ClassCastException.
Simply use the `instanceof` operator. For example:
```java
if (staff[i] instanceof Manager) {
    Manager boss = (Manager) staff[i];
    boss.setBonus(5000);
}

// since Java 16 You can declare the subclass variable right in the instanceof test
if (staff[i] instanceof Manager boss) {
    boss.setBonus(5000);
}
```
> x instanceof C does not generate an exception if x is null.

In general, it is best to **minimize the use** of _casts_ and the `instanceof` operator.

### Pattern Matching for instanceof
When an `instanceof` pattern introduces a variable, you can use it right away
```java
Employee e;
if (e instanceof Manager m && m.getBonus() > 10000) . . .
// or
double bonus = e instanceof Manager m ? m.getBonus() : 0;
```

The variable-declaring `instanceof` form is called _“pattern-matching”_ because it is similar to type patterns in switch,
a _“preview”_ feature of Java 17.
```java
String description = switch (e) {
    case Executive exec -> "An executive with a fancy title of " + exec.getTitle();
    case Manager m -> "A manager with a bonus of " + m.getBonus();
    default -> "A lowly employee with a salary of " + e.getSalary();
}
```

### Protected Access
When you want to restrict a method to subclasses only or to allow subclass methods to access a superclass field - declare
a class feature as `protected`. In Java, a protected field is accessible by any class in the same package, as well as
by subclasses regardless of package. (for package-private - no access outside a package)

Use protected fields with caution, unknown to you, other programmers may inherit classes from your class and start 
accessing your protected fields.

## `Object`: The Cosmic Superclass
Every class in Java extends _Object_. The ultimate superclass _Object_ is taken for granted if no superclass is
explicitly mentioned. All variables except _primitive_ types, are _Object_ type.

### The `equals` Method
The `equals` method in the _Object_ class tests whether one object is considered equal to another. The `equals` method,
as **implemented in the Object** class, **determines** whether two **object references** are identical.

> To guard against the possibility that one of the arguments might be null use `Objects.equals(a, b)`. It is null-safe.
> Returns `true` if both arguments are `null`. Returns `false` if only one is `null`, if null check passed, it calls
> a.equals(b) under the hood.

When you define the equals method for a subclass, first call equals on the super-class. If that test doesn’t pass, then
the objects can’t be equal. If the superclass fields are equal, you are ready to compare the instance fields of the
subclass.

> _Records_ automatically define an equals method that compares the fields. Two record instances are equals when the
> corresponding field values are equal.

### Equality Testing and Inheritance
Language Specification requires that the `equals` method has the following characteristics:
1. It is _reflexive_: For any non-null reference `x`, `x.equals(x)` should return **true**.
2. It is _symmetric_: For any references `x` and `y`, `x.equals(y)` should return **true** if and only if `y.equals(x)`
returns **true**.
3. It is _transitive_: For any references `x`, `y`, and `z`, if `x.equals(y)` returns **true** and `y.equals(z)` returns
**true**, then `x.equals(z)` should return **true**.
4. It is _consistent_: If the objects to which `x` and `y` refer haven’t changed, then repeated calls to `x.equals(y)` return 
the same value.
5. For any non-null reference `x`, `x.equals(null)` should return **false**.

A common mistake when implementing the equals method:
```java
public boolean equals(Employee other)
``` 
This method declares the explicit parameter type as Employee. As a result, it does not override the `equals` method of
the _Object_ class but defines a completely unrelated method. Correct way is:
```java
@Override public boolean equals(Object other)
```

