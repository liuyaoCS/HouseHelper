package com.ly.spider.test;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        
        String urlString="http://bj.lianjia.com/ershoufang/101100766770.html";
        int index=urlString.indexOf(".html");
        String ret=urlString.substring(index-12, index);
        System.out.println(ret);
        
        System.out.println(Integer.MAX_VALUE);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
        String show=dateFormat.format(new Date());
        System.out.println(show);
	}

}
