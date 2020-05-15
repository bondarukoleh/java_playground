package src;

public class TestInheritance {
    public static void main(String[] args) {
        Animal[] animals = new Animal[2];
        animals[0] = new Cat("Pussy cat");
        animals[1] = new Tiger("Big Tiger");

        for (Animal animal : animals) {
            System.out.printf("The '%s' says: ", animal.name);
            animal.makeSound();
        }
        ;
    }
}

class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void wiggleTail() {
        System.out.println("Tail wiggling.");
    }

    public void makeSound() {
        System.out.println("Animal sound");
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println("Meow");
    }
}

class Tiger extends Animal {
    public Tiger(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println("Roar");
    }
}


/*==================================================================*/
class Car {
    private String label;

    public Car(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

class SpeedCar extends Car {
    private int startSpeed;

    public SpeedCar(String label, int startSpeed) {
        super(label);
        this.startSpeed = startSpeed;
    }

    public SpeedCar(String label) {
        this(label, 100);
    }

    public String getInfo() {
        return String.format("Speed car: %s, with speed: %s", getLabel(), startSpeed);
    }
}

class TestCar {
    public static void main(String[] args) {
        System.out.println(new SpeedCar("Ford").getInfo());
    }
}