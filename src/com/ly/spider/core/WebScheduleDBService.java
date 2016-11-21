package com.ly.spider.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ly.spider.app.Config;
import com.ly.spider.app.DataSource;
import com.ly.spider.app.JsoupConn;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.util.TextUtil;


public class WebScheduleDBService
{
	public static Set<HouseInfoData> newDatas=new ConcurrentSkipListSet<HouseInfoData>();
	public static Set<HouseInfoData> modifyDatas=new ConcurrentSkipListSet<HouseInfoData>();
	/**
	 * 每个区限制100页结果（3000套）
	 */
	public static void extract(String preUrl,String conditionUrl)
	{
		
		int pageNum=fetchPages(preUrl);
		for(int i=1;i<=pageNum;i++){
			String url=preUrl+"pg"+i+conditionUrl;
			fetchHousesInfo(url);       //同步方式
			System.out.println(url+" finish");
		}
	
		try {
			System.out.println(preUrl+" finish,count->"+newDatas.size());
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 无限制
	 * url:区的url a：代表小区
	 */
	public static void extractFine(String url,String conditionUrl){
		
		Document doc = null;
		try {
			doc = JsoupConn.getInstance(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements results = doc.select("div[data-role=ershoufang]");
		if(results==null ||  results.size()==0){
			System.out.println(url+" err");
			return;
		}
		Element child=results.get(0).child(1);
		Elements as = child.select("a");
		for (Element a : as)
		{
			String areaUrl=Config.HOST+a.attr("href");
			System.out.println(a.attr("href")+" begin");
			
			int pageNum=fetchPages(areaUrl);
			for(int i=1;i<=pageNum;i++){
				String workUrl=areaUrl+"pg"+i+conditionUrl;
				fetchHousesInfo(workUrl);       //同步方式
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(workUrl+" finish");
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(a.attr("href")+" finish,count->"+newDatas.size());
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int fetchPages(String url){
		
		Document doc = null;
		try {
			doc = JsoupConn.getInstance(url).get();
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
		
		Document doc = null;
		try {
			doc = JsoupConn.getInstance(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/////////////mysql////////
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = DataSource.getInstance().getConnection();
			//connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		////////////////////
		Elements results = doc.select(Config.TAG);
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
			data.setUnitPrice(TextUtil.atoi(unitPrice));
			data.setTitle(title);
			data.setArea(area);
			data.setAddress(address);
			
			//////mysql////
			int index=linkUrl.indexOf(".html");
	        String id=linkUrl.substring(index-12, index);
			try {

				String searchSql="select * from houseinfo where id=?";
				statement=(PreparedStatement) connection.prepareStatement(searchSql);
				statement.setString(1, id);
				ResultSet rs=statement.executeQuery();
				
				if(rs!=null && rs.next()){
					double dbprice=rs.getDouble("price");
					if(dbprice!=data.getPrice()){
						//update
						double gap=data.getPrice()-dbprice;
						String history=rs.getString("history");
				        
				        JSONArray jsonArray=JSONArray.fromObject(history);
				        JSONObject jsonObject=new JSONObject();
				        jsonObject.put("price", data.getPrice());
				        jsonObject.put("date", TextUtil.getDateString(-1));
				        jsonArray.add(jsonObject);
				        history=jsonArray.toString();
						
						
						statement.close();
						String updateSql="update houseinfo set price=? , history=? , gap=?, flag=? where id=?";
						statement=(PreparedStatement) connection.prepareStatement(updateSql);
						
						statement.setDouble(1, data.getPrice());
						statement.setString(2, history);
						statement.setDouble(3, gap);
						statement.setInt(4, 1);
						statement.setString(5, id);
						
						
						statement.executeUpdate();
						statement.close();
						modifyDatas.add(data);
						System.out.println("价格变动->"+data.getLinkUrl());
					}else{
						//clear last gap and set flag
						statement.close();
						String updateSql="update houseinfo set  gap=? ,flag=? where id=?";
						statement=(PreparedStatement) connection.prepareStatement(updateSql);
						
						statement.setDouble(1, 0);
						statement.setInt(2, 1);
						statement.setString(3, id);
						
						
						statement.executeUpdate();
						statement.close();
					}
				}else{
					//insert
					
					statement.close();
					String insertSql="insert into houseinfo(linkUrl,picUrl,title,price,unitPrice,area,address,history,id,flag) values(?,?,?,?,?,?,?,?,?,?)";
					statement=(PreparedStatement) connection.prepareStatement(insertSql);
					
//					JSONObject jsonObject=new JSONObject();
//			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
//			        String now=dateFormat.format(new Date());
//			        jsonObject.put(data.getPrice(), now);
//			        String history=jsonObject.toString();
					
					JSONArray jsonArray=new JSONArray();
			        JSONObject jsonObject=new JSONObject();
			        jsonObject.put("price", data.getPrice());
			        jsonObject.put("date", TextUtil.getDateString(-1));
			        jsonArray.add(jsonObject);
			        String history=jsonArray.toString();
					
					statement.setString(1, data.getLinkUrl());
					statement.setString(2, data.getPicUrl());
					statement.setString(3, data.getTitle());
					statement.setDouble(4, data.getPrice());
					statement.setInt(5, data.getUnitPrice());
					statement.setString(6, data.getArea());
					statement.setString(7, data.getAddress());
					statement.setString(8, history);
					statement.setString(9, id);
					statement.setInt(10, 1);
					
					statement.executeUpdate();
					statement.close();
					newDatas.add(data);
					System.out.println("新增房源->"+data.getLinkUrl());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			//////mysql//////
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
