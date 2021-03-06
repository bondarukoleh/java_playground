package utilities;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class BDDRest {
    public static String mainUrl = "http://localhost:3000";

    public void checkEntityById(String path, int id) {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s/%d", mainUrl, path, id))
                .then()
                .body("author", is("typicode"));
    }

    public void checkEntityByIdWithPathParams(String path, String id) {
        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParams("postId", id)
                .get(String.format("%s/%s/{postId}", mainUrl, path))
                .then()
                .body("author", is("typicode"));
    }

    public void checkEntityByParameters(String path, String title, String author) {
        given()
                .contentType(ContentType.JSON)
                .when()
                .queryParams("title", title, "author", author)
                .get(String.format("%s/%s", mainUrl, path))
                .then()
                .body("id", hasItem(2));
    }

    public void checkAuthorsCollection(String path) {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s", mainUrl, path))
                .then()
                .body("author", containsInAnyOrder("typicode", "Oleh", "Bondaruk"))
                .statusCode(200);
    }
}
