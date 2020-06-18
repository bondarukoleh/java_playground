// Got damn peace of shit this cucumber is. Lord what a poor crap :(
package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import utilities.BDDRest;

public class SimpleGetSteps {
    private String path;
    private int postId;
    private BDDRest bddRest = new BDDRest();

    @Given("^I send GET to \"([^\"]*)\" endpoint$")
    public void iSendGETToEndpoint(String path) throws Throwable {
        this.path = path;
    }

    @And("^post id is \"(\\d+)\"$")
    public void postIdIs(int postId) {
        this.postId = postId;
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

    @And("^query parameters title is \"([^\"]*)\" and author is \"([^\"]*)\"$")
    public void queryParametersTitleIsAndAuthorIs(String title, String author) throws Throwable {
        bddRest.checkEntityByParameters(path, title, author);
    }
}
