<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>北京二手房</title>
	<style>
		/* Basic Grey */
		.basic-grey {
			max-width:500px;
			margin-left:auto;
			margin-right:auto;
			padding: 10px 10px 10px 10px;
			background: #F7F7F7;
			color: #888;			
			font: 12px Georgia, "Times New Roman", Times, serif;
			border:1px solid #E4E4E4;
		}
		.basic-grey h1 {
			font-size: 25px;
			padding: 0px 0px 10px 40px;
			display: block;
			border-bottom:1px solid #E4E4E4;
			margin-top:5px;
			margin-bottom:30px;
			color: #888;
		}
		.basic-grey h1>span {
			display: block;
			font-size: 11px;
		}
		.basic-grey label {
			display: block;
			margin: 0px;
		}
		.basic-grey label>span {
			float: left;
			width: 20%;
			text-align: right;
			padding-right: 10px;
			margin-top: 10px;
			color: #888;
		}
		.basic-grey input[type="text"], .basic-grey select {
			border: 1px solid #DADADA;
			color: #888;
			width: 25%;
			height: 20px;
			margin-bottom: 16px;
			margin-right: 6px;
			margin-top: 2px;
			padding: 5px;
			font-size: 12px;
			box-shadow: inset 0px 1px 4px #ECECEC;
			-moz-box-shadow: inset 0px 1px 4px #ECECEC;
			-webkit-box-shadow: inset 0px 1px 4px #ECECEC;
		}
		 .basic-grey select {
		 	height: 30px;
		 }
		.basic-grey .button {
			background: #E27575;
			border: none;
			padding: 10px 25px 10px 25px;
			color: #FFF;
			box-shadow: 1px 1px 5px #B6B6B6;
			border-radius: 3px;
			text-shadow: 1px 1px 1px #9E3F3F;
			cursor: pointer;
		}
		.basic-grey .button:hover {
			background: #CF7A7A
		}
	</style>
</head>
<body>
	<%
		ServletContext context=getServletContext();
		String newHouseNum=(String)context.getAttribute("newHouseNum");
		String modifyHouseNum=(String)context.getAttribute("modifyHouseNum");
		String avgPrice=(String)context.getAttribute("avgPrice");
		String unitAvgPrice=(String)context.getAttribute("unitAvgPrice");
		
	 %>
	<form action="/HouseHelper/UIServlet" method="post" class="basic-grey">
		<h1>二手房查询|费用计算神器
			<span>今日新增房源:<%=newHouseNum %>套 &nbsp报价变动:<%=modifyHouseNum %>套</span>
			<span>全市均价:<%=unitAvgPrice %>元/每平米&nbsp每套均价:<%=avgPrice %>万元</span>
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
			</select>
		</label>
		<label>
			<span>房型 :</span>
			<select name="l">
				<option value="1">一室</option>
				<option value="2" selected="selected">二室</option>
				<option value="3">三室</option>
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