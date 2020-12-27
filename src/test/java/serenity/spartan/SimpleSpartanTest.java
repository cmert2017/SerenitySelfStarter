package serenity.spartan;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.*;
import serenity.utility.SpartanUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SimpleSpartanTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://3.92.77.194:8000/";
        RestAssured.basePath = "/api";
    }
    @AfterAll
    public static void cleanUp(){
        reset();
    }
    @DisplayName("Testing GET /api/hello Endpoint")
    @Test
    @Disabled
    public void testingHelloEndPoint(){
        when()
                .get("/hello").
                then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body( is("Hello from Sparta") )
        ;

        //        Serenity's way of generating some steps for verification
        // in the report using Ensure class
        Ensure.that("Make sure endpoint works " ,
                response -> response
                        .statusCode(200)
                        .contentType(ContentType.TEXT)
                        .body( is("Hello from Sparta") )
        );


        Ensure.that("Success response was received",
                thenResponse -> thenResponse.statusCode(200))
                .andThat("I got text response",
                        newResponse ->newResponse.contentType(ContentType.TEXT))
                .andThat("I got Hello From Spartan",
                        vResponse -> vResponse.body(is("Hello from Sparta")))
                .andThat("I got my response within 3 seconds",
                        vResponse -> vResponse.time(lessThan(2l), TimeUnit.SECONDS))
                ;

    }


    @DisplayName("Admin User Should be able to Add Spartan")
    @Test
    public void testAdd1Data(){
        Map<String, Object> payload = SpartanUtil.getRandomSpartanRequestPayload();
        given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(payload).
        when()
                .post("/spartans");


        Ensure.that("Request was successful",

                thenResponse -> thenResponse.statusCode(201))
                .andThat("We got json format result",
                        thenResponse -> thenResponse.contentType(ContentType.JSON))
                .andThat("success message is A Spartan is Born!",
                        thenResponse -> thenResponse.body("success",containsString("A Spartan is Born!")))

        ;

        Ensure.that("The data "+ payload +"we provided added correctly",
                vResponse -> vResponse.body("data.name",is(payload.get("name")))
                                       .body("data.gender",is(payload.get("gender")))
                                        .body("data.phone",is(payload.get("phone"))))
                .andThat("New Id generated and not null",
                        vResponse -> vResponse.body("data.id",is(notNullValue())))
        ;


        //how do we extract information after sending request?
        //for example I want to print out ID
        //lastResponse() is coming from SerenityRest class and return the Response Object obtained from last ran request.

        System.out.println("lastResponse().prettyPeek().path(\"data.id\") = " + lastResponse().prettyPeek().path("data.id"));
        System.out.println("lastResponse().path(\"data.id\") = " + lastResponse().path("data.id"));



    }


    @DisplayName("Admin User Should be able to Add Spartan")
    @Test
    public void testAdd1Data2() {
        given()
                .auth().basic("admin", "admin")
                .accept(ContentType.JSON).
        when()
                .get("/spartans");

        //path return a list as well
        System.out.println("lastResponse().path(\"name\") = " + lastResponse().path("name"));


    }

}
