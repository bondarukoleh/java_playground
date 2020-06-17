package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;

public class TDDSteps {
    private TDDRest rest = new TDDRest();

    @Given("^I send TDD GET to \"([^\"]*)\" endpoint$")
    public void checkTddGet(String path) {
        Response response = rest.simpleGet(path);
        System.out.println(response.getBody().prettyPeek());
    }

    @Then("^I should get posts$")
    public void iShouldGetPosts() {
    }
}
