public class Static {
    public static void main(String[] args) {
        Duck[] dukcs = {new Duck(), new Duck(), new Duck()};
        System.out.println(Duck.getNumOfCreation());

        Singleton singleton = Singleton.instance;
        Singleton singleton2 = Singleton.instance;
        System.out.println(singleton == singleton2);
    }
}

class SomeClass {
    static void someStaticMethod() {
        System.out.println("Lala");
    }
}

class Duck {
    private static int numOfCreation;
    private String name;
    public final byte legs = 2;

    public Duck() {
        name = "Unnamed duck";
        numOfCreation++;
    }

    public static int getNumOfCreation() {
        return numOfCreation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Singleton {
    public static Singleton instance = null;

    static {
        instance = new Singleton();
    }
    private Singleton() {}
}
