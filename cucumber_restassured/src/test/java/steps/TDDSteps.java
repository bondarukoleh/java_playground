package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import utilities.TDDRest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TDDSteps {
    private TDDRest rest = new TDDRest();
    private int postId;
    private String path;
    private ResponseOptions<Response> response;

    @Given("^I send TDD GET to \"([^\"]*)\" endpoint$")
    public void checkTddGet(String path) {
        Response response = rest.get(path);
//        assertThat(response.getBody().jsonPath())
        System.out.println(response.getBody().prettyPeek());
    }

    @Given("^Sending GET to \"([^\"]*)\" endpoint$")
    public void sendingGETToEndpointWithId(String arg0) throws Throwable {
        path = arg0;
    }

    @And("^Post id is \"([^\"]*)\"$")
    public void postIdIs(int arg0) throws Throwable {
        postId = arg0;
    }

    @When("^GET request is send$")
    public void getRequestIsSend() {
        response = rest.get(path, "postId", postId);
    }

    @Then("^I should get post with author \"([^\"]*)\"$")
    public void iShouldGetPosts(String authorName) {
        System.out.println(response.getBody().prettyPeek());
        assertThat(response.getBody().jsonPath().get("author"), is(authorName));
    }
}
