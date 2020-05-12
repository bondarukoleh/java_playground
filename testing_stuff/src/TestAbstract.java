public class TestAbstract {
    public static void main(String[] args) {

    }

    public void someTestMethod() {
        System.out.println("someTestMethod");
    }
}

abstract class SomeAbstract extends TestAbstract {
    private final String privateField = "abc";

    abstract public void someMethod();
}

class Concrete extends SomeAbstract {
    @Override
    public void someMethod() {

    }
}

class TestAll {
    public static void main(String[] args) {
//        new Concrete().someMethod();

        String[] a = new String[5];
        for (String aa : a){
            System.out.println("asdas" + aa);
        }
    }
}
