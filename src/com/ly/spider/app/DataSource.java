package com.ly.spider.app;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {
	private static ComboPooledDataSource cpds;
	public static ComboPooledDataSource getInstance(){
		if(cpds==null){
			cpds=new ComboPooledDataSource(); 
			try {
				configMysql();
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cpds;
	}
	public  static void configMysql()throws PropertyVetoException{
		cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/houses");
		cpds.setUser("root");
		cpds.setPassword("985910");
	}
}
