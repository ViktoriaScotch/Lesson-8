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
import java.util.Map;



import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    Order order = new Order();
    int orderId = 123;
    int id = 3;
    int petId = 5;
    int quantity =8;

    @BeforeClass
    public void testPrepare()  {

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", "api.key")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }


    @Test(priority = 0)
    public void testCreateOrder() {

        order.setId(id);
        order.setPetId(petId);
        order.setQuantity(quantity);
        order.setShipDate("2020-07-15T16:13:12.430+0000");
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
    public void testOrderSave() {
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
        Assert.assertEquals(order, actual, "Objects not equals");
    }

    @Test(priority = 2)
    public void testDelete() {

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
    public void testGetInventory() {
        Map inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getMap("");

        Assert.assertTrue(inventory.containsKey("sold"), "Inventory does not contain sold");
    }
}

