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
 