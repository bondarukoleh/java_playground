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
