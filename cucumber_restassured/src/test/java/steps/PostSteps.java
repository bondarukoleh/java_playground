package steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import groovy.json.JsonOutput;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.w3c.dom.ls.LSOutput;
import utilities.TDDRest;

import java.net.URISyntaxException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PostSteps {
    private TDDRest rest = new TDDRest();
    private int postId;
    private String path;
    private String pathParameterName;
    private HashMap<String, String> bodyData = new HashMap<>();
    private HashMap<String, String> pathParams = new HashMap<>();
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
    public ResponseOptions<Response> iShouldSeeCreatedPost() {
        response = rest.post(path, bodyData);
        response.getBody().prettyPeek();
        return response;
    }

    @Given("^POST for \"([^\"]*)\"$")
    public void postFor(String path) throws Throwable {
        this.path = path;
    }

    @And("^message body is$")
    public void messageBodyIs(DataTable table) {
        var data = table.raw();
        System.out.println("messageBodyIs");
        System.out.println(data.get(0).get(0));
        System.out.println(data.get(1).get(0));
        bodyData.put(data.get(0).get(0), data.get(0).get(1)); // adding name : "value"
        pathParams.put(data.get(1).get(0), data.get(1).get(1)); // getting "profileId" string
    }

    @When("^I'm send POST request$")
    public void iMSendPOSTRequest() throws URISyntaxException {
        if(pathParams != null) {
            response = rest.post(path, pathParams, bodyData);
        } else {
            response = rest.post(path, bodyData);
        }
        response.getBody().prettyPeek();
    }

    @Then("^I should GET the profile with ID: \"([^\"]*)\" and check that Profile name is \"([^\"]*)\"$")
    public void iShouldGETTheProfile(String profileId, String profileName) throws Throwable {
        response = rest.get(path, "profileId", Integer.parseInt(profileId)); // TODO: fix this hardcode
        assertThat(response.getBody().jsonPath().get("profileName"), is(profileName));
    }
}
