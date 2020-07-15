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

    @BeforeClass
    public void set() throws IOException {

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

    @Test
    public void checkGetInventory() {
        Map<String, Integer> inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Map.class);

        Assert.assertTrue(inventory.containsKey("pending"), "Inventory не содержит статус pending");
    }

    @Test
    public void testOrderSaving() {

        Order order;

        order = new Order();
        order.setId(1);
        order.setPetId(1);
        order.setQuantity(3);
        order.setShipDate("2020-07-15T10:20:42.179Z");
        order.setStatus("placed");
        order.setComplete(true);

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actualOrder = given()
                .pathParam("orderId", 1)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Order.class);

        Assert.assertEquals(order, actualOrder, "Обьекты разные");

    }

    @Test
    public void testDeleteOrder() throws IOException {

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));

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
