# Concurrency
_Multitasking_ — your operating system’s ability to have more than one program working at what seems like the same time.

_Multithreaded_ programs extend the idea of multitasking by taking it one level lower: Individual programs will appear
to do multiple tasks at the same time. Each task is executed in a thread, which is short for thread of control. Programs
that can run more than one thread at once are said to be _multithreaded_.

The difference between multiple _processes_ and multiple _threads_ is that while each process has a complete set of its
own variables, threads share the same data. However, shared variables make communication between threads more efficient
and easier to program than interprocess communication.

## What Are Threads?
A simple procedure for running a task in a separate thread:
1. Place the code for the task into the run method of a class that implements the _Runnable_ interface.
```java
public interface Runnable {
    void run();
}
// Since Runnable is a functional interface, you can use lambda
Runnable r = () -> {
    task code
};
```
2. Construct a _Thread_ object from the _Runnable_:
```java
var t = new Thread(r);
```
3. Start the thread:
```java
t.start();
```
> **Do not call the run method** of the Thread class or the Runnable object. Calling the run method directly merely
> executes the task in the same thread no new thread is started. Instead, call the `Thread.start` method. It creates
> a new thread that executes the `run` method.

### Thread States
Threads can be in one of six states:
- New
- Runnable
- Blocked
- Waiting
- Timed waiting
- Terminated

To determine the current state of a thread, simply call the `getState` method.

### New Threads
When you create a thread with the `new` operator — i.e. `new Thread(r)` the thread is not yet running. This means that
it is in the **new state**. When a thread is in the new state, the program has not started executing code inside of it.

### Runnable Threads
Once you invoke the `start` method, the thread is in the **runnable state**. A _runnable thread_ may or may not actually
be running. It is up to the operating system to give the thread time to run. (Java does not call this a separate state,
though. A **running thread** is still **in** the **runnable state**.) Once a thread is _running_, it doesn’t
necessarily keep running. In fact, it is desirable that running threads occasionally pause so that other threads have a
chance to run. The details of thread scheduling depend on the services that the operating system provides. Preemptive
scheduling systems give each runnable thread a slice of time to perform its task. When that slice of time is exhausted,
the operating system preempts the thread and gives another thread an opportunity to work. When selecting the next thread,
the operating system takes into account the thread priorities.

All modern desktop and server operating systems use _preemptive scheduling_. However, small devices such as cell phones
may use _cooperative scheduling_. In such a device, a thread loses control only when it calls the `yield` method, or when
it is blocked or waiting.

On a machine with multiple processors, each processor can run a thread, and you can have multiple threads run in parallel.
Of course, if there are more threads than processors, the scheduler still has to do time slicing.

### Blocked and Waiting Threads
