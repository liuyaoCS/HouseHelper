package com.ly.spider.listener;

import java.beans.PropertyVetoException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.ly.spider.app.Config;
import com.ly.spider.app.DataSource;
import com.ly.spider.core.WebScheduleDBService;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ScheduleTask extends TimerTask {
	private final String preUrl="http://bj.lianjia.com/ershoufang/";
	private ServletContext mContext;
	private static ComboPooledDataSource cpds;
	public ScheduleTask(ServletContext context) {
		// TODO Auto-generated constructor stub
		mContext=context;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		try {
//			configMysql();
//		} catch (PropertyVetoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		cpds=DataSource.getInstance();
		fetchPriceFromDB();
		scheduleDB();
	}

	private  void scheduleDB()
	{	
	
		long begintime=System.currentTimeMillis();
		for(int i=0;i<Config.areas.length;i++){
			String area=Config.areas[i];
			WebScheduleDBService.extract(preUrl+area+"/","/");		
		}
		int newHouseNum=WebScheduleDBService.newDatas.size();
		int modifyHouseNum=WebScheduleDBService.modifyDatas.size();
		long endtime=System.currentTimeMillis();
		System.out.println("新增"+newHouseNum+"套。报价变动"+modifyHouseNum+"套。耗时:"+(endtime-begintime)/60000+"分钟");
		
		this.mContext.setAttribute("newHouseNum", newHouseNum+"");
		this.mContext.setAttribute("modifyHouseNum", modifyHouseNum+"");
	}
	private  void fetchPriceFromDB(){
		
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = cpds.getConnection();
			
			String searchSql="select avg(price) as avgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(searchSql);
			ResultSet rs=statement.executeQuery();
			DecimalFormat df = new DecimalFormat("0.00");
			if(rs!=null && rs.next()){
				double avgPrice=rs.getDouble("avgPrice");
				this.mContext.setAttribute("avgPrice", df.format(avgPrice));
			}
			
			String uSql="select avg(unitPrice) as unitAvgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(uSql);
			ResultSet urs=statement.executeQuery();
			if(urs!=null && urs.next()){
				double unitAvgPrice=urs.getDouble("unitAvgPrice");
				this.mContext.setAttribute("unitAvgPrice", df.format(unitAvgPrice));
			}
			
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void configMysql()
			throws PropertyVetoException{
		cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/houses");
		cpds.setUser("root");
		cpds.setPassword("985910");
	}

}
