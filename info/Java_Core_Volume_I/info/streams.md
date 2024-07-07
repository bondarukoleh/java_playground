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

You can turn any collection into a stream with the `stream` method of the `Collection` interface. If you have an array,
use the static `Stream.of` method instead.

```java
Stream<String> words = Stream.of(contents.split("\\PL+"));
// split returns a String[] array
```

The `of` method has a varargs parameter.
Use `Arrays.stream(array, from, to)` to make a stream from a part of an array.
To make a stream with no elements, use the static `Stream.empty` method.

The `Stream` interface has two static methods for making infinite streams. The `generate` method takes a function with
no arguments (or, technically, an object of the `Supplier<T>` interface). Whenever a stream value is needed, that
function is called to produce a value.

```java
Stream<String> echos = Stream.generate(() -> "Echo");
```

To produce sequences such as `0 1 2 3 . . .`, use the `iterate` method instead. It takes a “seed” value and a
function (technically, a `UnaryOperator<T>`) and repeatedly applies the function to the previous result.

```java
Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
```

To produce a finite stream instead, add a predicate that specifies when the iteration should finish:

```java
var limit = new BigInteger("10000000");
Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO,
    n -> n.compareTo(limit) < 0,
    n -> n.add(BigInteger.ONE));
```

Finally, the `Stream.ofNullable` method makes a really short stream from an object.

To view the contents of one of the streams introduced in this section, use the `toList` method, which collects the
stream’s elements in a list. Like `count`, `toList` is a terminal operation. If the stream is infinite, first truncate
it with the `limit` method:

```java
System.out.println(Stream.generate(Math::random).limit(10).toList());
```

> If you have an `Iterable` that is not a collection, you can turn it into a stream by calling 
> `StreamSupport.stream(iterable.spliterator(), false);`

> It is very important that you don't modify the collection backing a stream while carrying out a stream operation. 
> If you do modify that collection, the outcome of the stream operations becomes `undefined`

## The filter, map, and flatMap Methods

A stream transformation produces a stream whose elements are derived from those of another stream.

The argument of `filter` is a `Predicate<T>`—that is, a function from `T` to `boolean`.
Often, you want to transform the values in a stream in some way. Use the `map` method and pass the function that
carries out the transformation.

```java
Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);
```

Here, we used `map` with a method reference. Often, you will use a lambda expression instead:

```java
Stream<String> firstLetters = words.stream().map(s -> s.substring(0, 1));
```

Occasionally, you are faced with a mapping function that generates an optional result, or more than one result.

You will get a stream of streams, like `[. . . ["y", "o", "u", "r"], ["b", "o", "a", "t"], . . .]`. To flatten it out 
to a single stream `[. . . "y", "o", "u", "r", "b", "o", "a", "t", . . .]`, use the `flatMap` method instead of `map`:

```java
Stream<String> flatResult = words.stream().flatMap(w -> codePoints(w)); // Calls codePoints on each word and flattens the results
```

Next, we need to transform each of the integers to a string, there is a technical difficulty. The `codePoints` method
produces an `IntStream`, which is a little different from a `Stream<Integer>`. Instead of `map`, you use the `mapToObj`
method:

```java
public static Stream<String> codePoints(String s) {
    return s.codePoints().mapToObj(cp -> new String(new int[] { cp }, 0, 1));
}
```

When using `flatMap`, you provide a method that produces a new stream for every stream element. As you have seen, that
can be tedious. It can also be a little inefficient. The `mapMulti` method offers an alternative. Instead of producing
a stream of results, you generate the results and pass them to a collector—an object of a class implementing the
functional interface `Consumer`. For each result, invoke the collector’s `accept` method.

When calling `mapMulti`, you provide a function that is invoked with the stream element and the collector. In your
function, pass your results to the collector:

```java
Stream<String> result = words.stream().mapMulti((s, collector) -> {
    int i = 0;
    while (i < s.length()) {
        int cp = sentence.codePointAt(i);
        collector.accept(cp);
        if (Character.isSupplementaryCodePoint(cp)) i += 2;
        else i++;
    }
});
```

> <R> Stream<R> mapMulti(BiConsumer<? super T,? super Consumer<R>> mapper)
For each stream element, the mapper is invoked, and all elements passed to the Consumer during invocation are added to
the result stream.


### Extracting Substreams and Combining Streams
`stream.limit(n)` returns a new stream that ends after `n` elements. \
The call `stream.skip(n)` does the exact opposite. It discards the first `n` elements. This is handy to skip the unwanted element.

```java
Stream<String> words = Stream.of(contents.split("\\PL+")).skip(1);
```

The `stream.takeWhile(predicate)` call takes all elements from the stream while the predicate is true, and then stops.

```java
Stream<String> initialDigits = codePoints(str).takeWhile(s -> "0123456789".contains(s));
```

The `dropWhile` method does the opposite.

```java
Stream<String> withoutInitialWhiteSpace = codePoints(str).dropWhile(s -> s.strip().length() == 0);
```

You can concatenate two streams with the static `concat` method.

```java
Stream<String> combined = Stream.concat(codePoints("Hello"), codePoints("World")); // Yields the stream ["H", "e", "l", "l", "o", "W", "o", "r", "l", "d"]
```

Of course, the first stream should not be infinite.

### Other Stream Transformations

The `distinct` method returns a stream that yields elements from the original stream, in the same order, except that
duplicates are suppressed.

```java
Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
// Only one "merrily" is retained
```

For sorting a stream, there are several variations of the `sorted` method. One works for streams of `Comparable`
elements, and another accepts a `Comparator`.

```java
Stream<String> longestFirst = words.stream().sorted(Comparator.comparing(String::length).reversed());
```

Finally, the `peek` method yields another stream with the same elements as the original, but a function is invoked every
time an element is retrieved. That is handy for debugging.

```java
Object[] powers = Stream.iterate(1.0, p -> p * 2).peek(e -> System.out.println("Fetching " + e)).limit(20).toArray();
```

### Simple Reductions

The methods covered in this section are called reductions. Reductions are terminal operations. They reduce the stream to
a nonstream value that can be used in your program. You have already seen a simple reduction: the `count` method.

Other simple reductions are `max` and `min` that return the largest or smallest value. There is a twist—these methods
return an `Optional<T>` value that either wraps the answer or indicates that there is none (because the stream happened
to be empty). In the olden days, it was common to return `null` in such a situation. But that can lead to null pointer
exceptions when it happens in an incompletely tested program. The `Optional` type is a better way of indicating a missing
return value.

The `findFirst` returns the first value in a nonempty collection. It is often useful when combined with `filter`.

```java
Optional<String> startsWithQ = words.filter(s -> s.startsWith("Q")).findFirst();
```

If you are OK with any match, not just the first one, use the `findAny` method. This is effective when you parallelize
the stream.

```java
Optional<String> startsWithQ = words.parallel().filter(s -> s.startsWith("Q")).findAny();
```

If you just want to know if there is a match, use `anyMatch`. That method takes a predicate argument, so you won't
need to use `filter`.

```java
boolean aWordStartsWithQ = words.parallel().anyMatch(s -> s.startsWith("Q"));
```

There are methods `allMatch` and `noneMatch` that return true if all or no elements match a predicate.

### The Optional Type
An `Optional<T>` object is a wrapper for either an object of type `T` or no object. The `Optional<T>` type is intended
as a safer alternative for a reference of type `T` that either refers to an object or is null. But it is only safer if
you use it right. It is kind of like optional chaining `?.` in TypeScript.


#### Getting an Optional Value
The key to using Optional effectively is to use a method that either produces an alternative if the value is not present,
or consumes the value only if it is present. \ 
We will look at the first strategy here. Use a default value.

```java
String result = optionalString.orElse("");
// The wrapped string, or "" if none

String result = optionalString.orElseGet(() ->  System.getProperty("myapp.default"));
// The function is only called when needed

String result = optionalString.orElseThrow(IllegalStateException::new);
// Supply a method that yields an exception object
```

#### Consuming an Optional Value
The other strategy for working with optional values is to consume the value only if it is present.

```java
optionalValue.ifPresent(v -> Process v);
optionalValue.ifPresent(v -> results.add(v));
optionalValue.ifPresent(results::add);
```

If you want to take one action if the Optional has a value and another action if it doesn't, use `ifPresentOrElse`:

```java
optionalValue.ifPresentOrElse(v -> System.out.println("Found " + v), () -> logger.warning("No match"));
```

#### Pipelining Optional Values
Another useful strategy is to keep the `Optional` intact, without changes. You can transform the value inside an `Optional`
by using the `map` method:

```java
Optional<String> transformed = optionalString.map(String::toUpperCase);
```

If `optionalString` is empty, then transformed is also empty. \
Here is another example. We add a result to a list if it is present:

```java
optionalValue.map(results::add);
```

If `optionalValue` is empty, nothing happens.

> This map method is the analog of the `map` method of the `Stream` interface. Simply imagine an optional value as a
> stream of size zero or one. The result again has size zero or one, and in the latter case, the function has been applied.

Similarly, you can use the `filter` method to only consider `Optional` values that fulfill a certain property before or
after transforming it.

```java
Optional<String> transformed = optionalString.filter(s -> s.length() >= 8).map(String::toUpperCase);
```

You can substitute an alternative `Optional` for an empty `Optional` with the `or` method. The alternative is computed
lazily (if the value is not there).

```java
Optional<String> result = optionalString.or(() -> // Supply an Optional
```

#### How Not to Work with Optional Values
The get method gets the wrapped element of an Optional value if it exists, or throws a `NoSuchElementException` if it
doesn’t. Therefore

```java
Optional<T> optionalValue = . . .;
optionalValue.get().someMethod()
```
is no safer than
```java
T value = . . .;
value.someMethod();
```
The isPresent and isEmpty methods report whether or not an Optional<T> object has a value. But
```java
if (optionalValue.isPresent())
optionalValue.get().someMethod();
```
is no easier than
```java
if (value != null) value.someMethod();
```

> Java 10 introduced `orElseThrow`, a scarier-sounding synonym for the `get` method. When you call `optionalValue.orElseThrow()
> .someMethod()`, it becomes explicit that an exception will occur if the `optionalValue` is empty. The hope is that
> programmers will only use `orElseThrow` when it is absolutely clear that this cannot happen.

Here are a few more tips for the proper use of the Optional type:
- A variable of type `Optional` should never be null.
- Don't use fields of type `Optional`. The cost is an additional object. Inside a class, use `null` for an absent field.
To discourage `Optional` fields, the class is not serializable.
- Method parameters of type `Optional` are questionable. They make the call unpleasant in the common case where the
requested value is present. Instead, consider two overloaded versions of the method, with and without the parameter.
(On the other hand, returning an `Optional` is fine. It is the proper way to indicate that a function may not have a
result.)
- Don't put `Optional` objects in a set, and don't use them as keys for a map. Collect the values instead.

#### Creating Optional Values
If you want to write a method that creates an `Optional` object, there are several static methods for that purpose,
including `Optional.of(result) and Optional.empty()`

```java
public static Optional<Double> inverse(Double x)  {
    return x == 0 ? Optional.empty() : Optional.of(1 / x);
}
```

The `ofNullable` method returns `Optional.of(obj)` if `obj` is not null and` Optional.empty()` otherwise.

#### Composing Optional Value Functions with flatMap
```java
Optional<U> result = s.f().flatMap(T::g);
```

Optional has `.flatmap()` if `s.f()` is present, then `g` is applied to it. Otherwise, an empty `Optional<U>` is returned.

> The Optional.flatMap method works in the same way. By mapping an optional-yielding method to an optional, you get an
> optional of optional, which is then flattened out.

#### Turning an Optional into a Stream
The stream method turns an `Optional<T>` into a `Stream<T>` with zero or one element.

Suppose you have a stream of user IDs and a method - `Optional<User> lookup(String id)` \
How do you get a stream of users, skipping those IDs that are invalid? \ 
Of course, you can filter. But that uses the `isPresent` and `get` methods that we warned about. It is more elegant to call

```java
Stream<User> users = ids.map(Users::lookup).flatMap(Optional::stream);
```

Each call to stream returns a stream with zero or one element. The flatMap method combines them all. That means the
nonexistent users are simply dropped.

### Collecting Results
To look at the stream results you can call the `iterator` method, which yields an oldfashioned iterator to visit the elements.
Alternatively, you can call the forEach method:

```java
stream.forEach(System.out::println);
```

On a parallel stream, the `forEach` method traverses elements in arbitrary order. If you want to process them in stream
order, call `forEachOrdered` instead, or some parallelism.

Sometimes, you will want to collect the result in a data structure. `toList` method that yields a list of the stream
elements. `toArray` returns an Object[] array. If you want an array of the correct type, pass in the array constructor:

```java
String[] result = stream.toArray(String[]::new); // stream.toArray() has type Object[]
```

For collecting stream elements to another structure (to calculating some result), there is a convenient collect method
that takes an instance of the `Collector` interface. A collector is an object that accumulates elements and produces a
result. The `Collectors` class provides a large number of factory methods for common collectors. Before the `toList`
method was added in Java 16, you had to use the collector produced by `Collectors.toList()`:

```java
List<String> result = stream.collect(Collectors.toList());
```

The collection might not be mutable, serializable, or threadsafe. If you want to control which kind of collection you
get, use the following call instead:
```java
TreeSet<String> result = stream.collect(Collectors.toCollection(TreeSet::new));
```

Suppose you want to collect all strings in a stream by concatenating them. You can call

```java
String result = stream.collect(Collectors.joining());
```

If you want a delimiter between elements, pass it to the joining method:

```java
String result = stream.collect(Collectors.joining(", "));
```

If your stream contains objects other than strings, you need to first convert them to strings, like this:

```java
String result = stream.map(Object::toString).collect(Collectors.joining(", "));
```

If you want to reduce the stream results to a sum, count, average, maximum, or minimum, use one of the 
`summarizing(Int|Long|Double)` methods. These methods take a function that maps the stream objects to numbers and yield
a result of type `(Int|Long|Double)SummaryStatistics`, simultaneously computing the sum, count, average, maximum, and
minimum.

```java
IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String::length));
double averageWordLength = summary.getAverage();
double maxWordLength = summary.getMax();
```

### Collecting into Maps
The `Collectors.toMap` method has two function arguments that produce the map’s keys and values.

```java
Map<Integer, String> idToName = peopleStream.collect(Collectors.toMap(Person::id, Person::name));
```

In the common case when the values should be the actual elements, use `Function.identity()` for the second function.

```java
Map<Integer, Person> idToPerson = people.collect(
Collectors.toMap(Person::id, Function.identity()));
```

If there is more than one element with the same key, there is a conflict, and the collector will throw an
`IllegalStateException`. You can override that behavior by supplying a third function argument that resolves the
conflict and determines the value for the key, given the existing and the new value.

```java
Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
Map<String, String> languageNames = locales.collect(
        Collectors.toMap(Locale::getDisplayLanguage,loc -> loc.getDisplayLanguage(loc), (existingValue, newValue) -> existingValue)
);
```

If you want a TreeMap, supply the constructor as the fourth argument. You must provide a merge function

> For each of the toMap methods, there is an equivalent toConcurrentMap method that yields a concurrent map.

### Grouping and Partitioning
Forming groups of values with the same characteristic is very common, so the groupingBy method supports it directly.

Grouping locales by country. First, form this map:

```java
Map<String, List<Locale>> countryToLocales = locales.collect(Collectors.groupingBy(Locale::getCountry));
```

Call groupBy without a callback for Map values - will use default `Collection.toList()` callback.
The function `Locale::getCountry` is the classifier function of the grouping. You can now look up all locales for a
given country code, for example

```java
List<Locale> swissLocales = countryToLocales.get("CH"); // Yields locales de_CH, fr_CH, it_CH, and maybe more
```
 
> Each locale has a language code (such as en for English) and a country code (such as US for the United States). The
> locale en_US describes English in the United States, and en_IE is English in Ireland. Some countries have multiple
> locales. For example, ga_IE is Gaelic in Ireland, and, as the preceding example shows, the JDK knows at least three
> locales in Switzerland.

When the classifier function is a predicate function, the stream elements are partitioned into two lists: those where
the function returns true and the complement. In this case, it is more efficient to use partitioningBy instead of 
`groupingBy`. 

### Downstream Collectors
The `groupingBy` method yields a map whose values are lists. If you want to process those lists in some way, supply a
downstream collector. For example, if you want sets instead of lists, you can use the `Collectors.toSet` collector that
you saw in the preceding section:

```java
Map<String, Set<Locale>> countryToLocaleSet = locales.collect(groupingBy(Locale::getCountry, toSet()));
```

Several collectors are provided for reducing collected elements to numbers:
- counting produces a count of the collected elements. For example,

```java
Map<String, Long> countryToLocaleCounts = locales.collect(groupingBy(Locale::getCountry, counting()));
```

- summing(Int|Long|Double) and averaging(Int|Long|Double) take a function argument, apply the function to the downstream
elements, and produce their sum or average.

```java
Map<String, Integer> stateToCityPopulation = cities.collect(groupingBy(City::state, averagingInt(City::population)));
```

- maxBy and minBy take a comparator and produce maximum and minimum of the downstream elements.

```java
Map<String, Optional<City>> stateToLargestCity = cities.collect(
        groupingBy(City::state, maxBy(Comparator.comparing(City::population)))
);
```

The collectingAndThen collector adds a final processing step behind a collector.

```java
Map<Character, Integer> stringCountsByStartingLetter = strings.collect(
        groupingBy(s -> s.charAt(0), collectingAndThen(toSet(), Set::size))
);
```

The mapping collector does the opposite. It applies a function to each
collected element and passes the results to a downstream collector.

```java
Map<Character, Set<Integer>> stringLengthsByStartingLetter = strings.collect(
    groupingBy(s -> s.charAt(0), mapping(String::length, toSet()))
);
```

There is a flatMapping method as well, for use with functions that
return streams.
If the grouping or mapping function has return type int, long, or
double, you can collect elements into a summary statistics object,

```java
Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.collect(
        groupingBy(City::state, summarizingInt(City::population))
);
```

The filtering collector applies a filter to each group, for example:

```java
Map<String, Set<City>> largeCitiesByState = cities.collect(
        groupingBy(City::state, filtering(c -> c.population() > 500000, toSet()))
); // States without large cities have empty sets
```

Finally, you can use the teeing collector to branch into two parallel downstream collections. This is useful whenever
you need to compute more than one result from a stream.

```java
record Pair<S, T>(S first, T second) {}
Pair<List<String>, Double> result = cities.filter(c -> c.state().equals("NV")).collect(
    teeing(mapping(City::name, toList()), // First downstream collector
    averagingDouble(City::population), // Second downstream collector
    (list, avg) -> new Result(list, avg)) // Combining function
); 
```
Use is with `groupingBy` or `partitioningBy` to process the “downstream” map values. Otherwise, simply apply methods
such as map, reduce, count, max, or min directly on streams.

### Reduction Operations
The `reduce` method is a general mechanism for computing a value from a stream.

```java
List<Integer> values = . . .;
Optional<Integer> sum = values.stream().reduce((x, y) -> x + y);
```

> In this case, you can write reduce(Integer::sum) instead of reduce((x, y) -> x + y).

If you want to use reduction with parallel streams, the operation must be _associative_: It shouldn't matter in which
order you combine the elements.

Often, there is an _identity_ of an initial value that can be used as the start of the computation.
```java
List<Integer> values = . . .;
Integer sum = values.stream().reduce(0, (x, y) -> x + y); // Computes 0 + v0 + v1 + v2 + . . .
```
The identity value is returned if the stream is empty, and you no longer need to deal with the _Optional_ class.

When the computation is parallelized, there will be multiple computations of this kind, and you need to combine their
results. You supply a second function for that purpose. The complete call is

```java
int result = words.reduce(0, (total, word) -> total + word.length(), (total1, total2) -> total1 + total2);
```

> In the above example, you could have called `words.mapToInt(String::length).sum()`, which is both simpler and more
> efficient since it doesn’t involve boxing.

### Primitive Type Streams
So far, we have collected integers in a `Stream<Integer>`, even though it is clearly inefficient to wrap each integer 
into a wrapper object. The stream library has specialized types `IntStream`, `LongStream`, and `DoubleStream` that store
primitive values directly, without using wrappers.

To create an IntStream, call the IntStream.of and Arrays.stream methods:

```java
IntStream stream = IntStream.of(1, 1, 2, 3, 5);
stream = Arrays.stream(values, from, to); // values is an int[] array
```

`IntStream` and `LongStream` have static methods `range` and `rangeClosed` that generate integer ranges. \
When you have a stream of objects, you can transform it to a primitive type stream with the `mapToInt`, `mapToLong`, or
`mapToDouble` methods.

```java
Stream<String> words = . . .;
IntStream lengths = words.mapToInt(String::length);
```

To convert a primitive type stream to an object stream, use the boxed method:
```java
Stream<Integer> integers = IntStream.range(0, 100).boxed();
```

The methods on primitive type streams are analogous to those on object streams. Here are the most notable differences:
- The toArray methods return primitive type arrays.
- Methods that yield an optional result return an OptionalInt, OptionalLong, or OptionalDouble. These classes are analogous
to the Optional class, but they have methods getAsInt, getAsLong, and getAsDouble instead of the get method.
- There are methods sum, average, max, and min that return the sum, count, average, maximum, and minimum. These methods
are not defined for object streams.
- The summaryStatistics method yields an object of type IntSummaryStatistics, LongSummaryStatistics, or 
DoubleSummaryStatistics that can simultaneously report the sum, count, average, maximum, and minimum of the stream.

> The Random class has methods ints, longs, and doubles that return primitive type streams of random numbers.

### Parallel Streams
Streams make it easy to parallelize bulk operations. The process is mostly automatic, but you need to follow a few rules.
First of all, you must have a parallel stream. You can get a parallel stream from any collection with the 
`Collection.parallelStream()` method:
```java
Stream<String> parallelWords = words.parallelStream();
```
Moreover, the parallel method converts any sequential stream into a parallel one.
```java
Stream<String> parallelWords = Stream.of(wordArray).parallel();
```
When stream operations run in parallel, the intent is that the same result is returned as if they had run serially. It
is important that the operations are stateless and can be executed in an arbitrary order.

Don't turn all your streams into parallel streams in the hope of speeding up operations. Keep these issues in mind:
- There is a substantial overhead to parallelization that will only pay off for very large data sets.
- Parallelizing a stream is only a win if the underlying data source can be effectively split into multiple parts.
- The thread pool that is used by parallel streams can be starved by blocking operations such as file I/O or network access.

Parallel streams work best with huge in-memory collections of data and computationally intensive processing.
