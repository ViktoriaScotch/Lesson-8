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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HomeTaskApiTest {
    public static final int SUCCESSFUL_OPERATION = 200;
    public static final int INVALID_ORDER = 400;
    public static final int INVALID_ID_SUPPLIED = 400;
    public static final int ORDER_NOT_FOUND = 404;

    @BeforeClass
    public void prepare() {
        InputStream inp = ClassLoader.getSystemResourceAsStream("my.properties");
        assert (inp != null) : "Файл \"my.properties\" не найден!";
        try {
            System.getProperties().load(inp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String URLPostfix = "store/";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/" + URLPostfix)
                .addHeader("api_key", System.getProperty("api.key"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    public void testGetInventory() {
        Map inventory = RestAssured.given()
                .when()
                .get("inventory")
                .then()
                .statusCode(SUCCESSFUL_OPERATION)
                .extract()
                .body()
                .as(Map.class);
        Assert.assertTrue(inventory.containsKey("sold"), "Inventory не содержит статус sold (ключ \"sold\" не найден).");
    }

    @Test(dataProvider = "dataPostOrder", dataProviderClass = HomeTaskApiData.class)
    public void testPostOrder(Order order) {
        RestAssured.given()
                .body(order)
                .when()
                .post("order")
                .then()
                .statusCode(SUCCESSFUL_OPERATION);

        Order actualOrder = getOrderByIdAsOrder(order.getId(), SUCCESSFUL_OPERATION);
        Assert.assertEquals(order, actualOrder);
    }

    public void getOrderById(Object id, int code) {
        RestAssured.given()
                .pathParam("orderId", id)
                .when()
                .get("order/{orderId}")
                .then()
                .statusCode(code);
    }

    public Order getOrderByIdAsOrder(Object id, int code) {
        return RestAssured.given()
                .pathParam("orderId", id)
                .when()
                .get("order/{orderId}")
                .then()
                .statusCode(code)
                .extract()
                .body()
                .as(Order.class);
    }

    // For valid response try integer IDs with value >= 1 and <= 10. Other values will generated exceptions.
    @Test(dataProvider = "dataGetOrderById", dataProviderClass = HomeTaskApiData.class, priority = 1)
    public void testGetOrderById(Object id) {
        Order order = getOrderByIdAsOrder(id, SUCCESSFUL_OPERATION);
        Assert.assertEquals(order.getId(), Integer.parseInt(id.toString()));
    }

    @Test(dataProvider = "dataGetOrderById", dataProviderClass = HomeTaskApiData.class, priority = 2)
    public void testDeleteOrderById(Object id) {
        getOrderById(id, SUCCESSFUL_OPERATION);

        RestAssured.given()
                .pathParam("orderId", id)
                .when()
                .delete("order/{orderId}")
                .then()
                .statusCode(SUCCESSFUL_OPERATION);

        getOrderById(id, ORDER_NOT_FOUND);
    }
}