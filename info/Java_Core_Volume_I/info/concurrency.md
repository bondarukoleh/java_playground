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
So every Java object has a _lock_. A thread can acquire the lock by calling a `synchronized` method. There is a second
mechanism for acquiring the lock: by entering a _synchronized block_. When a thread enters a  block of the form:
```java
synchronized (obj) // this is the syntax for a synchronized block
{
    critical section
}
```
then it acquires the lock for `obj`.

> With `synchronized` blocks, be careful about the lock object. Don't lock on string literal, or primitive type wrappers,
> they could be shared with the same piece of code in some other place, and shared lock leads to deadlock.
> If you must use `synchronized` blocks, _know your lock object_! You must use the same lock for all protected access
> paths, and nobody else must use your lock.

As you can see, client-side locking is very fragile and not generally recommended.

### The Monitor Concept
Researchers have looked for ways to make multithreading safe without forcing programmers to think about explicit locks. \
The Java designers loosely adapted the monitor concept. Every object in Java has an intrinsic lock and an intrinsic
condition. If a method is declared with the `synchronized` keyword, it acts like a monitor method. The condition variable
is accessed by calling `wait/notifyAll/notify`. However, a Java object differs from a monitor in three important ways,
compromising thread safety:
- Fields are not required to be private.
- Methods are not required to be synchronized.
- The intrinsic lock is available to clients.

### Volatile Fields
The `volatile` keyword offers a lock-free mechanism for synchronizing access to an instance field. If you declare a
field as `volatile`, then the compiler and the virtual machine take into account that the field may be concurrently
updated by another thread.

The compiler will insert the appropriate code to ensure that a change to the `volatile` variable in one thread is
visible from any other thread that reads the variable.

`volatile` variable will be always read and wrote form the memory, not from the thread cache. That makes all changes
visible for every other thread. Without it changes from one thread not always instantly visible for another thread due
to caching of the VM.

### Final Variables
There is one other situation in which it is safe to access a shared field — when it is declared `final`.

### Atomics
There are a number of classes in the `java.util.concurrent.atomic` package that guarantee atomicity of other operations
without using `locks`. For example, the _AtomicInteger_ class has methods `incrementAndGet` and `decrementAndGet`, that
atomically make this operation.

```java
public static AtomicLong nextNumber = new AtomicLong();
// in some thread. . .
long id = nextNumber.incrementAndGet();
```
That is, the operations of getting the value, adding 1, setting it, and producing the new value cannot be interrupted.
It is guaranteed that the correct value is computed and returned, even if multiple threads access the same instance
concurrently. There are methods for atomically setting, adding, and subtracting values, but if you want to make a more
complex update, you have to use the `compareAndSet` method. 
```java
largest.updateAndGet(x -> Math.max(x, observed));
largest.accumulateAndGet(observed, Math::max);
```
When you have a very large number of threads accessing the same atomic values, performance suffers because the optimistic
updates require too many retries. The _LongAdder_ and _LongAccumulator_ classes solve this problem.

### Deadlocks
It is possible that all threads get blocked because each is waiting for some resource. Such a situation is called a _deadlock_.

> When the program hangs, press _Ctrl+\_ for a thread dump. Alternatively, run `jconsole`, and consult the Threads panel.

Unfortunately, there is nothing in the Java programming language to avoid or break these deadlocks. You must design
your program.

### Why the stop and suspend Methods Are Deprecated
The `stop` and `suspend` methods both attempt to control the behavior of a given thread without the thread’s cooperation.
The `stop`, `suspend`, and `resume` methods have been deprecated.

### On-Demand Initialization
Sometimes, you want to ensure that initialization happens exactly once. The virtual machine executes a static initializer
exactly once and ensures this with a lock, so you don’t have to program your own.
```java
public class OnDemandData {
    // private constructor to ensure only one object is constructed
    private OnDemandData() {/* Initialization */}
    public static OnDemandData getInstance() {
        return Holder.INSTANCE;
    }
    // only initialized on first use, i.e. in the first call to getInstance
    // This should work form first try, won't be any second attempt, this happens at most once
    private static Holder {
        static final OnDemandData INSTANCE = new OnDemandData();
    }
}
```

### Thread-Local Variables
Sometimes, you can avoid sharing variables by giving each thread its own instance, using the _ThreadLocal_ helper class.
To construct one instance per thread, use the following code:
```java
public static final ThreadLocal<SimpleDateFormat> dateFormat =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
```
To access the actual formatter, call
```java
String dateStamp = dateFormat.get().format(new Date());
```
The _java.util.Random_ class is thread-safe. But it is still inefficient if multiple threads need to wait for a single
shared generator to share. You could use the ThreadLocal helper to give each thread a generator:
```java
int random = ThreadLocalRandom.current().nextInt(upperBound);
```
The call `ThreadLocalRandom.current()` returns a random number generator instance that is unique to the current thread.

Thread-local variables are also sometimes used to make objects available to all methods that collaborate on a task, without
having to pass the object from one caller to another. Suppose you want to share a database connection. Declare a variable
```java
public static final ThreadLocal<Connection> connection = ThreadLocal.withInitial(() -> null);
```
When the task starts, initialize the connection for this thread: 
```java
connection.set(connect(url, username, password));
```
The task calls some methods, all within the same thread, and eventually one of them needs uses the connection.

## Thread-Safe Collections
If multiple threads concurrently modify a data structure, such as a hash table, it is easy to damage that data structure.
You can supply a lock, but it is usually easier to choose a thread-safe implementation instead.

### Blocking Queues
_Producer threads_ insert items into the queue, and _consumer threads_ retrieve them. This type of collection is handy
in scenarios where you need to manage the flow of tasks or data between producer and consumer threads, ensure thread-safe
operations, and handle situations where threads should wait for certain conditions. These scenarios include the producer
-consumer problem, thread pool executors, bounded buffers, and task scheduling systems.

Instead of accessing the bank resource directly, the _transfer threads_ insert _transfer instruction_ objects into a queue.
Another thread removes the instructions from the queue and carries out the transfers. Only that thread has access to the
internals of the bank resource.

A _blocking queue_ causes a thread to block when you try to _add_ an element when the queue is currently full or to _remove_
an element when the queue is empty.

If you use the _queue_ as a thread management tool, use the `put` and `take` methods. The `add`, `remove`, and element
operations throw an exception when you try to `add` to a full queue or `get` the head of an empty queue. Of course,
in a multithreaded program, the queue might become full or empty at any time, so you will instead want to use the `offer`,
`poll`, and `peek` methods.

> The `poll` and `peek` methods return `null` to indicate failure. Therefore, it is illegal to insert `null` values into
> these queues.

There are also variants of the `offer` and `poll` methods with a _timeout_.
```java
boolean success = q.offer(x, 100, TimeUnit.MILLISECONDS);
```
tries for 100 milliseconds to insert an element to the tail of the _queue_.

The _java.util.concurrent_ package supplies several variations of _blocking queues_. The _LinkedBlockingQueue_ has no 
upper bound on its capacity, (max capacity can be specified). The _ArrayBlockingQueue_ is constructed with a given capacity.
The _PriorityBlockingQueue_ is a priority queue, not a first-in/first-out queue.

### Efficient Maps, Sets, and Queues
The _java.util.concurrent_ package supplies efficient implementations for maps, sorted sets, and queues: _ConcurrentHashMap,
ConcurrentSkipListMap, ConcurrentSkipListSet, and ConcurrentLinkedQueue._ Unlike most collections, the `size` method of
these classes does not necessarily operate in constant time, to get the size - it needs to traverse the collection \ 
The collections return _weakly consistent iterators_. That means that the iterators may or may not reflect all
modifications that are made after they were constructed, but they will not return a value twice and they will not
throw a _ConcurrentModificationException_.

> A hash map keeps all entries with the same hash code in the same “bucket.” \
> To the elements with the same hash _resolving collision mechanism_ adds some special keys so, they can be distinguished 
> among others and puts the into the bucket. 

### Atomic Update of Map Entries
You often need to do something special when a key is added for the first time. The `merge` method makes this particularly
convenient.
```java
map.merge(word, 1L, (existingValue, newValue) -> existingValue + newValue);
//or simply
map.merge(word, 1L, Long::sum);
```
### Bulk Operations on Concurrent Hash Maps
The Java API provides bulk operations on concurrent hash maps that can safely execute even while other threads operate
on the map.
There are three kinds of operations:
- search (like find in JS)
- reduce
- forEach

Each operation has four versions:
- operationKeys: operates on keys.
- operationValues: operates on values.
- operation: operates on keys and values.
- operationEntries: operates on Map.Entry objects

With each of the _operations_, you need to specify a _parallelism threshold_. The _threshold_ parameter specifies a
minimum size for the number of elements that should be processed by a single task before it is split into smaller tasks.
This influences the level of parallelism and performance efficiency. If you want the bulk operation to run in a single
thread, use a threshold of `Long.MAX_VALUE`. If you want the maximum number of threads to be made available for the
bulk operation, use a threshold of 1.

Suppose we want to find the first word that occurs more than 1,000 times. We need to `search` keys and values:
```java
String result = map.search(threshold, (k, v) -> v > 1000 ? k : null);
// or
map.search(10, (k, v) -> {
    if(v > 1000) {
        // end search with this value
        return k
    }
    // or continue search
    return null; 
})
```
Then result is set to the _first match_, or to `null` if the search function returns `null` for all inputs.

The `forEach` methods have two variants. The first one simply applies a consumer function for each map entry:
```java
map.forEach(threshold, (k, v) -> System.out.println(k + " -> " + v));
```
The second variant takes an additional _transformer_ function, which is applied first, and its result is passed to the
consumer:
```java
map.forEach(threshold,
(k, v) -> k + " -> " + v, // transformer
System.out::println); // consumer
```
The _transformer_ can be used as a _filter_. Whenever the transformer returns `null`, the value is silently skipped.
For example, here we only print the entries with large values:
```java
map.forEach(threshold,
(k, v) -> v > 1000 ? k + " -> " + v : null, // filter and transformer
System.out::println); // the nulls are not passed to the consumer
```
The `reduce` operations combine their inputs with an accumulation function. i.e, here we compute the sum of all values:
```java
Long sum = map.reduceValues(threshold, Long::sum);
```
As with `forEach`, you can also supply a transformer function.
```java
Integer maxlength = map.reduceKeys(threshold,
String::length, // transformer
Integer::max); // accumulator
```
The transformer can act as a filter, by returning `null` to exclude unwanted inputs.
```java
Long count = map.reduceValues(threshold, v -> v > 1000 ? 1L : null, Long::sum);
```

> If the map is empty, or all entries have been filtered out, the reduce operation returns `null`. If there is only
> one element, its transformation is returned, and the accumulator is not applied.

### Concurrent Set Views
There is no _ConcurrentHashSet_ class. The static `newKeySet` method yields a _Set<K>_ that is actually a wrapper around
a _ConcurrentHashMap<K, Boolean>_.
```java
Set<String> words = ConcurrentHashMap.<String>newKeySet();
```

### Copy on Write Arrays
The _CopyOnWriteArrayList_ and _CopyOnWriteArraySet_ are thread-safe collections in which all mutators make a copy of
the underlying array. This arrangement is useful if the threads that iterate over the collection greatly outnumber the
threads that mutate it. When you construct an iterator, it contains a reference to the current array. If the array is
later mutated, the iterator still has the old array, but the collection’s array is replaced. As a consequence, the older
iterator has a consistent (but potentially outdated) view that it can access without any synchronization expense.

### Parallel Array Algorithms
The _Arrays_ class has a number of parallelized operations. Good methods for logs array's data, starting form thousands
of elements, or to sort them (i.e. with _Arrays.parallelSort_)

### Older Thread-Safe Collections
This is an old mechanism, with worse intrinsic (synch) locks and mechanisms of traversing. You should use Concurrent 
collections from above sections. 

Ever since the initial release of Java, the _Vector_ and _Hashtable_ classes provided thread-safe implementations of a
dynamic array and a hash table. These classes are now considered obsolete, having been replaced by the _ArrayList_ and
_HashMap_ classes. Those classes are not thread-safe. Instead, a different mechanism is supplied in the collections
library. Any collection class can be made thread-safe by means of a synchronization wrapper:
```java
List<E> synchArrayList = Collections.synchronizedList(new ArrayList<E>());
Map<K, V> synchHashMap = Collections.synchronizedMap(new HashMap<K, V>());
```
The methods of the resulting collections are protected by a lock, providing thread-safe access. \
You should make sure that no thread accesses the data structure through the original _unsynchronized_ methods. The easiest
way to ensure this is not to save any reference to the original object. Simply construct a collection and immediately
pass it to the wrapper, as we did in our examples. \
You still need to use “client-side” locking if you want to iterate over the collection while another thread has the
opportunity to mutate it:
```java
synchronized (synchHashMap)
{
    Iterator<K> iter = synchHashMap.keySet().iterator();
    while (iter.hasNext())
        . . .;
}
```
The synchronization is still required so that the concurrent modification can be reliably detected.

You are usually better off using the collections defined in the _java.util.concurrent_ package instead of the
synchronization wrappers. In particular, the _ConcurrentHashMap_ has been carefully implemented so that multiple threads
can access it without blocking each other, provided they access different buckets. One exception is an array list that
is frequently mutated. In that case, a synchronized ArrayList can outperform a _CopyOnWriteArrayList_.

## Tasks and Thread Pools
Constructing a new thread is somewhat expensive because it involves interaction with the operating system. If your
program creates a large number of short-lived threads, you should not map each task to a separate thread, but use a
_thread pool_ instead. A _thread pool_ contains a number of threads that are ready to run. You give a Runnable to the
pool, and one of the threads calls the run method. When the run method exits, the thread doesn’t die but stays around
to serve the next request.

### Callables and Futures
A _Runnable_ encapsulates a task that runs asynchronously; you can think of it as an asynchronous method with no
parameters and no return value. A _Callable_ is similar to a _Runnable_, but it **returns a value**. The _Callable_
interface is a parameterized type, with a single method call. \

_Callable_, _Runnable_ and _Future_ do not run tasks asynchronously by themselves. \
- _Callable_ is used to define, specify the tasks that return a result and can throw exceptions.
- _Future_ is used to retrieve the result (defined by _Callable_ or _Runnable_), represent it once it has been executed 
asynchronously. 
- _ExecutorService_ or _Thread_ is responsible for the asynchronous execution of tasks.
You submit _Callable_ tasks to an _ExecutorService_, which returns a _Future_ to represent the task's result.
```java
public class FutureExample {
    public static void main(String[] args) {
        // Create an executor service with a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(1);
        
        // Define a callable task
        Callable<Integer> callableTask = () -> {
            // Simulate some computation
            Thread.sleep(2000); // Simulate delay
            return 42;
        };

        // Submit the callable task to the executor for asynchronous execution
        Future<Integer> future = executor.submit(callableTask);

        try {
            // Retrieve the result of the computation (blocking call)
            Integer result = future.get();
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the executor
            executor.shutdown();
        }
    }
}
```

_Runnable_, _Callable_ those could be used to define the task that might be executed by extra threads. \
_Future_ is to get the results form them also from separate threads.
```java
public interface Callable<V> {
    V call() throws Exception;
}
```
A _Future_ holds the result of an asynchronous computation. You start a computation, give someone the _Future_ object,
and forget about it. The owner of the _Future_ object can obtain the result when it is ready. \
The _Future<V>_ interface has the following methods:
```text
V get()
V get(long timeout, TimeUnit unit)
void cancel(boolean mayInterrupt)
boolean isCancelled()
boolean isDone()
```
A call to the first `get` method blocks the execution (sort of like `await` with Promise) until async the computation is
finished. The second `get` method also blocks the execution, but it throws a _TimeoutException_ if the call timed out
before the computation finished.

The `isDone` returns `true/false` if the computation is _finished_ or still _in progress_. \
You can cancel the computation with the `cancel` method. If the computation has not yet started, it is _canceled_ and
will never start. If the computation is currently in progress, it is interrupted if the `mayInterrupt` parameter is `true`.

> If a _Future_ object does not know on which thread the task is executed, or if the task does not monitor the interrupted
> status of the thread on which it executes, cancellation will have no effect.

One way of executing a _Callable_ is to use a _FutureTask_, which implements both the _Future_ and _Runnable_ interfaces,
so that you can construct a thread for running it:
```java
Callable<Integer> task = . . .;
var futureTask = new FutureTask<Integer>(task);
var t = new Thread(futureTask); // it's a Runnable t.start();
. . .
Integer result = futureTask.get(); // it's a Future
```
However, more commonly, you will pass a _Callable_ to an executor.

### Executors
The _Executors_ class has a number of static factory methods for constructing _thread pools_.

- The `newCachedThreadPool` method constructs a thread pool that executes each task immediately.
- The `newFixedThreadPool` method constructs a thread pool with a fixed size. 
- The `newSingleThreadExecutor` size 1 pool where a single thread executes the submitted tasks, one after another.
These three methods return an object of the _ThreadPoolExecutor_ class that implements the _ExecutorService_ interface.

Use a _cached thread_ pool when you have threads that are short-lived or spend a lot of time blocking. However, if you
have threads that are working hard without blocking, you don’t want to run a large number of them together. \
For optimum speed, the number of concurrent threads is the number of processor cores. In such a situation, you should use
a _fixed thread_ pool that bounds the total number of concurrent threads. \
The _single-thread_ executor is useful for performance analysis. If you temporarily replace a cached or fixed thread
pool with a single-thread pool, you can measure how much slower your application runs without the benefit of concurrency.

You can submit a _Runnable_ or _Callable_ to an _ExecutorService_ with one of the following methods:
```java
Future<T> submit(Callable<T> task)
Future<?> submit(Runnable task)
Future<T> submit(Runnable task, T result)
```
When you call `submit`, you get back a Future object that you can use to get the result or cancel the task. \
The second `submit` method returns an odd-looking Future<?>. You can use such an object to call `isDone`, `cancel`, or
`isCancelled`, but the `get` method simply returns `null` upon completion. \
The third version of `submit` yields a Future whose get method returns the given result object upon completion.

When you are done with a thread pool, call `shutdown`. This method initiates the shutdown sequence for the pool.
When all tasks are finished, the pool dies. If you call `shutdownNow` - pool then cancels all tasks that have not yet begun.

The _newScheduledThreadPool_ and _newSingleThreadScheduledExecutor_ methods of the _Executors_ class return objects that
implement the _ScheduledExecutorService_ interface. You can schedule a _Runnable_ or _Callable_ to run once, after an
initial delay. You can also schedule a _Runnable_ to run periodically.

### Controlling Groups of Tasks
Sometimes, an _executor_ is used for a more tactical reason — simply to control a group of related tasks. with `shutdownNow`
method or with `invokeAny` method that submits all objects in a collection of Callable objects. You don’t know which task
that is presumably, it is the one that finished most quickly. Use this method for a search problem in which you are 
willing to accept any solution.

The `invokeAll` method submits all objects in a collection of Callable objects, blocks until all of them complete, and
returns a list of `Future` objects that represent the solutions to all tasks.

```java
List<Callable<T>> tasks = . . .;
List<Future<T>> results = executor.invokeAll(tasks);

for (Future<T> result : results)
    processFurther(result.get());
```
In the for loop, the first call `result.get()` blocks execution until the first result is available. That is not a
problem if all tasks finish in about the same time. However, it may be worth obtaining the results in the order in
which they are available. This can be arranged with the _ExecutorCompletionService_.

```java
var service = new ExecutorCompletionService<T>(executor);
for (Callable<T> task : tasks) service.submit(task);
for (int i = 0; i < tasks.size(); i++)
processFurther(service.take().get());
```
The `invokeAny` method terminates as soon as any task `returns`.
> You should use executor services to manage threads instead of launching threads individually.

### The Fork-Join Framework