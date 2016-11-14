<%@ page language="java" contentType="image/png;charset=utf8"
	import="java.awt.*"
	import="javax.imageio.*"
	import="java.awt.geom.*"
	import="java.awt.image.*"
	import="java.io.*"
	import="java.util.List"
    import="com.ly.spider.bean.PriceTrendData"
%>

<%
response.setContentType("image/png;charset=utf8");
ServletContext context=getServletContext();
List<PriceTrendData> trends=(List<PriceTrendData>)context.getAttribute("trends");
// 创建一个 610X400 的图像
int width = 610, height = 400;
BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
// 创建Java2D对象
Graphics2D g2d = image.createGraphics();
// 填充背景
g2d.setColor(Color.WHITE);
g2d.fillRect(0, 0, width, height);
// 绘制图表标题
String chartTitle = "北京二手房房价走势";
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

for (int i = 1; i <= 10; i++)
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
int avgPos=0;
for (int i = 0; i < 300; i += 30)
{
  // 绘制水平方向虚线
  g2d.drawLine(120, 60+i, 570, 60+i);

  // 绘制纵轴上销售量的说明文字
  str += (10-i / 30)*10000;
  stringHeight = g2d.getFontMetrics().getAscent();
  stringLength = g2d.getFontMetrics().stringWidth(str);

  g2d.drawString(str, 110-stringLength, 60+i + stringHeight / 2);
  //中间五万的位置
  if(i==150){
  	avgPos=60+i + stringHeight / 2;
  }
  str = "";
}

// 绘制坐标轴
g2d.setStroke(new BasicStroke(3.0f));
g2d.setColor(new Color(53, 76, 112));
g2d.drawLine(120, 50, 120, 360);
g2d.drawLine(120, 360, 570, 360);

// 绘制纵坐标上的标题
/* g2d.setFont(new Font("宋体", Font.PLAIN, 15));
g2d.drawString("元", 40, 45); */


String[] bookTitle = { "均价(元)"/*, "总价"*/ };
Color[] bookColor = { Color.RED, Color.ORANGE };
int[] prices = new int[10];
int[] times = new int[10];

g2d.setFont(new Font("宋体", Font.PLAIN, 12));
for (int i = 0; i < bookTitle.length; i++)
{
  // 初始化绘制数据
  int bookSales = 0;
  for (int j = 0; j < prices.length; j++)
  {

    double tmp =avgPos-(trends.get(j).getUnitPrice()-50000)*30/10000;
    prices[j]=(int)tmp;
    times[j] = 120+j * 40;
    //System.out.println(prices[j] );
  }

  g2d.setStroke(new BasicStroke(5.0f));
  g2d.setColor(bookColor[i]);

  // 绘制月销售量折线
  g2d.drawPolyline(times, prices, prices.length);

  // 绘制图例
  g2d.fillRect(30, 40+i * 20, 10, 10);
  g2d.setColor(Color.BLACK);
  g2d.setFont(new Font("宋体", Font.PLAIN, 12));
  g2d.drawString(bookTitle[i], 45, 50+i * 20);
}

//部署图形
g2d.dispose();

// 利用ImageIO类的write方法对图像进行编码
ServletOutputStream sos = response.getOutputStream();
ImageIO.write(image, "PNG", sos);
sos.close();
%>
