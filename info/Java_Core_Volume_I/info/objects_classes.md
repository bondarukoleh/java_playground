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
• A constructor has the same name as the class.
• A class **can have more than one** constructor.
• A constructor can take zero, one, or more parameters.
• A constructor **has no return value**.
• A constructor is always called with the new operator.

### Declaring Local Variables with var
The `var` keyword can only be used with local variables inside methods. You must always declare the types of parameters
and fields.

### Implicit and Explicit Parameters
