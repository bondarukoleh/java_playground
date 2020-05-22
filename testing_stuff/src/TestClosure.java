package src;

public class TestClosure {
    public static void main(String[] args) {
        WithInner withInner = new WithInner();
        WithInner.InnerClass innerClass = withInner.getInnerInstance();
        withInner = null;
        innerClass.printPrivate();
    }
}

class WithInner {
    private String superPrivate = "some private variable of WithInner";

    public void printInnerPrivate() {
        System.out.println(new InnerClass().innerPrivate);
    }

    public InnerClass getInnerInstance() {
        return new InnerClass();
    }

    class InnerClass {
        private String innerPrivate = "some private variable of InnerClass";

        public void printPrivate() {
            System.out.println(superPrivate);
        }
    }
}


