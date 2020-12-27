package serenity.spartan.search;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serenity.utility.SpartanTestBase;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SearchSpartanTest extends SpartanTestBase {

    @DisplayName("Authenticated user should be able to search")
    @Test
    public void testSearch(){
        given()
                .auth().basic("admin","admin")
                .queryParam("gender","Female")
                .queryParam("nameContains","e").
        when()
                .get("/spartans/search");


        Ensure.that("Request was successful",
                vResponse -> vResponse.statusCode(200))
        .andThat("We got Json Result",
                validatableResponse -> validatableResponse.contentType(ContentType.JSON));

        //chain above ensure you got json result
        JsonPath jsonPath = lastResponse().jsonPath();
        //open another ensure
        //make sure you got all names contains a
        //and all gender value is Male
        Ensure.that("We got all names contains e",
                vResponse -> vResponse.body("content.name", everyItem(anyOf(containsString("e"),containsString("E"))))
                )
        .andThat("All the gender value is Female",
                validatableResponse -> validatableResponse.body("content.gender",everyItem(is("Female"))));

    }
}
