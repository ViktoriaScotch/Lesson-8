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

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {

    Order order;

    @BeforeClass
    public void set() throws IOException {
        order = new Order();
        order.setId(1);
        order.setPetId(1);
        order.setQuantity(3);
        order.setShipDate("2020-07-15T10:20:42.179Z");
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

    @Test(priority = 1)
    public void checkGetInventory() {
        Map<String, Integer> inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getMap("");

        Assert.assertTrue(inventory.containsKey("pending"), "Inventory не содержит статус pending");
    }

    @Test(priority = 2)
    public void testOrderSaving() {
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actualOrder = given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Order.class);

        Assert.assertEquals(order, actualOrder, "Обьекты разные");

    }

    @Test(priority = 3)
    public void testDeleteOrder() {

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }

}
