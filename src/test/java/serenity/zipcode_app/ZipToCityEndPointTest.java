package serenity.zipcode_app;

import com.vdurmont.emoji.EmojiParser;
import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class ZipToCityEndPointTest {

    //http://api.zippopotam.us/us/{{zipcode}}

    @BeforeAll
    public static void setUp() {

        RestAssured.baseURI = "http://api.zippopotam.us";
        // SerenityRest.setDefaultRootPath("http://api.zippopotam.us");
        // SerenityRest.getDefaultRootPath();

    }

    @AfterAll
    public static void tearDown() {

        SerenityRest.clear();
        RestAssured.reset();

    }

    @DisplayName("Testing 1 zip code and get the result")
    @Test
    public void test1ZipCode() {

        given()
                .log().uri()
                .pathParam("country", "us")
                .pathParam("zipcode", "22030").
                when()
                .get("/{country}/{zipcode}").prettyPeek().
                then()
                .statusCode(200)
                .body("'post code'", is("22030"))
                .body("places[0].'place name'", is("Fairfax"));

    }

    @DisplayName("Testing multiple zipcodes and get the result")
    @ParameterizedTest
    @ValueSource(strings = {"22030", "51105", "51031", "67552", "22334"})
    public void testZipCodes(String zipcode) {

        //run this parametrized test with 5 zipcodes of your choice
        //start with no external  file
        //then add external csv file in separate test

        given()
                .log().uri()
                .pathParam("country", "us")
                .pathParam("zipcode", zipcode).
                when()
                .get("/{country}/{zipcode}").prettyPeek().
                then()
                .statusCode(200);


        Ensure.that("We got successful result",
                v -> v.statusCode(200)
        );
    }

    /**
     * {index}--> to represent iteration number
     * {arguments}-->shows the resource arguments
     * {methodParameterIndexNumber}--->indexof the arguments in the recourse
     */

    @ParameterizedTest(name = "Iteration number {index} zipcode is  {arguments} ")  //this is coming from junit5
    @ValueSource(strings = {"22030", "51105", "51031", "67552", "22334"})
    public void testDisplayNameManipulation(String zipcode) {

        System.out.println(zipcode);

    }


    @ParameterizedTest(name = "Iteration number {index} Country is {0}, Zipcode is {1} " )  //this is coming from junit5
    @CsvFileSource(resources = "/country_zip.csv",numLinesToSkip = 1)
    public void testCountryZip(String country, String zipcode) {

        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println("result = " + result);

        given()
                .log().uri()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode).
        when()
                .get("/{country}/{zipcode}").prettyPeek().
        then()
                .statusCode(200);

        Ensure.that("We got successful result",
                v -> v.statusCode(200)
        );


    }




}