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

