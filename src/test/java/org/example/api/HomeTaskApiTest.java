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
import java.lang.reflect.Type;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    @BeforeClass
    public void prepare() throws IOException {

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2")
                .addHeader("api_key", System.getProperty("api_key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }


    @Test
    public void checkObjectSaveAndDel() {
        Order order = new Order();
        int id = new Random().nextInt(100);
        order.setId(id + 5);
        order.setPetId(id + 15);
        order.setQuantity(id + 10);
        order.setShipDate("2020-07-16");
        order.setStatus("placed");
        order.setComplete(true);
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        Order actual =
                given()
                        .pathParam("orderId", id + 5)
                        .when()
                        .get("/store/order/{orderId}")
                        .then()
                        .statusCode(200)
                        .extract().body()
                        .as((Type) Order.class);

        Assert.assertEquals(actual.getPetId(), order.getPetId());

        given()
                .pathParam("orderId", id + 5)
                .when()
                .delete("store/order/{orderId}")
                .then()

                .statusCode(200);
        given()
                .pathParam("orderId", id + 5)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }

}