package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSenderOptions;
import io.restassured.specification.RequestSpecification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TDDRest {
    public static String mainUrl = "http://localhost:3000";
    private RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
    private static RequestSpecification request;
    Response response;

    public static void main(String[] args) {
//        new TDDRest().get("posts").getBody().prettyPeek();
//        new TDDRest().get("posts", "paramWhatever", 1).getBody().prettyPeek();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("postIdLALA", "1");
//        new TDDRest().get("posts", params).getBody().prettyPeek();
//        new TDDRest().get("posts", params).getBody().prettyPeek();
    }

    public TDDRest() {
        this.requestBuilder.setBaseUri(mainUrl);
        requestBuilder.setContentType(ContentType.JSON);
        RequestSpecification requestSpecification = requestBuilder.build();
        request = given().spec(requestSpecification);
    }

    public Response get(String path) {
        return request.get("/{p}", path);
    }

    public Response get(String path, String paramName, int paramValue) {
        request.pathParams(paramName, paramValue);
        response = request.get(String.format("/%s/{%s}", path, paramName), paramName); // tricky one, pay attention
        System.out.println("GOT AFTER GET: ");
        response.prettyPeek();
        return response;
    }

    public Response get(String path, Map<String, String> params) {
        request.pathParams(params);
        return request.get("{path}/{postIdLALA}", path);
    }

    public Response getOnlyWithPath(String path, Map<String, String> params) {
        request.pathParams(params);
        return request.get("{path}/{postIdLALA}", path);
    }

    public Response post(String path, HashMap<String, String> body) {
        request.body(body);
        return request.post("/{p}", path);
    }

    public Response post(String path,  HashMap<String, String> pathParams, HashMap<String, String> body) throws URISyntaxException {
        request.body(body);
        return request.post("/{path}", path);
    }
}
