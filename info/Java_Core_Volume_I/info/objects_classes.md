# Objects and Classes

## Objects
Java comes with a “cosmic superclass” called **Object**.
Objects in OOP has 3 key characteristics: behavior, state, identity.

The state of an object does not completely describe it, because each object has a distinct identity.
These key characteristics can influence each other. For example, the state of an object can influence its behavior.

### Identifying Classes
A simple rule of thumb in identifying classes is to look for nouns in the problem analysis. Methods, on the other hand,
correspond to verbs. \
For example, in an order-processing system, some of the nouns Item, Order, Account, etc. Verb, such as “add,” “ship,”
“cancel,” or “apply” - you identify the object that has the major responsibility for carrying it out, and make a class for it.

### Relationships between Classes
The most common relationships between classes are:
• Dependence (“uses–a”)
Thus, a class depends on another class if its methods use or manipulate objects of that class. But you want to minimize
the coupling between classes.
• Aggregation (“has–a”)
Containment means that objects of class **A** contain objects of class **B**.
• Inheritance (“is–a”)
If class **D** extends class **C**, class **D** inherits methods from class C but has more capabilities.

![uml](/info/Java_Core_Volume_I/info/media/objects/UML_diagram.PNG)

## Using Predefined Classes
### Objects and Object Variables
Constructors always have the same name as the class name.

### The LocalDate Class of the Java Library
The Date class represents the time by the number of milliseconds (positive or negative) from 00:00:00 UTC, January 1, 1970.
UTC is the Coordinated Universal Time, the scientific time standard which is the same as the more familiar GMT, or Greenwich
Mean Time.

### Mutator and Accessor Methods
A _mutator_ method - after invoking it, the state of the object will be changed. A method that only access objects without
modifying them are called _accessor_ method.

### Defining Your Own Classes
You can only have one public class in a source file. \
Start the program by giving the bytecode interpreter the name of the class that contains the `main` method of your program.
The bytecode interpreter starts running the code in the `main` method.

### First Steps with Constructors
Constructors:
- A constructor has the same name as the class.
- A class **can have more than one** constructor.
- A constructor can take zero, one, or more parameters.
- A constructor **has no return value**.
- A constructor is always called with the new operator.

### Declaring Local Variables with var
The `var` keyword can only be used with local variables inside methods. You must always declare the types of parameters
and fields.

### Implicit and Explicit Parameters

```java
employee1.raiseSalary(5);
```
The `raiseSalary` method has two parameters. The first parameter, called the _implicit_ parameter, is the **object** of
type Employee that appears before the method name. The second parameter, the number inside the parentheses after the
method name, is an _explicit_ parameter. (implicit parameter also called the _target_ or _receiver_). \
In every method, the keyword `this` refers to the implicit parameter.

### Benefits of Encapsulation
1. You can change the internal implementation without affecting any code. \
2. Mutator methods can perform error checking. Be careful not to write accessor methods that return references to mutable 
objects, if you need to return a reference to a mutable object, you should _clone_ it first.

### Class-Based Access Privileges
A method can access the _private_ data of **all objects of its class**.

### Private Methods
As long as the method is private - you can do whatever you like with it. If a method is public, you cannot simply drop
it because other code might rely on it.

### Final Instance Fields
_final_ - a field must be initialized when the object is constructed. Afterwards, the field may not be modified again.
The `final` is useful for fields whose type is primitive or an _immutable_ class. (A class is immutable if none of its
methods ever mutate its objects. For example, the String class is immutable.)

## Static Fields and Methods
### Static Fields
_static_ field is not present in the objects of the class. There is only a single copy of each static field.

### Static Constants
A _native_ method, it is not implemented in the Java programming language. Native methods can bypass the access control
mechanisms of the Java language.

### Static Methods
Static methods do not operate on objects, don’t have a _this_ parameter. It is legal to use an object to call a static
method, but not recommended.

### Factory Methods
```java
NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
NumberFormat percentFormatter = NumberFormat.getPercentInstance();
```
Use static factory methods that construct objects. Why?
- You can’t give names to constructors, but we want two different names to get the _currency_ instance and the _percent_
instance. 
- When you use a constructor, you can’t vary the type of the constructed object. But with factory you can provide different
types of instances, as long as they share some common parent or an interface.

### The main Method
The `main` method does not operate on any objects. In fact, when a program starts, there aren’t any objects yet. The 
static main method executes, and constructs the objects that the program needs.

## Method Parameters
How parameters can be passed?
- _call by value_ - the method gets just the value that the caller provides;
- _call by reference_ - the method gets the location of the variable that the caller provides;
  Thus, a method can modify the value stored in a variable passed by reference but not in one passed by value.

**Java always uses call by value**, the method gets a copy of all parameter values. The **method cannot modify** the
contents of **any parameter variables passed** to it. Also, object **references are passed by value**.

There are, however, two kinds of method parameters:
- Primitive types (numbers, boolean values)
- Object references
                        
## Object Construction
### Overloading
_overloading_ - occurs if several methods that have the **same name** but **different parameters**. The compiler must
sort out which method to call. The process of finding a match is called _overloading resolution_.
Java allows you to overload **any** method, (incl. constructor). Overloaded methods should have different *signature*
**name and parameter types**. The **return type** is **not part** of the method signature, it alone cannot be used
to distinguish overloaded methods. When calling a method, you specify the method name and arguments. The compiler uses
this information to determine which method to call. The return type is not considered during this process.

### Default Field Initialization
Difference between fields and local variables, you must always explicitly initialize local variables in a method.
But in a class, if you don’t initialize a field, it is automatically initialized to a default (0, false, or null).

### The Constructor with No Arguments
If a class has no constructors, then a no-argument constructor is provided for you. This constructor sets all the
instance fields to their default values.

### Explicit Field Initialization
Class **field assignments** are carried out **before the constructor executes**.

### Calling Another Constructor
If the first statement of a constructor has the form `this(. . .)`, then the constructor calls another constructor of
the same class.

### Initialization Blocks
You have already seen two ways to initialize an _instance field_:
- By setting a value in a constructor 
- By assigning a value in the declaration
Third mechanism in Java, called an _initialization block_. \
It's legal to do opposite, but you should always place initialization blocks after the field definitions. \
Static initialization occurs when the class is first loaded, once in a program run.
```java
class Employee
{
private static int nextId;
private int id;
private String name;
// object initialization block
{
    id = nextId;
    nextId++;
}
// static initialization block
static {
  nextId = generator.nextInt(10000);
}
public Employee(String name)
    ...
```

### Object Destruction and the finalize Method
Since Java does automatic garbage collection, Java does not support destructors. \
If a resource needs to be closed as soon as you have finished using it, supply a _close_ method that does the necessary
cleanup. \
If you can wait until the virtual machine exits, add a “shutdown hook” with the method **Runtime.addShutdownHook**. \
Also you can use the **Cleaner** class to register an action that is carried out when an object is no longer reachable. \
Do not use the _finalize_ method for cleanup, you don't know exactly when it will be called, and it is **deprecated**.

## Records
### The Record Concept
A _record_ is a special form of a class whose state is **immutable and readable** by the public.
```java
record Point(double x, double y) {
    private final double x;
    private final double y;
    
    Point(double x, double y){}
    
    public double x(){}
    public double y(){}
}
```
In the Java specification, the instance fields of a record are called its _components_. \
Record has a constructor with parameters, and accessor methods. \
In addition every record has three methods defined automatically: toString, equals, and hashCode. \
You can define your own methods, and redefine automatically provided, as long as you keep signature and return type. \
You can add a static field to the Record. \
You cannot add instance fields to a record, they are automatically _final_ \

Use a _record_ instead of a _class_ for **immutable** data that is completely represented by a set of variables.
Use a class if the data is mutable, or if the representation may evolve over time. Records are easier to read, more
efficient, and safer in concurrent programs.
                        
### Constructors: Canonical, Custom, and Compact
The automatically defined constructor that sets all instance fields is called the _canonical_ constructor. \
Compact, it's when you don't mention the parameters in the _records_ constructor.

## Packages
Java allows you to group classes in a collection called a _package_.

### Package Names
The main reason for using packages is to guarantee the uniqueness of class names.

### Class Importation
A class can use **all** classes **from its own** package and **all public** classes **from other** packages. \
You can access the public classes in another package in two ways. Use the fully qualified name or the `import` statement. \
Note that you can only use the `*` notation to import a single package. You cannot use import `java.*` or import `java.*.*`

### Static Imports
The `import` statement permits the importing of static methods and fields. \
You can use the static methods and fields of the _System_ class without the class name prefix. \
```java
import static java.lang.System.*;
out.println("Goodbye, World!"); // i.e., System.out
```

### Addition of a Class into a Package
```java
package com.horstmann.corejava;
public class Employee {}
```
If you don’t put a `package` statement in the source file, then the classes in that source file belong to the unnamed
package.
```shell
javac PackageTest.java                                       
```
The compiler search the Employee file com/horstmann/corejava/Employee.java and compiles it. \
The compiler does not check the directory structure when it compiles source files, but the virtual machine won’t find
the classes if the packages don’t match the directories.

### Package Access
If you don’t specify either _public_ or _private_, the feature (class, method, or variable) can be accessed by all 
methods in the same package.

### The Class Path