// Got damn peace of shit this cucumber is. Lord what a poor crap :(
package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class SimpleGetSteps {
    private String path;
    private BDDRest bddRest = new BDDRest();

    @Given("^I send GET to \"([^\"]*)\" endpoint$")
    public void iSendGETToEndpoint(String path) throws Throwable {
        this.path = path;
    }

    @And("^post id is \"(\\d+)\"$")
    public void postIdIs(int postId) {
        bddRest.checkEntityById(path, postId);
    }

    @Then("^I should see the post object$")
    public void iShouldSeeThePostObject() {
    }

    @Then("^I should see the authors collection$")
    public void iShouldSeeTheAuthorsCollection() {
        bddRest.checkAuthorsCollection(path);
    }


    @And("^post id is \"([^\"]*)\" but called with path params$")
    public void postIdIsButCalledWithPathParams(String postId) throws Throwable {
        bddRest.checkEntityByIdWithPathParams(path, postId);
    }
}
