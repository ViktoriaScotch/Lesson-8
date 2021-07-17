package org.example.api;

import org.example.model.Order;
import org.testng.annotations.DataProvider;

import java.util.Date;

public class HomeTaskApiData {
    @DataProvider
    public static Object[][] dataGetOrderById() {
//        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        String id = System.getProperty("orderId");
        return id == null ?
                new Object[][]{{1}} :
                new Object[][]{{id}};
    }

    @DataProvider
    public static Object[] dataPostOrder() {
        String id = System.getProperty("orderId");
        String petId = System.getProperty("petId");
        String quantity = System.getProperty("quantity");
        String shipDate = System.getProperty("shipDate");
        String status = System.getProperty("status");
        String complete = System.getProperty("complete");

        Order order = new Order();
        order.setId(id == null ? 11 : Integer.parseInt(id));
        order.setPetId(petId == null ? 1 : Integer.parseInt(petId));
        order.setQuantity(quantity == null ? 1 : Integer.parseInt(quantity));
        order.setShipDate(shipDate == null ? "2021-07-17T20:48:40.374+0000" : shipDate);
        order.setStatus(status == null ? "placed" : status);
        order.setComplete(Boolean.parseBoolean(complete));

        return new Object[]{order};
    }
}