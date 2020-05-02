Java 1.2-1.4 -> Java 2, Java 1.5 -> Java 5.0 (Tiger), real versions and marketing names.

Compile a code:
```shell script
javac ./src/Test.java
```

jar and war
These files are simply zipped files using the java jar tool. These files are created for different purposes.
Here is the description of these files:
.jar files: The .jar files contain libraries, resources and accessories files like property files.
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
