package com.ly.spider.test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CpDataSourceAction3();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void CpDataSourceAction()
			throws PropertyVetoException, SQLException {
		ComboPooledDataSource cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
		cpds.setUser("root");
		cpds.setPassword("985910");
		
		String sql="select * from userAccount where name=? and password=?";
		Connection connection= cpds.getConnection();
		PreparedStatement statement=(PreparedStatement) connection.prepareStatement(sql);
		statement.setString(1, "ly");
		statement.setString(2, "123");
		
		ResultSet rSet=statement.executeQuery();
		if(rSet.next()){
			System.out.println(rSet.getString("name"));
			System.out.println(rSet.getString("password"));
		}
		rSet.close();
		statement.close();
		connection.close();
	}
	private static void CpDataSourceAction2()
			throws PropertyVetoException, SQLException {
		ComboPooledDataSource cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/houses");
		cpds.setUser("root");
		cpds.setPassword("985910");
		
		String sql="insert into houseinfo(linkUrl,picUrl,title,price,unitPrice,area,address) values(?,?,?,?,?,?,?)";
		//String sql="insert into test(name) values(?)";
		Connection connection= cpds.getConnection();
		PreparedStatement statement=(PreparedStatement) connection.prepareStatement(sql);
	
		statement.setString(1, "linkUrl");
		statement.setString(2, "picUrl");
		statement.setString(3, "title");
		statement.setDouble(4, 12.0);
		statement.setInt(5, 11);
		statement.setString(6, "wangjing");
		statement.setString(7, "nanhuquxili");
		
		statement.executeUpdate();
		
		
		
		statement.close();
		connection.close();
		System.out.println("insert over!");
	}
	private static void CpDataSourceAction3()
			throws PropertyVetoException, SQLException {
		ComboPooledDataSource cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/houses");
		cpds.setUser("root");
		cpds.setPassword("985910");
		
		//String sql0="select ";
		String sql="update houseinfo set history";
		//String sql="insert into test(name) values(?)";
		Connection connection= cpds.getConnection();
		PreparedStatement statement=(PreparedStatement) connection.prepareStatement(sql);
	
		statement.setString(1, "linkUrl");
		statement.setString(2, "picUrl");
		statement.setString(3, "title");
		statement.setDouble(4, 12.0);
		statement.setInt(5, 11);
		statement.setString(6, "wangjing");
		statement.setString(7, "nanhuquxili");
		
		statement.executeUpdate();
		
		
		
		statement.close();
		connection.close();
		System.out.println("insert over!");
	}

}
