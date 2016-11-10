package com.ly.spider.core;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.rule.Rule;
import com.ly.spider.rule.RuleException;
import com.ly.spider.util.TextUtil;

//import net.sf.json.JSONArray; 
//import net.sf.json.JSONObject;
//import net.sf.json.util.JSONStringer;

public class JavaHouseAddDBService2
{
	private static Set<HouseInfoData> datas=new ConcurrentSkipListSet<HouseInfoData>();
	private static ExecutorService service=Executors.newCachedThreadPool();
	private static ArrayList<FutureTask<Integer>> tasks=new ArrayList<FutureTask<Integer>>();
	private static class Task implements Callable<Integer>{
		String url;
		String tag;
		int index;
		public Task(String url,String tag,int index) {
			// TODO Auto-generated constructor stub
			this.url=url;
			this.tag=tag;
			this.index=index;
		}
		
		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			//System.out.println("task"+this.index+" enter");
			fetchHousesInfo(this.url, this.tag);
			//System.out.println("task"+this.index+" exit");
			return 0;
		}
		
	}
	public static Set<HouseInfoData> extract(String preUrl,String conditionUrl,String tag)
	{

		int pageNum=fetchPages(preUrl);
		for(int i=1;i<=pageNum;i++){
			String url=preUrl+"pg"+i+conditionUrl;
			//System.out.println(url);
			//fetchHousesInfo(url, rule.getResultTagName());       //同步方式
			FutureTask<Integer> task=new FutureTask<Integer>(new Task(url, tag,i));
			service.submit(task);//异步方式
			tasks.add(task);
		}
		//service.shutdown();
//		while (true) {  
//            if (service.isTerminated()) {  
//                System.out.println("结束了！");  
//                break;  
//            }  
//            try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//        }  
		for(FutureTask<Integer> task:tasks){
			try {
				int count=task.get();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			System.out.println(preUrl+" finish,count->"+datas.size());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	private static int fetchPages(String url ){
		
		Connection conn = Jsoup.connect(url);
//		conn.header("cookie", "lianjia_uuid=2a78cbb5-d0ec-4114-8d56-aba9b4e80711; _jzqx=1.1478601358.1478672171.2.jzqsr=localhost:8080|jzqct=/househelper/uiservlet.jzqsr=192%2E168%2E66%2E117:8080|jzqct=/househelper/uiservlet; select_city=110000; _jzqckmp=1; logger_session=867cde6f18891c90abbe32e5de7082a7; all-lj=144beda729446a2e2a6860f39454058b; _jzqy=1.1478596144.1478743068.1.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6.-; _smt_uid=5821962f.34eed583; CNZZDATA1253477573=1234600298-1478593989-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478739856; CNZZDATA1254525948=818341921-1478594932-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740732; CNZZDATA1255633284=992971032-1478594548-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740363; CNZZDATA1255604082=674674676-1478594240-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740068; _qzja=1.1925370159.1478596144085.1478740436340.1478743067627.1478743100551.1478743110944.0.0.0.129.7; _qzjb=1.1478743067627.4.0.0.0; _qzjc=1; _qzjto=24.2.0; _jzqa=1.1805320548480478500.1478596144.1478740436.1478743068.7; _jzqc=1; _jzqb=1.4.10.1478743068.1; lianjia_ssid=78e376c2-57b9-4d4b-8cd0-3de2fb674102");
//		conn.header("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
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
	private static void fetchHousesInfo(String url,String tag){
		Connection conn = Jsoup.connect(url);
//		conn.header("cookie", "lianjia_uuid=2a78cbb5-d0ec-4114-8d56-aba9b4e80711; _jzqx=1.1478601358.1478672171.2.jzqsr=localhost:8080|jzqct=/househelper/uiservlet.jzqsr=192%2E168%2E66%2E117:8080|jzqct=/househelper/uiservlet; select_city=110000; _jzqckmp=1; logger_session=867cde6f18891c90abbe32e5de7082a7; all-lj=144beda729446a2e2a6860f39454058b; _jzqy=1.1478596144.1478743068.1.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6.-; _smt_uid=5821962f.34eed583; CNZZDATA1253477573=1234600298-1478593989-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478739856; CNZZDATA1254525948=818341921-1478594932-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740732; CNZZDATA1255633284=992971032-1478594548-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740363; CNZZDATA1255604082=674674676-1478594240-http%253A%252F%252Fbzclk.baidu.com%252F%7C1478740068; _qzja=1.1925370159.1478596144085.1478740436340.1478743067627.1478743100551.1478743110944.0.0.0.129.7; _qzjb=1.1478743067627.4.0.0.0; _qzjc=1; _qzjto=24.2.0; _jzqa=1.1805320548480478500.1478596144.1478740436.1478743068.7; _jzqc=1; _jzqb=1.4.10.1478743068.1; lianjia_ssid=78e376c2-57b9-4d4b-8cd0-3de2fb674102");
//		conn.header("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
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
			data.setPrice(Double.valueOf(price));
			data.setUnitPrice(atoi(unitPrice));
			data.setTitle(title);
			data.setArea(area);
			data.setAddress(address);
			
			datas.add(data);
		}
	}
	private static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}

}
