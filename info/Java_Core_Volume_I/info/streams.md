# Streams

With a stream, you specify what you want to have done, not how to do it. You leave the way of computation to the implementation.


## From Iterating to Stream Operations
When you process a collection, you usually iterate over its elements and do some work with each of them.

```java
var contents = Files.readString(Path.of("alice.txt")); // Read file into string
List<String> words = List.of(contents.split("\PL+")); // Split into words; nonletters are delimiters
// iterate
int count = 0;
for (String w : words) {
    if (w.length() > 12) count++;
}
```
With streams, the same operation looks like this:
```java
long count = words.stream()
    .filter(w -> w.length() > 12)
    .count();
```
The method names tell you right away what the code intends to do.
Simply changing `stream` to `parallelStream` allows the stream library to do the filtering and counting in parallel.
```java
long count = words.parallelStream()
    .filter(w -> w.length() > 12)
    .count();
```
Streams follow the “what, not how” principle. Declarative way of programming, not imperative.

A stream seems superficially similar to a collection, allowing you to transform and retrieve data. But there are
significant differences:
1. A stream does not store its elements. They may be stored in an underlying collection or generated on demand.
2. Stream operations don't mutate their source. For example, the filter method does not remove elements from a stream
but yields a new stream in which they are not present.
3. Stream operations are lazy when possible. This means they are not executed until their result is needed. For example,
if you only ask for the first five long words instead of all, the filter method will stop filtering after the fifth
match. As a consequence, you can even have infinite streams!

The stream and parallelStream methods yield a stream for the words list. The filter method returns another stream that
contains only the words of length greater than 12. The count method reduces that stream to a result. This workflow is
typical when you work with streams. You set up a pipeline of operations in three stages:
1. Create a stream.
2. Specify intermediate operations for transforming the initial stream into others, possibly in multiple steps.
3. Apply a terminal operation to produce a result. This operation forces the execution of the lazy operations that 
precede it. Afterwards, the stream can no longer be used.

## Stream Creation