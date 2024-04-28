package interfaces;

interface Person {
    default String getName() {return ""; }
}

interface Named {
    default int getName() {return 1; }
}

public class Human implements Person, Named {
    // Explicit implementation for Person's getName() method
    @Override
    public String getName() {
        return Person.super.getName();
    }

    @Override
    public int getName() {
        return Named.super.getName();
    }
}