###### Java Sound API
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


###### GUI 
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

###### Inner classes
Can use everything from outside class, as it were theirs and vise versa, outer have access to inner's private.
Exception from the rule - inner class declared in outer static method.
Outer and inner are specially bounded on the heap.

- Gives you ability to implement some interface, or behavior as many times you want. You have class with a couple of
 inner classes that can implement same interface many times - so outside class became super powerful. Or same method,
 but with different behavior, fo example you want a few action listeners of some event with same callback but too diff
 behavior to place it in one callback - implement them in inner classes.
- Also, do not forget that inner cass can extend any class it wants, while outer class is free to extend something else.

###### Saving/restoring the object state
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
network sockets, file, or console, and ```chain streams``` that work only if chained to other streams. Often, it takes
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

Static stuff - don't make serializable objects dependent on a dynamically-changing static variable! They won't be
serialized, because they one per class. They might not be the same when the object comes back.

`2. Using writing to a file`


