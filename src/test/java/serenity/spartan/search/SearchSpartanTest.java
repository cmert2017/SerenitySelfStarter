package serenity.spartan.search;

import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serenity.utility.SpartanTestBase;

import static net.serenitybdd.rest.SerenityRest.given;

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
                vResponse -> vResponse.statusCode(200));

    }
}
