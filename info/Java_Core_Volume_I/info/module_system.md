# The Java Platform Module System
An important characteristic of object-oriented programming is encapsulation. A class declaration consists of a public
interface and a private implementation. A class can evolve by changing the implementation without affecting its users.
A _module system_ provides the same benefits for programming in the large. A module system provides the same benefits
for programming in the large. A module can make classes and packages selectively available so that its evolution can be
controlled. Several existing Java module systems rely on class loaders to isolate classes. However, Java 9 introduced
a new system, called the Java Platform Module System

## The Module Concept
Classes provide encapsulation. Packages provide the next larger organizational grouping. A
package is a collection of classes. Packages also provide a level of
encapsulation. \
However, in large systems, this level of access control is not enough.
A Java platform module consists of
- A collection of packages
- Optionally, resource files and other files such as native libraries
- A list of the accessible packages in the module
- A list of all modules on which this module depends

There are two advantages of modules over classical jarfiles and classpath.
- Strong encapsulation, You can control which of your packages are accessible
- Reliable configuration, You avoid common class path problems such as duplicate or missing classes

## Naming Modules
A module is a collection of packages. \
The easiest approach is to name a module after the top-level package that the module provides. For example, the SLF4J
logging façade has a module org.slf4j with packages org.slf4j, org.slf4j.spi,
org.slf4j.event, and org.slf4j.helpers.

>Note
Module names are only used in module declarations. In the source
files for your Java classes, you never refer to module names;
instead, use package names the way they have always been used.

## The Modular “Hello, World!” Program
A module declaration. You place it in a file named module-info.java, located in the base directory (that is, the same
directory that contains the com directory). By convention, the name of the base directory is the same as the module
name.
```text
v2ch09.hellomod/
    └ module-info.java
    com/
        └ horstmann/
            └ hello/
                └ HelloWorld.java
```

To run this program as a modular application, you specify the _module path_
```shell
java --module-path v2ch09.hellomod --module v2ch09.hellomod/com.horstmann.hello.HelloWorld
# or shorten
java -p v2ch09.hellomod -m v2ch09.hellomod/com.horstmann.hello.HelloWorld
```
## Requiring Modules
If our module needs to declare that it relies on some module:
```text
module v2ch09.requiremod {
    requires java.desktop;
}
```
It is a design goal of the module system that modules are explicit about their requirements, so the virtual machine can
ensure that all requirements are fulfilled before starting a program.

A module does not automatically pass on access rights to other modules. It needs to explicitly declare that requirement.

## Exporting Packages
Requiring another module if it wants to use its packages. However, that does not automatically make all packages in the
required module available. A module states which of its packages are accessible, using the `exports` keyword.

When a package is exported, its public and protected classes and interfaces, and their public and protected members, are
accessible outside the module. (As always, protected types and members are accessible only in subclasses.)

However, a package that is not exported is not accessible outside its own module.

## Modular JARs
So far, we have simply compiled modules into the directory tree of the source code. Clearly, that is not satisfactory
for deployment. Instead, a module can be deployed by placing all its classes in a JAR file, with a module-info.class in
the root. Such a JAR file is called a modular JAR.

To create a modular JAR file, use the jar tool
```shell
javac -d modules/com.horstmann.greet $(find com.horstmann.greet -name *.java)
jar -c -v -f com.horstmann.greet.jar -C modules/com.horstmann.greet .
```
If you use a build tool such as Maven, Ant, or Gradle, just keep building your JAR file as you always do. As long as
module-info.class is included, you get a modular JAR.

You can specify a main class in a modular JAR:
```text
javac -p com.horstmann.greet.jar -d modules/v2ch09.exportedpkg $(find v2ch09.exportedpkg -name *.java)
jar -c -v -f v2ch09.exportedpkg.jar **-e com.horstmann.hello.HelloWorld** -C modules/v2ch09.exportedpkg .
```
When you launch the program, you specify the module containing the main class
```shell
java -p com.horstmann.greet.jar:v2ch09.exportedpkg.jar -m v2ch09.exportedpkg
```
When creating a JAR file, you can optionally specify a version number.

> The module equivalent to a class loader is a layer. The Java Platform Module System loads the JDK modules and
> application modules into the boot layer. A program can load other modules, using the layer API

## Automatic Modules
Almost all projects rely on third-party libraries. Of course, you can wait until the providers of all libraries have
turned them into modules, and then modularize your own code. \
But what if you don't want to wait? The Java Platform Module System provides two mechanisms for crossing the chasm that
separates today’s premodular world and fully modular applications: automatic modules and the unnamed module.

For migration purposes, you can turn any JAR file into a module simply by placing it onto a directory in the module path
instead of the class path. A JAR without a _module-info.class_ on the module path is called an automatic module. An
automatic module has the following properties:
1. The module implicitly has a requires clause for all other modules.
2. All of its packages are exported and opened.
3. If there is an entry with key Automatic-Module-Name in the JAR file manifest META-INF/MANIFEST.MF, its value becomes
   the module name.
4. Otherwise the module name is obtained from the JAR file name, dropping any trailing version number and replacing
   sequences of nonalphanumeric characters with a dot.

## The Unnamed Module
Any class that is not on the module path is part of an _unnamed module_. Technically, there may be more than one unnamed
module, but all of them together act as if they are a single module which is called the unnamed module. As with automatic
modules, the unnamed module can access all other modules, and all of its packages are exported and opened. However, no
explicit module can access the _unnamed module_. (An explicit module is a module that is neither automatic nor unnamed 
that is, a module with a _module-info.class_ on the module path.) In other words, explicit modules are always free from 
the “class path hell.”

## Transitive and Static Requirements
Module declares the requirement with the transitive modifier:
```java
module java.desktop {
    requires java.prefs;
    requires transitive java.datatransfer;
    requires transitive java.xml;
    . . .
}
```

Any module that declares a requirement on that transitive user module _java.desktop_ now automatically requires these two
modules.

One compelling use of the requires transitive statement is an aggregator module—a module with no packages and only
transitive requirements.

Finally, there is an uncommon requires static variant that declares that a module must be present at compile time but
is optional at runtime.

## Qualified Exporting and Opening
```text
exports sun.net to
java.net.http,
jdk.naming.dns;
```
Such a statement is called a _qualified export_. The listed modules can access the exported package, but other modules
cannot.

## Service Loading
The _ServiceLoader_ class provides a lightweight mechanism for matching up service interfaces with implementations. The
Java Platform Module System makes this mechanism easier to use. Adds maintainability, flexibility, easier to manage and
evolve.

## Tools for Working with Modules
The _jdeps_ tool analyzes the dependencies of a given set of JAR files. Suppose, for example, that you want to modularize
JUnit 4. Run
```shell
jdeps -s junit-4.12.jar hamcrest-core-1.3.jar
```

Use the _jlink_ tool to produce an application that executes without a separate Java runtime.

Finally, the _jmod_ tool builds and inspects the module files that are included with the JDK. 