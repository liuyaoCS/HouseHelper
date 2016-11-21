package com.ly.spider.api;

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

import com.ly.spider.app.Config;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.core.WebSearchService;
import com.ly.spider.util.TextUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class api_search extends HttpServlet {
	
	private  ComboPooledDataSource cpds;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");//针对post请求,对于get: new String(data.getBytes("iso8859-1"),"utf-8")
		
		Map<String, String[]> map=request.getParameterMap();
		StringBuilder sb=new StringBuilder();
//		for(String key:map.keySet()){
//			String value=map.get(key)[0];
//			if(key.equals("rs")){
//				value=URLEncoder.encode(value, "utf8");
//			}
//			sb.append(key).append(value);	
//		}
		if(!TextUtil.isEmpty(map.get("f")[0])){
			sb.append("f").append(map.get("f")[0]);
		}
		if(!TextUtil.isEmpty(map.get("l")[0])){
			sb.append("l").append(map.get("l")[0]);
		}
		if(!TextUtil.isEmpty(map.get("ba")[0])){
			sb.append("ba").append(map.get("ba")[0]);
		}	
		if(!TextUtil.isEmpty(map.get("ea")[0])){
			sb.append("ea").append(map.get("ea")[0]);
		}
		if(!TextUtil.isEmpty(map.get("bp")[0])){
			sb.append("bp").append(map.get("bp")[0]);
		}	
		if(!TextUtil.isEmpty(map.get("ep")[0])){
			sb.append("ep").append(map.get("ep")[0]);
		}
		if(!TextUtil.isEmpty(map.get("rs")[0])){
			sb.append("rs").append(URLEncoder.encode(map.get("rs")[0], "utf8"));
		}
		
		
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "text/json;charset=utf-8");
		
		String ret=getLianJiaDatas(sb.toString()).toString();
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(ret);
		} catch (IOException e) {
		    e.printStackTrace();
		} 
		    
	}
	public  JSONObject getLianJiaDatas(String condition){
		
		Set<HouseInfoData> extracts = new WebSearchService().extract(Config.BASEURL,condition,Config.TAG);
		JSONArray array=JSONArray.fromObject(extracts);
		JSONObject ret=new JSONObject();
		ret.put("count", extracts.size());
		ret.put("houses", array);
		return ret;
	}
}
