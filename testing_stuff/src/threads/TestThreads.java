package threads;

public class TestThreads {
    public static void main(String[] args) {
        SomeJob job = new SomeJob();
        Thread worker = new Thread(job);
        worker.start();
        for (int i = 0; i < 3; i++) {
            System.out.println("main stack");
        }

        CheckThread thread = new CheckThread();
        Thread someThreadA = new Thread(thread);
        Thread someThreadB = new Thread(thread);
        someThreadA.setName("someThreadA");
        someThreadB.setName("someThreadB");
        someThreadA.start();
        someThreadB.start();
        System.out.println("The end of main thread");
    }
}

class SomeJob implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("Hello from the new stack");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class SomeThread extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " is running;");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CheckThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " is running");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class RunThreads implements Runnable {
    public static void main(String[] args) {
        RunThreads runner = new RunThreads();
        Thread alpha = new Thread(runner);
        Thread beta = new Thread(runner);
        alpha.setName("Alpha thread");
        beta.setName("Beta thread");
        alpha.start();
        beta.start();
    }
    public void run() {
        for (int i = 0; i < 25; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "is running");
        }
    }
}