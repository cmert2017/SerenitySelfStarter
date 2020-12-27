package serenity.spartan;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.core.SerenitySystemProperties;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import serenity.utility.SpartanUtil;
import java.util.Map;
import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SpartanAdminCRUDTest {



    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://54.90.101.103:8000";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = given().log().body().auth().basic("admin","admin");

    }
    @AfterAll
    public static void cleanUp(){
        reset();
        //SerenityRest.clear();
    }

    @DisplayName("1. Admin User Should be able to Add Spartan")
    @Test
    public void testAdd1Data(){
        Map<String,Object> payload = SpartanUtil.getRandomSpartanRequestPayload();
        getDefaultRequestSpecification().
        given()
                .log().body()
               // .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(payload).
       when()
                .post("/spartans") ;
        Ensure.that("Request was successful"  ,
                thenResponse -> thenResponse.statusCode(201) )
                .andThat("We got json format result",
                        thenResponse -> thenResponse.contentType(ContentType.JSON) )
                .andThat("success message is A Spartan is born!" ,
                        thenResponse ->
                                thenResponse.body("success",is("A Spartan is Born!") )  )
        ;
        Ensure.that("The data " + payload + " we provided added correctly",
                vRes -> vRes.body("data.name", is( payload.get("name")  ) )
                        .body("data.gender", is( payload.get("gender")  ) )
                        .body("data.phone", is( payload.get("phone")  ) ) )
                .andThat("New ID has been generated and not null" ,
                        vRes -> vRes.body("data.id" , is(not(nullValue() )))    ) ;
        // how do we extract information after sending requests ? :
        // for example I want to print out ID
        // lastResponse() method is coming SerenityRest class
        // and return the Response Object obtained from last ran request.
//        lastResponse().prettyPeek();
        System.out.println("lastResponse().jsonPath().getInt(\"data.id\") = "
                + lastResponse().jsonPath().getInt("data.id"));
    }

    @DisplayName("2. Admin Should be able to read single data")
    @Test
    public void getOneData(){
        
        int newID = lastResponse().jsonPath().getInt("data.id");
        System.out.println("newID = " + newID);
        
       // given().auth().basic("admin","admin").
        getDefaultRequestSpecification().
        when().get("/spartans/{id}",newID);

        Ensure.that(" We can access newly generated data.",
                vResponse -> vResponse.statusCode(200))

        ;

    }

    //add put and patch here



    @DisplayName("3. Admin Should be able to delete single data")
    @Test
    public void deleteTestOneData(){

        int myID = lastResponse().path("id");
        System.out.println("newID = " + myID);

      /*  // given().auth().basic("admin","admin").
        getDefaultRequestSpecification().
                when().delete("/spartans/{id}",myID);
*/ //this didnt work.

    /*    //alternative way of  above code
        getDefaultRequestSpecification()
                .pathParam("id",myID).
                when().delete("/spartans/{id}");*/

        //another alternative way
        getDefaultRequestSpecification().
                when().delete("/spartans/"+myID);


        Ensure.that(" Request is successful.",
                vResponse -> vResponse.statusCode(204))

        ;

        //send another get request to make sure you got 404

        getDefaultRequestSpecification().when().get("/spartans/{id}",myID);

        Ensure.that("Delete was successful, Can not find data anymore",
                thenResponse -> thenResponse.statusCode(404));





    }





}
