package src;

import java.util.Arrays;

public class TestArr {
    public static void main(String[] args) {
        Dog[] dogs = new DogsFarm().getDogsArr();
        dogs[0].bark();
    }
}

class Dog {
    private String type;
    private String name;

    public Dog(String type, String name) {
        this.type = type;
        this.name = name;
    }

    void bark() {
        System.out.println("Woof!");
    }

    @Override
    public String toString() {
        return String.format("{type: '%s', name: '%s'}", type, name);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

class DogsFarm {
    private String[] names = {"Foxy", "Frody", "Mody"};
    private String[] type = {"Poodle", "Labrador", "German shepard"};

    Dog[] getDogsArr() {
        Dog[] dogs = new Dog[3];
        for (int i = 0; i < dogs.length; i++) {
            dogs[i] = new Dog(names[i], type[i]);
        }
        for (Dog dog : dogs) {
            System.out.println(dog);
        }
        return dogs;
    }
}

class MultiDimensionalArr {
    public static void main(String[] args) {
        Dog [][] dogsAndPuppies = {new Dog[] {new Dog("Sheppard", "Joe"), new Dog("Sheppard", "Lisa")}};
        Dog secondPuppy = dogsAndPuppies[0][1];
        int [][] multi = new int[3][2];
        multi[2] = new int[5];
        multi[2][0] = 1;
        int[] subMulti = multi[2];
        int [][] multi2 = {{1,2,3}, {1,2}};
        for(int [] sub : multi){
            System.out.println(Arrays.toString(sub));
        }
    }
}