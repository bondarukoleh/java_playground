# Generic Programming
## Why Generic Programming?
Generic programming means writing code that can be reused for objects of many different types. \
Generics offer a _type parameters_. The ArrayList class now has a _type parameter_ that indicates the element type:
```java
var files = new ArrayList<String>();
```
### Who Wants to Be a Generic Programmer?
It is not so easy to implement a generic class. \
A programmer may want to add all elements from an _ArrayList<Manager>_ to an _ArrayList<Employee>._ \
But, of course, doing it the other way round should not be legal. How do you allow one call and disallow the other?
The Java language designers invented an ingenious new concept, the _wildcard type_, to solve this problem. Wildcard
types are rather abstract, but they allow a library builder to make methods as flexible as possible.

## Defining a Simple Generic Class
