package steps.post;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.MatcherAssert;

import java.net.URISyntaxException;

import static org.hamcrest.core.Is.is;

public class PostStepsWithDataTable extends BasePostSteps {
    @Given("^POST for \"([^\"]*)\"$")
    public void postFor(String path) throws Throwable {
        this.path = path;
    }

    @And("^message body is$")
    public void messageBodyIs(DataTable table) {
        var data = table.raw();
        pathParams.put(data.get(0).get(1), data.get(1).get(1)); // getting "id" string
        bodyData.put(data.get(0).get(0), data.get(1).get(0)); // adding name : "value"
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
        response = rest.get(path, "id", Integer.parseInt(profileId)); // TODO: fix this hardcode
        MatcherAssert.assertThat(response.getBody().jsonPath().get("profileName"), is(profileName));
    }
}
