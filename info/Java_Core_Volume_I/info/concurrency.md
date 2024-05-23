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
When a thread is blocked or waiting, it is temporarily _inactive_. It doesn’t execute any code and consumes minimal
resources. It is up to the _thread scheduler_ to reactivate it. The details depend on how the inactive state was reached.
- When the thread tries to get an inner object lock that is currently held by another thread, it becomes _blocked_.
The thread becomes _unblocked_ when all other threads have relinquished the lock and the thread scheduler has allowed
this thread to hold it.
- When the thread waits for another thread to notify the scheduler of a condition, it enters the _waiting state_. This 
happens by calling the `Object.wait` or `Thread.join` method, or by waiting for a _Lock_ or _Condition_ (java.util.concurrent).
In practice, the difference between the _blocked_ and _waiting_ state is not significant. 
- Several methods have a _timeout_ parameter. Calling them causes the thread to enter the _timed waiting state_. This
state persists either until the timeout expires or the appropriate notification has been received. Methods with timeout
include `Thread.sleep` and the timed versions of `Object.wait`, `Thread.join`, `Lock.tryLock`, and `Condition.await`.

When a thread is _blocked_ or _waiting_ (or terminated), another thread will be scheduled to run. When a thread is
_reactivated_ (i.e. its timeout has expired or it has succeeded in acquiring a lock), the scheduler checks to see if it
has a higher priority than the currently running threads. If so, it preempts one of the current threads and picks a new
thread to run.

![thread_states](/info/Java_Core_Volume_I/info/media/concurency/thread_states.PNG)

A thread is terminated for one of two reasons:
- It dies a natural death because the run method exits normally.
- It dies abruptly because an uncaught exception terminates the `run` method.
You can kill a thread by invoking its `stop` method, but method is deprecated, you should use it.

## Thread Properties
### Interrupting Threads
A thread terminates when its `run` method returns. By executing a `return` statement, after executing the _last statement_
in the method body, or if a not caught _exception occurs_. \
Other than with the deprecated `stop` method, there is no way to force a thread to terminate. However, the `interrupt`
method can be used to request termination of a thread. \
When the `interrupt` method is called on a thread, the _interrupted status_ of the thread is set to `true`. This is a
boolean flag that is present in every thread. Each thread should occasionally check whether it has been interrupted.

However, if a thread is _blocked_, it cannot check the _interrupted status_. This is where the _InterruptedException_
comes in. When the `interrupt` method is called on a thread that blocks on a call such as `sleep` or `wait`, the blocking
call is terminated by an InterruptedException.

Interrupting a thread simply grabs its attention. The interrupted thread can decide how to react to the interruption.
Some threads should handle the exception and continue. But quite commonly, a thread will simply want to interpret an
interruption as a _request for termination_.

The `isInterrupted` method check neither necessary nor useful if you call the `sleep` method (or another _interruptible_ 
method) after every work iteration. If you call the `sleep` method when the `interrupted` status is set, it doesn’t sleep.
Instead, it clears the `status` (!) and throws an _InterruptedException_. Therefore, if your loop calls `sleep`, don’t
check the _interrupted_ status. Instead, catch the _InterruptedException_.
```java
Runnable r = () -> {
    try {
        . . .
        while (more work to do) {
            do more work
            Thread.sleep(delay);
        }
    }
    catch(InterruptedException e) { 
        // thread was interrupted during sleep
    } 
    finally {
        cleanup, if required
    }
    // exiting the run method terminates the thread
};
```

> There are two very similar methods, `interrupted` and `isInterrupted`. The **`interrupted`** method is a **static** method
> that checks whether the current thread has been _interrupted_. Furthermore, calling the `interrupted` method clears
> the interrupted status of the thread. On the other hand, the **`isInterrupted`** method is an **instance** method* that
> you can use to check whether any thread has been interrupted. **Calling `isInterrupted` does not** change the interrupted
> **status**.

Do not ignore the _InterruptedException_.
- In the catch clause, call `Thread.currentThread().interrupt()` to set the interrupted status.
- tag your method with throws _InterruptedException_

### Daemon Threads
You can turn a thread into a _daemon_ thread by calling `t.setDaemon(true);`. A daemon is simply a thread that has no
other role in life than to serve others. Examples are timer threads that send regular “timer ticks” to other threads or
threads that clean up stale cache entries. When only daemon threads remain, the virtual machine exits. Could be used as 
background operation, routine data savings, backups, something that doesn't require human attention.
`setDaemon` must be called before the thread is started.

### Thread Names
You can set any name with the `setName` method:
```java
var t = new Thread(runnable);
t.setName("Web crawler");
```

### Handlers for Uncaught Exceptions
The `run` method of a thread cannot throw any checked exceptions, because it is not in signature of the method in _Runnable_
interface, but it can be terminated by an unchecked exception. In that case, the thread dies. \
However, there is no `catch` clause to which the exception can be propagated. Instead, just before the thread dies,
the exception is passed to a _handler_ for uncaught exceptions. \
The _handler_ must belong to a class that implements the _Thread_. _UncaughtExceptionHandler_ interface. That interface
has a single method `void uncaughtException(Thread t, Throwable e)`.

You can install a handler into any thread with the `setUncaughtExceptionHandler` method. You can also install a default
handler for all threads with the static method `setDefaultUncaughtExceptionHandler` of the _Thread_ class.

However, if you don’t install a handler for an individual thread, the handler is the thread’s _ThreadGroup_ object.

The _ThreadGroup_ class implements the _Thread.UncaughtExceptionHandler_ interface. Its `uncaughtException` method takes
the following action:
1. If the thread group has a parent, then the `uncaughtException` method of the parent group is called. 
2. Otherwise, if the `Thread.getDefaultUncaughtExceptionHandler` method returns a non-null handler, it is called.
3. Otherwise, if the _Throwable_ is an instance of _ThreadDeath_, nothing happens.
4. Otherwise, the name of the thread and the stack trace of the _Throwable_ are printed on _System.err_.
That is the stack trace that you have undoubtedly seen many times in your programs.

### Thread Priorities
In the Java every thread has a _priority_. By default, a thread inherits the priority of the thread that constructed it.
You can set priority with the `setPriority` method. A value between MIN_PRIORITY (1), MAX_PRIORITY (10) and NORM_PRIORITY (5). \
Whenever the _thread scheduler_ picks a new thread it prefers higher priority. However, thread **priorities** are highly
**systemdependent**.

Windows has seven priority levels. In the Oracle JVM for Linux, thread priorities are ignored, they are equal. \
You should not use them nowadays.

## Synchronization
If two threads have access to the same object and each calls a method that modifies the state of the object? A corrupted
objects can be a result. Such a situation is called a _race condition_. \
To avoid corruption of shared data, you must learn how to _synchronize the access_.

### The Race Condition Explained
Two threads are simultaneously trying to update an account. 
```java
accounts[index] += amount;
```
The problem is that these are not _atomic operations_. Means an operation that is completed in a single step from the
perspective of other threads. Once begins, it runs to completion without any possibility of being interrupted, thread-safe
preventing race conditions.

The instruction for that amount update might be processed as follows:
1. Load `accounts[index]` into a register.
2. Add amount.
3. Move the result back to `accounts[index]`.

Suppose the first thread executes Steps 1 and 2, and then it is _preempted_. Suppose the second thread awakens and
updates the same entry in the account array. Then, the first thread awakens and completes its Step 3. That action wipes
out the modification of the other thread.

> You can peek at the virtual machine bytecodes that execute each statement with 
```shell
 javap -c -v Bank
```
The increment command is made up of several instructions, and the thread executing them can be interrupted at any
instruction. \
If we could ensure that the method runs to completion before the thread loses control, the state of the bank account
object would never be corrupted.

### Lock Objects
There are two mechanisms for protecting a code block from concurrent access. A `synchronized` keyword for this purpose,
and the _ReentrantLock_ class. The `synchronized` keyword automatically provides a lock as well as an associated “condition,”
that suites for most cases that require explicit locking.

The basics of a ReentrantLock is:
```java
myLock.lock(); // a ReentrantLock object
try {
    critical section
} 
finally {
    myLock.unlock(); // make sure the lock is unlocked even if an exception is thrown
}
```
As soon as one thread locks the lock object, no other thread can get past the lock statement. When other threads call
lock, they are deactivated until the first thread unlocks the lock object.
> When you use locks, you cannot use the try-with-resources statement


```java
private Lock bankLock = new ReentrantLock();
public void transfer(int from, int to, int amount) {
    bankLock.lock();
    try {
        accounts[from] -= amount;
        accounts[to] += amount;
    } finally {
        bankLock.unlock();
    }
}
```
Suppose one thread calls `transfer` and gets preempted before it is done. Suppose a second thread also calls `transfer`.
The second thread cannot acquire the lock and is blocked in the call to the `lock` method. It is deactivated and must
wait for the first thread to finish executing the `transfer` method. When the first thread unlocks the lock, then the
second thread can proceed.

The lock is called `reentrant` because a thread can repeatedly acquire a lock that it already owns. The lock has a _hold
count_ that keeps track of the nested calls to the `lock` method. The thread has to call `unlock` for every call to `lock`.
Because of this feature, code protected by a `lock` can call another method that uses the same locks. \
In general, you will want to protect blocks of code that update or inspect a shared object.
> Be careful to ensure that the code in a critical section is not bypassed by throwing an exception. If an exception is
> thrown before the end of the section, the finally clause will relinquish the lock, but the object may be in a damaged
> state.

> Even if you use a fair lock, you have no guarantee that the thread scheduler is fair.

### Condition Objects
Often, a thread enters a critical section only to discover that it can’t proceed until a condition is fulfilled. Use a
_condition object_ to manage threads that have acquired a lock but cannot do useful work, _condition objects_ are often
called _condition variables_.

```java
class Bank
{
    private Condition sufficientFunds;
    public Bank(){
        sufficientFunds = bankLock.newCondition();
    }
        
    public void transfer(int from, int to, int amount) {
        bankLock.lock();
        try {
            while (accounts[from] < amount) { 
                sufficientFunds.await();
                // transfer funds . . .
                sufficientFunds.signalAll();
            }
        }
        finally {
            bankLock.unlock();
       }
    }
}
```

If a thread gains the exclusive access to a resource and waits for another thread to change something there, this is where
_condition objects_ come in. A _lock object_ can have one or more associated _condition objects_. You obtain _a condition
object_ with the `newCondition` method. It is smart to give each condition object a name.

If the condition not match thread conditions, a thread calls `await()`, in this way the current thread is deactivates
and gives up the lock.

There is a difference between a thread that is waiting to acquire a lock and a thread that has called await. Once a
thread calls the `await` method, it enters a _wait set_ for that condition. The thread is not made runnable when the
lock is available. Instead, it stays _deactivated_ until another thread has called the `signalAll` method on the same
condition.

This call reactivates all threads waiting for the condition. When the threads are removed from the _wait set_, they are
again _runnable_ and the _scheduler_ will eventually activate them again. As soon as the lock is available, one of them
will acquire the lock and continue where it left off, returning from the call to await. \
At this time, the thread should test the condition again. There is no guarantee that the condition is now fulfilled the
`signalAll` method merely signals to the waiting threads that it may be fulfilled.

> In general, a call to `await` should be inside a loop of the form
```java
while (!(OK to proceed))
    condition.await();
```
It is crucially that _some other_ thread calls the `signalAll` method eventually. When a thread calls `await`, **it has no
way of reactivating itself**. If no other thread bothers to reactivate the waiting thread, it will never run again.
This can lead to unpleasant _deadlock situations_, the program might hang.

When should you call `signalAll`? The rule of thumb is to **call** `signalAll` whenever the **state** of an object 
**changes** in a way that might be advantageous to waiting threads.

> If a thread was awaited in the `while` loop body, after it awakes - it checks the condition once again before continue \
> if there was a regular block of the code with a `lock` - it just continue

`signalAll` does not immediately activate a waiting thread. It only unblocks the waiting threads. \
Another method, `signal`, unblocks only a single thread from the wait set, chosen at random. That is more efficient than
unblocking all threads, but there is a danger. If the randomly chosen thread finds that it still cannot proceed, it
becomes blocked again. If no one can wake up another thread - deadlock.

### The `synchronized` Keyword
Key points about locks and conditions:
- A lock protects sections of code, allowing only one thread to execute the code at a time. 
- A lock manages threads that are trying to enter a protected code segment. 
- A lock can have one or more associated condition objects. 
- Each condition object manages threads that have entered a protected code section but that cannot proceed.

The _Lock_ and _Condition_ interfaces give programmers a high degree of control over locking. However, in most
situations, you don’t need that control — you can use an out-of-the-box mechanism in Java. _everything_ inherited from
_Object_ (so basically everything) in Java has an inner lock. If a method is declared with the `synchronized` keyword,
the object’s lock protects the entire method the same as we did in the _Lock Objects_ section.

The _intrinsic_ object lock also has a single _associated condition_. The `wait` method adds a thread to the _wait set_,
and the `notifyAll/notify` methods unblock waiting threads. Calling `wait` or `notifyAll` is the equivalent of `await` and
`signalAll`

> The `wait`, `notifyAll`, and `notify` methods are **final** methods of the _Object_ \
> The _Condition_ methods had to be named `await`, `signalAll`, and `signal` to avoid conflicts

It is legal to declare static methods as `synchronized`. It has the intrinsic lock of the associated _class_ object. \
The intrinsic locks and conditions have some limitations:
- You cannot interrupt a thread that is trying to acquire a lock.
- You cannot specify a timeout when trying to acquire a lock.
- Having a single condition per lock can be inefficient.

What should you use in your code — _Lock_ and _Condition_ objects or `synchronized` methods?
- It is best to use neither _Lock/Condition_ nor the `synchronized` keyword. In many situations, you can use one of the
mechanisms of the java.util.concurrent package that do all the locking for you. i.e. Blocking Queues and Parallel streams.
- If the `synchronized` keyword works for your situation, by all means, use it.
- Use _Lock/Condition_ if you really know what are you doing.

### Synchronized Blocks
