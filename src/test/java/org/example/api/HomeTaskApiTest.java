package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
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

    public Order order;
    public int orderId;

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

        order = new Order();
        orderId = new Random().nextInt(99999);

        order.setId(orderId)
                .setPetId(new Random().nextInt(99999))
                .setQuantity(new Random().nextInt(100))
                .setShipDate("2020-07-13T13:54:03.148+0000")
                .setComplete(true)
                .setStatus("placed");
    }

    @Test
    public void testPost() {

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

    @Test(dependsOnMethods = "testPost")
    public void testDelete() {
        given()
                .pathParam("orderId", orderId)
                .when()
                .delete("/store/order/{orderId}")
                .then().statusCode(200);

        given()
                .pathParam("orderId", orderId)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testInventory() {
      Map map =  given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body().as(Map.class);

        Assert.assertTrue(map.containsKey("available"),"Inventory не содержит статус available");
        Assert.assertTrue(map.containsKey("sold"),"Inventory не содержит статус sold");
        Assert.assertTrue(((Double) map.get("available"))>0,"Параметр Availabale <= 0");
    }
}
