package src.somePackage;

import java.util.ArrayList;

public class TestCasting {
    public static void main(String[] args) {
        ArrayList<Object> dogs = new ArrayList<>();
        dogs.add(new Dog());
        if (dogs.get(0) instanceof Dog){
            Dog dog = (Dog) dogs.get(0);
            dog.bark();
        }
    }
}

class Dog {
    public void bark(){
        System.out.println("Woof!");
    }
}
