package org.example.api;

public class Order{
    private int petId;
    private int quantity;
    private int id;
    private String shipDate;
    private boolean complete;
    private String status;

    public int getPetId(){
        return petId;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getId(){
        return id;
    }

    public String getShipDate(){
        return shipDate;
    }

    public boolean isComplete(){
        return complete;
    }

    public String getStatus(){
        return status;
    }

    public void setPetId(int id){this.petId = id;}

    public void setQuantity(int quantity){this.quantity=quantity;}

    public void setId (int id){this.id = id;}

    public void setShipDate(String shipDate){this.shipDate=shipDate;}

    public void setComplete(boolean complete){this.complete=complete;}

    public void setStatus(String status){this.status=status;}



}