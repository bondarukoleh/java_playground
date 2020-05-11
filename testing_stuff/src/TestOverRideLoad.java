public class TestOverRideLoad {
    public static void main(String[] args) {
        Child obj = new Child();
        obj.simpleMethod();
        obj.simpleMethod(123);
    }
}

class Parent {
    public void simpleMethod() {
        System.out.println("simpleMethod called");
    }
}

class Child extends Parent {
    public void simpleMethod(int arg){
        super.simpleMethod();
        System.out.println("From child with: " + arg);
    }
}
