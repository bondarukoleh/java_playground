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
As an ```event object```, he's the argument to the event call-back method (from the interface) and his job is to 
```carry data``` about the event back ```to the listener```.