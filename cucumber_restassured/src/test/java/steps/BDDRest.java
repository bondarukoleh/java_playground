package steps;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

public class BDDRest {
    public static String mainUrl = "http://localhost:3000";

    public void checkEntityById(String path, int Id){
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(String.format("%s/%s/%d", mainUrl, path, Id))
        .then()
                .body("author", is("typicode"));
    }

    public void checkAuthorsCollection(String path){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s", mainUrl, path))
                .then()
                .body("author", containsInAnyOrder("typicode", "Oleh", "Bondaruk"))
                .statusCode(200);
    }
}
