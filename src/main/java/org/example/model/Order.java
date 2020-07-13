package org.example.model;

import com.google.gson.annotations.SerializedName;

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

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return id == order.id &&
				petId == order.petId &&
				quantity == order.quantity &&
				complete == order.complete &&
				status.equals(order.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, petId, quantity, shipDate, status, complete);
	}
}