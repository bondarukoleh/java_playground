package src;

public class Common {
    public static void main(String[] args) {
//        Common.returnNum();
        A a = new A();
        System.out.println(a.getSomeValue());
    }

    public static int returnNum() {
        return 1;
    }
}

class A {
    private boolean someValue;

    public void setSomeValue(boolean someValue) {
        this.someValue = someValue;
    }

    public boolean getSomeValue() {
        return someValue;
    }
}