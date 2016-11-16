package com.ly.spider.core;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
	 */
	public static void extractFine(String url,String conditionUrl){
		
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
		
		Element result = doc.select("div[data-role=ershoufang]").get(0);
		Element child=result.child(1);
		Elements as = child.select("a");
		for (Element a : as)
		{
			String areaUrl=Config.HOST+a.attr("href");
			System.out.println(a.attr("href")+" begin");
			
			int pageNum=fetchPages(areaUrl);
			for(int i=1;i<=pageNum;i++){
				String workUrl=areaUrl+"pg"+i+conditionUrl;
				fetchHousesInfo(workUrl);       //同步方式
				System.out.println(workUrl+" finish");
			}
		
			try {
				System.out.println(a.attr("href")+" finish,count->"+newDatas.size());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
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
		header.put("Referer", "http://bj.lianjia.com/");
		//header.put("Cookie", "lianjia_uuid=2a78cbb5-d0ec-4114-8d56-aba9b4e80711; all-lj=144beda729446a2e2a6860f39454058b; _jzqckmp=1; select_city=110000; _jzqx=1.1478601358.1478853669.6.jzqsr=localhost:8080|jzqct=/househelper/uiservlet.jzqsr=captcha%2Elianjia%2Ecom|jzqct=/; _jzqy=1.1478596144.1478854424.4.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6.jzqsr=baidu; _ga=GA1.2.1353923336.1478745673; CNZZDATA1253477573=1234600298-1478593989-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478853276; CNZZDATA1254525948=818341921-1478594932-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478854139; CNZZDATA1255633284=992971032-1478594548-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478853771; CNZZDATA1255604082=674674676-1478594240-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478853475; _qzja=1.1925370159.1478596144085.1478841931567.1478853668904.1478854497485.1478854669372.0.0.0.220.14; _qzjb=1.1478853668903.5.0.0.0; _qzjc=1; _qzjto=22.3.0; _smt_uid=5821962f.34eed583; _jzqa=1.1805320548480478500.1478596144.1478841932.1478853669.14; _jzqc=1; _jzqb=1.5.10.1478853669.1; lianjia_ssid=333626f2-a0b8-425d-9b1c-ed066d206673");
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
				        jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				        jsonArray.add(jsonObject);
				        history=jsonArray.toString();
						
						
						statement.close();
						String updateSql="update houseinfo set price=? , history=? , gap=? where id=?";
						statement=(PreparedStatement) connection.prepareStatement(updateSql);
						
						statement.setDouble(1, data.getPrice());
						statement.setString(2, history);
						statement.setDouble(3, gap);
						statement.setString(4, id);
						
						statement.executeUpdate();
						statement.close();
						modifyDatas.add(data);
						System.out.println("价格变动->"+data.getLinkUrl());
					}else{
						//clear last gap
						double gap=0;
						
						statement.close();
						String updateSql="update houseinfo set  gap=? where id=?";
						statement=(PreparedStatement) connection.prepareStatement(updateSql);
						
						statement.setDouble(1, gap);
						statement.setString(2, id);
						
						statement.executeUpdate();
						statement.close();
					}
				}else{
					//insert
					
					statement.close();
					String insertSql="insert into houseinfo(linkUrl,picUrl,title,price,unitPrice,area,address,history,id) values(?,?,?,?,?,?,?,?,?)";
					statement=(PreparedStatement) connection.prepareStatement(insertSql);
					
//					JSONObject jsonObject=new JSONObject();
//			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
//			        String now=dateFormat.format(new Date());
//			        jsonObject.put(data.getPrice(), now);
//			        String history=jsonObject.toString();
					
					JSONArray jsonArray=new JSONArray();
			        JSONObject jsonObject=new JSONObject();
			        jsonObject.put("price", data.getPrice());
			        jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
