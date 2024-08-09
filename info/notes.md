#### DTO
Data transfer object, in most of the cases like **POJO** (plain old java object), simple classes that are used to
transfer data between layers of application. Service layer to presentational layer, over the network. Often
serializable. Does not contain (unlike POJO) business logic, or any behavior.

#### Swagger (OpenAPI)
Documentation is generated from the annotations that are added to the code via dependencies and plugins in pom.xml

#### JUnit Jupiter
In Junit is one of the parts from JUnit5, that brings all new cool features to JUnit. There is also _JUnit Platform_ for
launching tests on JVM and _JUnit Vantage_ that supports JUnit 3 and JUnit 4.

#### JavaBeans (not Spring beans)
Were used to have classes that are hidden from the user and have getters and setters. Have no constructor and serializable.
It is abandoned because in modern Java there is _Record_ as an immutable object, many tools for JSON/XML bindings and
mappings, dependency injection frameworks like _Spring_, and getters and setters generators like _Lombok_. Those things
substitute JavaBeans with more modern approaches.

#### @Autowired
Spring dependency injection instantiate annotated field for you. You can use it for class field, method parameter or
constructor parameter. Dependencies that can be instantiated annotated with @Bean and lie in the classes annotated special
annotations, like @Configuration.

#### @Bean
In Spring declare a method as a bean producer, to be managed by the Spring IoC (Inversion of Control) container.
Each container has a few contexts, Spring Application Context - a central interface to Spring's IoC container, managing
the lifecycle and configuration of beans.
It's typically used within a _@Configuration_ class. You can customise it a bit, and also declare a _@Scope_:
- Singleton: (Default) Only one instance of the bean is created and shared throughout the Spring container.
- Prototype: A new instance of the bean is created every time it is requested from the container.
- Request: A new instance is created for each HTTP request. (Web applications only)
- Session: A new instance is created for each HTTP session. (Web applications only)
- Global Session: A new instance is created for each global HTTP session. (Portlet-based web applications)

Can also be used in with _@Component, @Service, @Controller,_ or _@Repository_ classes, the difference with _@Configuration_
that bean within _@Configuration_ will behave as a singleton, there will be single instance with a Spring container, and
with rest it will be the new instance each time. 

A bit about other annotations:
- @Component: A generic stereotype annotation indicating a Spring-managed component.
- @Service: Specialization of @Component indicating that the class provides some business services (service layer,
business logic of the app)
- @Repository: indicating a Data Access Object (DAO) and will interact with the database (automatic
exception translation from database)
- @Controller: indicating Spring MVC controller, classes that handle HTTP requests and return web responses.

#### Mockito
Mocking framework for unit testing in Java
- Mocking: Creating a mock object that simulates the behavior of a real object. 
- Stubbing: Defining the behavior of a mock object when specific methods are called.
- Verification: Ensuring that certain methods were called on the mock object with expected arguments.

```java
MyService myService = Mockito.mock(MyService.class);
Mockito.when(myService.someMethod()).thenReturn("some value");
Mockito.verify(myService).someMethod();
```

Spring Boot provides annotations like _@MockBean_ to integrate Mockito mocks into the Spring context easily.
- @MockBean: used to add mocks to the Spring application context, replacing a real bean with a mock in your test. When 
you mockbean it, you can define behavior.
- @SpyBean: Similar to @MockBean, but allows partial mocking of real beans, meaning some methods can be mocked while
others retain their original behavior
- @Captor used to create and automatically initialize an argument captor, a feature that allows you to capture arguments
passed to a mock method for further assertions or verification.


#### @ActiveProfiles
A powerful annotation that allows you to control the active profiles in Spring's environment during testing, making
it easier to manage and isolate different configurations based on the environment or testing needs. _@ActiveProfiles_ is
typically used at the class level on test classes annotated with _@SpringBootTest_, _@ContextConfiguration_, or similar
annotations that bootstrap the Spring context. If a test class uses _@ActiveProfiles_, it can be inherited by subclasses,
meaning that all subclasses will also use the specified active profiles unless they override them. \
There should be _application.properties_ or _application.yml_ file somewhere. If profile is "unit", there should be
_application-unit.properties_ file somewhere. Maybe in config, or in root folder, or in _classpath:_.

#### @SpringBootTest
Commonly used annotation in the Spring Boot framework for writing integration tests. It is used to create an application
context and run your tests in an environment similar to how your application would run in production.

Application Context Creation:
- @SpringBootTest loads the complete Spring application context, which means that all the beans and dependencies are
available during the test. This is in contrast to unit tests, where typically only a subset of the application context
is loaded, or sometimes none at all.

Web Environment Configuration:
It starts a server and put your app in this server, so you don't need to do it by your own.
- WebEnvironment.MOCK (default): Loads a mock web environment, meaning no real HTTP server is started, and the test
interacts with the application in a mock servlet environment.
- WebEnvironment.RANDOM_PORT: Starts the web server on a random port and injects the port number into your tests. Useful
for testing full-stack components. If you do nothing it will start it on localhost.
- WebEnvironment.DEFINED_PORT: Starts the web server on the port defined in the application properties or configuration.
- WebEnvironment.NONE: No web environment is started. Useful for non-web tests.

Auto-configuration:
- @SpringBootTest triggers Spring Boot’s auto-configuration, so any configuration that is automatically applied when your
application starts will also be applied during your tests. This makes it ideal for integration testing, where you want
to ensure that all aspects of your application are correctly configured and working together.

@SpringBootTest can be used alongside other Spring testing annotations such as @MockBean, @SpyBean, @ActiveProfiles, etc.

#### @AutoConfigureWebTestClient
Annotation in Spring Boot is used in testing to automatically configure and inject a WebTestClient instance into your 
test class and connects WebClient to your Application Service Context started earlier. From that context it knows what
is the baseUrl and port of the app, so you don't need to set it in your webTestClient, just hit the path.

#### WebTestClient
Is a reactive client for testing web applications and is typically used for testing Spring WebFlux applications,
though it can also be used to test non-reactive (Spring MVC) applications.

#### @DynamicPropertySource
Annotation in Spring Boot is used to dynamically configure properties for tests at runtime. It allows you to supply
properties that will be available in the Spring Environment during the test execution, making it particularly useful
for setting up environment-dependent or context-sensitive properties.

A common use case is when using tools like Testcontainers to spin up a Docker container during tests. Since the
container's properties (like the exposed port) are determined at runtime, @DynamicPropertySource can be used to inject
these properties into the Spring Environment.

The method annotated with `@DynamicPropertySource` must be `static` and accept a single argument of type _DynamicPropertyRegistry_.
The _DynamicPropertyRegistry_ is a callback interface that allows you to register properties as key-value pairs.

####  CDATA in XML
XML feature known as CDATA (Character Data) used to include text data that should not be parsed by the XML parser. When
you wrap text in a CDATA section, it tells the parser to treat everything inside as literal text, not as markup.

### Lombok 
aims to reduce boilerplate code by automatically generating commonly used methods and code patterns. It uses annotations
to generate code at compile time, allowing developers to write cleaner and more concise code. When Java compiler runs,
Lombok's annotations are processed, and the corresponding methods or code patterns are injected into the generated 
_.class_ files. This means that the boilerplate code does not appear in your source code but exists in the compiled bytecode.
Annotations can be configured `@EqualsAndHashCode(callSuper = true)`

1. Automatic Getter and Setter Generation:
```java
@Getter @Setter
public class User {
    private String name;
    private int age;
}
```
2. Constructors:
   1. _@NoArgsConstructor_: Generates a no-argument constructor.
   2. _@RequiredArgsConstructor_: Generates a constructor with required arguments (i.e., final fields or fields with 
   constraints like _@NonNull_). 
   3. _@AllArgsConstructor_: Generates a constructor with all fields as arguments.
3. _@ToString_ - Generates a toString() method that includes all fields or selected fields.
4. _@EqualsAndHashCode_ - Generates equals() and hashCode() methods based on the fields of the class.
5. _@Data_ - A convenience annotation that bundles _@Getter_, _@Setter_, _@ToString_, _@EqualsAndHashCode_, and 
_@RequiredArgsConstructor_ into one.
6. _@Builder_ - Implements the builder pattern for your class, providing a fluent API to construct objects.
```java
@Builder
public class User {
    private String name;
    private int age;
}
// User.builder().name("John").age(25).build();.
```
7. @Value - Makes a class immutable, similar to a combination of @Data and final for all fields. All fields are made
private and final, and no setters are generated only getters.
8. @Slf4j and Other Logging Annotations - Automatically generates a logger for the class, reducing the boilerplate needed
to instantiate a logger. Adds a `private static final Logger log = LoggerFactory.getLogger(UserService.class)` to the 
class so you can use it right away.
9. @SneakyThrows - Suppresses checked exceptions, allowing you to throw them without declaring them.

#### MapStruct
Java library used for generating type-safe, performant, and easy-to-use mappers that map between different object models
(e.g., DTOs to entities). It works at compile-time to generate the implementation of mapping interfaces based on simple
method signatures and annotations.

- @Mapper: annotaion for the interface that defines methods for mapping between different object types. MapStruct
automatically generates the implementation for the mapper interface at compile time.
- @Mapping: used within mapper methods to define how specific fields should be mapped between source and target objects.
It allows you to customize the mapping process for individual fields, especially when the field names or types don't
match exactly.
```java
@Mapper
public interface UserMapper {
    @Mapping(source = "name", target = "fullName")
    UserDTO toUserDTO(User user);
}
// Here, the name field in User is mapped to the fullName field in UserDTO.
```
- @Named annotation is used to assign a specific name to a method, allowing it to be referenced in @Mapping annotations
especially when reusing methods or applying custom logic. You can use @Named to define reusable conversion methods or
complex mappings that can be applied across multiple mapping methods.
```java
@Mapper
public interface UserMapper {

    @Mapping(source = "birthDate", target = "age", qualifiedByName = "calculateAge")
    UserDTO toUserDTO(User user);

    @Named("calculateAge")
    default int calculateAge(Date birthDate) {
        // Custom logic to calculate age from birth date
        return Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears();
    }
}
```
