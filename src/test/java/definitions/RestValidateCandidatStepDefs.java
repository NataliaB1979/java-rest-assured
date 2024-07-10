package definitions;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import support.TestDataManager;
import org.assertj.core.api.Assertions;

import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class RestValidateCandidatStepDefs {


    @Given("I create and validate candidate")
    public void iCreateAndValidateCandidate() {
        Map<String, String> candidateData = TestDataManager.getJuniorCandidateCredentialsFromFile();
        String baseUrl = "https://skryabin.com/recruit/api/v1/";
        int id = RestAssured.given().log().all()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(candidateData)
                .when()
                .post("/candidates")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().jsonPath().getInt("id");

        Map<Object, Object> map = RestAssured.given().log().all()
                .baseUri(baseUrl)
                .when()
                .get("/candidates/" + id)
                .then().log().all()
                .statusCode(SC_OK)
                .extract().jsonPath().getMap("");

        int idFromGetMethod = (Integer) map.get("id");

        Assertions.assertThat(id).isEqualTo(idFromGetMethod);
    }
    }

