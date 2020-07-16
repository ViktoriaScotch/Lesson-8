package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Pet;
import org.example.store.Inventory;
import org.example.store.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
  int id;

  @BeforeClass
  public void prepare() throws IOException {
    System.getProperties().load(ClassLoader.getSystemResourceAsStream("set.properties"));

    RestAssured.requestSpecification = new RequestSpecBuilder()
            .setBaseUri("https://petstore.swagger.io/v2/")
            .addHeader("api_key", System.getProperty("api_key"))
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    RestAssured.filters(new ResponseLoggingFilter());
  }

  @Test
  public void checkOrder() {
    Order order = new Order();
    id = new Random().nextInt(10);
    System.setProperty("id", String.valueOf(id));
    int petId = new Random().nextInt(10);
    int quantity = new Random().nextInt(100);
    String shipDate = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now()).replace("Z", "+0000");
    String status = "placed";
    boolean complete = new Random().nextBoolean();
    order.setId(id);
    order.setPetId(petId);
    order.setQuantity(quantity);
    order.setShipDate(shipDate);
    order.setStatus(status);
    order.setComplete(complete);

    given()
            .body(order)
            .when()
            .post("/store/order")
            .then()
            .statusCode(200);

    Order actual =
            given()
                    .pathParam("orderId", id)
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
  public void orderDelete() throws IOException {
    System.getProperties().load(ClassLoader.getSystemResourceAsStream("set.properties"));

    given()
            .pathParam("orderId", id)
            .when()
            .delete("/store/order/{orderId}")
            .then()
            .statusCode(200);
    given()
            .pathParam("orderId", id)
            .when()
            .get("/store/order/{orderId}")
            .then()
            .statusCode(404);
  }

  @Test
  public void checkInventory() {
    Inventory inventory = new Inventory();

    Inventory actual =
            given()
                    .body(inventory)
                    .when()
                    .get("/store/inventory")
                    .then()
                    .statusCode(200)
                    .extract().body()
                    .as(Inventory.class);

    Assert.assertTrue(actual.containsKey("available"), "Inventory не содержит статус sold");
  }
}
