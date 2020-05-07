Java 1.2-1.4 -> Java 2, Java 1.5 -> Java 5.0 (Tiger), real versions and marketing names.

Compile a code:
```shell script
javac ./src/Test.java
```

jar and war
These files are simply zipped files using the java jar tool. These files are created for different purposes.
Here is the description of these files:
.jar files: The .jar (java archive, pkzip format) files contain libraries, resources and accessories files like
property files. In the jar file, you can include a simple text file formatted as something called a manifest, that
defines which class in that jar holds the main() method that should run.

.war files: The war file contains the web application that can be deployed on any servlet/jsp container.
The .war file contains jsp, html, javascript and other files necessary for the development of web applications.

The Way Java Works
1. Source code (.java files).
2. Compile.
3. Output - The compiler creates a new document, coded into Java bytecode. (.class files)
Any device capable of running Java will be able to interpret/translate this file into something it can run.
The compiled bytecode is platform-independent.
4. Virtual Java machine (implemented in software) running inside their electronic gadgets. The virtual machine reads
and runs the bytecode. 

When the JVM starts running, it looks for the class you give it at the command line.
```shell script
java ./My_Class.java
```
Then it starts looking for a specially-written method main that looks exactly like:
```java
public class My_Class {
    public static void main (String[] args) {
        // your code goes here
    }
}
```
Next, the JVM runs everything between the curly braces {  } of your main method. Every Java application has to have at
least one class, and at least one main method (not one main per class; just one main per application).

In Java, everything goes in a class.
The main() method is where your program starts running.

The two uses of main:
■  to test your real class
■  to launch/start your Java application

Variables come in two flavors: primitive and reference.
instance variables - object state;
local variables - variables declared within a method;
arguments - values sent to a method by the calling code;
return types - values sent back to the caller of the method; 
Primitives hold - fundamental values including integers, booleans, and floating point numbers.
References - references to objects.

Remember primitives passed by value, mean they passed by a copy, don't forget that object reference - also a primitive.

##### Primitive Types:
##### boolean char byte short int long float double

Type       Bit Depth    Value Range
boolean and char
boolean   (NM-specific) true or false
char        16 bits     O to 65535
numeric (all are signed)
integer
byte        8 bits      -128 to 127
short      16 bits      -32768 to 32767
int        32 bits      -2147483648 to 2147483647
long       64 bits      -huge to huge

floating point
float       32 bits    varies
double      64 bits    varies
Primitive declarations with assignments:
int x;
x = 234;
int z = x;
byte b = 89;
boolean isFun = true;
double d = 3456.98;
char c = 'f';
boolean isPunkRock;
isPunkRock = false;
boolean powerOn;
powerOn = isFun;
long big = 3456789;
float f = 32.5f; Gotta have ‘f’ with a float, Java thinks anything with a floating point is a double, unless you use ‘f’.

A primitive byte: x = 7; The bits representing 7 are go into the variable. (actual value will be placed - 00000111).
A reference type: Dog myDog = new Dog(); The bits representing a way to get to the Dog object go into the variable.

final variable unlike const in js cannot be reassigned, even for a new object.

`instance variable` - declared in class, gets default value.
`local variable` - declared in method, and doesn't get default value, need to be initialized before used.
A method uses `parameters`. Parameter - same as local variable with type and name, (it's declared in argument list of
the method), and doesn't need to e initiated, since a compiler won't let you call the method without the arguments.
A caller passes `arguments`. Argument - value. 
You can return anything that can be implicitly promoted as `returned type`. You must use an explicit cast when the
declared type is smaller than what you're trying to return.

Encapsulation
About hide the data and make some checks (set the appropriate boundary values for the field, or security check)
or changes (round, set the nearest acceptable value, make some action) before saving it in an instance, or make some
checks or changes before you return the data from the instance.     

Point of getters setters, is an ability to change the behaviour of the instance without changes the code what interacts
with it, so the API of the instance will remain the same while behaviour will change. 

Default values for variables:
Number primitives (and Char)   0/0.0
booleans                       false
references                     null

##### Comparing variables.  
== used only for comparing bits in variables, they either equal or not.
.equals() used to compare objects.

```java
int a = 3;
byte b = 3;
a == b; // true
```
a == b looks at the bits in a and b and returns true if the bit pattern is the same. It doesn't care about the size of
the variable, so all the extra zeroes on the left end don't matter.

Creation of the class:
□   Figure out what the class is supposed to do.
□   List the instance variables and methods.
□   Write pseudocode for the methods. (You'll see this in just a moment.)
□   Write test code for the methods.
□   Implement the class.
□   Test the methods.
□   Debug and reimplement as needed.
□   Express gratitude that we don't have to test our so-called learning experience app on actual live users.

Cast
To force the compiler to jam the value of a bigger primitive variable into a smaller one, you can use the cast operator.
```java
long l = 42;
short s = (short) l;
```
If the primitive was bigger that casting one - it chops the value until it fits casting type, but be aware - it can 
produce weird values
```java
long l = 4200000;
short s = (short) l; // 5696

// get int from float
float f = 3.14f;
int x = (int) f;   //  3
```

##### Collection
ArrayList
add(Object elem) - add object parameter
remove(Object elem) - remove object parameter
remove(int index) - remove object at an index
contains(Object elem) - return true if there's a match for the object parameter 
isEmpty() - return true if list has no elements
indexOf(Object elem) - returns either index of elem in list or -1
size() - returns list length
get(int index) returns object at index parameter

ArrayList comparing to Array:
- doesn't force to set a size
- dot based syntax, without brackets
- doesn't force to set a specific place for an added element (index)
- parametrized Arraylist<Type>

ArrayList holds objects and not primitives, the compiler will automatically “wrap” (and “unwrap” when you take it out)
a primitive into an Object, and place that object in the ArrayList instead of the primitive.

###### Packages 
Has 3 main reasons to use:
1. Organization. Help the overall organization of a project or library, they're all grouped into packages for specific
kinds of functionality (like GUI, or data structures, or database stuff, etc.)
2. Scoping. Give you a name-scoping, to help prevent name collisions. If you have a class named Set and someone else
(including the Java API) has a class named Set, you need some way to tell the JVM which Set class you're trying to use.
3. Security. Provide a level of security, because you can restrict the code you write so that only other classes in the
same package can access it.

In the Java API, classes are grouped into packages.
Collections classes in java.utils package.
System, Math, String in java.lang. It's preimported, and you don't have to import in explicitly.

To use some class you need to know the full name of it.
java.utils.ArrayList - full name, package + class name.

You can import from the top, or write a full name
```java
import java.util.*; 
// or:
import java.util.HashMap;
// or:
java.util.HashMap<String, Object> h = new java.util.HashMap<String, Object>();
```
import doesn't make your class bigger, it's the way to tell java what class you want to use

