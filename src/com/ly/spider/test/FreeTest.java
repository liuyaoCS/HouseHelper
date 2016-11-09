package com.ly.spider.test;

public class FreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double dy=4.9*0.85/100;
		double dm=dy/12; //每月贷款利率       
		double price=350;
		double  hep=price*10000*0.9; //0.9房评    
		double sum=hep*0.65;//可贷款总额
        int months=360;
        double downPay=price*10000-sum+hep*1/100+price*10000*2.7/100;//总首付
        downPay=downPay/10000;
        System.out.println("downPay->"+downPay);
	}

}
