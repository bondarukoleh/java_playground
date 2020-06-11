public class Generics {
    public static void main(String[] args) {
        new GenTry().doSomething(new SomeObject());
    }
}

class GenTry {
    public <T extends SomeObject> T doSomething(T obj){ return obj; };
}

class SomeObject {}
