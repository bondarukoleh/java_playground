package src.collections;

import java.io.ObjectOutputStream;
import java.util.*;

public class TestDifferentStuff {
    public static void main(String[] args) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("C");
        strings.add("B");
        strings.add("A");

        Collections.sort(strings);
        System.out.println(strings);

        ArrayList<Animal> a = new ArrayList<>();
        a.add(new Dog());
        /*===================================*/
//        List<Object> animalList = new ArrayList<Dog>(); // error
        List<?> animalList = new ArrayList<Dog>(); // wildcard means anything
//        animalList.add(new Dog());

        ArrayList<Dog> dogs = new ArrayList<>();

        new Some().takeGenericAnimal(new Dog());
        new Some().takeAnimal(new Dog());
        new Some().takeGenericAnimalList(dogs);
//        new Some().takeAnimalList(dogs); error

        OnlyAnimal<Animal> onlyAnimal = new OnlyAnimal<Animal>();
//        onlyAnimal.doSomethingWeird(new WeirdAnimal<Dog>()); // error because WeirdAnimal is not an Animal
    }
}

class MyCustom implements Comparable {
    private String name;

    public MyCustom(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String toString() {
        return "name: '" + name + "'";
    }
}

class Animal {
    public String type;

    @Override
    public String toString() {
        return type;
    }
}

class Dog extends Animal {}
class Cat extends Animal {}

class WeirdAnimal<T extends Animal> {
    public void doSomethingWeird(T o){
        System.out.println(o.type);
    }
}

class OnlyAnimal<MyGenericType> {
    public void doSomethingWeird(MyGenericType o){
        System.out.println(o);
    }
}

class Some {
    public Some() {}

    public <T extends Animal> void takeGenericAnimal(T someType){
        System.out.println("do something");
    }

    public void takeAnimal(Animal someType){
        System.out.println("do something");
    }

    public void takeGenericAnimalList(ArrayList<? extends Animal> someType){
        System.out.println("do something");
    }

    public void takeAnimalList(ArrayList<Animal> someType){
        System.out.println("do something");
    }
}

class TestTree {
    public static void main(String[] args) {
        new TestTree().go();
    }

    public void go() {
        Book b1 = new Book("How Cats Work");
        Book b2 = new Book("Remix your Body");
        Book b3 = new Book("Finding Emo");
        TreeSet<Book> tree = new TreeSet<>(new BookComparator());
//        HashSet<Book> tree = new HashSet<>();
        tree.add(b1);
        tree.add(b2);
        tree.add(b3);
        System.out.println(tree);
    }
}

class Book implements Comparable<Book> {
    String title;

    public Book(String t) {
        title = t;
    }

    @Override
    public int compareTo(Book o) {
        return this.title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                '}';
    }
}

class BookComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.title.compareTo(o2.title);
    }
}

class TestMap {
    public static void main(String[] args) {
        Map<Integer, String> m = new HashMap<>();
        m.put(1, "sdas");
        System.out.println(m);
    }
}