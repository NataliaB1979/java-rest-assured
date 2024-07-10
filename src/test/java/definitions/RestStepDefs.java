package definitions;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import static org.apache.http.HttpStatus.SC_OK;

public class RestStepDefs {
    @Given("I validate get candidates api call")
    public void iValidateGetCandidatesApiCall() {
            RestAssured.given()
                    .baseUri("https://skryabin.com/recruit/api/v1")
                    .log().all()
                    .when()
                    .get("/candidates")
                    .then()
                    .log().all()
                    .statusCode(SC_OK);
    }
}
