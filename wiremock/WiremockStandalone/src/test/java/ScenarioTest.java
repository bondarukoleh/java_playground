import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

public class ScenarioTest {

    @BeforeEach
    public void prep() {
        RestAssured.baseURI = "http://localhost:8081";
        String scenariosStatePath = "/__admin/scenarios/to_do_list/state";
        given().when().put(scenariosStatePath).then().statusCode(200);

    }

    @Test
    public void statefulWiremock() {
        String itemsPath = "/test/items";
        String itemToAdd = "Buy apples";
        {
            Response response =
                    given()
                            .when()
                            .get(itemsPath).then().
                            log().body()
                            .statusCode(200)
                            .extract()
                            .response();

            assertEquals(1, (int) response.jsonPath().get("itemsToDo.size()"),
                    "Should be one item in the list");
        }
        {
                    given()
                            .body(String.format("{\"items\": [\"%s\"]}", itemToAdd))
                            .contentType("application/json")
                            .when()
                            .post(itemsPath)
                            .then()
                            .statusCode(201);
        }
        {
            Response response = given()
                    .when()
                    .get(itemsPath)
                    .then()
                    .statusCode(200)
                    .log().body()
                    .extract().response();
            assertEquals(2, (int) response.jsonPath().get("itemsToDo.size()"),
                    "Should be two item in the list");
            assertEquals(itemToAdd, response.jsonPath().get("itemsToDo[1]"),
                    String.format("Should include item \"%s\"", itemToAdd));
        }
    }
}

