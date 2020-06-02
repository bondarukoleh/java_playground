package src.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDifferentStuff {
    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("C");
        strings.add("B");
        strings.add("A");

        Collections.sort(strings);
        System.out.println(strings);


        /*===================================*/
//        List<Animal> animalList = new ArrayList<Dog>(); error
//        animalList.add(new Dog());

        ArrayList<Dog> dogs = new ArrayList<>();

        new Some().takeGenericAnimal(new Dog());
        new Some().takeAnimal(new Dog());
        new Some().takeGenericAnimalList(dogs);
//        new Some().takeAnimalList(dogs); error
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
}

class Dog extends Animal {}

class Some {
    public Some() {}

    public <T extends Animal> void takeGenericAnimal(T someType){
        System.out.println("do something");
    }

    public void takeAnimal(Animal someType){
        System.out.println("do something");
    }

    public <T extends Animal> void takeGenericAnimalList(ArrayList<T> someType){
        System.out.println("do something");
    }

    public void takeAnimalList(ArrayList<Animal> someType){
        System.out.println("do something");
    }
}