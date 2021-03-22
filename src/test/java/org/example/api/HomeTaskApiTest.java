package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.example.model.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;


import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {

    Order order;

    @BeforeTest
    public void beforeOrder() throws IOException {

        order = new Order();
        String time = LocalDateTime.now().toString().concat("+0000");

        order.setId(new Random().nextInt(31415));
        order.setPetId(new Random().nextInt(27182));
        order.setQuantity(new Random().nextInt(10));
        order.setShipDate(time);
        order.setStatus("placed");
        order.setComplete(true);

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", "api_key")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test(priority = 1)
    public void placeOrder()  {

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
    }

    @Test(priority = 2)
    public void getOrder() {

        Order actual = given()
                .pathParam("orderId", order.getId())
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Order.class);

        Assert.assertEquals(order, actual);
    }

    @Test(priority = 3)
    public void deleteOrder() {

        given()
                .pathParam("orderId", order.getId())
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);

    }

    @Test(priority = 4)
    public void getOrderAgain() {

        given()
                .pathParam("orderId", order.getId())
                .when()
                .get("/order/{orderId}")
                .then()
                .statusCode(404);
    }

    @Test(priority = 5)
    public void inventory() {
        Map inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body().as(Map.class);
        Assert.assertTrue(inventory.containsKey("available"), "Inventory doesn't contain key \"available\"");
        Assert.assertFalse(inventory.containsKey("Pizza"), "Inventory contain key \"pizza\"");
    }
}