package com.ly.spider.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TextUtil
{
	public static  boolean isEmpty(String str)
	{
		if(str == null || str.trim().length() == 0)
		{
			return true ;
		}
		return false ;
	}
	public static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}
	/**
	 * 
	 * @param day 0表示今天 -1表示昨天 1表示明天 以此类推
	 * @return
	 */
	public static String getDateString(int day){
		Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,day);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(date);
        return dateString;
	}
}
