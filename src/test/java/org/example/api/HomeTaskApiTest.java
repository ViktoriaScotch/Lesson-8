package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import com.sun.org.apache.xpath.internal.operations.Or;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.model.Order;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    Order order = new Order();
    public int orderId = new Random().nextInt(10);
    int id = new Random().nextInt(10);
    int petId = new Random().nextInt(10);

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


    @Test(priority = 0)
    public void create() {

        order.setId(id);
        order.setPetId(petId);
        order.setQuantity(new Random().nextInt(10));
        order.setShipDate("2020-07-14T14:28:41.247Z");
        order.setStatus("placed");
        order.setComplete(true);
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
    }


    @Test(priority = 1)
    public void getUp(){

        Order actual = given()
                .pathParam("orderId", orderId)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Order.class);
        Assert.assertEquals(actual.getId(), order.getId());
    }

    @Test(priority = 2)
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

    @Test(priority = 3)
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
