package com.ly.spider.core;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ly.spider.app.DataSource;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.rule.Rule;
import com.ly.spider.rule.RuleException;
import com.ly.spider.util.TextUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class WebSearchService
{
	private  Set<HouseInfoData> datas=new ConcurrentSkipListSet<HouseInfoData>();
	private  ExecutorService service=Executors.newCachedThreadPool();
	
	private  class Task implements Runnable{
		String url;
		String tag;
		public Task(String url,String tag) {
			// TODO Auto-generated constructor stub
			this.url=url;
			this.tag=tag;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			fetchHousesInfo(this.url, this.tag);
		}
		
	}
	public  Set<HouseInfoData> extract(Rule rule,String preUrl,String conditionUrl)
	{

		// 进行对rule的必要校验
		//validateRule(rule);
		int pageNum=fetchPages(preUrl+conditionUrl+"/");
		for(int i=1;i<=pageNum;i++){
			String url=preUrl+"pg"+i+conditionUrl+"/";
			System.out.println("fetch url->"+url);
			//fetchHousesInfo(url, rule.getResultTagName());       //同步方式
			service.submit(new Task(url, rule.getResultTagName()));//异步方式
		}
		service.shutdown();
		while (true) {  
            if (service.isTerminated()) {  
                System.out.println("结束了！");  
                break;  
            }  
            try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  
		return datas;
	}
	private  int fetchPages(String url){
		
		Connection conn = Jsoup.connect(url);
		
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
	private  void fetchHousesInfo(String url,String tag){
		
		/////mysql////////////
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = DataSource.getInstance().getConnection();
			//connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////jsoup////////////
		Connection conn = Jsoup.connect(url);
		Document doc = null;
		try {
			doc = conn.timeout(100000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			data.setPrice(Integer.valueOf(price));
			data.setUnitPrice(atoi(unitPrice));
			data.setTitle(title);
			data.setArea(area);
			data.setAddress(address);
			
			int index=linkUrl.indexOf(".html");
	        String id=linkUrl.substring(index-12, index);
	      
			try {
				String searchSql="select * from houseinfo where id=?";
				statement=(PreparedStatement) connection.prepareStatement(searchSql);
				statement.setString(1, id);
				
				ResultSet rs=statement.executeQuery();
				String history;
				if(rs!=null && rs.next()){
					history=rs.getString("history");
				}else{
					//新找到的房源暂时不添加到数据库 数据库的添加默认只有每天schedule时
//					JSONObject jsonObject=new JSONObject();
//			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
//			        String now=dateFormat.format(new Date());
//			        jsonObject.put(data.getPrice(), now);
//			        history=jsonObject.toString();
					
					JSONArray jsonArray=new JSONArray();
			        JSONObject jsonObject=new JSONObject();
			        jsonObject.put("price", data.getPrice());
			        jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			        jsonArray.add(jsonObject);
			        history=jsonArray.toString();
				}
				data.setHistory(history);
				
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			datas.add(data);
		}
	}
	private static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}
	/**
	 * 对传入的参数进行必要的校验
	 */
	private static void validateRule(Rule rule)
	{
		String url = rule.getUrl();
		if (TextUtil.isEmpty(url))
		{
			throw new RuleException("url不能为空！");
		}
		if (!url.startsWith("http://"))
		{
			throw new RuleException("url的格式不正确！");
		}

		if (rule.getParams() != null && rule.getValues() != null)
		{
			if (rule.getParams().length != rule.getValues().length)
			{
				throw new RuleException("参数的键值对个数不匹配！");
			}
		}

	}


}
