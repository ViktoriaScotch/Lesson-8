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

	@Override
	public String toString() {
		return "Order{" +
				"petId=" + petId +
				", quantity=" + quantity +
				", id=" + id +
				", shipDate='" + shipDate + '\'' +
				", complete=" + complete +
				", status='" + status + '\'' +
				'}';
	}

	@SerializedName("shipDate")
	private String shipDate;

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

	public Order() {
		super();
	}

	public Order(int petId, int quantity, int id, String shipDate, boolean complete, String status) {
		this.petId = petId;
		this.quantity = quantity;
		this.id = id;
		this.shipDate = shipDate;
		this.complete = complete;
		this.status = status;
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

	@Override
	public int hashCode() {
		return Objects.hash(petId, quantity, id, shipDate, complete, status);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public int getQuantity() {
		return quantity;
	}

	@SerializedName("complete")
	private boolean complete;

	@SerializedName("status")
	private String status;

	public int getPetId() {
		return petId;
	}

	public int getQuantity(int i) {
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


}