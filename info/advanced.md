#### Java Sound API
It splits in two parts MIDI and Sampled. 
MIDI - Musical Instrument Digital interface.
MIDI files don't include any sounds but instructions how we sound should be played. And where it will be played - that's
another story it can be electric piano, synthesizer, or any other instrument that understands MIDI files. It can be 
Java-program on your PC.
So MIDI is like html file, MIDI-device is like a browser.

Sequencer - object that takes MIDI files and sends them to player.

MIDI Message - is an instructions for the Sequencer. 
a.setMessage(144 - message type (NOTE ON, NOTE OFF, etc.),
             1 - channel (piano, drums, etc),
             44 - note to play (0 -127),
             100 - velocity (how fast and hard it plays));

MIDI event - says when to do what message says.             


#### GUI 
JFrame is an object represents a window on the screen.
To JFrame you can add Widgets (buttons, text, checkboxes, UI Elements)

If you want to listen the button event - you need to implement the interface that will listen to it.
A listener interface is the bridge between the listener (you) and event source (the button).
When you implement a listener interface, you give the button a way to call you back. The interface is where the 
call-back method is declared. ActionListener, ItemListener, KeyListener.

In Swing GUI components are event sources. Event source is an object that can turn user actions (click, close, type) in
events. Every event - is an object of some event class MouseEvent, KeyEvent, WindowEvent, ActionEvent, etc.
Your code just need to catch that events, and react on it. 
Every event has matched EventListener, you want window event - implement WindowListener.

If you declare a MouseListener interface, you must write implementation of all methods declared in it, mousePressed, 
mouseReleased, mouseMoved, etc. they will be involved when matched mouse event type is caught by the listener. 

So Listener is an observer, and Button is subject. To listener gets the event - he needs to subscribe on it in Button.
As a ```listener```, your job is to ```implement``` the interface, ```register``` with the button, and ```provide``` the
 event-handling.
As an ```event source```, his job is to ```accept registrations``` (from listeners), ```get events``` from the user
 and ```call the listener’s``` event-handling method (when the user clicks him).
As an ```event object```, he's the argument to the event call-back method (from the interface), and his job is to 
```carry data``` about the event back ```to the listener```.

Three ways to put things on your GUI:
 - Put widgets on a frame. frame.getContentPane().add(myButton);
 - Draw 2D graphics on a widget (arts, games, graphics, charts). graphics.fillOval(70,70,100,100);
 - Put a JPEG on a widget. graphics.drawImage(myPic,10,10,this);
 
Frame has 5 regions you can add something to North, South, East, West, Center.
Something can be another frame with it's an object and so on.

```Layout Manager``` objects control the size and location of the widgets in a Java GUI. A layout manager is a Java
object associated with a particular component, almost always a background component. The layout manager controls the 
components contained within the component the layout manager is associated with. 
In other words, if a frame holds a panel, and the panel holds a button, the panel's layout manager controls the size 
and placement of the button, while the frame's layout manager controls the size and placement of the  panel. The button,
on the other hand, doesn't need a layout manager because the button isn't holding other components.
Layout managers come in several flavors, and each background component can have its own layout manager, with its politics
how components should be layout (same size, grid, with custom size but stack vertically, etc.)
So as far as I get frame layout manager asks of panel layout manager "What you want for your size?", panel LM asks her
component "What about the size, guys?", Component (if we talking about the buttons - simple components don't have layout
manager) respond to panel, panel respond to frame, frame draw the needed size. 

The Big Three layout managers: ```border, flow, and box```.
- `BorderLayout` manager divides a background component into five regions. You can add only one component per region.
Components laid out by this manager usually don’t get to have their preferred size. BorderLayout is the ```default``` layout
manager ```for a frame```!
- `FlowLayout` cares about flow of the components, left to right, top to bottom, in the order they were added. Each
component is the size it wants to be, and they’re laid out left to right in the order that they’re added. So when a 
component won’t fit horizontally, it drops to the next line. FlowLayout is the ```default``` layout manager ```for a panel```!
- `BoxLayout` manager is like FlowLayout in that each component gets to have its own size, and the components are placed
in the order they’re added. But, unlike FlowLayout, they stack the components vertically (or horizontally, but rare).
panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); - something like this.

Widget - is Component. The things a user sees and interacts with, text fields, buttons, scrollable lists, radio buttons,
etc. are all components. In fact, they all extend  javax.swing.JComponent. Almost each component can hold other 
component.
Except JFrame, though, the distinction between interactive components and background components is
artificial. A JPanel, for example, is usually used as a background for grouping other components, but even a JPanel 
can be interactive. Just as with other components, you can register for the JPanel's events including mouse clicks and 
keystrokes. JFrame, JPanel - background, JButton, JTextField - interactive. 

#### Inner classes
Can use everything from outside class, as it were theirs and vise versa, outer have access to inner's private.
Exception from the rule - inner class declared in outer static method.
Outer and inner are specially bounded on the heap.

- Gives you ability to implement some interface, or behavior as many times you want. You have class with a couple of
 inner classes that can implement same interface many times - so outside class became super powerful. Or same method,
 but with different behavior, fo example you want a few action listeners of some event with same callback but too diff
 behavior to place it in one callback - implement them in inner classes.
- Also, do not forget that inner cass can extend any class it wants, while outer class is free to extend something else.

#### Saving/restoring the object state
Several ways to save the state (we don't have DB for now):
1. If your data will be used by only the Java program that generated it? 
```serialization``` - write a file that holds flattened (serialized) objects. Then have your program read the serialized 
objects from the file and inflate them back into living, breathing, heap-inhabiting objects.
2. If your data will be used by other programs?
```plain parceble format file``` - write a file, with delimiters that other programs can parse. For example,
a tab-delimited file that a spreadsheet or database application can use. Or any kind of yur format.

`1. Using serialization.`
```java
FileOutputStream fileStream = new FileOutputStream("MyGame.ser"); // creates a file, connection stream
ObjectOutputStream os = new ObjectOutputStream(fileStream); /* lets you write objects, but it's a chain stream high level,
can’t directly connect to a file. So it needs low-level connection stream */
os.writeObject(characterOne); // serialize a few object to file
os.writeObject(characterTwo);
os.close(); /* Closing the stream at the top closes the ones underneath, so the FileOutputStream (and the file) will 
             close automatically.*/
```

The Java I/0 API has ```connection streams```, that represent connections to destinations and sources such as files or
network sockets, file, or console, and ```chain/filter streams``` that work only if chained to other streams. Often, it takes
at least two streams hooked together to do something useful-one to represent the connection and another to call methods
on. Why two? Because connection streams are usually too low-level. FileOutputStream (a connection stream), for example,
has methods for writing bytes. But we don't want to write bytes, we want to write objects, so we need a higher-level 
ObjectOutputStream (chain) stream.

Serialized objects save the values of the instance variables, so that an identical instance (object) can be brought 
back to life on the heap. Primitives just copied to serialized object, but what if object is more complicated with
inheritance hierarchy, and has references to different object, what we need to save?
When an object is serialized, all the objects it refers to from instance variables are also serialized, and so on and on.
Serialization saves whole object graph. To make this happened all graph should implement a ```Serializable``` interface,
(their superclasses should) if something is not implement it:
- if it is some parent with his state - it won't be saved silently (when an object is deserialized and its superclass is
  not serializable, the superclass constructor will run just as though a new object of that type were being created)
- if something that current serializing object has reference to isn't serializable - NotSerializableException. So 
  every direct referenced state should be saved, if it cannot be saved - operation is impossible. Otherwise, you could
  get Dog without the weight, or Engine without the carburetor. If you cannot change third party class - you can mark
  reference variable ```transient``` - serialization will avoid it, and won't complain. Or maybe you can subclass 
  non-serialize class and make it serializable. transient value will be null when it comes back. Either you can 
  re-initiate it somehow or you can save the state of this var and re-assign it after deserialization.
  Such things like runtime info dependency, network connection, threads, files - cannot be serialized. 
The Serializable interface is known as a ```marker``` or ```tag``` interface, because the interface doesn't have any
methods to implement. Its sole purpose is to announce that the class implementing it is, well, serializable.
If any superclass of a class is serializable, the subclass is automatically serializable even if the subclass doesn't
explicitly declare implements Serializable. (This is how interfaces always work. If your superclass "IS-A" Serializable,
you are too).
If reference var points on the same object - it will be saved once.

The whole point of serializing is restoring back to original state at some later date, in a different 'run' of the JVM
(which might not even be the same JVM that was running at the time the object was serialized). Or to send object over
the network connection. 
Deserialization is a lot like serialization in reverse.
```java
FileInputStream fileStream = new FileInputStream("MyGame.ser"); // connects to file, and can read from it
ObjectInputStream os = new ObjectInputStream(fileStream); /* lets you read objects */
Object first = os.readObject(); // deserialize first object in file
Object second = os.readObject(); /* deserialize second one. Each time you call readObject - you get next one in order 
 they were serialized. If you try to get more objects you serialized - you'll get an exception, for gods sake, wth. */
os.close(); /* Closing the stream */

MyFirstCharacter = (MyFirstCharacter) first; // you need to cast deserialized objects to your type
MySecondCharacter = (MySecondCharacter) second;
```
While deserialization JVM tries to restore the object, and it won't run the object's constructor to not renew the state,
but if somewhere in the chain there is a non-serializable class, the constructor for that non-serializable class will
run along with any constructors above that (even if they’re serializable), which means all superclasses, beginning with
the first non-serializable one, will reinitialize their state. If they won't be in the app object tries to deserialize -
there will be an error. There is a method Java's Remote Method Invocation (RMI) to get the classes via URL to more safe
deserialize the object.
During deserialization, the class of all objects in the graph must be available to the JVM.
If you've changed the code of the serialized object's class - deserialization will give you an error, “you can’t teach
an old Dog new code". When you serialize, the object (including every object in its graph) is 'stamped' with a version
ID number for the object's class. If you think there is ANY possibility that your class might evolve, put a serial version
ID in your class. 
```shell script
$> serialver Dog;
serialVersionUID = -5849794470654667210L;
```
```java
public class Dog {
static final long serialVersionUID = -6849794470754667710L;
/* Now after you change the class, JVM will check before the serialization current serialVersionUID, if it matches the 
serialized object value - JVM think that class has not changed (but it has), and won't throw the exception
but you should be careful with the changes */  
}
```

 Changes that won't break the code, after you trick the JVM with serialVersionUID:
 - Adding new instance variables to the class (existing objects will deserialize with default values for the instance)
 - Adding/Removing classes to the inheritance tree
 - Changing the access level of an instance variable has no affect on deserialization
 - Changing an instance variable from transient to non-transient

Static stuff - don't make serializable objects dependent on a dynamically-changing static variable! They won't be
serialized, because they one per class. They might not be the same when the object comes back.

`2. Using writing to a file`
```java
BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString())); // to write a file
writer.write(data);

BufferedReader reader = new BufferedReader(new FileReader(filePath.toString())); // to read not by char but line
String line;
while ((line = reader.readLine()) != null){
  System.out.print(line);
}
```

##### java.io.File
Represent the file or directory or path on the disk, not the content. It's like an address, it's not the house, not 
people living in this house, it's just an address. It's more confident to have File instance instead
of just string name. All classes that can have string filename in their constructors, also can have File instance. 

##### Buffers
Buffer gives you a temporary place to store your data.
You have a text, you put it in BufferedStream, chain stream that works with Char, when BufferedStream is full, it adds
a chunk to FileWriter, connection stream of a file, that put this chunk to a file. If you want to add chunk before it
is full - you can flush() it to the file.

```java
File myFile = new File("MyText.txt");
FileReader fileReader = new FileReader(myFile);
BufferedReader reader = new BufferedReader(fileReader); // when buffer is full, it will go to the file for another one
String line = null;
while ((line = reader.readLine()) != null) {
    System.out.println(line);
}
reader.close();
```  

#### Network
To connect to server you need - IP address and TCP port number.
Socket connection.
A Socket is an object that represents a network connection between two machines.
Connection - two pieces of software know how to communicate with each other, how to send bits to each other.

A TCP port is just a number. A 16-bit number that identifies a specific program/application on the server/machine.
Well-known TCP port numbers for common server applications:
20 - FTP, 23 - Telnet, SMTP - 25, Time - 37, 443 - HTTPS, 80 - HTTP, 110 - POP3. 65536 ports at all. 0 - 1023 are
occupied by well know servers, so you shouldn't use them.

Port needed so OS know what app you are reaching. If your browser, landed at the POP3 mail server instead of the HTTP
server - the mail server won't know how to parse an HTTP request and doesn't know how servicing the HTTP request.

To communicate over a Socket connection, you use I/O streams.
```java
Socket chatSocket = new Socket("196.164.1.103", 5000); // gets bytes from server
/* To read data */
InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream()); // Convert them to symbols/chars
BufferedReader reader = new BufferedReader(stream); // buffers chars
String message = reader.readLine(); // get buffered data
/* To send data */ 
PrintWriter writer = new PrintWriter(chatSocket.getOutputStream()); // to write one line to stream and send it 
// BufferedWriter - to write more that one line
writer.println("message to send");
writer.print("another message");
```

Socket server
Couple of Sockets. A ServerSocket, which waits for client requests (when a client makes a new Socket()) and a plain
old Socket socket to use for communication with the client.
```java
ServerSocket serverSock = new ServerSocket(5000); // start server
Socket sock = serverSock.accept(); // create a socket to communicate with client
```
The accept() method blocks while it’s waiting for a client Socket connection.
When a client connects, the method returns a plain old Socket (on a different port) that knows how to communicate with
the client (i.e., knows the client’s IP address and port number).
The Socket is on a different port, so that the ServerSocket can go back to waiting for other clients.
So client connects on one port but gets response from another, from one server's communication Socket is started.

####Threads
A Thread object represents a thread of execution, you'll create an instance of class Thread each time you want to 
start up a new thread of execution, separate call stack.
Every Java application starts up a main thread-the thread that puts the main() method on the bottom of the stack.
The JVM is responsible for starting the main thread (and others, as it chooses, like garbage collection thread).
As a programmer, you can write code to start other threads of your own.

With more than one call stack, you get the appearance of having multiple things happen at the same time.
In reality, only a true multiprocessor system can actually do more than one thing at a time.
With Java threads, execution can move back and forth between stacks so rapidly that you feel as though all stacks are
executing at the same time. Remember, Java is just a process running on your OS. So first, Java itself has to be
'the currently executing process' on the OS.
But once Java gets its turn to execute, exactly what does the JVM run? Which bytecodes execute?
Whatever is on the top of the currently-running stack! And in 100 milliseconds, the currently executing code might switch
to a different method on a different stack.
One of the things a thread must do is keep track of which statement (of which method) is currently executing on the
thread's stack.

###### Launch a new Thread
1. Make a Runnable object (a job), one that implements Runnable interface. Job that Thread object will perform, and 
run() method it will put at the bottom of the stack. 
2. Make a thread object (a worker) and give it a Runnable (a job), it will puts job.run() at the bottom of worker stack
3. Start the thread. worker.start() will create a separate stack and put job.run() at the bottom. 

You can create thread without the runnable. Via an instance of class extended Thread, and overriding Thread run() method.
Time you want to subclass/extend the Thread class, is if you are making a new and more specific type of Thread.
If you think of the Thread as the worker, don't extend the Thread class unless you need more specific worker behaviors.


Thread states:
1. New. Thread t = new Thread(r); A Thread instance has been created but not started. In other words, there is a Thread
object, but no thread of execution.
2. Runnable. t.start(); When you start the thread, it moves into the runnable state. Thread is ready to run and just
waiting for be selected for execution. At this point, there is a new call stack for this thread.
3. Running. The Currently Running Thread. Only the JVM thread scheduler can make that decision. You can sometimes 
influence that decision, but you cannot force a thread to move from runnable to running. In the running state, a thread
(and ONLY this thread) has an active call stack, and the method on the top of the stack is executing.
4. Blocked. Once the thread becomes runnable, it can move back and forth between runnable, running, temporarily not
runnable/blocked. (sleeping, waiting for another thread to finish, waiting for data to be available, waiting for an
object he wants to work with is unlocked);
5. Dead. When thread finishes his run() method. Thread cannot be restarted. The Thread object might still be on the
heap, you can call other methods if there is, but there is no longer a separate call stack, and the Thread object is no
longer a thread. It's just an object, at that point, like all other objects.

Thread Scheduler - is the entity that can operate the state of the runnable thread to running or blocked and back. 
You cannot control it, so don't base too much on it your logic, your program must run no matter how scheduler behaves
The scheduler implementations are different for different JVM's, and even running the same program on the same machine 
can give you different results.

To change from running to runnable state - sleep thread for a few seconds, if Thread.sleep(2000) means thread will 
become blocked for 2 seconds then runnable and then running somewhere after 2 seconds, not earlier.

The thing is, there no guarantee about new thread.run() will run some job in first run, only guarantee that runnable.run()
will fire, but thread can be kicked to runnable state after the first curly brace.  

But threads can lead to concurrency ‘issues’. Concurrency issues lead to race conditions. Race conditions lead to data
corruption. Data corruption leads to fear. It all comes down to one potentially deadly scenario: two or more threads
have access to a single object's data. In other words, methods executing on two different stacks are both calling, say, 
getters or setters on a single object on the heap. Thread think that he is the only one, and he never knows that he was
asleep. Or threads want to change the data with some condition. 1st thread check condition, and before the change he
falls asleep, 2nd thread in mean time, checks the condition, change the data, and make condition unchangeable, and die.
1st thread awakes, thinks that condition is still changeable, and changes the data once again. Boom, mess up. 

Synchronized
Tells that thread that entered the method, must finish it, before any other thread can access this method. Even if
thread falls asleep in the middle of the method. It means thread needs an object key in order to access synchronized code.
When Thread Scheduler kicks thread1 that not finished the sync method back to Runnable state, thread keps the key of the 
object, thread2 wants to enter the sync method, but there is no key, so thread2 is also back to Runnable, thread1 finishes
his work with sync, returns the key, back to Runnable, thread2 wants to enter the sync, so he grabs the key and so on.

Object's lock.
Every object has lock, if class object is created from has a synchronized method, then thread that works with this
object's sync method gets the key of this lock, and no other thread can enter any of sync object's methods until key is
set back. That's why they call them synchronized, means it's like they are work only together. If some thread using
something from this group of sync methods - no one can use any of those until previous work is done.

Synchronized can cost:
1. Performance, and complexity.
2. Restrict a concurrency, means multithreading became single-thread here.
3. Deadlock. Be super careful with this one.

You should sync only the critical atomic things in your code.
```java
public synchronized void someMethod(){}
// or even
public void someMethod(){
// some not critical stuff
    synchronized(this /*or object you want to lock*/){
        // some critical stuff
    }
}
```
