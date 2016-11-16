package com.ly.spider.listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;

import com.ly.spider.app.Config;
import com.ly.spider.app.DataSource;
import com.ly.spider.bean.HouseInfoData;
import com.ly.spider.bean.PriceTrendData;
import com.ly.spider.core.WebScheduleDBService;

public class ScheduleTask extends TimerTask {

	private ServletContext mContext;
	public ScheduleTask(ServletContext context) {
		// TODO Auto-generated constructor stub
		mContext=context;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		fetchPriceFromDB();
		scheduleDB();
	}

	private  void scheduleDB()
	{	
	
		long begintime=System.currentTimeMillis();
		for(int i=0;i<Config.Areas.length;i++){
			String area=Config.Areas[i];
			//WebScheduleDBService.extract(Config.BASEURL+area+"/","/");
			System.out.println(area+" begin-----------");
			WebScheduleDBService.extractFine(Config.BASEURL+area+"/","/");	
			System.out.println(area+" end-----------");
		}
		int newHouseNum=WebScheduleDBService.newDatas.size();
		int modifyHouseNum=WebScheduleDBService.modifyDatas.size();
		long endtime=System.currentTimeMillis();
		System.out.println("新增"+newHouseNum+"套。报价变动"+modifyHouseNum+"套。耗时:"+(endtime-begintime)/60000+"分钟");
		
		this.mContext.setAttribute("newHouseNum", newHouseNum+"");
		this.mContext.setAttribute("modifyHouseNum", modifyHouseNum+"");
	}
	private  void fetchPriceFromDB(){
		
		java.sql.Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection = DataSource.getInstance().getConnection();
			double avgPrice=0,unitAvgPrice = 0;
			//计算总价均价
			String searchSql="select avg(price) as avgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(searchSql);
			ResultSet rs=statement.executeQuery();
			DecimalFormat df = new DecimalFormat("0.00");
			if(rs!=null && rs.next()){
				avgPrice=rs.getDouble("avgPrice");
				this.mContext.setAttribute("avgPrice", df.format(avgPrice));
			}
			//计算单价均价
			String uSql="select avg(unitPrice) as unitAvgPrice from houseinfo";
			statement=(PreparedStatement) connection.prepareStatement(uSql);
			ResultSet urs=statement.executeQuery();
			if(urs!=null && urs.next()){
				unitAvgPrice=urs.getDouble("unitAvgPrice");
				this.mContext.setAttribute("unitAvgPrice", df.format(unitAvgPrice));
			}
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
			this.mContext.setAttribute("upDatas", upJson);
		
			
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
			this.mContext.setAttribute("downDatas", downJson);
			//均价走势图	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
			String time=dateFormat.format(new Date());  
			
			try {
				String iSql="insert into price values("+time+","+unitAvgPrice+","+avgPrice+")";
				statement=(PreparedStatement) connection.prepareStatement(iSql);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String trendSql="select id,unitPrice  from price order by id desc limit "+Config.TrendsLimit+"";
			statement=(PreparedStatement) connection.prepareStatement(trendSql);
			ResultSet trs=statement.executeQuery();
			
			List<PriceTrendData> trends=new ArrayList<PriceTrendData>();
			while(trs.next()){
				double unitPrice=trs.getDouble("unitPrice");
				String id=trs.getString("id");
				trends.add(0, new PriceTrendData(id,unitPrice,0));
			}
			this.mContext.setAttribute("trends", trends);
			for(PriceTrendData ptd:trends){
				System.out.println(ptd);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
