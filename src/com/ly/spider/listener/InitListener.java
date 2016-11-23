package com.ly.spider.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class InitListener implements ServletContextListener{
	Timer timer=null;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("system Initialized.........");
		ServletContext context=sce.getServletContext();
		context.setAttribute("newHouseNum", "0");
		context.setAttribute("modifyHouseNum", "0");
		
		timer=new Timer();
		long delay=1000*30; //10s
		long period=1000*60*60*24;//1 day
		timer.schedule(new ScheduleTask(context),delay,period);
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		System.out.println("task begin time->"+dateFormat.format(new Date()));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("system Destroyed.........");
		timer.cancel();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		System.out.println("task end time->"+dateFormat.format(new Date()));
	}

}
