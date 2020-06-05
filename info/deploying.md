Deployment options:
 - Local
The entire application runs on the end-user’s computer, as a stand-alone, probably GUI, program, deployed as an
executable JAR.
 - Combination of local and remote
The application is distributed with a client portion running on the user’s local system, connected to a server where
other parts of the application are running. Desctop on client machine that connects to the server via web. (Web/RMI apps)
 - Remote
The entire Java application runs on a server system, with the client accessing the system through some non-Java means,
probably a web browser.

```LOCAL```
##### Compiling
```shell script
javac SomeClass.java
```
Will create compiled .class files in the same directory.
To keep source .java files from compiled .class files - we can use -d option

```shell script
javac -d ./classes_dir SomeClass.java
## to compile all classes:
javac -d ../classes *.java
## folder structure will be saved.
```

```shell script
## another way to compile
~/project_dir/source:$>javac -d ../out com/olehsSite/MyClass.java
## or
~/project_dir/source:$>javac -d ../classes com/olehsSite/*.java
## run
~/project_dir/out:$>java com.olehsSite.PackageExercise
```

class not found exceptions - Once your class is in a package, you can't call it by its 'short' name. You MUST specify,
at the command-line, the fully-qualified name of the class whose main() method you want to run. But since the 
fully-qualified name includes the package structure, Java insists that the class be in a matching directory structure. 
So if at the command-line you say:
```shell script
%java com.foo.Book
```
the JVM will look in its current directory (and the rest of its classpath), for a directory named "com". ```JVM will not
look for a class named Book, until it has found a directory named "com" with a directory inside named "foo"```

JAR
JAR file is Java Archive. It's pkzip file format bundle of your .class files. To user can run this bundle we need to make
.jar file executable. User's OS doesn't know about this bunch of .class files, so we need to explain what file have 
main() method, what file needs to be run. This is the `MANIFEST.MF` file, we should put it in JAR and user's java API 
will look for this file to understand how it should run this JAR.
Package structure must be the same as it was before you use the classes. In most cases com package must be the first in
the jar.

```manifest
Manifest-Version: 1.0
Main-Class: your_package.SomeClass

```

to create jar. Manifest file will be in automatically generated folder META-INF/
```shell script
~/project_dir/classes_dir:$>jar -cvmf MANIFEST.MF app.jar *
##jar -cvmf manifest.txt packEx.jar com ## You can put root package and create jar from it.
##jar -cmf jar-file existing-manifest input-file(s) or jar -cf jar-file input-file(s)
```

to run it
```shell script
~/project_dir/classes_dir:$>java -jar app.jar
## or if you want to tell JVM what you want to call java -jar app.jar Main-Class: your_package.SomeClass.
```

to list jar content
```shell script
jar -tf packEx.jar
## tf - table file
```

to unzip the jar
```shell script
jar -xf packEx.jar
## xf - extrct file
```

If there is no manifest file in JAR - you'll get runtime exception.

##### Packaging
It's like a directory structure. Full (qualified) class name is a name with packages java.util.ArrayList.
Putting your class in a package reduces the chances of naming conflicts with other classes, but what's to stop two
programmers from coming up with identical package names?
Sun strongly suggests a package naming convention that greatly reduces that risk-prepend every class with your reverse
domain name. Remember, domain names are guaranteed to be unique.
There can be one package statement in .java source file.  
```java
package com.olehsSite.MyPackage
class Some {}
```
All that in this file will be in com.olehsSite.MyClass. Directory structure should be the same with package.

```COMBINATION OF LOCAL AND REMOTE```
