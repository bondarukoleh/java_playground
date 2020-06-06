package threads;

public class Logger {
    static public void log(String message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }
}
