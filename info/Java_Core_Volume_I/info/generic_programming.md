# Generic Programming
## Why Generic Programming?
Generic programming means writing code that can be reused for objects of many different types. \
Generics offer a _type parameters_. The ArrayList class now has a _type parameter_ that indicates the element type:
```java
var files = new ArrayList<String>();
```
### Who Wants to Be a Generic Programmer?
It is not so easy to implement a generic class. \
A programmer may want to add all elements from an _ArrayList<Manager>_ to an _ArrayList<Employee>._ \
But, of course, doing it the other way round should not be legal. How do you allow one call and disallow the other?
The Java language designers invented an ingenious new concept, the _wildcard type_, to solve this problem. Wildcard
types are rather abstract, but they allow a library builder to make methods as flexible as possible.

## Defining a Simple Generic Class
A generic class is a class with one or more type variables. `public class Pair<T>` \
You instantiate the generic type by substituting types for the type variables, such as `Pair<String>`

## Generic Methods
In generic method type variables are inserted after the modifiers and before the return type.
```java
public static <T> T getMiddle(T ...a)
```
When you call a generic method, you can place the actual types, enclosed in angle brackets, before the method name:
```java
String middle = ArrayAlg.<String>getMiddle("John", "Q.","Public");
```
In this case, you can omit the `<String>` type parameter from the method call. The compiler has enough information to
infer the method.

## Bounds for Type Variables
How to be sure that some class `T` has a `compareTo` method? - Restrict `T` to a class that implements the `Comparable`
interface. By giving a _bound_ for the type variable T:
```java
public static <T extends Comparable> T min(T[] a) . . .
```
`<T extends BoundingType>` expresses that `T` should be a subtype of the _bounding_ type. Both `T` and the _bounding_
type can be either a class or an interface. \
A type variable or wildcard can have multiple bounds. e.g. `T extends Comparable & Serializable`

As with Java inheritance, you can have as many interface supertypes as you like, but at most one of the _bounds_ can be
a class. If you have a _class_ as a _bound_, it **must be** the **first** one **in the _bounds_ list**.

## Generic Code and the Virtual Machine
### Type Erasure
The VM does not have objects of generic types — all objects belong to ordinary classes. The compiler “erases” type
parameters. Whenever you define a generic type, a corresponding _raw_ type is automatically provided. The name of the
raw type is simply the name of the generic type, with the type parameters removed. The type variables are **erased** and
replaced by their bounding types (or _Object_ for variables without bounds).

### Translating Generic Methods
The problem is that the type erasure interferes with polymorphism. To fix this problem, the compiler generates a _bridge
method_ with _Object_ as a parameter and casting to `T`:
```java
class DateInterval extends Pair<LocalDate> {
    public void setSecond(LocalDate second) {
        if (second.compareTo(getFirst()) >= 0)
        super.setSecond(second);
    }
. . .
}
// after erasure
public void setSecond(Object second) { 
    setSecond((LocalDate) second);
}
```
It would be illegal to have two methods with the same _parameter types_ but different _return types_. However, in the
virtual machine, the _parameter types_ and the _return type_ **specify a method**. Therefore, the compiler can produce
bytecodes for two methods that differ only in their return type, and the virtual machine will handle this situation
correctly. \
In summary:
- There are no generics in the virtual machine, only ordinary classes and methods.
- All type parameters are replaced by their bounds.
- Bridge methods are synthesized to preserve polymorphism.
- Casts are inserted as necessary to preserve type safety.

## Restrictions and Limitations
### Type Parameters Cannot Be Instantiated with Primitive Types
You cannot substitute a _primitive_ type for a type parameter. Thus, there is no Pair<double>, only Pair<Double>.
The reason is, of course, _type erasure_. After erasure, the Pair class has fields of type Object, and you can’t use
them to store double values.

### Runtime Type Inquiry Only Works with Raw Types
_Objects_ in the virtual machine always have a specific nongeneric type. Therefore, all type inquiries yield only the
raw type. You could only test whether `a` is a _Pair_ of any type.
```java
if (a instanceof Pair<String>) // ERROR
if (a instanceof Pair<T>) // ERROR
Pair<String> p = (Pair<String>) a; // warning, can only test that a is a Pair
```
In the same spirit the `getClass` method always returns the raw type.
```java
Pair<String> stringPair = . . .;
Pair<Employee> employeePair = . . .;
if (stringPair.getClass() == employeePair.getClass()) // they are equal
```
The comparison yields true because both calls to getClass return _Pair.class_.

### You Cannot Create Arrays of Parameterized Types
You cannot instantiate arrays of parameterized types (generics), such as:
```java
var table = new Pair<String>[10]; // ERROR
```
Restricted because of erasure, an array remembers its component type and throws an ArrayStoreException if someone puts 
something there that is not _Pair_. if you try to store an element of the wrong type.

You can declare arrays of wildcard types and then cast them:
```java
var table = (Pair<String>[]) new Pair<?>[10];
```
The result is not safe.

### Varargs Warnings
There is an issue with passing instances of a generic type to a method with a variable number of arguments. \
The rules have been relaxed for this situation, and you only get a warning, not an error. \
You can suppress the warning in one of two ways. You can add the annotation `@SuppressWarnings("unchecked")`, or `@SafeVarargs`.

The `@SafeVarargs` can only be used with constructors and methods that are static, final, or private. Any other method
could be overridden, making the annotation meaningless.

### You Cannot Instantiate Type Variables
You cannot use type variables in an expression such as `new T(. . .)`. \
The best workaround is to make the caller provide a constructor expression.
```java
Pair<String> p = Pair.makePair(String::new);
```

The _makePair_ method receives a _Supplier<T>_, the functional interface for a function with no arguments and a result
of type `T`:
```java
public static <T> Pair<T> makePair(Supplier<T> constr) {
    return new Pair<>(constr.get(), constr.get());
}
```
A more traditional workaround is to construct generic objects through reflection.

### You Cannot Construct a Generic Array
As you cannot instantiate a single generic instance, you cannot instantiate an array.

### Type Variables Are Not Valid in Static Contexts of Generic Classes
You cannot reference _type_ variables in static fields or methods.
```java
public class Singleton<T> {
    private static T singleInstance; // ERROR
    public static T getSingleInstance() // ERROR 
    {
        if (singleInstance == null) {/* construct new instance of T*/}
        return singleInstance;
    }
}
```
If this could be done, then a program could declare a _Singleton<Random>_ or a _Singleton<JFileChooser>_. But it can’t
work. After type erasure there is only one _Singleton_ class, and only one _singleInstance_ field. For that reason,
static fields and methods with type variables are simply outlawed.

### You Cannot Throw or Catch Instances of a Generic Class
You can neither `throw` nor `catch` objects of a generic class. In fact, it is **not legal** for a **generic class to
extend Throwable**.
```java
catch (T e) // ERROR
```
However, it is OK to use type variables in exception specifications. The following method is legal:
```java
public static <T extends Throwable> void doWork(T t) throws T
```

### Beware of Clashes after Erasure
It is illegal to create conditions that cause clashes when generic types are erased. \
The generics specification cites another rule: “To support translation by erasure, we impose the restriction that a class
or type variable may not at the same time be a subtype of two interface types which are different parameterizations of
the same interface.”. The reason is not in erasure but there would be a conflict with the synthesized _bridge methods_.
For example, the following is illegal:
```java
class Employee implements Comparable<Employee> { . . . }
class Manager extends Employee implements Comparable<Manager> { . . . } // ERROR
```

### Inheritance Rules for Generic Types
In general, there is **no relationship** between _Pair<Employee>_ and _Pair<Manager>_, no matter how `S` and `T` are
related, and that Manager is a child of Employee. \
You can always convert a parameterized type to a raw type.

## Wildcard Types
In a _wildcard type_, a type parameter is allowed to vary. That's a convenient and shorter type declaration.
```java 
public static void printBuddies(Pair<Employee> pair)
```
No corruption is possible, because the compiler knows that the `pair` parameter has some specific type, which extends
_Employee_ There is no way for the compiler to know exact type. \
This is the key idea behind _bounded wildcards_. We now have a way of **distinguishing** between the **safe accessor** methods 
**and** the **unsafe mutator** methods, and since **compiler** doesn't know what is the type - he **prevents any call to 
mutator**.

### Supertype Bounds for Wildcards
Wildcard bounds are similar to type variable bounds, but they have an added capability - you can specify a _supertype
bound_, like this:
```java
? super Manager
```
This wildcard is restricted to all supertypes of Manager. With a wildcard with a _supertype bound_ - you can supply
parameters to methods, but you can’t use the return values. `void setFirst(? super Manager)` \
It is only possible to pass an object of type _Manager_ or a subtype such as Executive. Conversely, if you call _getFirst_,
there is no guarantee about the type of the returned object. \
Wildcards with _supertype bounds_ let you **write to a generic object**, you can work with `T` and all supertypes of `T` \  
Wildcards with _subtype bounds_ let you **read from a generic object**, you can work with `T` and all subtypes of `T`

Manager \
   | \
Employee \
   | \
Object

```java
<? super Employee> /* Supertype bound. Employee, Object and lower */
<? extends Employee> /* Subtype bound. Employee, Manager and upper */
```

### Unbounded Wildcards
You can even use wildcards with no bounds at all `Pair<?>`. \
```java
? getFirst()
void setFirst(?)
```
The return value of getFirst can only be assigned to an Object. The setFirst method can never be called, not even with
an Object. It is useful for very simple operations, when you don't care about the type, and want to check if the object
is not null for instance.

### Wildcard Capture
A wildcard is not a type variable, so we can’t write code that uses ? as a type. \
Wildcard capture is only legal in very limited circumstances. The compiler must be able to guarantee that the wildcard
represents a single, definite type. e.g. the `T` in `ArrayList<Pair<T>>` can never capture the wildcard in `ArrayList<Pair<?>>`
because compiler doesn't know what exact type is in that list.

## Reflection and Generics
### The Generic Class Class
The _Class_ class is now generic. e.g. String.class is actually an object of the class `Class<String>`. 
> The Class class, describing concrete types

### Using Class<T> Parameters for Type Matchingc
```java
public static <T> Pair<T> makePair(Class<T> c) throws InstantiationException, IllegalAccessException {
    return new Pair<>(c.newInstance(), c.newInstance());
}
// call
makePair(Employee.class)
```
_Employee.class_ is an object of type `Class<Employee>`. The type parameter `T` of the _makePair_ method matches _Employee_,
and the compiler can infer that the method returns a _Pair<Employee>_.

### Type Literals
Sometimes, you want to drive program behavior by the type of a value. You can capture an instance of the Type interface
that you encountered in the preceding section.

