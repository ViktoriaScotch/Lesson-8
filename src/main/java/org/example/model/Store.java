package org.example.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Store {
    @SerializedName("id")
    private int id;

    @SerializedName("petId")
    private int petId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("shipDate")
    private String shipDate = HelperClass.getActualData();

    @SerializedName("status")
    private String status;

    @SerializedName("complete")
    private boolean complete;

    public void setId(int id) {
        id = this.id;
    }

    public int getId(int id) {
        return id;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getPetId(int petId) {
        return petId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity(int quantity) {
        return quantity;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(String status) {
        return status;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean getComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return
                "store {" +
                        "id = ='" + id + '\'' +
                        ",petId = '" + petId + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",shipDate = '" + shipDate + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}