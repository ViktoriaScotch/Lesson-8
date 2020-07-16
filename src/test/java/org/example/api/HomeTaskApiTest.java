package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.example.model.Order;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    Order order;

    /**
     * Создаём шапку запроса и инициализируем объект класса Order.
     * @throws IOException System.getProperties().load() - может выбросить исключение.
     */
    @BeforeClass
    public void setUp() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        order = new Order();
        order.setPetId(Integer.parseInt(System.getProperty("pet.id")));
        order.setQuantity(Integer.parseInt(System.getProperty("quantity")));
        order.setId(Integer.parseInt(System.getProperty("order.id")));
        order.setShipDate(System.getProperty("ship.date"));
        order.setComplete(Boolean.parseBoolean(System.getProperty("compete")));
        order.setStatus(System.getProperty("status"));

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", System.getProperty("api.key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    /**
     * Проверка на корректное сохрание заказа.
     * Действия: добавляем и пробуем получить заказ.
     */
    @Test
    public void checkOrderSave() {
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

        Assert.assertEquals(order, actual, "Объекты не равны");
    }

    /**
     * Проверка на корректное удаление заказа.
     * Действия: добавляем, удаляем, пробуем получить заказ.
     */
    @Test
    public void checkDeleteOrder() {
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", order.getId())
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("orderId", order.getId())
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }

    /**
     * Проверка на корректное получение данных из инвентаря.
     */
    @Test
    public void checkGetInventory() {
        Map<String, Integer> inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getMap("");

        Assert.assertTrue(inventory.containsKey("sold"), "Inventory не содержит статус sold" );
    }
}
