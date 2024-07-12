# Scripting, Compiling, and Annotation Processing
 Three techniques for processing code. \
 The scripting API lets you invoke code in a scripting language such as JavaScript or Groovy. \
 You can use the compiler API when you want to compile Java code inside your application. \
 Annotation processors operate on Java source or class files that contain annotations.
 
## Scripting for the Java Platform
The scripting API enables you to invoke scripts written in JavaScript, Groovy, Ruby, and even exotic languages such as
Scheme and Haskell, from a Java program.

### Getting a Scripting Engine
A scripting engine is a library that can execute scripts in a particular language. When the virtual machine starts, it
discovers the available scripting engines.

Usually, you know which engine you need, and you can simply request needed engine by name, MIME type, or extension.
```java
var manager = new ScriptEngineManager();
ScriptEngine engine = manager.getEngineByName("javascript");
```

### Script Evaluation and Bindings
Once you have an engine, you can call a script simply by invoking
```java
Object result = engine.eval(scriptString);
```

If the script is stored in a file, open a Reader and call
```java
Object result = engine.eval(reader);
```

You can invoke multiple scripts on the same engine. If one script defines variables, functions, or classes, most
scripting engines retain the definitions for later use.
```java
engine.eval("n = 1728");
Object result = engine.eval("n + 1");
```
will return 1729.

Instead of adding bindings to the engine or global scope, you can collect them in an object of type Bindings and pass it
to the eval method:
```java
Bindings scope = engine.createBindings();
scope.put("b", new JButton());
engine.eval(scriptString, scope);
```

### Calling Scripting Functions and Methods
You can invoke a function in the scripting language without having to evaluate the actual script code.
The script engines must implement the Invocable interface.
```java
// Define greet function in JavaScript
engine.eval("function greet(how, whom) { return how + ', ' + whom + '!' }");
// Call the function with arguments "Hello", "World"
result = ((Invocable) engine).invokeFunction("greet", "Hello", "World");
```

You can go a step further and ask the scripting engine to implement a Java interface.

### Compiling a Script
Some scripting engines can compile scripting code into an intermediate form for efficient execution. Those engines
implement the Compilable interface. The following example shows how to compile and evaluate code contained in a script
file:
```java
var reader = new FileReader("myscript.js");
CompiledScript script = null;
if (engine implements Compilable)
script = ((Compilable) engine).compile(reader);
```
Once the script is compiled, you can execute it. The following code executes the compiled script if compilation was
successful, or the original script if the engine didn’t support compilation
```java
if (script != null) {
  script.eval();    
} else {
  engine.eval(reader);    
}
```

## The Compiler API
### Invoking the Compiler

It is very easy to invoke the compiler. Here is a sample call:
```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
OutputStream outStream = . . .;
OutputStream errStream = . . .;
int result = compiler.run(null, outStream, errStream, "-sourcepath", "src", "Test.java");
```
A result value of 0 indicates successful compilation.

### Launching a Compilation Task
You can have more control over the compilation process with a _CompilationTask_ object.

## Using Annotations
Annotations are tags that you insert into your source code so that some tool can process them. The tools can operate on
the source level, or they can process class files into which the compiler has placed annotations.

To benefit from annotations, you need to select a processing tool. Use annotations that your processing tool understands,
then apply the processing tool to your code.

### An Introduction into Annotations
```java
public class MyClass {
    . . .
    @Test public void checkRandomInsertions()
}
```
In Java, an annotation is used like a modifier and is placed before the annotated item _without a semicolon_.
(A modifier is a keyword such as _public_ or _static_)

By itself, the `@Test` annotation does not do anything. It needs a tool to be useful. For example, the JUnit testing
tool calls all methods that are labeled @Test when testing a class.

Annotations can be defined to have elements, such as
```java
@Test(timeout="10000")
```
These elements can be processed by the tools that read the annotations.

Besides methods, you can annotate classes, fields, and local variables an annotation can be anywhere you could put a
modifier such as public or static. In addition, you can annotate packages, parameter variables, type parameters, and
type uses.

Each annotation must be defined by an annotation interface. The methods of the interface correspond to the elements of
the annotation. For example, the JUnit Test annotation is defined by the following interface:
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    long timeout() default 0L;
    . . .
}
```
The `@interface` declaration creates an actual Java interface. Tools that process annotations receive objects that
implement the annotation interface. A tool would call the timeout method to retrieve the timeout element of a particular
Test annotation.

The Target and Retention annotations are meta-annotations. They annotate the Test annotation, marking it as an annotation
that can be applied to methods only and is retained when the class file is loaded into the virtual machine.

### An Example: Annotating Event Handlers
Of course, the annotations don’t do anything by themselves. They sit in the source file. The compiler places them in the
class file, and the virtual machine loads them. We now need a mechanism to analyze them and install action listeners.
That is the job of the _ActionListenerInstaller_ class.

Annotations could be processed at runtime. It is also possible to process them at the source level: A source code
generator would then produce the code for adding the listeners. Alternatively, the annotations can be processed at the
bytecode level: A bytecode editor could inject the calls to addActionListener into the frame constructor. This sounds
complex, but libraries are available to make this task relatively straightforward.

## Annotation Syntax
### Annotation Interfaces
An annotation is defined by an annotation interface:
```java
modifiers @interface AnnotationName {
    elementDeclaration1
    elementDeclaration2
    . . .
}
```
Each element declaration has the form
```java
type elementName();
// or
type elementName() default value;
```
All annotation interfaces implicitly extend the _java.lang.annotation.Annotation_ interface. \
You cannot extend an annotation interface. \

The methods of an annotation interface have no _parameters_ and no _throws_ clauses. They cannot be `default` or `static`
methods, and they cannot have _type parameters_.

Here are examples of valid element declarations:
```java
public @interface BugReport {
    enum Status { UNCONFIRMED, CONFIRMED, FIXED, NOTABUG };
    boolean showStopper() default false;
    String assignedTo() default "[none]";
    Class<?> testCase() default Void.class;
    Status status() default Status.UNCONFIRMED;
    Reference ref() default @Reference(); // an annotation type
    String[] reportedBy();
}
```

### Annotations

