package com.ly.spider.bean;

public class PriceTrendData {
	String id;
	double unitPrice;
	double totalPrice;
	public PriceTrendData(String id, double unitPrice, double totalPrice) {
		super();
		this.id = id;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	

}
