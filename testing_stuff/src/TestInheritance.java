public class TestInheritance {
    public static void main(String[] args) {
        Cat tiger = new Tiger();
        tiger.makeSound();
        tiger.wiggleTail();
        System.out.println(tiger.name);
        System.out.println(tiger.stripes);
        tiger.printPrivate();
    }
}

class Cat {
    public boolean stripes = false;
    public String name = "Pussy";
    private int somethingPrivate = 1;

    public void wiggleTail() {
        System.out.println("Tail wiggling.");
    }

    public void makeSound() {
        System.out.println("Meow");
    }

    public void printPrivate() {
        System.out.println(this.somethingPrivate);
    };
}

class Tiger extends Cat {
    public String name = "Big Tiger";
    public boolean stripes = true;
    private int somethingPrivate = 2;

    public Tiger() {
        super();
    }

    @Override
    public void makeSound() {
        System.out.println("Roar");
    }
}


class B {
    public int a = 10;
    public void print() {
        System.out.println("inside B superclass");
    }
}

class C extends B {
    public int a = 20;
    public void print() {
        System.out.println("inside C subclass");
    }
}

class A {
    public static void main(String[] args) {
        B b = new C();
        b.print(); // prints: inside C subclass
        System.out.println(b.a); // prints superclass variable value 10
    }
}