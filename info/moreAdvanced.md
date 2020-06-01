#### Collections

We can use static Collections methods for some simple cases;
```java
ArrayList<String> strings = new ArrayList<>();
Collections.sort(myStringsList);
System.out.println(myStringsList); 
```

But to sort some more complicated things, you need your class extends Comparable.
```java
class MyCustom {
    
}
ArrayList<MyCustom> customs = new ArrayList<>();
Collections.sort(customs);
System.out.println(customs); // will use MyCustom.toString() method to print it in the terminal
```

##### Generics
Almost all the code you write that deals with generics will be collection-related code.
The main point of generics is to let you write type-safe collections. Code that makes the compiler stop you from putting
a Dog into a list of Ducks. Usually, except some special cases it named "T" as a type declaration. 

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
class MyClass<MyGeneric> {
    public void myClassMethod(MyGeneric o){}
}
```
public boolean myClassMethod(MyGeneric o) - we can use generic as an argument since MyClass< MyGeneric > is in class
declaration. When you declare a type parameter for the class, you can simply use that type any place that you'd 
use a  real class or interface type. The type declared in the method argument is essentially 
replaced with the type you use when you instantiate the class.
2. Using a type parameter that was NOT defined in the class declaration
```java
public <T extends Animal> void takeThing(ArrayList<T> list){}
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
It works with plain old polymorphism but if you try to use same "base type" tricks with Collections - you need generics.
   
In generic `extends` - means extends or implements, little soft typing, at least something.




 

