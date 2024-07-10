package definitions;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import support.TestDataManager;

import java.util.Map;

public class RestPositionStepDefs {
    @Given("I validate CRUD operations for a position")
    public void iValidateCRUDOperationsForAPosition() {
        Map<String, String> positionToCreate = TestDataManager.getPositionFromFile("automation", "rest_data");
        Map<String, String> credentials = TestDataManager.getRecruiterCredentialsFromFile();

        int createdPositionId = RestAssured.given()
                .baseUri("https://skryabin.com/recruit/api/v1/")
                .header("Content-Type", "application/json")
                .auth().preemptive().basic(credentials.get("username"), credentials.get("password"))
                .body(positionToCreate)
                .log().all()
                .when()
                .post("/positions")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .jsonPath()
                .getInt("id");


        Map<String, Object> createdPosition = RestAssured.given()
                .baseUri("https://skryabin.com/recruit/api/v1/")
                .log().all()
                .when()
                .get("/positions/" + createdPositionId)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getMap("");
        System.out.println(createdPosition);

    }
    }

