package org.example.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Order {

    @SerializedName("id")
    private int id;

    @SerializedName("petId")
    private int petId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("shipDate")
    private String shipDate;

    @SerializedName("status")
    private String status;

    @SerializedName("complete")
    private boolean complete;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "id = '" + id + '\'' +
                "petId = '" + petId + '\'' +
                "quantity = '" + quantity + '\'' +
                "shipDate = '" + shipDate + '\'' +
                "status = '" + status + '\'' +
                "complete = '" + complete +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return petId == order.petId &&
                quantity == order.quantity &&
                id == order.id &&
                complete == order.complete &&
                Objects.equals(shipDate, order.shipDate) &&
                Objects.equals(status, order.status);
    }
}

