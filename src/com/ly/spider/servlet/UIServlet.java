package com.ly.spider.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.common.collect.Multiset.Entry;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.core.WebHouseService;
import com.ly.spider.rule.Rule;

public class UIServlet extends HttpServlet {
	private  String BASEURL="http://bj.lianjia.com/ershoufang/";
	private  String TAG="li.clear";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");//针对post请求,对于get: new String(data.getBytes("iso8859-1"),"utf-8")
		
		Map<String, String[]> map=request.getParameterMap();
		StringBuilder sb=new StringBuilder();
		for(String key:map.keySet()){
			String value=map.get(key)[0];
			if(key.equals("rs")){
				value=URLEncoder.encode(value, "utf8");
			}
			sb.append(key).append(value);	
		}
		
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "text/html;charset=utf-8");
		
		//System.out.println(sb.toString());
		String ret=getLianJiaDatas(sb.toString()).toString();
		
		//response.getWriter().write(ret);
		request.setAttribute("data", ret);
		//转发之前不能写入客户端数据，否则会报IllegalStateException
		//如果之前write写入数据,write.close没有调用,不会报异常，之前写入的数据会被清空，但是响应头数据保持
		request.getRequestDispatcher("/result.jsp").forward(request, response);
	}
	public  JSONObject getLianJiaDatas(String condition){
		Rule rule = new Rule(BASEURL+condition,
				null, 
				null,
				TAG, //div.title a[data-el=ershoufang]
				Rule.SELECTION, 
				Rule.GET);
		Set<HouseInfoData> extracts = new WebHouseService().extract(rule,BASEURL,condition);
		JSONArray array=JSONArray.fromObject(extracts);
		JSONObject ret=new JSONObject();
		ret.put("count", extracts.size());
		ret.put("houses", array);
		return ret;
	}

}
