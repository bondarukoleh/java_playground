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
The fundamental interface for collection classes in the Java library is the _Collection_ interface. The interface has
two fundamental methods: `boolean add(E element)` and `Iterator<E> iterator()`.

The `add` method returns `true` if adding the element actually changes the collection, and `false` if the collection
is unchanged. e.i., if you try to add an object to a set and the object is already present, the add request has no 
effect because sets reject duplicates. \
The iterator method returns an object that implements the _Iterator_ interface.

### Iterators
```java
public interface Iterator<E> {
    E next();
    boolean hasNext();
    void remove();
    default void forEachRemaining(Consumer<? super E> action);
}
```
By repeatedly calling the _next_ method, you can visit the elements from the collection one by one. However, if you reach
the end of the collection, the next method throws a _NoSuchElementException_. Therefore, you need to call the _hasNext_
method before calling next. \
You might use `while (iter.hasNext())` but also “for each” loop:
```java
for (String element : c) {
    // do something with element
}
```
The “for each” loop works with any object that implements the _Iterable_, with a single abstract method _iterator_:
```java
public interface Iterable<E> {
    Iterator<E> iterator();
    . . .
}
```
The _Collection_ interface extends the _Iterable_ interface. Therefore, you can use the “for each” loop with any collection. \
Instead of writing a loop, you can call the _forEachRemaining_ method with a _lambda_ expression that consumes an element.
```java
iterator.forEachRemaining(element -> do something with element);
```
The order in which the elements are visited depends on the collection type. _ArrayList_, starts at index 0. In _HashSet_,
get them in a random order. \
In Java iterators the lookup and position change are tightly coupled. The only way to look up an element is to call
_next_, and that lookup advances the position.
> Think of **Java iterators** as **being between elements**. When you call _next_, the iterator jumps over the next
> element, and it **returns** a **reference** to the element that **it just passed**.

The _remove_ method of the _Iterator_ interface removes the element that was returned by the last call to _next_. That
makes sense — you need to see the element before you can decide that it is the one that should be removed. \
It is illegal to call _remove_ if it wasn’t preceded by a call to _next_.

### Generic Utility Methods
The _Collection_ and _Iterator_ interfaces are generic, which means you can write utility methods that operate on any
kind of collection.

The Collection interface declares quite a few useful methods
```java
int size()
boolean isEmpty()
boolean contains(Object obj) / containsAll(Collection<?> c)
boolean equals(Object other)
boolean addAll(Collection<? extends E> from)
boolean remove(Object obj) / removeAll(Collection<?> c)
void clear()
boolean retainAll(Collection<?> c)
Object[] toArray()
```
To make life easier for implementors, the library supplies a class _AbstractCollection_ that leaves the fundamental methods
_size_ and _iterator_ abstract but implements the routine methods.

A concrete collection class can now extend the _AbstractCollection_ class. It is up to the concrete collection class
to supply an _iterator_ method, but the _contains_ method has been taken care of by the _AbstractCollection_ superclass.
This approach is a bit outdated. It would be nicer if the methods were default methods of the _Collection_ interface.

## Interfaces in the Collections Framework
The Java collections framework defines a number of interfaces for different types of collections.

![collections](/info/Java_Core_Volume_I/info/media/collections/collections.PNG)

> Queue - FIFO, adding to the end of the queue and removing from the beginning \
> Dequeue - adding and removing from any end

There are two fundamental interfaces for collections: _Collection_ and _Map_. \
You **insert** elements **into a _collection_** with a method `boolean add(E element)` \
But, _Maps_ hold _key/value pairs_, and you use the put method `V put(K key, V value)`

A _List_ is an _ordered collection_. Elements are added into a particular position in the container.
An element can be **accessed** in two ways: by an ****iterator** or by an integer **index**. Access by index is called
_random access_.

In practice - two kinds of ordered collections, with very different performance tradeoffs. An array has fast random access,
and it makes sense to use the _List_ methods with an integer index. A _linked list_, also ordered, has slow random access,
and it is best traversed with an _iterator_.

> Interface _RandomAccess_ can be used it to test whether a particular collection supports efficient random access
```java
if (c instanceof RandomAccess) {
    use random access algorithm
} else {
    use sequential access algorithm
}
```
The _Set_, it's _add_ method rejects duplicates. The _equals_ and _hashCode_ method of a _set_ should be defined for
that purpose. \
The _SortedSet_ and _SortedMap_ interfaces expose the _comparator_ object used for sorting. \
Interfaces _NavigableSet_ and _NavigableMap_ that contain additional methods for searching and traversal in sorted sets and
maps. The _TreeSet_ and _TreeMap_ classes implement these interfaces.

## Concrete Collections
![collections](/info/Java_Core_Volume_I/info/media/collections/collection_scheme.PNG)
![collections](/info/Java_Core_Volume_I/info/media/collections/collection_implementations.PNG)

### Linked Lists
Arrays and array lists suffer from removing or inserting an element from the middle being too expensive. _Linked list_,
solves this problem. It stores each object in a separate link. Each link stores a reference to the next link in the 
sequence and to its predecessor. Linked lists are _doubly linked_.

A difference between _linked lists_ and generic collections - a linked list is an **ordered collection** in which the
**position** of the objects **matters**. The _LinkedList.add_ method adds the object to the end of the list. The 
position-dependent _add_ method is the responsibility of an _iterator_, since iterators describe positions in collections.
Using iterators to add elements makes sense only for collections that have a natural ordering. The _set_ data type
does not impose any ordering. Therefore, there is no _add_ method in the _Iterator_ interface. Instead, the collections 
library supplies a subinterface _ListIterator_ that contains an _add_ method:
```java
interface ListIterator<E> extends Iterator<E> {
    void add(E element);
    . . .
}
```
Unlike _Collection.add_, this method does not return a boolean — it is assumed that the _add_ operation always modifies
the list. \
_ListIterator_ has backwards traversing methods: `E previous()` and `boolean hasPrevious()`

> The _add_ method **adds** the new element **before the iterator position** for any _List_ iterator.

> Be careful with the “cursor” analogy. The _remove_ operation does not work exactly like _add_. After a call to _next_,
> the _remove_ method indeed removes the element to the left of the iterator, but if you have just called _previous_, the
> element to the right will be removed. Unlike the _add_ method, which depends only on the iterator position, the
> _remove_ method depends on the iterator state.

A _set_ method replaces the last element, returned by a call to _next_ or _previous_, with a new element.

If an iterator traverses a collection while another iterator is modifying it, and an iterator finds that its collection
has been modified by another iterator it throws a ConcurrentModificationException. \
To avoid this, follow this simple rule:
You can attach as many iterators to a collection as you like, provided that all of them are only **readers**. \
Alternatively, you can attach a single iterator that can both read and write.

Concurrent modification detection - collection keeps track of the number of mutating operations and _Iterator_ keeps his
count. Before the operation Iterator checks that its track with the collection track.

> The _linked list_ only keeps track of _structural_ modifications (add/remove). The _set_ method does not count as a
> structural modification - because it changes the existing one.

Linked lists do not support random access, but there is `get(index)`. If you find yourself using it, you are probably
using a wrong data structure.

The only reason to use a linked list is to minimize the cost of insertion and removal in the middle of the list, so with 
linked collection stay away from all methods that use an integer.

### Array Lists
