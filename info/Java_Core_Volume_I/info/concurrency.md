# Concurrency
_Multitasking_ — your operating system’s ability to have more than one program working at what seems like the same time.

_Multithreaded_ programs extend the idea of multitasking by taking it one level lower: Individual programs will appear
to do multiple tasks at the same time. Each task is executed in a thread, which is short for thread of control. Programs
that can run more than one thread at once are said to be _multithreaded_.

The difference between multiple _processes_ and multiple _threads_ is that while each process has a complete set of its
own variables, threads share the same data. However, shared variables make communication between threads more efficient
and easier to program than interprocess communication.

## What Are Threads?