package org.example.api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Order;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

/**
 * Api test Home task
 * Src/main/resources must contain my.properties file with keys and
 * your own values as follows:
 *   api.key="yourApiKey"
 *   petId=1234567
 *   orderId=1234567
 *
 * Start with prepare method.
 */
public class HomeTaskApiTest {

    /**
     * Preparations before all tests
     * @throws IOException
     */
    @BeforeClass
    public void prepare() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", System.getProperty("api.key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    /**
     * Check object saving
     */
    @Test
    public void checkObjectSave() {
        Pet pet = new Pet();
        pet.setId(Integer.parseInt(System.getProperty("petId")));
        pet.setName("Pet_" + UUID.randomUUID().toString());

        Order order = new Order();
        int id = Integer.parseInt(System.getProperty("orderId"));
        boolean completeness = false;
        order.setId(id);
        order.setComplete(completeness);
        order.setPetId(pet.getId());

        given()
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200);

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actual
                = given()
                    .pathParam("orderId", id)
                    .when()
                    .get("/store/order/{orderId}")
                    .then()
                    .statusCode(200)
                    .extract().body()
                    .as(Order.class);

        Assert.assertEquals(actual, order);
    }

    /**
     * Test object removing
     */
    @Test
    public void testDelete() {
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);

        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(404);

    }

    /**
     * Test pet inventory
     */
    @Test
    public void testPetInventory() {

        String response = given()
                            .when()
                            .get("store/inventory")
                            .then()
                            .statusCode(200)
                            .extract().body().asString();

        Gson gson = new Gson();
        Map<String, String> inventoryMap = gson.fromJson(response, Map.class);
        Assert.assertTrue(inventoryMap.containsKey("testing"));
    }

}
