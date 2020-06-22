package steps;

import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import utilities.TDDRest;

public class BaseSteps {
    protected TDDRest rest = new TDDRest();
    protected int postId;
    protected String path;
    protected String pathParameterName;
    protected ResponseOptions<Response> response;
}
