package com.ly.spider.core;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.text.SimpleDateFormat;

import javax.enterprise.inject.New;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ly.spider.bean.HouseInfoData;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 防止爬虫被封：单线程爬虫+header头伪造
 * 历史版本:
 * 	1 线程池执行任务 每次执行完要关闭线程池 线程池不可重复使用
 * 	2 线程池执行任务 通过FutureTask获取线程结束 线程池可复用,但是爬虫容易被封 所以建议仍然使用单线程爬虫
 */
public class JavaAddDBService
{
	private static Set<HouseInfoData> datas=new ConcurrentSkipListSet<HouseInfoData>();
	private static String tag="li.clear";
	private static ComboPooledDataSource cpds;
	private static String sql="insert into houseinfo(linkUrl,picUrl,title,price,unitPrice,area,address,history,id) values(?,?,?,?,?,?,?,?,?)";
	
	public static Set<HouseInfoData> extract(String preUrl,String conditionUrl)
	{		
		int pageNum=fetchPages(preUrl);
		for(int i=1;i<=pageNum;i++){
			String url=preUrl+"pg"+i+conditionUrl;
			fetchHousesInfo(url);       //同步方式
			System.out.println(url+" finish");
		}
	
		try {
			System.out.println(preUrl+" finish,count->"+datas.size());
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	private static int fetchPages(String url){
		
		////////header/////////
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "bj.lianjia.com");
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection", "keep-alive");
		Connection conn = Jsoup.connect(url).data(header);
		//////////////
		Document doc = null;
		try {
			doc = conn.timeout(100000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//处理返回数据
		Elements pageEles=doc.select("div.house-lst-page-box");
		if(pageEles.size()==0)return 0;
		Element pageEle=pageEles.get(0);
		String pagesStr=pageEle.attr("page-data");
		JSONObject pagesJson = JSONObject.fromObject(pagesStr);
		int pages=(Integer) pagesJson.get("totalPage");
		
		return pages;
	}
	private static void fetchHousesInfo(String url){
		
		////////header/////////
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "bj.lianjia.com");
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection", "keep-alive");
		Connection conn = Jsoup.connect(url).data(header);
		//////////////
		Document doc = null;
		try {
			doc = conn.timeout(100000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/////////////mysql////////
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = cpds.getConnection();
			//connection.setAutoCommit(false);
			statement=(PreparedStatement) connection.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		////////////////////
		Elements results = doc.select(tag);
		for (Element result : results)
		{
			Element linkUrlEle = result.select("div.title a").get(0);
			String linkUrl=linkUrlEle.attr("href");
			Element picUrlEle = result.select("a.img img").get(0);
			String picUrl=picUrlEle.attr("data-original");
			Element priceEle=result.select("div.totalPrice span").get(0);
			String price=priceEle.text();
			Element unitPriceEle=result.select("div.unitPrice span").get(0);
			String unitPrice=unitPriceEle.text();
			Element titleEle=result.select("div.title").get(0);
			String title=titleEle.text();			
			Element areaEle=result.select("div.positionInfo a").get(0);
			String area=areaEle.text();				
			Element addressEle=result.select("div.houseInfo a").get(0);
			String address=addressEle.text();
			
			HouseInfoData data=new HouseInfoData();
			data.setLinkUrl(linkUrl);
			data.setPicUrl(picUrl);
			data.setPrice(Double.valueOf(price));
			data.setUnitPrice(atoi(unitPrice));
			data.setTitle(title);
			data.setArea(area);
			data.setAddress(address);
			
			datas.add(data);
			//////insert into mysql////
			int index=linkUrl.indexOf(".html");
	        String id=linkUrl.substring(index-12, index);
	        
//	        JSONObject jsonObject=new JSONObject();
//	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
//	        String now=dateFormat.format(new Date());
//	        jsonObject.put(data.getPrice(), now);
//	        String history=jsonObject.toString();
	        
	        JSONArray jsonArray=new JSONArray();
	        JSONObject jsonObject=new JSONObject();
	        jsonObject.put("price", data.getPrice());
	        jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	        jsonArray.add(jsonObject);
	        String history=jsonArray.toString();
	        
	        
			try {
				statement.setString(1, data.getLinkUrl());
				statement.setString(2, data.getPicUrl());
				statement.setString(3, data.getTitle());
				statement.setDouble(4, data.getPrice());
				statement.setInt(5, data.getUnitPrice());
				statement.setString(6, data.getArea());
				statement.setString(7, data.getAddress());
				statement.setString(8, history);
				statement.setString(9, id);
				
				//statement.addBatch();
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			////////
		}
		try {
			//statement.executeBatch();
			//connection.commit();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}
	public static void configMysql()
			throws PropertyVetoException, SQLException {
		cpds=new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/houses");
		cpds.setUser("root");
		cpds.setPassword("985910");
	}
}
