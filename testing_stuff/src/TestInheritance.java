public class TestInheritance {
    public static void main(String[] args) {
        Tiger tiger = new Tiger();
        tiger.makeSound();
        tiger.wiggleTail();
        System.out.println(tiger.stripes);
    }
}

class Cat {
    public boolean stripes = true;

    public void wiggleTail() {
        System.out.println("Tail wiggling.");
    }

    public void makeSound() {
        System.out.println("Meow");
    }
}

class Tiger extends Cat {
    public Tiger() {
        super();
    }

    @Override
    public void makeSound() {
        System.out.println("Roar");
    }
}
