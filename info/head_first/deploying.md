Deployment options:
 - Local
The entire application runs on the end-user’s a computer, as a stand-alone, probably GUI, program, deployed as an
executable JAR.
 - Combination of local and remote
The application is distributed with a client portion running on the user’s local system, connected to a server where
other parts of the application are running. Desktop on client machine that connects to the server via web. (Web/RMI apps)
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
`JWS`
End-users launch a "Java Web Start" (JWS) app in the browser by clicking on a link in a Web page. JWS app downloads
and runs downloaded app outside the browser. Java Web Start app is just an executable JAR that’s distributed over the Web.
1. The client clicks on a Web page link to your JWS application (a .jnlp file, xml with .jar name).
2. The Web server (HTTP) gets the request and sends back a .jnlp file (this is NOT the JAR).
3. Java Web Start (a small ‘helper app’ on the client) is started in browser. The JWS helper app reads the .jnlp file,
and asks the server for the MyApp.jar file.
4. The Web server ‘serves’ up the requested .jar file.
5. Java Web Start gets the JAR and starts the application by calling the specified main( ) method (just like an executable
JAR). Next time the user wants to run this app, he can open the Java Web Start application and from there launch your
app, without even being online.

`.jnlp` Java launch network protocol. File that describes your application.
Java applet unlike JWS cannot run outside the browser, and cannot automatically update like JWS.

`RMI`
Remote method Invocation
Method calls are always between two objects on the same heap. Both objects are in one JVM. JVM is responsible for 
stuffing bits into the reference variable that represent how to get to an object on the heap. The JVM always knows where
each object is, and how to get to it in OWN heap. A JVMs running even on the same machine can't know anything about 
each other, it matters only the invocation of JVM, if they separate - no information between them shared.

But how to invoke method from one JVM object on another? We can think of something remote can help us.  
If we try to think how we would organize such a thing as remote invocation. We would probably have client, client-net-helper,
server, server-net-helper. Client and server were pretending that they are running in the same JVM, and helpers make all
I/O work. 
Helpers act like Services. The client object thinks it's calling a method on the remote service, because the client
helper is pretending to be the service object. But the client helper isn't really the remote service, the client helper
doesn't have any of the actual method logic the client is expecting. Instead, the client helper contacts the server,
transfers information about the method call (e.g., name of the method, arguments, etc.), and waits for a return from the
server. It's like Selenium library asking driver and waits what driver responds.

On the server side, the service helper receives the request from the client helper (through a Socket connection),
unpacks the information about the call, and then invokes the real method on the real service object. So to the service 
object, the call is local. It's coming from the service helper, not a remote client. The service helper gets the return
value from the service, packs it up, and ships it back (over a Socket's output stream) to the client helper. The client
helper unpacks the information and returns the value to the client object.

In RMI method calls also moved by a network which is risky. Information transfered via protocols: JRMP or IIOP.
JRMP is RMI's 'native' protocol, the one made just for Java-to-Java remote calls.
IIOP is the protocol for CORBA (Common Object Request Broker Architecture), and lets you make remote calls on things
which aren't necessarily Java objects. CORBA is usually much more painful than RMI, because if you don't have Java on
both ends, there's an awful lot of translation and conversion that has to happen.

In RMI, the client helper is a ‘stub’ and the server helper is a ‘skeleton’.
RMI consist of several parts:
- A remote interface. Defines the methods that a client can call remotely. It's what the client will use as the polymorphic
class type for your service. Both the Stub and actual service will implement this! Must extent Remote interface.
Be sure arguments and return values are primitives or Serializable.
```java
public interface IRemote extends Remote {
    public String getRemoteServiceData() throws RemoteException;
}
```
- Remote Implementation. This is the class that does the Real Work. It has implementation of the remote methods defined
in the remote interface. It's the object that the client wants to call methods on.
```java
public class RemoteService extends UnicastRemoteObject implements IRemote {
    public RemoteService() throws RemoteException {}

    @Override
    public String getRemoteServiceData() throws RemoteException {
        return "Hello form remote service!";
    }
}
```
- Register remote service in RMI registry
You have to make your service available to remote clients. You do this by instantiating it and putting it into the RMI
registry (which must be running). When you register the implementation object, the RMI system actually puts the stub in
the registry, so it is available for the client. Register your service using the rebind().
```java
try {
    System.setProperty("java.rmi.server.hostname","192.168.56.1");
    Registry registry = LocateRegistry.getRegistry(); // By default, the registry runs on TCP port 1099.
    registry.bind("Say_Hello_Remote", remoteService);
} catch (AlreadyBoundException | RemoteException e) {
    e.printStackTrace();
}
```
- Create client
```java
String host = (args.length < 1) ? null : args[0];
try {
    Registry registry = LocateRegistry.getRegistry(host);
    IRemote stub = (IRemote) registry.lookup("Say_Hello_Remote");
    String response = stub.getRemoteServiceData();
    System.out.println("Remote data is: " + response);
} catch (Exception e) {
    System.err.println("Client exception: " + e.toString());
    e.printStackTrace();
}
```

- DEPRECATED. Generated the stubs and skeletons using rmic. These are the client and server 'helpers'. You don't have to create 
these classes or ever look at the source code that generates them. It's all handled automatically when you run the rmic
tool that ships with your Java development kit. 
```shell script
rmic my_package.MyRemoteImpl; ## this is .class file, not .java
```

- Compile Interface, service, and client
- Start the RMI registry (rmiregistry) The rmiregistry is like the white pages of a phone book. It's where the user 
goes to get the proxy (the client stub/helper object). Better run it from ./classes directory.
By default, the registry runs on TCP port 1099. Registry one that returns skeleton stub to the client.
Stub is dynamically generated, proxy, broker between a server and client.
```shell script
where_my_compiled_classes$> rmiregistry; ## command without no output (when successful), and sometimes run in background
``` 
- Start the remote service. You have to get the service object up and running. Your service implementation class
instantiates an instance of the service and registers it with the RMI registry. Registering it makes the service 
available for clients. rmiregistry should be running before you want register service, on the same JVM.
Setting the java.rmi.server.codebase system property ensures that the registry can load the remote interface definition;
```shell script
where_my_compiled_classes$> java -classpath RMI_service/ -Djava.rmi.server.codebase=file:RMI_service/ network.RMI.RunRemote
## Remote service ready.
```

- Start client
```shell script
where_my_compiled_classes$> java -classpath ./RMI_service/ network.RMI.Client
```

```REMOTE```
Servlets
Java programs that run on an HTTP web server. When a client uses a web browser to interact with a web page, a request is
sent back to the web server. If the request needs the help of a Java servlet, the web server runs (or calls, if the
servlet is already running) the servlet code. Servlet code is simply code that runs on the server, to do work as a 
result of whatever the client requests. Servlets can everything from sending user-submitted info to a database, to
running a web-site's discussion board. And even servlets can use RMI! By far, the most common use of J2EE technology is
to mix servlets and EJBs together, where servlets are the client of the EJB. And in that case, the servlet is using RMI
to talk to the EJBs.

Servlets vs JSP. In the end, the web server turns a JSP into a servlet, but the difference between a servlet and a JSP
With a servlet, developer write a Java class that contains HTML in the output statements (if you're sending back an HTML
page to the client). But with a JSP, it's the opposite-you write an HTML page that contains Java code!
Another "super" way to write front end with Java.
Servlets and JSPs are not part of the Java language, they're considered standard extensions. You need some sort of 
servers that has it. Like apache.

RMI is great for writing and running remote services. But you wouldn't run something like an Amazon or eBay on RMI alone.
For a large, deadly serious, enterprise application, you need something more. 
You need something that can handle transactions, heavy concurrency issues (like a gazillion people are hitting your
server at once), security, and data management. For that, you need an enterprise application server.
In Java, that means a Java 2 Enterprise Edition (J2EE) server. A J2EE server includes both a web server and an Enterprise
JavaBeans(EJB) server, so that you can deploy an application that includes both servlets and EJBs.

J2EE - Java 2 Platform, Enterprise Edition is a comprehensive set of Java-based tools, standards, components, and
technologies for large-scale distributed computing offered by Sun Microsystems Inc.
JavaServer Pages (JSP), Java Messaging Service (JMS), Enterprise JavaBeans (EJB) and Java Naming and Directory Interface
(JNDI) are among the parts of J2EE.

