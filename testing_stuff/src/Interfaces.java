public class Interfaces {
}

interface ISomething {
    void someMethod();
}

interface ISomething2 {
    void someMethod();
}

class Some implements ISomething, ISomething2 {
    @Override
    public void someMethod() {

    }
}