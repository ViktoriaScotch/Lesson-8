package org.example.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Order {

    @SerializedName("petId")
    private int petId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("id")
    private int id;

    @SerializedName("shipDate")
    private String shipDate;

    @SerializedName("complete")
    private boolean complete;

    @SerializedName("status")
    private String status;

    public Order setPetId(int petId) {
        this.petId = petId;
        return this;
    }

    public Order setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }

    public Order setShipDate(String shipDate) {
        this.shipDate = shipDate;
        return this;
    }

    public Order setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getPetId() {
        return petId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public String getShipDate() {
        return shipDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "Order{" +
                        "petId = '" + petId + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",id = '" + id + '\'' +
                        ",shipDate = '" + shipDate + '\'' +
                        ",complete = '" + complete + '\'' +
                        ",status = '" + status + '\'' +
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
                shipDate.equals(order.shipDate) &&
                status.equals(order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, quantity, id, shipDate, complete, status);
    }
}