# Security
## Class Loaders
A Java compiler converts source instructions into code for the Java virtual machine. The virtual machine code is stored
in a class file with a _.class_ extension. Each class file contains the definition and implementation code for one class
or interface.

### The Class-Loading Process
The virtual machine loads only those class files that are needed for the execution of a program.

For example, suppose program execution starts with _MyProgram.class_. Here are the steps that the virtual machine carries
out:
1. The virtual machine has a mechanism for loading class files—for example, by reading the files from disk or by
   requesting them from the Web; it uses this mechanism to load the contents of the _MyProgram_ class file.
2. If the MyProgram class has fields or superclasses of another class type, their class files are loaded as well. (The
   process of loading all the classes that a given class depends on is called resolving the class.)
3. The virtual machine then executes the main method in MyProgram (which is static, so no instance of a class needs to
   be created).
4. If the main method or a method that main calls requires additional classes, these are loaded next. The class loading
   mechanism doesn’t just use a single class loader, however.

Every Java program has at least three class loaders:
- The bootstrap class loader 
- The platform class loader 
- The system class loader (sometimes called the application class loader)

The bootstrap class loader loads the platform classes contained in the modules
```text
   java.base
   java.datatransfer
   java.desktop
   java.instrument
   java.logging
   java.management
   java.management.rmi
   java.naming
   java.prefs
   java.rmi
   java.security.sasl
   java.xml
```
as well as a number of JDK-internal modules.


Prior to Java 9, the Java platform classes were located in a file _rt.jar_. Nowadays, the Java platform is modular,
and each platform module is contained in a _JMOD file_. The platform class loader loads all classes of the Java platform
that are not loaded by the bootstrap class loader. The system class loader loads application classes from the module path
and class path.
   
### The Class Loader Hierarchy
Class loaders have a _parent/child relationship_. Every class loader except for the bootstrap one has a parent class
loader. A class loader will give its parent a chance to load any given class and will only load it if the parent has
failed. For example, when the system class loader is asked to load a system class (say, _java.lang.StringBuilder_), it
first asks the _platform class loader_. That class loader first asks the _bootstrap class loader_. The bootstrap class loader
finds and loads the class, so neither of the other class loaders searches any further. Some programs have a plugin 
architecture in which certain parts of the code are packaged as optional plugins. If the plugins are packaged as _JAR_
files, you can simply load the plugin classes with an instance of _URLClassLoader_.

```java
var url = new URL("file:///path/to/plugin.jar");
var pluginLoader = new URLClassLoader(new URL[] { url });
Class<?> cl = pluginLoader.loadClass("mypackage.MyClass");
```

![class_loader_hierarchy](/info/Java_Core_Volume_I/info/media/security/class_loader_hierarchy.PNG)

Most of the time, you don’t have to worry about the class loader hierarchy. Generally, classes are loaded because they
are required by other classes, and that process is transparent to you.

There is a phenomenon is called _classloader inversion_, that has something to do with the loading of the classes by not
exactly that loader author of the classes was expecting.

To overcome this problem, use the correct class loader. Alternatively, it can require that the correct class loader is
set as the context class loader. \
Each thread has a reference to a class loader, called the _context class loader_. \
However, you can set any class loader by calling
```java
Thread t = Thread.currentThread();
t.setContextClassLoader(loader);
```

### Using Class Loaders as Namespaces

Every Java programmer knows that package names are used to eliminate name conflicts. you can have two classes in the same
virtual machine that have the same class _and package_ name. A class is determined by its full name _and_ the class loader.

### Writing Your Own Class Loader
You can write your own class loader for specialized purposes.

### Bytecode Verification
When a class loader presents the bytecodes of a newly loaded Java platform class to the virtual machine, these bytecodes
are first inspected by a verifier. The verifier checks that the instructions cannot perform actions that are obviously
damaging. All classes except for system classes are verified.
- Here are some of the checks that the verifier carries out:
- Variables are initialized before they are used.
- Method calls match the types of object references.
- Rules for accessing private data and methods are not violated.
- Local variable accesses fall within the runtime stack.
- The runtime stack does not overflow.

Why a special verifier is needed to check all these features? \
Indeed, a class file generated by a compiler for the Java programming language always passes verification. However, the
bytecode format used in the class files is well documented, and it is an easy matter for someone with experience in
assembly programming and a hex editor to manually produce a class file containing valid but unsafe instructions for the
Java virtual machine. The verifier is always guarding against maliciously altered class files—not just checking the class
files produced by a compiler.

### Certificate Signing
There is a bunch of certificate related info stored in the Browser and OS. It is used to check if certain certificate
is to be trusted. It is getting updated periodically.



