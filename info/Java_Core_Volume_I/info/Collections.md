# Collections
## The Java Collections Framework
### Separating Collection Interfaces and Implementation
Java collection library separates _interfaces_ and _implementations_. \
A _queue_ interface specifies that you can add elements at the tail end of the queue, remove them at the head, and find
out how many elements are in the queue. You use a queue when you need to collect objects and retrieve them in a
“first in, first out” fashion.

It makes sense to use the _concrete class_ only when you construct the collection object. Use the _interface type_ to hold
the collection reference:
```java
Queue<Customer> expressLane = new CircularArrayQueue<>(100);
expressLane.add(new Customer("Harry"));
```
With this approach, if you change your mind, you can easily use a different implementation. You only need to change your
program in one place — in the constructor call.

Why to choose one implementation over another? The interface says nothing about the efficiency of an implementation. 
A circular array is somewhat more efficient than a linked list. But a circular array is a bounded collection — it has a
finite capacity. If you don’t have an upper limit you may be better off with a linked list implementation after all.

There are another set of classes whose name begins with _Abstract_, such as _AbstractQueue_. These classes are
intended for library implementors. If you want to implement your own queue class, you extend _AbstractQueue_.

### The Collection Interface

