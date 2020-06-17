package steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TDDRest {
    public static String mainUrl = "http://localhost:3000";
    private RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
    private static RequestSpecification Request;

    public TDDRest() {
        this.requestBuilder.setBaseUri(mainUrl);
        requestBuilder.setContentType(ContentType.JSON);
        RequestSpecification requestSpecification = requestBuilder.build();
        Request = given().spec(requestSpecification);
    }

    public Response simpleGet(String path) {
        Request.pathParams("postId", 1);
        return Request.get(String.format("/%s/{postId}", path));
    }
}
