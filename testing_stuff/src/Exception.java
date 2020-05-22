package src;

import com.sun.jdi.InternalException;

public class Exception {
    public static void main(String[] args) {
        ExceptionClass exceptionClass = new ExceptionClass();
        try {
            exceptionClass.throwException();
        } catch (InternalException | IndexOutOfBoundsException e) {
            System.out.println("InternalException");
            throw e;
        } finally {
            System.out.println("NOT YET!");
        }
    }
}

class ExceptionClass {
    public void throwException() throws IndexOutOfBoundsException, InternalException {
        throw new RuntimeException();
    }
}