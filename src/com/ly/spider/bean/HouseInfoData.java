package com.ly.spider.bean;

public class HouseInfoData implements Comparable<HouseInfoData>{
	private String picUrl;
	private String linkUrl;
	private String title;
	private double price;
	private int unitPrice;
	private String area;
	private String address;
	//////extra///////
	private String history;
	private String id;
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Override
	public int compareTo(HouseInfoData data) {
		// TODO Auto-generated method stub
		double ret= this.price-data.getPrice();
		if(ret==0){
			ret= this.unitPrice-data.unitPrice;
		}
		if(ret==0){
			ret=this.hashCode()-data.hashCode();
		}
		return (int) ret;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append(getTitle()+"\n");
		sb.append("链接:"+getLinkUrl()+"\n");
		sb.append("图片链接:"+getPicUrl()+"\n");
		sb.append("总价："+getPrice()+"万 单价："+getUnitPrice()+"元/平米"+"\n");
		sb.append("位置:"+getArea()+"."+getAddress()+"\n****************");
		return sb.toString();
	}
}
