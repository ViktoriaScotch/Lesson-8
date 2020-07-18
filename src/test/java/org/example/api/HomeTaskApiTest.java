package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Pet;
import org.example.model.Store;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomeTaskApiTest {
    //Глобал пет id
    int petID;

    @BeforeClass
    public void store() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/store/")
                .addHeader("api_key", System.getProperty("api_key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    public void checkObjectiveASave() {
        Store store = new Store();
        int id = new Random().nextInt(5000);
        int petId = new Random().nextInt(50);
        //сохраняем в глобале
        petID = petId;
        store.setId(id);
        store.setPetId(petID);
        store.setComplete(true);
        store.getQuantity(0);
        store.getShipDate();
        store.getStatus("placed");
        store.getComplete();
        given()
                .body(store)
                .when()
                .post("/order")
                .then()
                .statusCode(200);
    }

    @Test
    public void checkObjectiveBGet() {
        given()
                .when()
                .get("/order/" + petID)
                .then()
                .statusCode(200);
    }

    @Test
    public void checkObjectiveCDelete() {
        given()
                .when()
                .delete("/order/" + petID)
                .then()
                .statusCode(200);
    }
    @Test
    public void checkObjectiveDGet() {
        given()
                .when()
                .delete("/order/" + petID)
                .then()
                .statusCode(404);
    }
}
