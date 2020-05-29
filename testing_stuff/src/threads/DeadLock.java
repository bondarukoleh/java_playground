package threads;

public class DeadLock {
    public static void main(String[] args) {
        /* TODO: Not finished yet, need to think about how to make it  */
        ObjectA objectA = new ObjectA();
        ObjectB objectb = new ObjectB();

        SomeJobToRun someJobToRun = new SomeJobToRun(objectA, objectb);

        Thread thread1 = new Thread(someJobToRun);
        Thread thread2 = new Thread(someJobToRun);

        thread1.setName("thread1");
        thread2.setName("thread2");

        thread1.start();
        thread2.start();
    }
}

class ObjectA {
    public synchronized void someMethod(ObjectB b) {
//        b.someMethod();
    }
}

class ObjectB {
    public synchronized void someMethod(ObjectA a) {
//        a.someMethod();
    }
}

class SomeJobToRun implements Runnable {
    private static int runCount;
    private ObjectA a;
    private ObjectB b;

    public SomeJobToRun(ObjectA a, ObjectB b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        SomeJobToRun.runCount++;
        if (SomeJobToRun.runCount % 2 == 0) {
            Logger.log("About to call A then B");
//            a.someMethod();
            Logger.log("Called A");
//            try {
//                Logger.log("Fall asleep");
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Logger.log("Awake and about tho call B");
//            b.someMethod();
            Logger.log("Called B");
        } else {
            Logger.log("About to call B then A");
//            b.someMethod();
            Logger.log("Called B");
            try {
                Logger.log("Fall asleep");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.log("Awake and about tho call A");
//            a.someMethod();
            Logger.log("Called A");
        }
    }
}

class Logger {
    static void log(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}