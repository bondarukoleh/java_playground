package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

public class SimpleGetSteps {
    @Given("^I send GET to \"([^\"]*)\" endpoint$")
    public void iSendGETToEndpoint(String url) throws Throwable {
        given().contentType(ContentType.JSON);
    }

    @And("^post id is \"(\\d+)\"$")
    public void postIdIs(int postId) {
        when().get(String.format("http://localhost:3000/posts/%s", postId))
                .then().body("author", is("typicode1"));
    }

    @Then("^I should see the post object$")
    public void iShouldSeeThePostObject() {
    }


}
