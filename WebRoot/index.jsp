<%@ page language="java" 
	contentType="text/html; charset=utf8"
    pageEncoding="utf8"
    import="java.util.List"
    import="com.ly.spider.bean.PriceTrendData"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>北京二手房</title>
<link rel="stylesheet" href="css/index.css"></link>
</head>
<body>
	<%
		ServletContext context=getServletContext();
		String newHouseNum=(String)context.getAttribute("newHouseNum");
		String modifyHouseNum=(String)context.getAttribute("modifyHouseNum");
		String avgPrice=(String)context.getAttribute("avgPrice");
		String unitAvgPrice=(String)context.getAttribute("unitAvgPrice");
		List<PriceTrendData> trends=(List<PriceTrendData>)context.getAttribute("trends");
		
	 %>
	<form action="/HouseHelper/UIServlet" method="post" class="basic-grey">
		<h1>二手房查询|费用计算神器
			<span>昨日新增房源:<%=newHouseNum %>套 &nbsp报价变动:<a href='modify.jsp'><%=modifyHouseNum %></a>套</span>
			<span>全市均价:<%=unitAvgPrice %>元/每平米&nbsp每套均价:<%=avgPrice %>万元&nbsp<a href='trends.jsp'>房价走势</a></span>
		</h1>
		<label>
			<span>价格 :</span>
			<input type="text" name="bp" value="330">-&nbsp;
			<input type="text" name="ep" value="365">&nbsp;万
		</label>
		<label>
			<span>面积 :</span>
			<input type="text" name="ba" value="50">-&nbsp;
			<input type="text" name="ea" value="70">&nbsp;平
		</label>
		<label>
			<span>朝向 :</span>
			<select name="f">
				<option value="5">南北</option>
				<option value="2">南</option>
				<option value="1">东</option>
				<option value="3">西</option>
				<option value="4">北</option>
			</select>
		</label>
		<label>
			<span>房型 :</span>
			<select name="l">
				<option value="1">一室</option>
				<option value="2" selected="selected">二室</option>
				<option value="3">三室</option>
				<option value="4">四室</option>
				<option value="5">五室</option>
				<option value="6">五室以上</option>
			</select>
		</label>
		<label>
			<span>位置 :</span>
			<input type="text" name="rs" value="望京">
			<input type="submit" class="button" value="查询" />
		</label>
	</form>
</body>
</html>