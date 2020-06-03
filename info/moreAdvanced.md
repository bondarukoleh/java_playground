#### Collections API
There is a three main interfaces List, Map, Set. Map is not extend the Collection interface but still considered as part
of Collection framework.
LIST - when sequence matters, `indexes`, `duplicates`. Lists know where elements are in the list. You can have more than
 one element referencing the same object.
SET - when uniqueness matters, `no duplicates`. Set knows element is already in the collection. You can never have more
 than one element referencing the same object (or more than one element referencing two objects that are considered equal)
MAP - when finding something `by key` matters, `key-value pairs`, `value duplicates`. You can have two keys that reference
 the same value, but you cannot have duplicate keys. Although keys are typically String names (so that you can make
 name/value property lists, for example), a key can be any object.

Iterable (interface) \
&ensp;&ensp;-.-.-> Collection (interface) \
&ensp;&ensp;&ensp;&ensp;-.-.-> Set (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-.-.-> SortedSet (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-.-.-> NavigableSet (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **TreeSet** (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> AbstractSet (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **HashSet** (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **LinkedHashSet** (class) \
&ensp;&ensp;&ensp;&ensp;-.-.-> Queue (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-.-.-> Deque (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> AbstractQueue (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **PriorityQueue** (class) \
&ensp;&ensp;&ensp;&ensp;-.-.-> List (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> AbstractList (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> AbstractSequentialList (class) (also implements Deque) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **LinkedList** (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **ArrayList** (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **Vector** (class) 
            
Map (interface) \
&ensp;&ensp;-.-.-> SortedMap (interface) \
&ensp;&ensp;&ensp;&ensp;-.-.-> NavigableMap (interface) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **TreeMap** (class) \
&ensp;&ensp;-----> AbstractMap (class) \
&ensp;&ensp;&ensp;&ensp;-----> **HashMap** (class) \
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;-----> **LinkedHashMap** (class) \
&ensp;&ensp;-----> **HashTable** (class) (extends Dictionary)        


##### Sorting
We can use static Collections methods for some simple cases;
```java
ArrayList<Е extends String> strings = new ArrayList<>();
Collections.sort(myStringsList);
System.out.println(myStringsList); 
```

But to sort some more complicated things, you need your class implement Comparable.
Comparable has one method compareTo(O o), and it should return a negative integer, zero, or a positive integer as this
object is less than, equal to, or greater than the specified object.

```java
class MyCustom implements Comparable<MyCustom> { /* we need to explain with what we can compare Comparable<MyCustom>
 because it's the same as with collections type parameter - public interface Comparable<T> {compareTo(T o)} */ 
    @Override
    public int compareTo(MyCustom o) {
        return this.getSomeNum() - o.getSomeNum();
    }
}
ArrayList<MyCustom> customs = new ArrayList<>();
Collections.sort(customs);
System.out.println(customs); // will use MyCustom.toString() method to print it in the terminal
```

To have more interesting sorting we can use Comparator interface, it has compare(T o2, T o2) method, and passed as a 
second argument to .sort() method. Advantage that in your MyCustom class you can declare only one way to comparing your
instance with Comparable in compareTo(MyCustom o) method (you can switch some flags in instance - but it is awful), but
with Comparator - you can declare as many comparator classes as you want to have many ways to compare your objects.
Remember, if you use Comparator object via sorting - than Comparable - doesn't count.
```java
class CompareByNum implements Comparator<MyCustom> {
  @Override
  public int compare(MyCustom o1, MyCustom o2) {
      return o1.getSomeNum() - o2.getSomeNum();
  }
}

ArrayList<MyCustom> customs = new ArrayList<>();
Collections.sort(customs, new CompareByNum());
System.out.println(customs);
```
 
##### Equality
`reference` vs `object` equality
How two objects considered equal? It is two reference pointing on the same object or it's two different objects with same
state, or same variable we are comparing?

Reference equality:
Two references that refer to the same object on the heap are equal.
Object.hashCode() override returns int number, based on objects state. Default behavior (on Most JVMs) - hashcode based
on the object’s memory address on the heap. ```To compare references``` use the ```== operator``` , which compares the
bits in the references variables. Also, ```default Object.equals()``` is ```reference compairing```.
Java hashCode() and equals() method are used in Hash table based implementations in java for storing and retrieving data.

Object equality:
Two different objects, but they equal meaningfully.
To make this happened  - you must override both the hashCode() and equals() methods inherited from class Object.
boolean equals(Object obj) -> must decide object equal or not to argument.

If two objects foo and bar are equal, foo.equals(bar) must be true, and both foo and bar must return the same value from
hashCode(). For a Set to treat two objects as duplicates, you must override the hashCode() and equals() methods inherited
from class Object, so that you can make two different objects be viewed as equal.

When you put an object into a Hashset, it uses the object's hashcode() value to determine where to put the object in
the Set. But it also compares the object's hashcode() to the hashcode() of all the other objects in the HashSet, and if
there's no matching hashcode, the HashSet assumes that this new object is not a duplicate.
So you must override hashCode() to make sure the objects have the same value.
But even if hashCodes will be same, objects still can be different. To be sure - HashSet will use one of the .equals()
objects methods to check that these objects are equal, after check HashSet will know that new object is a dublicate and
doesn't add it, and .add() method returns false.

a.equals(b) must also mean that a.hashCode() == b.hashCode();
But  a.hashCode() == b.hashCode() does NOT have to mean a.equals(b)

TreeSet - is a sorted HasSet, it uses .compareTo(), of .compare() if you've passed Comparator in TreeSet constructor.
So objects in the Sorted collections should implement Comparable interface, or you can pass objects comparator in 
collection constructor. So collection knows how it can sort values.

##### Generics
Almost all the code you write that deals with generics will be collection-related code.
The main point of generics is to let you write type-safe collections. Code that makes the compiler stop you from putting
a Dog into a list of Ducks. Usually, except some special cases it named "T" as a type declaration. 

In generic `extends` - means ```extends or implements```, little soft typing, at least something.

```java
public class ArrayList<E> extends AbstractList<E> implements List<E> ... {
    public boolean add(E o)
}
```
ArrayList< E > - placeholder for the type you want to use, type parameter for the class, here can be anything you can
use in java as a variable type. 
AbstractList< E > - E will be passed to abstract class.
List< E > - E becomes the type of the List interface as well.
add(E o) - declared type reduces type of the passed argument to th collection. 

Using generic METHODS:
1. Using a type parameter defined in the class declaration
```java
class MyClass<MyGeneric> { // here if you want to instantiate your Generic class
    public void myClassMethod(MyGeneric o){}
}
MyClass<SomeOfMyOtherClasses> my = new MyClass<SomeOfMyOtherClasses>();
my.myClassMethod(new SomeOfMyOtherClasses());

class MyClass<T extends MyGeneric> { // same stuff but you can pass anything that extends or implements MyGeneric
    public void myClassMethod(T o){}
}
```
public boolean myClassMethod(MyGeneric o) - we can use generic as an argument since MyClass< MyGeneric > is in class
declaration. When you declare a type parameter for the class, you can simply use that type any place that you'd 
use a  real class or interface type. The type declared in the method argument is essentially 
replaced with the type you use when you instantiate the class.
2. Using a type parameter that was NOT defined in the class declaration
```java
public <T extends Animal> void takeThing(ArrayList<T> list){}
// same stuff, ? - is a wildcard
public void takeThing(ArrayList<? extends Animal> list){}
// In some cases it's easier to type:
public <T extends Animal> void takeThing(ArrayList<T> one, ArrayList<T> two)
// instead of:
public void takeThing(ArrayList<? extends Animal> one, ArrayList<? extends Animal> two)
// you cannot do something like class A<? extends B> - not here. 
```
ArrayList< T > list - can be used because it is declared in method declaration before the returned type.
Special construction if class doesn't declare any generic type, but you still want to declare some generic just for the 
specific method. This method says that T can be "any type of Animal", means it's ready to work with any kind of array lists
that typed Animal or any subtype of Animal.
```java
// !!!Pay attention, this:
public <T extends Animal> void takeThing(ArrayList<T> list){}
// no the same with this:
public void takeThing(ArrayList<Animal> list){}
```
`public < T extends Animal > void takeThing(ArrayList< T > list){}` - means you can pass here: ArrayList< Cat >,
 ArrayList< Dog >, ArrayList< Animal >, etc.
`public void takeThing(ArrayList< Animal > list){}` - you can pass only ArrayList< Animal > that's all.

```BUT!!!``` Plain old polymorphism works if we are talking about typed parameters and array of typed parameters,
but if you want to use same "base type" tricks with Collections - you need generics.
```java
class Base {
    public void some1(Base o){}
    public void some2(Base[] o){}
    public void some3(ArrayList<Base> o){}
    public <T extends Base> void some4(ArrayList<T> o){}
}

class Specific extends Base {}
new Base().some1(new Specific()); // work
new Base().some2(new Specific[5]); // work
new Base().some3(new ArrayList<Specific>()); // Won't work!!!
new Base().some4(new ArrayList<Specific>()); // will work with generic overload
```
Why .some2(new Specific[5]) is ok but .some3(new ArrayList< Specific >()) is not?
Because Array types are checked again at runtime, but collection type checks happen only when you compile.
So the compiler won't let you mess up Collection inside the method, but it let's you mess the Array. 
If you mess the Array, that checked in runtime - ArrayStoreException will occur.

```java
Dog[] dogs = {new Dog()};
class AnimalsStuff {
    public void addCat(Animal[] animals){
        animals[0] = new Cat();
    }
}
new AnimalsStuff().addCat(dogs); // ArrayStoreException, in runtime. JVM won't let you add Cat to Dog[] array.
```

There is a trick with wildcard, that in some cases can save typings. 
```java
List<Object> animalList = new ArrayList<Dog>(); // error
List<?> animalList = new ArrayList<Dog>(); // wildcard means anything
List<? extends Animal> animalList = new ArrayList<Dog>(); // wildcard means anything
public static void addRegistry(Map<String, ? extends Person> registry)
```
But you should remember using generic typed parameter method 
```public < T extends Animal > void takeThing(ArrayList< T > list){}``` or wildcarded parameter method
```public void takeThing(ArrayList<?extends Animal> list){}``` WON"T let you add something to collection because it's
not safe. We don't know the what type of collection you'll pass to it, so we cannot change it. You can work with elements
but you cannot add any new elements to it easily. 