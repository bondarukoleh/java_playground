public class TestAbstract {
    public TestAbstract() {
        System.out.println("TestAbstract constructor called");
    }

    public static void main(String[] args) {

    }

    public void someTestMethod() {
        System.out.println("someTestMethod");
    }
}

abstract class SomeAbstract extends TestAbstract {
    private String prStr;
    public SomeAbstract (String arg) {
        prStr = arg;
        System.out.println("Abstract constructor called");
    }

    private final String privateField = "abc";

    abstract public void someMethod();
}

class Concrete extends SomeAbstract {
    public Concrete(String arg) {
        super(arg);
    }

    @Override
    public void someMethod() {

    }
}

class A {
    private String prStr;
    public String aStr = "A";
    public A(String arg) {
        prStr = arg;
        System.out.println("A const");
    }
    public void aMethod(){
        System.out.println("A m");
    }
}

class B  extends A {
    public String bStr = "A";
    public B() {
        super("asdasd");
        System.out.println("B m");
    }
    public void bMethod(){
        System.out.println("B const");
    }

    @Override
    public String toString() {
        return String.format("bStr='%s', aStr='%s'", bStr, aStr);
    }
}

class TestAll {
    public static void main(String[] args) {
//        new Concrete().someMethod();
//        new Concrete().someTestMethod();
        B b = new B();
        System.out.println(b);
    }
}
