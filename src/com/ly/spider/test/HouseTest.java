package com.ly.spider.test;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.config.Config;
import com.ly.spider.core.JavaHouseAddDBService;
import com.ly.spider.core.JavaHouseService;
import com.ly.spider.rule.Rule;

public class HouseTest
{
	//private static String url="http://bj.lianjia.com/ershoufang/bp360ep460rs望京/";
	private static String preUrl="http://bj.lianjia.com/ershoufang/";
	private static String conditionUrl="bp330ep365rs望京/";
	private static String tag="li.clear";

	
	public static void getLianJiaDatas()
	{
		Rule rule = new Rule(preUrl+conditionUrl,
				null, 
				null,
				tag, //div.title a[data-el=ershoufang]
				Rule.SELECTION, 
				Rule.GET);
		Set<HouseInfoData> extracts = JavaHouseService.extract(rule,preUrl,conditionUrl);
		System.out.println("共找到"+extracts.size()+"套\n");
		JSONArray array=JSONArray.fromObject(extracts);
		JSONObject ret=new JSONObject();
		ret.put("count", extracts.size());
		ret.put("houses", array);
		System.out.println(ret.toString());
		//for (HouseInfoData data : extracts)System.out.println(data.toString());
	}
	
	public static void addToDB()
	{
		Set<HouseInfoData> extracts =new ConcurrentSkipListSet<HouseInfoData>();
		for(int i=0;i<Config.areas.length;i++){
			String area=Config.areas[i];
			
			Set<HouseInfoData> extract = JavaHouseAddDBService.extract(preUrl+area+"/","/",tag);		
			System.out.println(Config.areas[i]+" 找到"+extract.size()+"套\n");
			
			extracts.addAll(extract);
		}
	
		System.out.println("共找到"+extracts.size()+"套\n");

	}
	public static void main(String[] args) {
		
		//getLianJiaDatas();
		addToDB();
	}
}
