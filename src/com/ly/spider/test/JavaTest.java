package com.ly.spider.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ly.spider.util.TextUtil;

public class JavaTest {

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
        
        double a=1.0;
        double b=1;
        System.out.println(a==b);
        
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        
        System.out.println(dateString);
        
        String time=TextUtil.getDateString(-1);
        System.out.println(time);
	}

}
