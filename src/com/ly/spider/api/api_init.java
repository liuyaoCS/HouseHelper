package com.ly.spider.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ly.spider.app.Config;
import com.ly.spider.app.DataSource;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.bean.PriceTrendData;
import com.ly.spider.core.WebSearchService;
import com.ly.spider.util.TextUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class api_init extends HttpServlet {
	
	private  ComboPooledDataSource cpds;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "text/json;charset=utf-8");
		
		String ret=fetchFromDB().toString();
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(ret);
		} catch (IOException e) {
		    e.printStackTrace();
		} 
		    
	}
	private  JSONObject fetchFromDB(){
		
		JSONObject ret=new JSONObject();
		
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = DataSource.getInstance().getConnection();
			double avgPrice=0,unitAvgPrice = 0;
			int newsNum=0;
			//新增房源
			String newSql="select newsNum  from price where id="+TextUtil.getDateString(0);
			statement=(PreparedStatement) connection.prepareStatement(newSql);
			ResultSet nrs=statement.executeQuery();
			if(nrs!=null && nrs.next()){
				newsNum=nrs.getInt("newsNum");
			}
			ret.put("newHouseNum", String.valueOf(newsNum));
			//计算总价均价
			String searchSql="select avg(price) as avgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(searchSql);
			ResultSet rs=statement.executeQuery();
			DecimalFormat df = new DecimalFormat("0.00");
			if(rs!=null && rs.next()){
				avgPrice=rs.getDouble("avgPrice");
			}
			ret.put("avgPrice", df.format(avgPrice));
			//计算单价均价
			String uSql="select avg(unitPrice) as unitAvgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(uSql);
			ResultSet urs=statement.executeQuery();
			if(urs!=null && urs.next()){
				unitAvgPrice=urs.getDouble("unitAvgPrice");
			}
			ret.put("unitAvgPrice", df.format(unitAvgPrice));
			//报价变动
			List<HouseInfoData> upDatas=new ArrayList<HouseInfoData>();
			String mUpSql="select * from houseinfo where gap>0 order by gap desc";
			statement=(PreparedStatement) connection.prepareStatement(mUpSql);
			ResultSet mUpRs=statement.executeQuery();
			while(mUpRs.next()){
				HouseInfoData data=new HouseInfoData();
				data.setLinkUrl(mUpRs.getString("linkUrl"));
				data.setPicUrl(mUpRs.getString("picUrl"));
				data.setPrice(mUpRs.getDouble("price"));
				data.setUnitPrice((int)mUpRs.getDouble("unitPrice"));
				data.setTitle(mUpRs.getString("title"));
				data.setArea(mUpRs.getString("area"));
				data.setAddress(mUpRs.getString("address"));
				data.setHistory(mUpRs.getString("history"));
				data.setGap(mUpRs.getDouble("gap"));
				
				upDatas.add(data);
			}
			JSONArray upJson=JSONArray.fromObject(upDatas);
			ret.put("upDatas", upJson);
			
			List<HouseInfoData> downDatas=new ArrayList<HouseInfoData>();
			String mDownSql="select * from houseinfo where gap<0 order by gap asc";
			statement=(PreparedStatement) connection.prepareStatement(mDownSql);
			ResultSet mDownRs=statement.executeQuery();
			while(mDownRs.next()){
				HouseInfoData data=new HouseInfoData();
				data.setLinkUrl(mDownRs.getString("linkUrl"));
				data.setPicUrl(mDownRs.getString("picUrl"));
				data.setPrice(mDownRs.getDouble("price"));
				data.setUnitPrice((int)mDownRs.getDouble("unitPrice"));
				data.setTitle(mDownRs.getString("title"));
				data.setArea(mDownRs.getString("area"));
				data.setAddress(mDownRs.getString("address"));
				data.setHistory(mDownRs.getString("history"));
				data.setGap(mDownRs.getDouble("gap"));
				
				downDatas.add(data);
			}
			JSONArray downJson=JSONArray.fromObject(downDatas);
			ret.put("downDatas", downJson);
			
			int modifySize=upJson.size()+downJson.size();
			ret.put("modifyHouseNum", String.valueOf(modifySize));
			//均价走势图	
			String trendSql="select id,unitPrice  from price order by id desc limit "+Config.TrendsLimit+"";
			statement=(PreparedStatement) connection.prepareStatement(trendSql);
			ResultSet trs=statement.executeQuery();
			
			List<PriceTrendData> trends=new ArrayList<PriceTrendData>();
			while(trs.next()){
				double unitPrice=trs.getDouble("unitPrice");
				String id=trs.getString("id");
				trends.add(0, new PriceTrendData(id,unitPrice,0));
			}
			ret.put("trends", trends);
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
