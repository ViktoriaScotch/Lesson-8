package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.example.model.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;


import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    Order order;

    @BeforeClass
    public void prepare() throws IOException {
        order = new Order();
        int id = new Random().nextInt(10);
        int petId = new Random().nextInt(20);
        int quantity = new Random().nextInt(30);
        order.setId(id);
        order.setPetId(petId);
        order.setQuantity(quantity);
        order.setShipDate("2020-07-15T19:21:10.379Z");
        order.setStatus("placed");
        order.setComplete(true);
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
    public void getUp() {

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actual = given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Order.class);

        Assert.assertEquals(actual, order);
    }

    @Test(priority = 1)
    public void delete() throws IOException {
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/order/{orderId}")
                .then()
                .statusCode(404);
    }

    @Test(priority = 2)
    public void inventory() {
        Map inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body().as(Map.class);
        Assert.assertTrue(inventory.containsKey("sold"), "Inventory не содержит статус sold");

    }

}