package com.ly.spider.app;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JsoupConn {
	private static Connection conn;
	public static Connection getInstance(String url){
		//charles监听
//		if(conn==null){
//			setProxy();
//		}
		configHeader(url);
		return conn;
	}
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//		Accept-Encoding:gzip, deflate, sdch
//		Accept-Language:zh-CN,zh;q=0.8
//		Cache-Control:max-age=0
//		Connection:keep-alive
//		Cookie:lianjia_uuid=2a78cbb5-d0ec-4114-8d56-aba9b4e80711; _jzqckmp=1; all-lj=59dc31ee6d382c2bb143f566d268070e; select_city=110000; _ga=GA1.2.1353923336.1478745673; _jzqy=1.1478596144.1479699580.6.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6.jzqsr=baidu; _smt_uid=5821962f.34eed583; _jzqa=1.1805320548480478500.1478596144.1479699580.1479701518.27; _jzqc=1; _jzqx=1.1478601358.1479701518.10.jzqsr=localhost:8080|jzqct=/househelper/uiservlet.jzqsr=captcha%2Elianjia%2Ecom|jzqct=/; CNZZDATA1253477573=1234600298-1478593989-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479701331; CNZZDATA1254525948=818341921-1478594932-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479696558; CNZZDATA1255633284=992971032-1478594548-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479696249; CNZZDATA1255604082=674674676-1478594240-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479701375; _jzqb=1.1.10.1479701518.1; _qzja=1.1925370159.1478596144085.1479699579783.1479701517814.1479699598906.1479701517814.0.0.0.310.27; _qzjb=1.1479701517813.1.0.0.0; _qzjc=1; _qzjto=13.4.0; lianjia_ssid=ca8869d6-a878-4ea7-9370-8ce903571050
//		Host:bj.lianjia.com
//		Referer:http://captcha.lianjia.com/?redirect=http%3A%2F%2Fbj.lianjia.com%2Fershoufang%2Fdongcheng%2F
//		Upgrade-Insecure-Requests:1
//		User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36
	private static void configHeader(String url){
		Map<String, String> cookies=new HashedMap();
		cookies.put("lianjia_uuid",	"2a78cbb5-d0ec-4114-8d56-aba9b4e80711");
		cookies.put("_jzqckmp","1");
		cookies.put("all-lj","59dc31ee6d382c2bb143f566d268070e");
		cookies.put("select_city","110000");
		cookies.put("_ga","GA1.2.1353923336.1478745673");
		cookies.put("_jzqx","1.1478601358.1479706600.11.jzqsr=localhost:8080|jzqct=/househelper/uiservlet.jzqsr=bj%2Elianjia%2Ecom|jzqct=/");
		cookies.put("_jzqy","1.1478596144.1479710968.7.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6.jzqsr=baidu|jzqct=%E9%93%BE%E5%AE%B6");
		cookies.put("_smt_uid","5821962f.34eed583");
		cookies.put("CNZZDATA1253477573","1234600298-1478593989-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479706731");
		cookies.put("CNZZDATA1254525948","818341921-1478594932-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479707358");
		cookies.put("CNZZDATA1255633284","992971032-1478594548-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479707049");
		cookies.put("CNZZDATA1255604082","674674676-1478594240-http%253A%252F%252Fbzclk.baidu.com%252F%7C1479706775");
		cookies.put("_qzja","1.1925370159.1478596144085.1479706599830.1479710967851.1479710997246.1479711149736.0.0.0.328.29");
		cookies.put("_qzjb","1.1479710967851.5.0.0.0");
		cookies.put("_qzjc","1");
		cookies.put("_qzjto","31.6.0");
		cookies.put("_jzqa","1.1805320548480478500.1478596144.1479706600.1479710968.29");
		cookies.put("_jzqc","1");
		cookies.put("_jzqb","1.5.10.1479710968.1");
		cookies.put("lianjia_ssid","705d2a97-bf9f-429a-81b6-0596efbf3a7a");
		
		conn = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36")
				.referrer("http://bj.lianjia.com/ershoufang/")
				.cookies(cookies)
				.timeout(50000);
	}
	private static void setProxy(){
		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		String ip = "127.0.0.1";
		System.getProperties().setProperty("http.proxyHost", ip);
		System.getProperties().setProperty("http.proxyPort", "8888");
	}
}
