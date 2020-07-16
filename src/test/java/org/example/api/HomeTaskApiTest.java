package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Order;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        String status = "pending";
        Map<String, Integer> inventory = given()
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Map.class);

        Assert.assertTrue(inventory.containsKey(status), "Inventory не содержит статус " + status);
    }

    @Test
    public void testOrderSaving() {

        Order order;
        int id = new Random().nextInt(500000);
        order = new Order();

        order.setId(id);
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
                .pathParam("orderId", id)
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

        Order order;
        int id = new Random().nextInt(500000);
        order = new Order();
        order.setId(id);

        //создаем айтем
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        //убеждаемся, что он есть
        given()
                .pathParam("orderId", id)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200);
        //удаляем
        given()
                .pathParam("orderId", id)
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        //убеждаемся, что его больше нет
        given()
                .pathParam("orderId", id)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }


    //запрос по статусу
    //тест проверяет, совпадают ли статусы с тем, по которому запрашивали
    @Test
    public void testGetByStatus() {
        String status = "sold";
        boolean check = true;
        List<String> petsByStatus = given()
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("status");

        for (String actualStatus : petsByStatus) {
            if (!actualStatus.equals(status)) {
                check = false;
            }
        }

        Assert.assertTrue(check, "Нет запрашиваемого статуса");
    }

    @Test
    public void testPut() {
        Pet pet = new Pet();
        int id = Integer.parseInt(System.getProperty("orderId"));
        pet.setId(id);
        pet.setName("Новая кличка");

        given()
                .body(pet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200);

        Pet actual = given()
                .pathParam("petId", id)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .extract().body()
                .as(Pet.class);

        Assert.assertEquals(actual.getName(), pet.getName(), "Кличка не совпадаете с переданной для обновления");

    }

}
