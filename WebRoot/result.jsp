<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<meta name="viewport" content="width=device-width" />
<title>北京二手房</title>
<script src="js/jquery-1.4.2.js"></script>
<style  type="text/css">
	#count{
		text-align:center;
	}
	#list{
		list-style:none;
		width:500px; 
		margin:auto;
	}
    .item{ 
    	width:500px; 
    	height:200px;
    	border:2px solid #E4E4E4;
    }
    .item img{ 
    	float:left; 
    	margin:10px; 
    	width:35%; 
    	height:85%;
    	
    	box-shadow: 1px 1px 4px #E4E4E4;
		-moz-box-shadow: 1px 1px 4px #E4E4E4;
		-webkit-box-shadow: 1px 1px 4px #E4E4E4;
   	}
    .item .right{ 
    	float:right;
    	width:60%; 
    	height:90%;
    	padding-top: 20px;
    }
    .item span{ 
    	font-size: 14px;
    	margin-bottom: 15px;
    }
    .item .right h3{ 
    	height:36px; 
    	font-size:16px;
    	margin-bottom: 10px;
    	color: #009de8;
   	}
    .item .right h4{
    	font-size: 14px;
    	color: #FF0000; 
    	display : inline;
   	}
    .item .right h5{
    	font-size: 12px; 
    	display : inline;
   	}
  
</style>
</head>
<body>
    <div id="count"></div>
    <ul id="list">
    </ul>
    <script type="text/javascript">
    	var json = <%=request.getAttribute("data")%>;     	
        // var json=eval(jsonstr);
        $("#count").html("共找到<h3 style='display : inline'> "+json.count+"</h3> 套");
        var houses=json.houses;
        for(var i=0;i<houses.length;i++){
        	  /* $("#list").append("<li>"
        	  				+"<div><img src="+houses[i].picUrl+"></img></div>"
        	  				+"<div><a href="+houses[i].linkUrl+" target='_blank'>"+houses[i].title+"</a></div>"
                            +"<div>总价:"+houses[i].price+"万  单价:"+houses[i].unitPrice+"元/平米</div>"
                            +"<div>"+houses[i].area+"-"+houses[i].address+"</div>"
                              +"</li>"); */
              var ch=2.7;
              var tch=1;
              var dy=4.9*0.85/100;
              var dm=dy/12; //每月贷款利率       
              var hep=houses[i].price*10000*0.9; //0.9房评    
              var sum=hep*0.65;//可贷款总额
              var months=360;
              var downPay=houses[i].price*10000-sum+hep*tch/100+houses[i].price*10000*ch/100;//总首付
              downPay=downPay/10000;
              
              var sellerGet=houses[i].price*10000-sum;
              var govGet=hep*tch/100;
              var agencyGet=houses[i].price*10000*ch/100;
              var ret=sum*dm*Math.pow((1+dm), months);
              ret/=(Math.pow((1+dm), months)-1); //每月还贷
              $("#list").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src="+houses[i].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+houses[i].linkUrl+">"+houses[i].title+"</a></h3>"
							+       "<div><span>"+houses[i].area+"."+houses[i].address+"</span></div>"
							+       "<div ><h4>"+houses[i].price+"万</h4>&nbsp;<h5>单价:"+houses[i].unitPrice+"元/平米</h5></div>"
							+       "<div ><span>首付:"+downPay.toFixed(2)+"万  每月还贷"+ret.toFixed(2)+"元</span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
        }
      	
     
    </script>
</body>
</html>