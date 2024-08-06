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
