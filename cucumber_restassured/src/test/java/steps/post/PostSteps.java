package steps.post;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

public class PostSteps extends BasePostSteps {
    @Given("^Posting to a \"([^\"]*)\" path$")
    public void postingToAPath(String arg0) throws Throwable {
        path = arg0;
    }

    @When("^I'm posting post with text \"([^\"]*)\"$")
    public void iMPostingPostWithText(String arg0) throws Throwable {
        bodyData.put("text", arg0);
    }

    @And("^title is \"([^\"]*)\"$")
    public void titleIs(String arg0) throws Throwable {
        bodyData.put("title", arg0);
    }

    @And("^author is \"([^\"]*)\"$")
    public void authorIs(String arg0) throws Throwable {
        bodyData.put("author", arg0);
    }

    @Then("^I should see created post$")
    public ResponseOptions<Response> iShouldSeeCreatedPost() {
        response = rest.post(path, bodyData);
        response.getBody().prettyPeek();
        return response;
    }
}
