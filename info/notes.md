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

### Spring IoC (Inversion of Control) container
The IoC container's primary function is to implement _Inversion of Control_ by injecting dependencies into objects
rather than having those objects create or look for dependencies themselves. This is a core concept in Spring's approach
to _Dependency Injection (DI)_. IoC container is the core of the Spring Framework. It is responsible for managing the
lifecycle of beans, including their creation, configuration, and destruction. \
"Container" is a metaphor, it means it contains and manages all beans and dependencies application needs.

DI provided by @Configuration class is better than directly instantiate the dependencies in service classes because:
- Separation of Concerns, service class does not mix logic of creating dependencies with his business logic;
- Flexibility in Configuration (Multiple Implementations), you can change the implementation of dependencies without
touching the service class, just swap config class or by Spring profiles, property files, or conditional beans
- Easier Testing, you can easily provide mock implementations for dependencies by injecting them;
- Centralized Configuration Management, if multiple classes need dependencies, each would need to instantiate them on its
own, with DI the configuration is centralized in @Configuration, ensuring consistency and reducing redundancy.
- Decoupling from Frameworks: you can use different DI framework that provides your dependencies without many changes of
the source code.

The _Spring Application Context is_ an extension of _IoC container_. It not only includes all the features of the basic
IoC container but also provides additional enterprise-specific functionalities (Event Propagation, Internationalization,
Environment Abstraction, Resource Loading). 

Some key annotations that help bootstrap the Spring context:
- @Configuration. Marks a class as a source of bean definitions, indicates that the class can be used by the Spring IoC
container as a configuration class.
- @ComponentScan. Tells Spring where to look for components (@Component, @Service, @Repository, @Controller, etc.) to 
auto-detect and register them as beans in the context. Often is used with @Configuration.
- @EnableAutoConfiguration. Enables Spring Boot’s auto-configuration mechanism, which automatically configures your
Spring application, helps in setting up the context by scanning the classpath and setting up beans based on what’s available
- @SpringBootApplication. Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan, used in main class.
- @Import. Allows to add extra classes to @Configuration class and so into the Spring context. Useful for modularizing
configuration and combining them into a single application context.
- @PropertySource. Used to add property files to the Spring Environment, allowing you to use properties from external
files, typically used in @Configuration
- @Bean (below)

How these all work together? \
When you start a Spring application, these annotations play a critical role in setting up the application context:
- @SpringBootApplication (or the combination of @Configuration, @ComponentScan, and @EnableAutoConfiguration) tells 
Spring to start scanning the specified packages, load the necessary configurations, and automatically configure components.
- @ComponentScan ensures that all the classes with relevant annotations (@Component, @Service, etc.) are detected and
registered as beans.
- @Configuration and @Bean methods explicitly define beans that should be managed by Spring.
- @PropertySource adds properties that can be injected into the beans.

This orchestration of annotations bootstraps the Spring context, allowing your application to start with all the
necessary configurations and beans in place.

#### @Autowired
Spring dependency injection instantiate annotated field for you. You can use it for class field, method parameter or
constructor parameter. Dependencies that can be instantiated annotated with @Bean and lie in the classes annotated special
annotations, like @Configuration.

#### @Bean
In Spring declare a method as a bean producer, to be managed by the Spring IoC (Inversion of Control) container.
@BBean is typically used within a _@Configuration_ class (a source of bean definitions). You can customise it a bit,
and also declare a _@Scope_:
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

### Some Spring Web annotations
#### @RestController
A specialized version of the _@Controller_ annotation:
- A combination of _@Controller_ and _@ResponseBody_. It tells Spring that the class is a web controller (means handles
  HTTP requests and return web responses), and because of ResponseBody - all the methods within it should return data
  directly in the response body rather than rendering a view.
- Handles JSON/XML Responses by Default,  automatically converts the return value of each method into JSON (or XML if
  configured) and sends it as the HTTP response. Perfect for creating RESTful services.

#### @RequestMapping("/path")
Can be applied at both the class level and the method level, allowing you to define request paths, request methods, and
other attributes such as parameters and headers that the handler should match. \
- Sets a base path for all endpoints in this controller.
- By default, handles all the methods to this path, but you can restrict them `@RequestMapping(value = "/hello", method = RequestMethod.GET)`
  Can be done with special annotations: `@GetMapping, @PostMapping, etc.`
- `params`: Restrict handler methods to requests that contain specific query parameters `@RequestMapping(value = "/hello", params = "name")`
- `headers`: Restrict handler methods to requests with specific headers.
- `produces`: Specifies the type of media types that the method can produce (e.g., application/json).

#### @GetMapping("/path"), @PostMapping(), etc.
A shorthand for `@RequestMapping(value = "/hello", method = RequestMethod.GET)` on method level.

#### @RequestParam
You can use it methods annotations `@GetMapping, @PostMapping, etc.`, it tells that this method will handle request only
with a specific parameter.
```java
@GetMapping("/greet")
public String greet(@RequestParam String name) {
    return "Hello, " + name;
}
```
Here, the greet method will handle GET requests like `/greet?name=John`

#### @PathVariable
Can be used with path variables to capture parts of the URL
```java
@GetMapping("/users/{id}")
public String getUserById(@PathVariable String id) {
    return "User ID: " + id;
}
```
will handle GET requests like `/users/123` and return `"User ID: 123"`.

#### @RequestBody
You can capture this data in your handler method using the @RequestBody annotation:
```java
@PostMapping("/users")
public String createUser(@RequestBody User user) {
    return "User created: " + user.getName();
}
```
The createUser method will handle POST requests to /users and expects a JSON (or other format) representation of a User
object in the request body.

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
- _@Named_ annotation is used to assign a specific name to a method, allowing it to be referenced in @Mapping annotations
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

#### Jackson
Java library used for converting Java objects to and from JSON. It is widely used in applications that need to serialize
Java objects into JSON for storage, communication, or logging purposes, and to deserialize JSON back into Java objects.

- ObjectMapper: core class of the Jackson library. It provides functionality for serialization and deserialization.
```java
ObjectMapper objectMapper = new ObjectMapper();
// Serialize Java object to JSON
String jsonString = objectMapper.writeValueAsString(someObject);
// Deserialize JSON to Java object
SomeClass someObject = objectMapper.readValue(jsonString, SomeClass.class);
```
- Annotations:
  - _@JsonProperty_: Used to specify the JSON property name for a field.
  - _@JsonIgnore_: Used to ignore a field during serialization and deserialization.
  - _@JsonInclude_: Controls whether to include properties in the JSON output based on criteria like non-null or non-empty.
    `@JsonInclude(JsonInclude.Include.NON_NULL)`
  - _@JsonFormat_: Used to specify the format for date and time fields during serialization and deserialization.
    `@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")`
  - _@JsonCreator_ and _@JsonSetter_: These are used to control how Jackson deserializes objects, especially when dealing
  with constructors or factory methods.
- Modules: \
  Jackson is highly extensible, customizable and there are modules available for various purposes:
  - _jackson-datatype-jsr310_: For Java 8 Date and Time API support.
  - _jackson-module-kotlin_: For better Kotlin support.
  - _jackson-databind_: Core data-binding functionality that combines JSON with POJOs.
- Data Binding: \
  Simple and Complex. Jackson supports both simple and complex data binding. Simple data binding deals with converting
  JSON to and from simple Java objects like Strings and integers, while complex data binding deals with more intricate
  types like collections, maps, and nested objects.
- Streaming API: \
  Performance-Oriented: Jackson provides a streaming API (`JsonParser` and `JsonGenerator`) that is more performance-oriented
  and allows for reading and writing JSON content token by token. This is useful for processing large amounts of JSON data
  efficiently.
```java
JsonParser parser = objectMapper.getFactory().createParser(jsonString);
while (parser.nextToken() != JsonToken.END_OBJECT) {
    // Process JSON tokens
}
```
- Tree Model: \
  Dynamic Processing: Jackson also offers a tree model (_JsonNode_), where you can parse JSON into a tree of nodes
  (_JsonNode_) and navigate through them. This approach is useful when the JSON structure is dynamic or unknown at
   compile time.
```java
JsonNode rootNode = objectMapper.readTree(jsonString);
String name = rootNode.path("name").asText();
```

### Gatling io.gatling.javaapi
- _Gatling_ Class: This is the entry point for running a simulation in the Java API. It provides methods to define and
configure the simulation.
- _Simulation_ Class: This is where you define your load testing scenario. It typically includes:
  - SetUp: Configuring the user journey (e.g., number of users, duration, and scenario definition).
  - Scenarios: A sequence of HTTP requests that simulate user behavior. 
- _http_ package: This package contains classes for defining HTTP protocols and requests.
  - _HttpDsl_: Contains methods to define HTTP requests, headers, bodies, etc. 
  - _HttpProtocolBuilder_: Used to configure HTTP settings, like base URL, headers, and connection settings.
- Core DSL:
  - _ScenarioBuilder_: This is where you define the scenario, which represents a user's journey or sequence of actions.
  - _ChainBuilder_: Used for defining sequences of actions (like HTTP requests) in a scenario.
- Actions: Classes that define specific actions users will perform during the simulation, like sending HTTP requests,
waiting for a certain amount of time, etc.
- Assertions: Gatling allows you to define assertions to validate the performance criteria, such as response times and
error rates.

`maxConnectionsPerHost` is a configuration setting used in HTTP clients to control the maximum number of concurrent
connections that the client can establish to a single host, number of clients connections. If the simulation tries to 
open more connections, they will either be queued or fail, depending on other configurations.

`exec()` - method is a member of the ScenarioBuilder class. `public ScenarioBuilder exec(ActionBuilder actionBuilder)`
takes an _ActionBuilder_ (which is often an HTTP request, but could be other actions as well) as a parameter and adds
it to the scenario's list of actions.

`http("request_name").post()` returns HttpRequestActionBuilder
```java
ScenarioBuilder scn = scenario("UserLoginScenario")
    .exec(http("LoginPageRequest")
        .get("/login"))
    .pause(1)
    .exec(http("SubmitLoginForm")
        .post("/login")
        .formParam("username", "testuser")
        .formParam("password", "password123"))
    .pause(2)
    .exec(http("DashboardRequest")
        .get("/dashboard"));
```

`setUp()` method is typically placed within a block of code, such as an instance initializer block or a constructor, to
ensure that it is executed when the simulation class is instantiated. This is how Gatling is designed to work. Gatling
simulations are classes that extend the _Simulation_ class. When a simulation is run, an instance of this class is
created, and Gatling expects the setUp() method to be called during this initialization phase to define the load test
configuration.

`injectOpen`: Open Workload Model
- The open workload model simulates a scenario where users arrive independently of each other. This means new users can
be continuously added to the system at a specific rate, regardless of how many users are currently active.
- It’s called "open" because the number of users in the system is not fixed; users can enter or leave the system
independently.
- Used for: Public Websites, Stress Testing (to simulate a continuous stream of traffic to see how the system handles)

`injectClosed`: Closed Workload Model
- The closed workload model simulates a scenario where the number of active users is controlled and kept constant. The
total number of users in the system at any time is fixed, with users typically replacing others as they finish their tasks.
- It’s called "closed" because the number of concurrent users is restricted to a specific number.
- Use Cases: Internal Applications, Capacity Planning (determining how many users the system can support before degrades)

`rampUsersPerSec` simulates a gradually increasing load of virtual users over time. This is part of the open workload
model, where the focus is on the rate of user arrival rather than the total number of concurrent users.
```java
rampUsersPerSec(startRate).to(endRate).during(duration)
```

`getInteger("config_value", 20)` attempts to retrieve an integer value associated with the key _"config_value"_. This key
could be linked to a system property, _environment variable_, or some other _external configuration_. If the value is not
found, the default value (20 in this case) is used.

#### SLF4J 
Placeholder `log.debug("Got a User greeting: \"{}\"", userGreeting);` will handle all data types for you.
You can set up logging in property files:
```text
logging.level.root=INFO
logging.level.com.yourpackage=DEBUG
logging.file.name=logs/spring-boot-application.log
```

### @ControllerAdvice
Is a Spring annotation that allows you to handle exceptions globally across all controllers. \
To that class you need to add specific handler methods annotated:
```java
    @ExceptionHandler(MyCustomExceptionHandles.class)
    public ResponseEntity<?> handleMyException(MyCustomExceptionHandles ex, WebRequest request) {
        /* Your error response class */
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
```
Then in the logic of controller, if you throw:
```java
    @GetMapping("/{id}")
    public String greetById(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new ResourceNotFoundException("Invalid ID: " + id);
        }
        return "Hello, User with ID: " + id;
    }
```
Spring will first check if that exception has a corresponding handler in the `@ControllerAdvice` class.

If you don't want it to handle all exceptions - you can target it to a Specific Controllers with the 
`@ControllerAdvice(basePackages = {"com.example.controller"})` or `@ControllerAdvice(assignableTypes = {YourController.class})`


### @Valid
The @Valid annotation is provided by the Java Bean Validation API (javax.validation package). It is commonly used to 
indicate that an object should be validated before it is processed. \
When applied to a method parameter (usually an incoming request object), Spring will automatically validate the object’s
fields against the constraints defined using Bean Validation annotations (e.g., @NotNull, @Size, etc.).
```java
    @PostMapping
    public String createUser(@RequestBody @Valid User user) {
        // If the 'user' object is valid, this method is executed
        return "User is valid";
    }
```
```java
import javax.validation.constraints.*;

public class User {

    @NotNull(message = "Username cannot be null")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 18, message = "Age should be at least 18")
    private int age;
}
```
If the passed User object violates any of the constraints (e.g., @NotNull, @Size, @Email), Spring will automatically 
return a 400 Bad Request response with validation errors.

### @Validated
The @Validated annotation is provided by the Spring Framework (org.springframework.validation.annotation.Validated).
It is used to indicate that a class or method parameter should be validated, just like `@Valid`. \
The key difference is that `@Validated` supports group-based validation. This allows you to define different validation
rules for different groups of operations (e.g., create vs. update operations).

### jakarta.validation-api and spring-boot-starter-validation
- jakarta.validation-api - Defines the validation annotations and interfaces.
- spring-boot-starter-validation - Bundles the jakarta.validation-api and integrates it into Spring Boot with additional
tools and libraries, like Hibernate Validator, to handle validation seamlessly.

