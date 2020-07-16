package org.example.store;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Inventory extends HashMap<String, Integer>{

	@SerializedName("text")
	private String text;

	@SerializedName("num")
	private Integer num;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "Inventory{" +
						"text='" + text + '\'' +
						", num=" + num +
						'}';
	}
}