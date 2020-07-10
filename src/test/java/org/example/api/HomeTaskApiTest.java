package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {

    int petId = new Random().nextInt(50000);
    int id = new Random().nextInt(500000);

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

    @Test(priority = 0)
    public void createOrder() {
        Order order = new Order();
        order.setId(id);
        order.setPetId(petId);

                given()
                    .body(order)
                .when()
                    .post("/store/order")
                .then()
                    .statusCode(200);
    }

    @Test(priority = 1)
    public void getOrder() {
        Order order = new Order();
        order.setId(id);

        Order actual = given()
                    .pathParam("orderId", id)
                .when()
                    .get("/store/order/{orderId}")
                .then()
                    .statusCode(200)
                    .extract().body()
                    .as(Order.class);

        Assert.assertEquals(actual.getId(), order.getId());
    }

    @Test(priority = 2)
    public void deleteOrder() {
        Order order = new Order();
        order.setId(id);

                given()
                    .pathParam("orderId", id)
                .when()
                    .delete("/store/order/{orderId}")
                .then()
                    .statusCode(200);
    }

    @Test(priority = 3)
    public void deleteNegativeOrder() {
        Order order = new Order();
        order.setId(id);

        given()
                .pathParam("orderId", id)
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }

    @Test(priority = 4)
    public void getInventory() {

        Map map = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Map.class);
        Assert.assertTrue(map.containsKey("sold"),"map не содержит sold");
        Assert.assertTrue(map.containsKey("pending"),"map не содержит pending");
    }
}
