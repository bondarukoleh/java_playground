# General
REST Assured is a _Java DSL_ for simplifying testing of REST based services built on top of HTTP Builder, can be used
to validate and verify the response of these requests.

Setup:
```xml
<dependency>
      <groupId>io.rest-assured</groupId>
      <artifactId>rest-assured</artifactId>
      <version>5.5.0</version>
      <scope>test</scope>
</dependency>
```

Depends on what you need to parse there is JsonPath and XmlPath setup. \
Interesting approach is to validate only JSON schemas, there is a separate setup for that also `json-schema-validator`.

### Given when then
- Base URI: Set the base URI for the REST API.
- given(): Prepare the request.
- when(): Specify the HTTP method and endpoint.
- then(): Validate the response.
```java
RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
given().
        param("x", "y").
when().
        get("/lotto").
then().
        statusCode(400).
        body("lotto.lottoId", equalTo(6));
```
You can add `and()` as a syntax sugar
```java
given()
    .param("x", "y").and().header("z", "w")
.when()
    .get("/something")
.then()
    .assertThat().statusCode(200).and().body("x.y", equalTo("z"));
```

### Get response body
```java
InputStream stream = get("/lotto").asInputStream(); // Don't forget to close this one when you're done
byte[] byteArray = get("/lotto").asByteArray();
String json = get("/lotto").asString();
```
Getting a value
```java
int id = response.jsonPath().get("userId");
int id = response.path("userId");
String headerValue = response.header("headerName");
```
Getting a value right away from the request
```java
int lottoId = get("/lotto").path("lotto.lottoid");
```

### Invoking HTTP resource
You can use any HTTP verb with your request by making use of the request method.
```java
when().
    request("CONNECT", "/somewhere").
then().
    statusCode(200);
```

### Params
REST Assured will automatically try to determine which parameter type (i.e. query or form parameter) based on the HTTP
method. But sometimes you need to set explicitly. There is a multiparam requests of course.
```java
given().
    formParam("formParamName", "value1").
    queryParam("queryParamName", "value2").
when().
    post("/something");
// or in URL
..when().get("/name?firstName=John&lastName=Doe");
```

### Cookies
```java
given().cookie("username", "John").when().get("/cookie").then().body(equalTo("username"));
// or 
given().cookie("cookieName", "value1", "value2"). ..
// 
Cookie someCookie = new Cookie.Builder("some_cookie", "some_value").setSecured(true).setComment("some comment").build();
given().cookie(someCookie).when().get("/cookie").then().assertThat().body(equalTo("x"));
```

### Request Body
```java
given().body("some body"). .. // Works for POST, PUT and DELETE requests
given().request().body(new byte[]{42}). .. // More explicit (optional)
```

### Verifying Response Data
You can add an assert section to the request call
```java
get("/x").then().assertThat().cookies("cookieName1", "cookieValue1", "cookieName2", "cookieValue2"). ..
get("/x").then().assertThat().statusCode(200). ..
get("/x").then().assertThat().statusLine("something"). ..
get("/x").then().assertThat().header("headerName", "headerValue"). ..
get("/x").then().assertThat().body(equalTo("something")). ..

// From the resp body
// { "userId" : "some-id", "href" : "http://localhost:8080/some-id" }
get("/x").then().body("href", new ResponseAwareMatcher<Response>() {
    public Matcher<?> matcher(Response response) {
        return equalTo("http://localhost:8080/" + response.path("userId"));
    }
});
```

### Measuring Response Time
```java
long timeInMs = get("/lotto").time()
long timeInSeconds = get("/lotto").timeIn(SECONDS);
when().get("/lotto").then().time(lessThan(2000L)); // Milliseconds
```

### Authentication
```java
//for each request
given().auth().basic("username", "password"). ..
//  for all requests:
RestAssured.authentication = basic("username", "password");
```
There are two types of authentication, **basic**, **digest**, and the way REST Assured sends them.
- Basic: Client sends HTTP request with an _Authorization_ header containing a base64-encoded string of `username:password`.
- Digest:
  - The server sends a `nonce` (a one-time token) to the client. 
  - The client creates a hash of the `username:password` and the `nonce`, then sends this hash back to the server. 
  - The server performs the same hashing operation and compares the results to authenticate the user.
The way authentication sent by REST Assured:
- Preemptive Authentication: Isn't protocol specific, sends the credentials without waiting for the server to challenge
for them. 
- "Challenged Basic Authentication": the client does not send credentials upfront but waits for the server to challenge
it with a _401 Unauthorized response_. After that, the client resends the request with the Authorization header
containing the credentials. \
By default, Rest Assured uses Challenged Basic Authentication when you specify basic without preemptive.

```java
// will send the basic authentication credentials always
given().auth().preemptive().basic("username", "password")
// will use challenged 
given().auth().basic("username", "password")
```

There are also Form, CSRF and OAuth authentication.

### Multi-part form data
When sending larger amount of data to the server - `multiPart`  allows you to specify a file, byte-array, input stream
or text to upload. 
```java
given().
        multiPart(new File("/path/to/file")).
        multiPart("controlName1", new File("/path/to/file")).
        multiPart("controlName2", "my_file_name.txt", someData).
        multiPart("controlName3", someJavaObject, "application/json").
        multiPart(new MultiPartSpecBuilder(greeting, ObjectMapperType.JACKSON_2)
                .fileName("greeting.json")
                .controlName("text")
                .mimeType("application/vnd.custom+json").build()).
when().
        post("/upload");
```

### Object Mapping
REST Assured supports mapping Java objects to and from JSON and XML. For JSON you need to have either Jackson, Jackson2,
Gson or Johnzon in the classpath and for XML you need Jakarta EE or JAXB.

#### Serialization
You have an object which you want to JSON and send it with the request.
```java
public class Message {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```
**From content type**
```java
Message message = new Message();
message.setMessage("My messagee");
given().
       contentType("application/json").
       body(message).
when().
      post("/message");
```
REST Assured will serialize the object to JSON since the request content-type is set to "application/json".
With Jackson if found, if not - Gson. If you change the content-type to "application/xml" it will try JAXB for to XML.

**From HashMap**
```java
Map<String, Object>  jsonAsMap = new HashMap<>();
jsonAsMap.put("firstName", "John");
jsonAsMap.put("lastName", "Doe");

given().
        contentType(JSON).
        body(jsonAsMap).
when().
        post("/somewhere").
then().
        statusCode(200);
```
**Using an Explicit Serializer**
```java
Message message = new Message();
message.setMessage("My messagee");
given().
       body(message, ObjectMapperType.JAXB).
when().
      post("/message");
```

### Deserialization
```java
// For this to work the response content-type must be "application/json"
Message message = get("/message").as(Message.class);
// Custom Content-Type Deserialization  
Message message = expect().parser("application/something", Parser.XML).when().get("/message").as(Message.class);
// Using an Explicit Deserializer
Message message = get("/message").as(Message.class, ObjectMapperType.GSON);
```

### Configuration
You can configure the pre-defined object mappers by using a ObjectMapperConfig and pass it to detailed configuration. \
You can add a Custom parser. The format is:
```java
RestAssured.registerParser(<content-type>, <parser>);
RestAssured.registerParser("application/vnd.uoml+xml", Parser.XML);
```

### Default values
You can also change the default values:
```java
RestAssured.baseURI = "http://myhost.org";
RestAssured.port = 80;
RestAssured.basePath = "/resource";
RestAssured.authentication = basic("username", "password");
RestAssured.rootPath = "x.y.z";
```

### Specification Re-use
