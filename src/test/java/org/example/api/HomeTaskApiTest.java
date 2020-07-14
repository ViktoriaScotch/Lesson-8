package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
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
    public int orderId;

    @BeforeClass
    public void prepare() throws IOException {
        //System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        RestAssured.requestSpecification = (RequestSpecification) new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", "api_key")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    public void create() {
        order = new Order();
        orderId = new Random().nextInt(73);
        int id = new Random().nextInt(42);
        int petId = new Random().nextInt(19);
        order.setId(id);
        order.setPetId(petId);
        order.getQuantity(new Random().nextInt());
        order.setShipDate("2020-07-14T00:44:17.669Z");
        order.setComplete(true);
        order.setStatus("placed");

    }


    @Test
    public void getUp() {
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actual = given()
                .pathParam("orderId", orderId)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Order.class);
        Assert.assertEquals(actual, order);
    }

    @Test
    public void delete() throws IOException {
        given()
                .pathParam("orderId", orderId)
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", orderId)
                .when()
                .get("/order/{orderId}")
                .then()
                .statusCode(404);
    }

    @Test
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
