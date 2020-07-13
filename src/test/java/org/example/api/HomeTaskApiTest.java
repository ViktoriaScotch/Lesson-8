package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import store.Order;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    Order order;

    /**
     * Создаём шапку запроса и инициализируем объект класса Order.
     * @throws IOException System.getProperties().load() - может выбросить исключение.
     */
    @BeforeClass
    public void setUp() throws IOException {
        order = new Order();
        order.setPetId(1);
        order.setQuantity(1);
        order.setId(1);
        order.setShipDate("2020-07-13T06:16:42.914Z");
        order.setComplete(true);
        order.setStatus("placed");

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
                .pathParam("orderId", System.getProperty("orderId"))
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
