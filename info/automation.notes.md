# Notes from different projects

### hamcrest
Hamcrest is a popular library for writing test assertions in Java. It provides a flexible and expressive way to construct
and combine matchers, allowing you to create readable and maintainable test cases. It is widely used in testing frameworks
like JUnit. There are also Custom Matchers, Combination of Matchers.
```java
assertThat(actualValue, is(expectedValue));
assertThat(list, hasItem("item"));
assertThat(string, allOf(startsWith("Hello"), endsWith("World")));
```

- `is()` - Checks for equality.
- `not()` - Asserts inequality.
- `hasItem()` - Verifies that a collection contains a specific item.
- `greaterThan()` and lessThan() - Numeric comparisons.


### apiguardian-api
The apiguardian-api library provides the `@API` annotation, which is used to document the status and stability of
public types, methods, constructors, and fields within a Java framework or application. By applying the `@API` annotation,
developers can communicate the intended use and stability level of API elements to consumers, indicating whether they
are stable, experimental, or deprecated.
`@API(status = API.Status.STABLE)`