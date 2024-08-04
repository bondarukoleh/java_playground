import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

public class FirstTest {
    @Test
    public void addition() {
        // Base URI
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // GET request
        Response response = given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println(response.body().peek());
    }
}
