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
So the path to the class must match the package name. Class files can also be stored in a JAR (Java archive) file. A JAR
file contains multiple class files and subdirectories in a compressed format, saving space and improving performance. \
To share classes among programs, you need to do the following:
1. Place your class files inside a directory — e.g. /home/user/classdir. Note that this directory is the base directory
for the package tree. If you add the class _com.horstmann.corejava.Employee_, then the _Employee.class_ file must be located
in the subdirectory _/home/user/classdir/com/horstmann/corejava_. 
2. Place any JAR files inside a directory — for example _/home/user/archives_. 
3. Set the _class path_. The class path is the collection of all locations that can contain class files.

The _javac_ compiler always looks for files in the current directory, but the _java virtual machine_ launcher only looks
into the current directory if the _“.”_ directory is on the class path. If you have no class path set, it’s not a problem
— the default class path consists of the “.” directory. But if you have **set the class path without “.” included**
directory, your programs **will compile** without error, but they **won’t run**.

The class path lists all directories and archive files that are _starting points_ for locating classes
```shell
/home/user/classdir:.:/home/user/archives/archive.jar
#[ this is one path]:[current dir]:[another path]
```
Suppose the _virtual machine_ searches for the class file of the com.horstmann.corejava.Employee class. It first looks
in the Java API classes. It won’t find the class file there, so it turns to the _class path_. 
- /home/user/classdir/com/horstmann/corejava/Employee.class 
- com/horstmann/corejava/Employee.class starting from the current directory 
- com/horstmann/corejava/Employee.class inside /home/user/archives/archive.jar

The compiler has to locate files without you specified their package, it consults all `import` directives as possible
sources for the class. e.g. `import java.util.*; import com.horstmann.corejava.*;` in source file that also refers to a
class Employee, The compiler then tries to find java.lang.Employee (java.lang package imported by default),
java.util.Employee, com.horstmann.corejava.Employee, and Employee in the current package. It searches for each of these
classes in all of the locations of the class path. It is a compile-time error if more than one class is found. The order
of import doesn't matter. \
You can import only public classes from other packages. A source file can only contain one public class, and the names of
the file and the public class must match. \
You can import nonpublic classes from the current package the compiler searches all source files of the current package
to see which one defines the class.

### Setting the Class Path
Using the _-classpath_ option is the preferred approach for setting the class path. Another is the **CLASSPATH** 
environment variable.
```shell
java -classpath c:\classdir;.;c:\archives\archive.jar MyProg
```

## JAR Files
When you package your application, you want to give your users a single file, not a directory structure filled with 
class files. Java Archive (JAR) files were designed for this purpose.

### Creating JAR files
Use the jar tool to make JAR files.

### The Manifest
Each JAR file contains a _manifest_ file that describes special features of the archive. Complex manifests can have many
entries, grouped into sections. The first section is called the _main section_. Subsequent entries can specify properties
of named entities such as individual files, packages, or URLs. Those entries must begin with a _Name_ entry. Sections
are separated by blank lines. The last line in the manifest must end with a newline character.
```text
Manifest-Version: 1.0
_lines describing this archive_
Name: Woozle.class
_lines describing this file_
Name: com/mycompany/mypkg/
_lines describing this package_
Main-Class: com.mycompany.mypkg.MainAppClass
```

### Executable JAR Files
_e_ option of the _jar_ command to specify the entry point of your program. Alternatively, you can specify the main
class of your program in the manifest. Users can simply start the program as: 
```shell
java -jar MyProgram.jar

```
You can use third-party wrapper utilities that turn JAR files into Windows executables. A wrapper is a Windows program
with the familiar _.exe_ extension that locates and launches the JVM or tells the user what to do when no JVM is found.

### Multi-Release JAR Files
In package if some previously accessible internal APIs are no longer available library providers distribute different 
code for different Java versions. Java 9 introduces multi-release JARs for this purpose.

Multi-release JARs are not intended for different versions of a program or library. The public API of all classes should
stay the same for the same version of the library. The sole purpose of multi-release JARs is to enable a particular
version of your program or library to work with multiple JDK releases. When you use multi-release JARs, you're
essentially providing different versions of certain classes or resources within your JAR, each tailored for specific
JDK versions. If you add functionality or change an API, you should provide a new version of the JAR instead.

## Documentation Comments
### Comment Insertion
Each `/** . . . */` documentation comment contains free-form text followed by tags. A tag starts with an `@`, such as
`@since` or `@param`. The first sentence of the free-form text should be a _summary statement_.

In the free-form text, you can use HTML modifiers such as `<em>. . .</em>` for emphasis, <strong>, <ul>/<li>, <img . . ./>
monospaced code - {@code . . . }.

### Class Comments
The class comment must be placed _after_ any `import` statements, **directly before** the class definition.
Same goes for method comments. You only need to document public fields — generally that means _static constants_.
```java
/**
* A {@code Card} object represents a playing card, such as "Queen of Hearts". A card has a suit (Diamond, Heart, Spade
* or Club) and a value (1 = Ace, 2 . . . 10, 11 = Jack, 12 = Queen, 13 = King)
*/
public class Card
{
  /**
   * The "Hearts" card suit
   */
  public static final int HEARTS = 1;
  
  /**
   * Raises the salary of an employee.
   * @param byPercent the percentage by which to raise the salary (e.g., 10 means 10%)
   * @return the amount of the raise
   */
  public double raiseSalary(double byPercent)
  {
    ...
  }
}
```

### Class Design Hints
1. Always keep data private.
2. Always initialize data.
3. Don’t use too many basic types in a class. The idea is to replace multiple related uses of basic types with other 
classes.
4. Not all fields need individual field accessors and mutators. You need to get and set an employee’s salary, but not
the hiring date.
5. Break up classes that have too many responsibilities.
6. Make the names of your classes and methods reflect their responsibilities.
7. Prefer immutable classes. If there are threads it is a good idea to make classes immutable when you can.
