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

##### Encapsulation
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


###### Inheritance
JVM same as V8 starts to looking for implementation of involved method or variable first in the lowest in inheritance
tree class. If the JVM doesn't find the method in the lowest class, it starts walking back up the inheritance hierarchy
until it finds a match.
Inheritance lets you guarantee that all classes grouped under a certain supertype have all inheritable the methods that
the supertype has. In other words, you define a common protocol for a set of classes related through inheritance.

IS-A HAS-A test
If class B extends class A, class B IS-A class A must make seance. If class C extends class B, class C passes the IS-A
test for both B and A. Animal -> Canidae -> Wolf. Wolf IS-A Canidae, Wolf IS-A Animal. Works in one direction.
But when classes are related, not inherited from each other, they have a HAS-A relation. 
Wolf IS-A WolfPride - no, WolfPride IS-A Wolf, nah, but WolfPride HAS-A wolf -> yes, means WolfPride has a reference to
wolf.

Access Modifier 	within class	within package	  outside package by subclass only	   outside package
Private	                  Y	              N	                        N	                       N
Default	                  Y	              Y	                        N                          N
Protected	              Y	              Y	                        Y                          N
Public	                  Y	              Y	                        Y                          Y

DO use inheritance when one class is a more specific type of superclass.
DO use inheritance when behavior that should be shared among multiple classes of the same general type.
DO NOT use inheritance so child can reuse code from another class, if the relationship between the superclass and
    subclass violate either of the above two rules.
DO NOT use inheritance if the subclass and superclass do not pass the IS-A test. Always ask yourself subclass IS-A more
    specific type of the superclass.

Methods can be overridden with same signature but variables - cannot. Means, if you put in parent class type variable
child class type instance it will have behaviour of the child but state of the parent. Parent field hides re-declared
field in the child class. This has several reasons:
1. Because overriding variables can break code in the superclass. e.g. if an override changes the variable's type,
that is likely to change the behavior of methods declared in the parent class that used the original variable.
2. If fields that were overridden were not private, it would be even worse. That would break the Liskov Substitutability
Principle (LSP) in a pretty fundamental way. That removes the basis for polymorphism.
3. On the flipside, overriding fields would not achieve anything that cannot be done better in other ways. e.g. a good
design declares all instance variables as private and provides getters/setters for them as required.
The getters/setters can be overridden, and the parent class can "protect" itself against undesirable overrides by
using the private fields directly, or declaring the getters/setters final.

Some cases when you cannot inherit from class:
1. default, or package-private, non-public class can be subclassed only by in the same package. It's not visible outside. 
2. final class means that it's the end of the inheritance line. Nobody, ever, can extend a final class.
    This can be done with security reasons, when you want your method should have one behaviour, because methods cannot
    be overridden. Method can be final too, means it cannot be overriden.
3. If a class has only private constructors, it can't be subclassed.

##### Polymorphism
When you define a supertype for a group of classes, any subclass of that supertype can be substituted where the 
supertype is expected.
Let's recall object declaration and assignment:
Dog myDog = new Dog();
```Dog myDog``` - Tells the JVM to allocate space for a reference variable. It is, forever, of type Dog, Dog contract.
```new Dog()``` - Tells the JVM to allocate space for a new Dog object on the garbage collectible heap.
```=``` - Assigns Dog object to the reference variable myDog. Links the Dog contract reference to the object.

Dog myDog = new Dog() - reference type, and the assigned object type is the same here. But with polymorphism, the
reference type can be a superclass of the actual object type. Anything that extends the declared reference variable
type can be assigned to the reference variable.

```java
Animal[] animals = new Animal[2];
animals[0] = new Dog();
animals[1] = new Cat();

for(Animal animal : animals){
    animal.eat();
    animal.sleep();
};
```

You can have polymorphic arguments and return types.
```java
class A {
    Animal returnRandomAnimal() {
        return [new Dog(), new Cat()][Randomnum];
    }

    void takeRandomAnimal(Animal animal){ // can be a dog or a cat
        animal.eat(); 
        animal.sleep();
    }
}
```
This thing gives less code change in the future, if the code will depend on more generic types - it should be less
changed in the future, e.g. when you create a new subclass.

Even more flexible polymorphism can be if we declare some interface reference variable, that means doesn't matter where
from class could be, or what it inherits from, as long as he implements a declared interface - you can put instance of it
to the variable.   

###### override
When you override a method from a superclass, you're agreeing to fulfill the contract. The contract is method signature,
these arguments and this return type. In polymorphism the compiler looks at the variable reference type to decide
whether you can call a particular method on that reference. In code the compiler cares only if var type has the method
you're invoking. But at runtime, the JVM looks not at the variable reference type but at the actual object on the heap.
So if the compiler has already approved the method call, the only way it can work is if the overriding method has the
same arguments and return types.

If you want to change signature of the method - you need to overload it in the same basic type variable you are going
to use class, or overload it in child but change the type of variable.
Overloading is nothing more than having two methods with the same name but different argument lists. They are not 
connected is any way, you can do everything you want with that completely new method.
There's no polymorphism involved with overloaded methods! But you'll should remember via overloading - you cannot
change only return type - the compiler will assume you’re trying to override the method. And that won’t be legal
unless the return type is a subtype of the return type declared in the superclass.

Also, when you are overriding  - you cannot narrow down accessible modifiers, only make them more accessible.
```java
// class Parent
void someMethod() {}
String someMethod(int arg) {} // overloading in class

// class Child extends Prent
boolean someMethod(String arg) {} // overloading in child

Parent parent = new Child(); 
obj.someMethod(); 
obj.someMethod(123);

Child child = new Child();
child.someMethod("abc");
```

###### Abstract class
Some class should not be instantiated. Like Animal -> Canidae -> Wolf. We can imagine Wolf, but what is An animal
object what state he has, or what skin color should canidae has? Here is where ```abstract``` class comes into play.
You can use that abstract type as a reference type, as a polymorphic argument or return type, or as polymorphic array.

When you're thinking about the design - it's up to you to decide what should be concrete or what should be abstract.
Concrete classes are those that are specific enough to be instantiated. 

- Abstract class cannot be instantiated.
- Abstract method can only be in abstract class.
- Abstract method cannot have body. (they need to declare a contract in superclasses, so you can use polymorphism)
- If a concrete class extends an abstract class, concrete must implement (override) all the abstract methods of abstract
parent, or it has to be declared abstract as well. Abstract child can implement abstract parent's methods, so concrete
child doesn't have to do it.

```java
Animal[] animals = new Animal[5] // this is not making the new animal object, it's making new Animal array with length 5 
```

Every class in Java extends class Object. Any class that doesn’t explicitly extend another class, implicitly extends
Object.

The compiler decides whether you can call a method based on the reference type, not the actual object type.
Some Object methods:
equals()
getClass()
hashCode()
toString()

When you put an object in an ArrayList<Object>, you can treat it only as an Object, regardless of the type it was when
you put it in. Don't type variables with such generic type, it's restrict to do with it anything.

To make it behave like object you want it to be - you can cast it. But, better make sure about type with instanceof.
```java
ArrayList<Object> genericDogs = new ArrayList<>();
genericDogs.add(new Dog);
Dog dog = (Dog) genericDogs.get(0); // here you need to cast it

ArrayList<Dog> dogs = new ArrayList<>(); // ArrayList<Dog> compiler cast every object to Dog for you 
dogs.add(new Dog);
Dog dog = dogs.get(0); // here you don't need to cast, compiler is sure that everything is a dog there
```

###### Interface
Interface - a 100% abstract class.
Concrete class implements the interface, should override all methods from it.
Define an interface. Interface methods are implicitly public and abstract, so typing in ‘public’ and ‘abstract’ is
optional.
You can implement multiple interfaces which decides what roles your object can play.
```java
public interface ISome {
    public abstract void someMethod(); // no need to type "public abstract"
}
public class Some implements ISome {}
```

How do you know whether to make a ```class, a subclass, an abstract class, or an interface```?
- Make a class that doesn’t extend anything when your new class doesn’t pass the IS-A test for any other type.
- Make a subclass only when you need to make a more specific version of a class and need to override or add new behaviors.
- Use an abstract class when you want to define a template for a group of subclasses, and you have at least some
 implementation code that all subclasses could use, also, to guarantee that no one will make object of that type.
- Use an interface when you want to define a role that other classes can play, regardless of where those classes are
 in the inheritance tree.


An abstract class can have both abstract and non-abstract methods - interface only abstract
All abstract methods must be implemented in the first concrete subclass in the inheritance tree.
A reference variable of type Object can’t be assigned to any other reference type without a cast.

##### Life of the object
In java, we care about two areas of memory - heap (where the objects live) and stack (where method invocations and local
variables live)
When JVM starts up, it uses a chunk of memory from underlying OS and uses it to run the java programs.
All objects live in garbage-collectible heap. Variables depend on their type:
local vars (method parameters included), also known as a stack vars live on a stack, temporary, as long as method is 
invoked, till the closing curly brace is reached, instance - on a heap, as long as object lives.

When method is called, it's "frame" pushed onto the stack, it holds the state of the method, executing line of code, 
and executing right now method values of local vars (method variables, and parameters). If local var - is reference,
only primitive reference stored on the stack, object that var is pointing to - is on the heap.
Method onto the stack - is executing right now.

When you create an object, java makes space on the heap for it. Space that will be created for it - should be enough to
fit all instance variables of this object. If instance vars primitives, java will create space based on their type
int - 32 bits, long 64 bits etc. If instance var is reference type - inside instance stored only reference to object,  
object is on the heap.
Any object is inherited from Object, so when you crete an instance, remember that all things from inherited class
(Object, or any other) will be also created for this instance, object on the heap will be basically one, but it will
contain all variables from inherited classes.

```java
class CellPhone {
    public Antenna antenna = new Antenna(); // in cellPhone object stored reference antenna object, not the object itself    
}
```
###### constructor
has the code that runs when you instantiate an object, when you say new on a class type.
Every class has an implicit constructor. It named same as class and doesn't have a return type.
Constructor called before class is instantiated.

If you want to create object from your class in different ways - best solution instead of if/else in constructors, to 
have an overloaded constructor. Overloaded constructors must have different args list.
If you put an any constructor in your class, the compiler will not build the default constructor.

```java
class Some {
public Some(){};
public Some(int arg){};
public Some(int arg, String str){};
// here you can new Some() or new Some(1) or new Some(1, "abc");
};
```