<%@ page language="java" 
	contentType="image/png;charset=utf8"
	pageEncoding="utf8"
	import="java.awt.*"
	import="javax.imageio.*"
	import="java.awt.geom.*"
	import="java.awt.image.*"
	import="java.io.*"
	import="java.util.List"
    import="com.ly.spider.bean.PriceTrendData"
    import="com.ly.spider.app.Config"
%>
<%
	response.setContentType("image/png;charset=utf8");
	ServletContext context=getServletContext();
	List<PriceTrendData> trends=(List<PriceTrendData>)context.getAttribute("trends");
	//System.out.println("trends->"+trends.size());
	// 创建一个 610X400 的图像
	int width = 1500, height = 400;
	BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	// 创建Java2D对象
	Graphics2D g2d = image.createGraphics();
	// 填充背景
	g2d.setColor(Color.WHITE);
	g2d.fillRect(0, 0, width, height);
	// 绘制图表标题
	String chartTitle = "北京二手房30天内房价走势";
	g2d.setFont(new Font("宋体", Font.PLAIN, 22));
	g2d.setColor(Color.BLACK);
	g2d.drawString(chartTitle, 140, 40);
	// 创建虚线笔划
	float[]dashes = { 3.f };
	BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
	g2d.setStroke(bs);
	g2d.setFont(new Font("宋体", Font.PLAIN, 12));
	String str = "2016-";
	int stringLength = 0;
	
	for (int i = 1; i <= 30; i++)
	{
	  // 绘制垂直方向虚线
	  g2d.drawLine(80+i * 40, 50, 80+i * 40, 360);
	
	  // 绘制横轴上月份的说明文字
	  str = trends.get(i-1).getId();
	  stringLength = g2d.getFontMetrics().stringWidth(str);
	  if (i % 2 == 0)
	  {
	    g2d.drawString(str, 80+i * 40 - stringLength / 2, 387);
	  }
	  else
	  {
	    g2d.drawString(str, 80+i * 40 - stringLength / 2, 375);
	  }
	 // str = "2016-";
	}
	
	str = "";
	int stringHeight = 0;
	
	final int highestPrice=65000; //最高价
	final int priceDeta=1000;     //每个格表示的价格
	
	final int maxDis=300;   //最大距离
	final int pointDeta=30; //每个格的距离
	int avgDis=maxDis/2;    //中间距离
	int avgPosPrice=highestPrice-maxDis/pointDeta/2*priceDeta;//中间价格 55000 60000
	
	int avgPos=0;
	for (int i = 0; i < maxDis; i += pointDeta)
	{
	  // 绘制水平方向虚线
	  g2d.drawLine(120, 60+i, width-120, 60+i);
	
	  // 绘制纵轴上销售量的说明文字
	  //str += (10-i / 30)*10000;
	  str+=highestPrice-i*priceDeta/pointDeta;
	  stringHeight = g2d.getFontMetrics().getAscent();
	  stringLength = g2d.getFontMetrics().stringWidth(str);
	
	  g2d.drawString(str, 110-stringLength, 60+i + stringHeight / 2);
	  //中间五万的位置
	  if(i==avgDis){
	  	avgPos=60+i + stringHeight / 2;
	  }
	  str = "";
	}
	
	// 绘制坐标轴
	g2d.setStroke(new BasicStroke(3.0f));
	g2d.setColor(new Color(53, 76, 112));
	g2d.drawLine(120, 50, 120, 360);
	g2d.drawLine(120, 360, width-120, 360);
	
	// 绘制纵坐标上的标题
	/* g2d.setFont(new Font("宋体", Font.PLAIN, 15));
	g2d.drawString("元", 40, 45); */
	
	
	String[] yTitle = { "均价(元)"/*, "总价"*/ };
	Color[] pointColor = { Color.RED, Color.ORANGE };
	int[] prices = new int[Config.TrendsLimit];
	int[] times = new int[Config.TrendsLimit];
	
	g2d.setFont(new Font("宋体", Font.PLAIN, 12));
	for (int i = 0; i < yTitle.length; i++)
	{
	  // 初始化绘制数据
	  for (int j = 0; j < prices.length; j++)
	  {
	
	    double tmp =avgPos-(trends.get(j).getUnitPrice()-avgPosPrice)*pointDeta/priceDeta;
	    prices[j]=(int)tmp;
	    times[j] = 120+j * 40;
	     g2d.drawRect(times[j]-2, prices[j]-2, 4, 4);
	  }
	
	  g2d.setStroke(new BasicStroke(2.0f));
	  g2d.setColor(pointColor[i]);
	
	  // 绘制月销售量折线
	  g2d.drawPolyline(times, prices, prices.length);
	
	  // 绘制图例
	  g2d.fillRect(30, 40+i * 20, 10, 10);
	  g2d.setColor(Color.BLACK);
	  g2d.setFont(new Font("宋体", Font.PLAIN, 12));
	  g2d.drawString(yTitle[i], 45, 50+i * 20);
	}
	
	//部署图形
	g2d.dispose();
	
	// 利用ImageIO类的write方法对图像进行编码
	ServletOutputStream sos = response.getOutputStream();
	ImageIO.write(image, "PNG", sos);
	sos.close();
	
	out.clear();
	out = pageContext.pushBody();
%>
