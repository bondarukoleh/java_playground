import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
        Response response =
                given()
                .when()
                    .get("/albums")
                .then()
                    .statusCode(200)
                .extract()
                .response();

        int anotherResp = get("/albums/1").path("userId");

//        System.out.println(response.asString());
        assertEquals(1, (int) response.jsonPath().get("[0].userId"), "ID should be 1");
        assertEquals(1, (int) response.path("[0].userId"), "ID should be from path");
        assertEquals(1, anotherResp, "ID should be 1 from single path");


    }
}
