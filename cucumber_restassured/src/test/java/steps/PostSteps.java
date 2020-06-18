package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import utilities.TDDRest;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PostSteps {
    private TDDRest rest = new TDDRest();
    private int postId;
    private String path;
    private HashMap<String, String> bodyData = new HashMap<>();
    private ResponseOptions<Response> response;

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
    public void iShouldSeeCreatedPost() {
        response = rest.post(path, bodyData);
        response.getBody().prettyPeek();
    }
}
