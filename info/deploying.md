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

JAR
JAR file is Java Archive. It's pkzip file format bundle of your .class files. To user can run this bundle we need to make
.jar file executable. User's OS doesn't know about this bunch of .class files, so we need to explain what file have 
main() method, what file needs to be run. This is the `MANIFEST.MF` file, we should put it in JAR and user's java API 
will look for this file to understand how it should run this JAR.

```manifest
Manifest-Version: 1.0
Main-Class: your_package.SomeClass
```

to create jar
```shell script
~/project_dir/classes_dir:$>jar -cvmf MANIFEST.MF app.jar *
##jar -cmf jar-file existing-manifest input-file(s) or jar -cf jar-file input-file(s)
```

to run it
```shell script
java -jar app.jar
## or if you want to tell JVM what you want to call java -jar app.jar Main-Class: your_package.SomeClass.
```

If there is no manifest file in JAR - you'll get runtime exception.