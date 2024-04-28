# Fundamental Programming Structures in Java

The Java virtual machine always starts execution with the code in the main method in the class you indicate.

## Data types
Java is a _strongly typed language_. There are **eight** primitive types in Java. \
byte, int, short, long, float, double, char, boolean. 

### Integer Types
![integer_types](/info/Java_Core_Volume_I/info/media/Integer_types.PNG)

In most situations, the `int` type is the most practical. `long` integer numbers have a suffix **L** or **l** (e.g. 4000000000L).
You can add underscores to number literals, the underscores are for human eyes only.

### Floating-Point Types
![floating_types](/info/Java_Core_Volume_I/info/media/Floating_types.PNG)

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
![numeric_conversions](/info/Java_Core_Volume_I/info/media/Numeric_conversions.PNG)

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




