package com.ly.spider.test;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ly.spider.app.Config;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.core.JavaAddDBService;
import com.ly.spider.core.WebScheduleDBService;

public class HouseTest
{
	//private static String url="http://bj.lianjia.com/ershoufang/bp360ep460rs望京/";
	private static String preUrl="http://bj.lianjia.com/ershoufang/";
	private static String conditionUrl="bp330ep365rs望京/";
	private static String tag="li.clear";

	
//	public static void getLianJiaDatas()
//	{
//		Rule rule = new Rule(preUrl+conditionUrl,
//				null, 
//				null,
//				tag, //div.title a[data-el=ershoufang]
//				Rule.SELECTION, 
//				Rule.GET);
//		Set<HouseInfoData> extracts = JavaSearchService.extract(rule,preUrl,conditionUrl);
//		System.out.println("共找到"+extracts.size()+"套\n");
//		JSONArray array=JSONArray.fromObject(extracts);
//		JSONObject ret=new JSONObject();
//		ret.put("count", extracts.size());
//		ret.put("houses", array);
//		System.out.println(ret.toString());
//		//for (HouseInfoData data : extracts)System.out.println(data.toString());
//	}
//	
	public static void addToDB()
	{
		
		Set<HouseInfoData> extracts =new ConcurrentSkipListSet<HouseInfoData>();
		long begintime=System.currentTimeMillis();
		for(int i=0;i<Config.Areas.length;i++){
			String area=Config.Areas[i];
			Set<HouseInfoData> extract = JavaAddDBService.extract(preUrl+area+"/","/");		
			extracts.addAll(extract);
		}
		long endtime=System.currentTimeMillis();
		System.out.println("共找到"+extracts.size()+"套,耗时:"+(endtime-begintime)/60000+"分钟");

	}
//	public static void scheduleDB()
//	{
//		try {
//			JavaScheduleDBService.configMysql();
//		} catch (PropertyVetoException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		Set<HouseInfoData> extracts =new ConcurrentSkipListSet<HouseInfoData>();
//		long begintime=System.currentTimeMillis();
//		for(int i=0;i<Config.areas.length;i++){
//			String area=Config.areas[i];
//			Set<HouseInfoData> extract = WebScheduleDBService.extract(preUrl+area+"/","/");		
//			extracts.addAll(extract);
//		}
//		long endtime=System.currentTimeMillis();
//		System.out.println("新增"+extracts.size()+"套。报价变动"+JavaScheduleDBService.modifyDatas.size()+" 套。耗时:"+(endtime-begintime)/60000+"分钟");
//
//	}
	public static void main(String[] args) {
		
		//getLianJiaDatas();
		addToDB();
		//scheduleDB();
	}
}
