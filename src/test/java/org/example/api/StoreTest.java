package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Random;
import static io.restassured.RestAssured.given;

public class StoreTest {
    Order order;

    @BeforeClass
    public void setUp() throws IOException {

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        order = new Order();
        int id = new Random().nextInt(500000); // просто нужно создать произвольный айди
        int orderId = new Random().nextInt(500000); // просто нужно создать произвольный айди
        String shipDate = "2020-07-17T09:48:20.214+0000";
        order.setPetId(id);
        order.setQuantity(3);
        order.setId(orderId);
        order.setShipDate(shipDate);
        order.setComplete(true);
        order.setStatus("placed");

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
    public void PostAndGet(){
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Order actual = given()
                .pathParam("orderId", order.getId())
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Order.class);

        Assert.assertEquals(actual.getPetId(), order.getPetId());
        Assert.assertEquals(actual.getQuantity(), order.getQuantity());
        Assert.assertEquals(actual.getShipDate(), order.getShipDate());
        Assert.assertEquals(actual.getStatus(), order.getStatus());
    }

    @Test
    public void Delete() {
        given()//создаем
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        given()//удаляем
                .pathParam("orderId", order.getId())
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()//получаем
                .pathParam("orderId", order.getId())
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }
}