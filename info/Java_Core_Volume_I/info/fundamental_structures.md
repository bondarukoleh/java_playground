### Preface
In late 1995, the Java programming language burst onto the Internet scene. \
Java is Simple, Object-Oriented, Distributed, Robust, Secure, Architecture-Neutral, Portable, Interpreted,
High-Performance,  Multithreaded,  Dynamic.

Java Applets - The idea here is simple: Users will download Java bytecodes from the Internet and run them on their own
machines. Java programs that work on web pages are called applets.

In the early days of Java, the language was interpreted. Nowadays, the Java virtual machine uses a just-in-time compiler.
JIT - helping to execute a Java bytecode, JIT compiles bytecode in machine instructions, this increases execution speed.
But _JIT_ is not _javac_ - those have two different functions.

## Java Programming environment
```shell
javac Welcome.java
java Welcome
```
The `javac` program is the Java compiler. It compiles the file Welcome.java into the file Welcome.class.
The `java` program launches the Java virtual machine. It executes the bytecodes that the compiler placed in the **class** file.

The `JShell` program provides a “read-evaluate-print loop,” or REPL. To start JShell, simply type `jshell` in a terminal.
Tab completion is a useful feature.

# Fundamental Programming Structures in Java

The Java virtual machine always starts execution with the code in the main method in the class you indicate.

## Data types
Java is a _strongly typed language_. There are **eight** primitive types in Java. \
byte, int, short, long, float, double, char, boolean. 

### Integer Types
![integer_types](/info/Java_Core_Volume_I/info/media/fundamentals/Integer_types.PNG)

In most situations, the `int` type is the most practical. `long` integer numbers have a suffix **L** or **l** (e.g. 4000000000L).
You can add underscores to number literals, the underscores are for human eyes only.

### Floating-Point Types
![floating_types](/info/Java_Core_Volume_I/info/media/fundamentals/Floating_types.PNG)

`float` have a suffix **F** or **f** (e.g. 3.14F). Floatingpoint numbers without an F suffix considered to be `double`.
You can optionally supply the **D** or **d** suffix.

### The char Type
Literal values of type `char` are enclosed in single quotes. For example, `'A'`. \
Unicode escape sequences are processed before the code is parsed. For example, `string a = "\u0022+\u0022"` is
not `string a = "**"+"**"`. Instead, the \u0022 are converted into " before parsing, yielding `""+""`, or an empty string.
`string a = ""`

#### Unicode and the char Type
Before Unicode, there were many different standards: ASCII in the United States, ISO 8859–1 for Western European languages,
KOI-8 for Russian, GB18030 and BIG-5 for Chinese, and so on.

In Java, the `char` type describes a _code unit_ in the **UTF-16** encoding.

### The boolean Type
You cannot convert between integers and boolean values. `if (x = 0)` doesn't compile.

## Variables and Constants
Starting with Java 10, you do not need to declare the types of local variables if they can be inferred from the initial
value. Simply use the keyword `var`

### Constants
Use the keyword `final` to denote a constant, a class constant - `static final`

## Operators
Methods in the `Math` class use the routines in the computer’s floatingpoint unit for fastest performance. If completely
predictable results are more important than performance, use the `StrictMath` class instead. It guarantees identical
results on all platforms.

The `Math` class provides quietly return wrong results when a computation overflows. If you call
`Math.multiplyExact(1000000000, 3)` instead, an exception is generated. There are also some other exact methods.

### Conversions between Numeric Types
Legal conversions between numeric types
![numeric_conversions](/info/Java_Core_Volume_I/info/media/fundamentals/Numeric_conversions.PNG)

1. If either of the operands is of type `double`, the other one will be converted to a `double`.
2. Otherwise, if either of the operands is of type `float` other converted to `float`
3. Otherwise, some is `long`, another is converted to `long`
4. Otherwise, both operands will be converted to an `int`.

### Casts
When you want to consider a `double` as an integer, some information may be lost. Conversions in which loss of
information is possible are done by means of _casts_.

### Assignment
In Java, an assignment is an _expression_.
```java
int x = 1;
int y = x += 4;
// y is 5
```

### Increment and Decrement Operators
```java
int m = 7;
int n = 7;
int a = 2 * ++m; // now a is 16, m is 8
int b = 2 * n++; // now b is 14, n is 8
```

### Parentheses and Operator Hierarchy
If no parentheses are used, operations are performed in the hierarchical order indicated.
For example, `&&` has a higher precedence than `||`, so the expression
```java
a && b || c
// means
(a && b) || c
```

![operator_hierarchy](/info/Java_Core_Volume_I/info/media/fundamentals/Operator_hierarchy.PNG)

## Strings
Java does not have a built-in string type, instead the standard Java library contains a predefined class **String**.
Each quoted string is an instance of the String class. \
_Strings Are Immutable_, the String class gives **no** methods that let you change a character in an existing string. \
Immutable strings have one great advantage: The compiler can arrange that strings are _shared_.

### Testing Strings for Equality
To test whether two strings are equal, use the `equals` method. `s.equals(t)` \
You could use the `==` operator, but only string literals are shared, not strings that are the result of operations
like `+` or `substring`. Never use == to compare strings. \
If you need to test that a string is neither `null` nor empty. Then use `if (str != null && str.length() != 0)`


### Code Points and Code Units
The `length` method yields the number of code units required for a given string in the UTF-16 encoding, not number of `chars`

### Building Strings
Every time you concatenate strings, a new String object is constructed. **StringBuilder** class avoids this problem.
```java
StringBuilder builder = new StringBuilder();
builder.append(ch); // appends a single character
builder.append(str); // appends a string
String completedString = builder.toString();
```

### Text Blocks
The white space before the closing """ is significant.
```java
String greeting = """
Hello
World
""";
// the same as
String greeting = "Hello\nWorld\n";
```

## Input and Output
To read console input, you first construct a Scanner that is attached to System.in:
```java
Scanner in = new Scanner(System.in);
System.out.print("What is your name? ");
String name = in.nextLine(); /* to read one word only .next() */
System.out.print("How old are you? ");
int age = in.nextInt();
System.out.println("Hello, " + name + ". Next year, you'll be " + (age + 1));
```

### Formatting Output
```java
System.out.printf("Hello, %s. Next year, you'll be %d", name, age);
```
![operator_hierarchy](/info/Java_Core_Volume_I/info/media/fundamentals/Formatting_output.PNG)

### File Input and Output
To read from a file, construct a Scanner object like this:
```java
Scanner in = new Scanner(Path.of("myfile.txt"), StandardCharsets.UTF_8);
```
To write to a file, construct a PrintWriter object:
```java
PrintWriter out = new PrintWriter("myfile.txt", StandardCharsets.UTF_8);
```

## Control Flow
A **block**, or compound _statement_, consists of a number of Java statements, surrounded by a pair of braces. Blocks define
the _scope_ of your variables. A block can be _nested_ inside another block. \
You may not declare identically named local variables in two nested blocks, _shadowing_ is not allowed.

Conditionals, loops, all works the same.

To use statements in a branch of a `switch` expression without fallthrough, you must use braces and `yield` or `break` without braces.
```java
switch (something) {
    case "Spring" ->
        {
            System.out.println("spring time!");
            yield 6;
        }    
}
// or
switch (something) {
    case "Summer", "Winter" -> 6;
}
// or
switch (something) {
    case 1:
        // ...
        break;
}
```

Java also offers a _labeled break_.

## Big Numbers
If the precision of the basic integer and floating-point types is not sufficient, you can use **BigInteger** and **BigDecimal**
from _java.math_. Unfortunately, you cannot use the familiar operators such as `+` and `*` to combine big numbers.
```java
BigInteger c = a.add(b); // c = a + b
BigInteger d = c.multiply(b.add(BigInteger.valueOf(2))); // d = c * (b + 2)
```
## Arrays
```java
int[] a;
// or
int a[];
// or
int[] a = new int[100];
// or
var a = new int[100];
// or
int[] smallPrimes = { 2, 3, 5, 7, 11, 13 };
```
Once you create an array, you cannot change its length. It is legal to have arrays of length 0. \
When you create an array of numbers, all elements are initialized with `0`. Arrays of boolean with `false`. Arrays of
objects with `null`, that includes array of Strings.

### The “for each” Loop
```java
for (int element : a)
    System.out.println(element);
```

### Array Copying
use the `copyOf` method in the Arrays class.
```java
int[] copiedLuckyNumbers = Arrays.copyOf(luckyNumbers, luckyNumbers.length);
```