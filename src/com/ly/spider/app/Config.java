package com.ly.spider.app;

import java.util.Map;

public class Config {
	public static String BASEURL="http://bj.lianjia.com/ershoufang/";
	public static String HOST="http://bj.lianjia.com";
	public static final String[] Areas=new String[]{
		"dongcheng",
		"xicheng",
		"chaoyang",
		"haidian",
		"fengtai",
		"shijingshan",
		"tongzhou",
		"changping",
		"daxing",
		"yizhuangkaifaqu",
		"shunyi",
		"fangshan",
		"mentougou",
		"pinggu",
		"huairou",
		"miyun",
		"yanqing",
		"yanjiao"
	};
	public static String TAG="li.clear";
	
	public static final int TrendsLimit=30;
	public static void configHeader(Map<String, String> header){
		header.put("Host", "bj.lianjia.com");
		//Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36
		header.put("User-Agent", "User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) App leWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53");
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Encoding", "gzip, deflate, sdch");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Cache-Control", "max-age=0");
		header.put("Cookie", "");
		header.put("Proxy-Connection", "keep-alive");
		header.put("Upgrade-Insecure-Requests", "1");
	}
}
