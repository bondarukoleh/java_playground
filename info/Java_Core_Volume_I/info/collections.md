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
By repeatedly calling the `next` method, you can visit the elements from the collection one by one. However, if you reach
the end of the collection, the `next` method throws a _NoSuchElementException_. Therefore, you need to call the `hasNext`
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
Instead of writing a loop, you can call the `forEachRemaining` method with a _lambda_ expression that consumes an element.
```java
iterator.forEachRemaining(element -> do something with element);
```
The order in which the elements are visited depends on the collection type. _ArrayList_, starts at index 0. In _HashSet_,
get them in a random order. \
In Java iterators the lookup and position change are tightly coupled. The only way to look up an element is to call
_next_, and that lookup advances the position.
> Think of **Java iterators** as **being between elements**. When you call _next_, the iterator jumps over the next
> element, and it **returns** a **reference** to the element that **it just passed**.

The `remove` method of the _Iterator_ interface removes the element that was returned by the last call to _next_. That
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
to supply an _iterator_ method, but the `contains` method has been taken care of by the _AbstractCollection_ superclass.
This approach is a bit outdated. It would be nicer if the methods were default methods of the _Collection_ interface.

## Interfaces in the Collections Framework
The Java collections framework defines a number of interfaces for different types of collections.

![collections](/info/Java_Core_Volume_I/info/media/collections/collections.PNG)

There are two fundamental interfaces for collections: _Collection_ and _Map_. \
You **insert** elements **into a _collection_** with a method `boolean add(E element)` \
But, _Maps_ hold _key/value pairs_, and you use the `put` method `V put(K key, V value)`

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
The _Set_, it's `add` method rejects duplicates. The `equals` and `hashCode` method of a _set_ should be defined for
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
**position** of the objects **matters**. The `LinkedList.add` method adds the object to the end of the list. The 
position-dependent `add` method is the responsibility of an _iterator_, since iterators describe positions in collections.
Using iterators to add elements makes sense only for collections that have a natural ordering. The _set_ data type
does not impose any ordering. Therefore, there is no `add` method in the _Iterator_ interface. Instead, the collections 
library supplies a subinterface _ListIterator_ that contains an `add` method:
```java
interface ListIterator<E> extends Iterator<E> {
    void add(E element);
    . . .
}
```
Unlike _Collection.add_, this method does not return a boolean — it is assumed that the `add` operation always modifies
the list. \
_ListIterator_ has backwards traversing methods: `E previous()` and `boolean hasPrevious()`

> The `add` method **adds** the new element **before the iterator position** for any _List_ iterator.

> Be careful with the “cursor” analogy. The _remove_ operation does not work exactly like `add`. After a call to _next_,
> the `remove` method indeed removes the element to the left of the iterator, but if you have just called _previous_, the
> element to the right will be removed. Unlike the `add` method, which depends only on the iterator position, the
> `remove` method depends on the iterator state.

A `set` method replaces the last element, returned by a call to _next_ or _previous_, with a new element.

If an iterator traverses a collection while another iterator is modifying it, and an iterator finds that its collection
has been modified by another iterator it throws a ConcurrentModificationException. \
To avoid this, follow this simple rule:
You can attach as many iterators to a collection as you like, provided that all of them are only **readers**. \
Alternatively, you can attach a single iterator that can both read and write.

Concurrent modification detection - collection keeps track of the number of mutating operations and _Iterator_ keeps his
count. Before the operation Iterator checks that its track with the collection track.

> The _linked list_ only keeps track of _structural_ modifications (add/remove). The `set` method does not count as a
> structural modification - because it changes the existing one.

Linked lists do not support random access, but there is `get(index)`. If you find yourself using it, you are probably
using a wrong data structure.

The only reason to use a linked list is to minimize the cost of insertion and removal in the middle of the list, so with 
linked collection stay away from all methods that use an integer.

### Array Lists
The _List_ interface describes an **ordered on insertion collection** in which the position of elements matters. With random
access. _ArrayList_ class also implements the _List_ interface.

> All methods of the Vector class are synchronized. It is safe to access a Vector object from two threads. But if you
> access a vector from only a single thread I recommend that you use an ArrayList instead of a Vector whenever you don’t
> need synchronization.

### Hash Sets
If you are looking for a particular element and don’t remember its position, you need to visit all elements until you
find a match. If you don’t care about the ordering, there are data structures that let you find elements much faster.
The drawback is that those data structures give you no control over the order in which the elements appear. These data
structures organize the elements in an order that is convenient for their own purposes. \
A well-known data structure for finding objects quickly is the _hash table_. A hash table computes an integer, called
the _hash code_, for each object. \
If you define your own classes, you are responsible for implementing your own _hashCode_. \

In Java, _hash tables_ are implemented as arrays of linked lists. Each list is called a _bucket_. HashSet computes a hash
from the object and make an index from it. Depends on the object hash - it places this object in the bucket.

Sometimes you will hit a _bucket_ that is already filled. This is called a _hash collision_. Then, hashset compares the
new object with all objects in that bucket to see if it is already present.

> For better performance, the keys of a hash table should belong to a class that implements the _Comparable_ interface.

If you want more control over performance, you can specify the _initial bucket count_. The bucket count gives the number
of buckets used to collect objects with identical hash values.

If the hash table gets too full, it needs to be _rehashed_. To rehash the table, another table with more buckets is
created, all elements are inserted into the new table, and the original table is discarded. The _load factor_ determines
when a hash table is _rehashed_. i.e. if the _load factor_ is 0.75 (default) and the table is more than 75% full,
it is automatically rehashed with twice as many buckets.

Hash tables can be used to implement several important data structures. The simplest - _set_ type. A _set_ is a
**collection** of elements **without duplicates**. The `add` method of a _set_ first tries to find the object to be added,
and **adds** it **only if** it is **not yet present**.

You would only **use** a _HashSet_ if you **don’t care about the ordering** of the elements in the collection.

### Tree Sets
The _TreeSet_ class is similar to the hash set, but it is a **sorted collection**. You insert elements there in any
order. When you iterate, the values are automatically presented in sorted order. The **sorting** is accomplished **by 
red-black tree data structure**. \
Adding an element to a tree is slower than adding it to a hash table, but still faster than checking for duplicates in
an array or linked list.

> In order to use a _tree set_, you must be able to **compare** the elements. The **elements must implement** the **Comparable**
> interface, **or** you must **supply a Comparator** when constructing the set.

If you don’t need the data sorted, there is no reason to pay for the sorting overhead.

### Queues and Deques
> Queue - FIFO, adding to the end of the queue and removing from the beginning. \
> Dequeue - adding and removing from any end.

With _queue_ adding elements in the middle is **not supported**.

### Priority Queues
A _priority queue_ retrieves elements in sorted order after they were inserted in arbitrary order. Whenever you call
the `remove` method, you get the smallest element currently in the priority queue. However, the priority queue does not
sort all its elements. If you iterate over the elements, they are not necessarily sorted. The priority queue makes use
of data structure called a _heap_. A _heap_ is a self-organizing binary tree in which the _add_ and _remove_ operations
cause the smallest element to gravitate to the root, without wasting time on sorting all elements.

Like a _TreeSet_, a _priority queue_ can either hold elements of a class that implements the _Comparable_ interface or
a _Comparator_ object you supply in the constructor. \
A typical use for a priority queue is job _scheduling_. Each job has a priority. Whenever a new job can be started, the
highest priority job is removed from the queue. (for priority 1 to be on top, `remove` yields the minimum element)

### Maps
A _set_ is a collection that lets you quickly find an existing element. However, to look up an element, you need to have
an exact copy of the element to find. Usually, you have some key information, and a _map_ stores _key/value_ pairs.
You can find a value if you provide the key.

### Basic Map Operations
There are two general-purpose implementations for maps: _HashMap_ and _TreeMap_. Both implement the _Map_ interface. \
A _hash map_ hashes the keys, and a _tree map_ uses an ordering on the keys to organize them in a search tree. The _hash_
or _comparison_ function is applied _only to the keys_. The values are not hashed or compared.
What you choose? As with sets, hashing is usually a bit faster, and if you don’t need to visit the keys in sorted order.
Whenever you add an object to a map, you must supply a key as well. \
To retrieve an object, you must use (and, therefore, remember) the key.
```java
var id = "987-98-9996";
Employee e = staff.get(id); // gets harry. If nothing is there - returns null
```
If the `null` return value can be inconvenient, then use the `getOrDefault` method.
```java
Map<String, Integer> scores = . . .;
int score = scores.getOrDefault(id, 0); // gets 0 if the id is
not present
```
Keys must be unique. If you call the `put` method twice with the same key, the second value replaces the first one.
In fact, put returns the previous value associated with its key parameter.

The easiest way of iterating over the keys and values of a map is the `forEach` method. Provide a lambda.
```java
scores.forEach((k, v) -> System.out.println("key=" + k + ", value=" + v));
```

### Updating Map Entries
NullPointerException if get returns null
```java
counts.put(word, counts.get(word) + 1);
```
The remedy is to use the `getOrDefault`
```java
counts.put(word, counts.getOrDefault(word, 0) + 1);
```
Another approach is to first call the `putIfAbsent` method. Puts a value if the key was previously absent or null
```java
counts.putIfAbsent(word, 0);
counts.put(word, counts.get(word) + 1); // now we are sure
```
The `merge` method simplifies this common operation. The call
```java
counts.merge(word, 1, Integer::sum);
```
associates `word` with `1` if the key wasn’t previously present, and otherwise combines the previous value and `1`,
using the _Integer::sum_ function.

### Map Views
The _collections framework_ does not consider a _map_ itself as a collection. However, you can obtain views of the map 
objects that implement the _Collection_ interface or one of its subinterfaces. There are three views:
- the _set of keys_ `Set<K> keySet()`
- the _collection of values_ (which is not a set) `Collection<V> values()`
- the _set_ of _key/value_ pairs. `Set<Map.Entry<K, V>> entrySet()`

Note that the _keySet_ is not a _HashSet_ or _TreeSet_, but an object of some other class that implements the _Set_ interface.

```java
for (Map.Entry<String, Employee> entry : staff.entrySet()) {
    String k = entry.getKey();
    Employee v = entry.getValue();
    // do something with k, v
}
```
You can avoid the cumbersome `Map.Entry` by using a `var` declaration.
```java
for (var entry : map.entrySet()) {
    // do something with entry.getKey(), entry.getValue()
} 
// Or simply use the forEach method:
map.forEach((k, v) -> {
    do something with k, v
});
```
> You **cannot add an element** to the `keySet` view. Adding a new key directly to the view will throw 
> UnsupportedOperationException, since you need to provide an associated value). However, modifying the _map_ through
> the keySet by removing keys is supported.

### Weak Hash Maps
_WeakHashMap_ cooperates with the garbage collector to remove key/value pairs when the only reference to the key is the
one from the hash table entry.

### Linked Hash Sets and Maps
A linked hash map can alternatively use access order, not insertion order, to iterate through the map entries. Every
time you call `get` or `put`, the affected entry is removed from its current position and placed at the end of the linked
list of entries. (Only the position in the linked list of entries is affected, not the hash table bucket. _Access order_ 
is useful for implementing a “least recently used” discipline for a cache.

### Enumeration Sets and Maps
The _EnumSet_ is an efficient _set_ implementation with elements that belong to an enumerated type. Since an enumerated
type has a finite number of instances, the _EnumSet_ is internally implemented simply as a sequence of bits. A bit is
turned on if the corresponding value is present in the _set_. The _EnumSet_ class **has no public constructors**.

An _EnumMap_ is a _map_ with keys that belong to an enumerated type.

### Identity Hash Maps
The _IdentityHashMap_ has a quite specialized purpose. Here, the hash values for the **keys** should **not be computed**
by the `hashCode` method but by the `System.identityHashCode` method. That’s the method that `Object.hashCode` uses to
compute a _hash_ code from the object’s memory address. Also, for comparison of objects, the IdentityHashMap uses ==, not
equals.

In other words, different key objects are considered distinct even if they have equal contents. This class is useful for
implementing object traversal algorithms, such as object serialization, in which you want to keep track of which objects
have already been traversed. It can distinguish the identity of the objects, avoid the indefinite loops.

_Serialization_ - making the stream bytes from an object. You can store it in DB, and afterwards pass it via internet and
deserialize somewhere else.

### Copies and Views
`keySet` method of the map classes returns an _object_ of a class that implements the _Set_ interface and whose methods
manipulate the original _map_. Such a collection is called a **view**. \
**Modifiable Views** created using methods like `subList` and `keySet`. These views allow modifications, which directly
affect the underlying collection.

### Small Collections
Java 9 introduces static methods yielding a _set_ or _list_ with given elements, and a _map_ with given _key/value_ pairs \

```java
List<String> names = List.of("Peter", "Paul", "Mary");
Set<Integer> numbers = Set.of(2, 3, 5);
Map<String, Integer> scores = Map.of("Peter", 2, "Paul", 3, "Mary", 5);
```

> The _elements_, _keys_, or _values_ **may not be null**. Set and map keys may not be duplicated. \
> **No guarantee** is made about the **iteration order** of these sets and maps.

The _List_ and _Set_ interfaces have an `of` method with a variable number of arguments. For the `.of` _Map_ up to 10
known arguments. However, for variable arguments - method `ofEntries` that accepts an arbitrary number of `Map.Entry<K, V>`
objects, which you can create with the static `entry` method.
```java
Map<String, Integer> scores = ofEntries(entry("Peter", 2), entry("Paul", 3), entry("Mary", 5));
```
These collection objects are _unmodifiable_. Any attempt to change their contents results in an _UnsupportedOperationException_.
If you want a _mutable_ collection, you can pass the _unmodifiable_ collection to the constructor:
```java
var names = new ArrayList<>(List.of("Peter", "Paul", "Mary"));
// A mutable list of names
```
The method call `Collections.nCopies(n, anObject)` returns an immutable object.
> Previously, there was a static `Arrays.asList` method that returns a list that is mutable but not resizable.

> when you use `Arrays.asList(intArray)`, it creates a fixed-size List that is backed by the original array. This means
> that any modification to the List will also modify the underlying array because they share the same data structure in
> memory. Mutable (modify but don't add/remove) but Fixed-Size, Null Values allowed

> List.of(intArray) - Immutable, Not Backed by Array (creates a new, independent list), No Null Values.

### Unmodifiable Copies and Views
To make an _unmodifiable copy_ of a collection, use the static _copyOf_. **Immutable Views** are created using methods
like `Collections.unmodifiableList`. These views do not allow any modifications. Any attempt to modify will result in
UnsupportedOperationException.
```java
ArrayList<String> names = ...;
Set<String> nameSet = Set.copyOf(names); // The names as an unmodifiable set
List<String> nameList = List.copyOf(names); // The names as an unmodifiable list
```
If the original collection changes, the view reflects those changes. That is what makes views different from copies.
You obtain unmodifiable views by eight methods:
- Collections.unmodifiableCollection
- Collections.unmodifiableList
- Collections.unmodifiableSet
- ...

```java
var staff = new LinkedList<String>();
lookAt(Collections.unmodifiableList(staff));
```
The `Collections.unmodifiableList` returns an object of a class implementing the _List_ interface. Its accessor methods
retrieve values from the staff collection. All mutator methods (such as add) have been redefined to throw an
_UnsupportedOperationException_.

The _unmodifiable view_ does not make the collection itself _immutable_. You can still modify the collection **through
its original reference**, and those changes will reflect on _unmodifiable view_. The _views_ wrap the interface and not
the actual collection object.

> The _unmodifiableCollection_ method (as well as the _synchronizedCollection_ and _checkedCollection_ methods) returns
> a collection whose _equals_ method does not invoke the _equals_ method of the underlying collection. Instead, it inherits
> the _equals_ method of the _Object_ class (ests whether the objects are identical). If you turn a set or list into
> just a collection, you **can no longer test for equal contents**. The view acts in this way because equality testing
> is not well defined at this level of the hierarchy.

### Subranges
Use the `subList` method to obtain a _view_ into the subrange of the list:
```java
List<Employee> group2 = staff.subList(10, 20);
```
The first index is inclusive, the second exclusive, like in `substring`. You can apply any operations to the subrange,
and they automatically reflect the entire list.

For sorted sets and maps:
```java
SortedSet<E> subSet(E from, E to)
SortedMap<K, V> subMap(K from, K to)
```
The _NavigableSet_ interface has more control over these subrange operations. You can specify whether the bounds are included:
```java
NavigableSet<E> subSet(E from, boolean fromInclusive, E to, boolean toInclusive)
```
 
### Checked Views
Checked views are intended as debugging support for a problem that can occur with generic types. It is actually possible
to smuggle elements of the wrong type into a generic collection.
```java
var strings = new ArrayList<String>();
ArrayList rawList = strings; // warning only, not an error, for compatibility with legacy code
rawList.add(new Date()); // now strings contains a Date object!
```
A checked view can detect this problem. Define a safe list as follows:
```java
List<String> safeStrings = Collections.checkedList(strings, String.class);
```
The view’s `add` method checks that the inserted object belongs to the given class.
>The checked views **are limited** i.e. ArrayList<Pair<String>> will allow you to insert a Pair<Date> since the virtual
> machine has a single “raw” Pair class.

### Synchronized Views
If you access a collection from multiple threads instead of implementing thread-safe collection classes, the library
designers used the _view_ mechanism to make regular collections thread-safe. i.e. the static _synchronizedMap_ method in
the _Collections_ class can turn any map into a _Map_ with synchronized access methods:
```java
var map = Collections.synchronizedMap(new HashMap<String, Employee>());
```
The methods such as `get` and `put` are synchronized — each method call must be finished completely before another
thread can call another method.

## Algorithms
### Sorting and Shuffling
The `sort` method in the _Collections_ class sorts a collection that implements the _List_ interface.
```java
var staff = new LinkedList<String>();
// fill collection
Collections.sort(staff);
```
This method assumes that the list elements implement the _Comparable_ interface. If you want to sort the list in some
other way, you can use the `sort` method of the _List_ interface and pass a _Comparator_ object. 
```java
staff.sort(Comparator.comparingDouble(Employee::getSalary));
```
It simply dumps all elements into an array, sorts the array, and then copies the sorted sequence back into the list.

### Binary Search
To find something in an array, you can look at the middle element and check whether it is larger than the element that
you are trying to find. If so, keep looking in the first half of the array; otherwise, look in the second half.
That cuts the problem in half, and you keep going in the same way. \
The _binarySearch_ of the _Collections_ class implements this algorithm. Note that the collection must already be sorted.
To be worthwhile, binary search requires random access.

### Bulk Operations
There are several operations that copy or remove elements “in bulk.”
```java
coll1.removeAll(coll2); // removes all elements from coll1 that are present in coll2
// find the intersection of two sets
var result = new HashSet<String>(firstSet);
result.retainAll(secondSet); //It retains all elements that occur in both sets.
staffMap.keySet().removeAll(terminatedIDs); // remove all IDs of terminated employees
relocated.addAll(staff.subList(0, 10));
```

### Converting between Collections and Arrays
If you have an array that you need to turn into a collection the `List.of`
```java
String[] names = . . .;
List<String> staff = List.of(names);
```
Obtaining an array from a collection is a bit trickier. You can use the `toArray` method:
```java
Object[] names = staff.toArray();
```
But the result is an **array of objects**, and you cannot use a cast, because array remembers that it was created from
_Object_ type. \
Instead, pass an array constructor expression to the `toArray` method. 
```java
String[] values = staff.toArray(String[]::new);
```

### Writing Your Own Algorithms
If you write your own algorithm, you should work with interfaces, not concrete implementations. Receive the Interface and
return the Interface.

## Legacy Collections
![legacy_collections](/info/Java_Core_Volume_I/info/media/collections/legacy_collection.PNG)

### The Hashtable Class
The classic _Hashtable_ class serves the same purpose as the _HashMap_ class and has essentially the same interface.
Just like methods of the _Vector_ class, the _Hashtable_ methods are synchronized. If you do not require compatibility
with legacy code, you should use a HashMap instead. If you need concurrent access, use a ConcurrentHashMap

### Enumerations
Legacy collections use the _Enumeration_ interface for traversing sequences of elements.

### Property Maps
A property map is a map structure of a special type. It has three particular characteristics:
- The keys and values are strings.
- The map can easily be saved to a file and loaded from a file.
- There is a secondary table for default values.

The Java platform class that implements a property map is called Properties. Property maps are useful in specifying
configuration options for programs, `System.getProperties()`

### Bit Sets
_BitSet_ class stores a sequence of bits. The _BitSet_ class gives you a convenient interface for reading, setting, and
resetting individual bits. Could be flags or something.