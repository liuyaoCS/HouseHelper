<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<meta name="viewport" content="width=device-width" />
<title>北京二手房</title>
<script src="js/jquery-1.4.2.js"></script>
<link rel="stylesheet" href="css/dialog.css" media="all">
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
    <script type="text/javascript" charset="UTF-8">
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
              
              //1 迭代变量是j不要用i！！！否则肯定要越界，要么只出现开始的不分结果，要么显示不出来。
              //2 不要用history这个变量声明，这是js的系统变量！！！
              //3 js有+=操作
              //4 jsonobject取value可以obj['key']或obj["key"] 但最好用。操作符 obj.key
              var jsonArray = houses[i].history;
              var historyprice="";
			  for(var j=0;j<jsonArray.length;j++) {
				  //historyprice=historyprice+jsonArray[i]["price"]+"万 "+jsonArray[i]["date"]+"; ";
				  historyprice+=jsonArray[j].price+"万 "+jsonArray[j].date;
			  } 

              $("#list").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src=http://read.html5.qq.com/image?imageUrl="+houses[i].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+houses[i].linkUrl+">"+houses[i].title+"</a></h3>"
							+       "<div><span>"+houses[i].area+"."+houses[i].address+"</span></div>"
							+       "<div ><h4>"+houses[i].price+"万</h4>&nbsp;单价:"+houses[i].unitPrice+"元/平米</div>"
							+       "<div ><h5>历史报价:"+historyprice+"</h5></div>"
							+       "<div ><span>首付:"+downPay.toFixed(2)+"万  每月还贷"+ret.toFixed(2)+"元<input type='button' onclick='setPrice("+houses[i].price+")' class='compute' value='计算'></span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
        }
      	
    </script>
    <div class="theme-popover">
     <div class="theme-poptit">
          <a href="javascript:;" title="关闭" class="close">×</a>
          <h3>房贷计算器</h3>
     </div>
     <div class="theme-popbod">

            <span>贷款类型:<select id="type">
                        <option value=1>商业贷款</option>
                        <option value=2>公积金贷款</option>
                        <option value=3>组合贷款</option>
                    </select>
            </span><br/>
            <span>
                成交价(万元):<input id="price" type="text" >
                房屋评估(折):<input id="eprice" type="text" value=0.9>
            </span><br/>
            <span>
                银行折扣:<select id="discount"><option value=1.0>基准</option><option value=0.85 selected="selected">85折</option></select>
                贷款年限:<select id="time"><option value=1>30年</option><option value=2>20年</option></select>
            </span><br/>
            <span>
                服务费点:<input id="service" type="text" value="2.7">
                契税费点:<input id="tax" type="text" value="1">
            </span><br/>
            <input type="button" id="btn" value="计算"><br/>
            <p>首付金额:<span id="downPay"></span></p>
            <p>贷款总额:<span id="sum"></span></p>
            <p>每月还款:<span id="repay"></span></p>
            <p>最低收入证明:<span id="income"></span></p>

     </div>
   </div>
   <script type="text/javascript">
  
    function setPrice(p){
    	
       $('.theme-popover').show(1,null);
       $("#price").val(p);
       $("#downPay").html("");
        $("#sum").html("");
        $("#repay").html("");
        $("#income").html("");
    }
    $('.theme-poptit .close').click(function(){

            $('.theme-popover').hide(1,null);
        });
    
    $("#btn").click(function () {
        //alert("clicked");
        var type=$("#type").val();
        var price=$("#price").val();
        var eprice=$("#eprice").val();
        var discount=$("#discount").val();
        var time=$("#time").val();
        var service=$("#service").val();
        var tax=$("#tax").val();
        //alert(type+" "+price+" "+eprice+" "+discount+" "+time+" "+service+" "+tax);
        var ch=service;
        var tch=tax;
        var dy=4.9*discount/100;
        var dm=dy/12; //每月贷款利率
        var hep=price*10000*eprice; //0.9房评
        var sum=hep*0.65;//可贷款总额
        var months=360;
        var downPay=price*10000-sum+hep*tch/100+price*10000*ch/100;//总首付
        downPay=downPay/10000;

        var sellerGet=price*10000-sum;
        var govGet=hep*tch/100;
        var agencyGet=price*10000*ch/100;
        var repay=sum*dm*Math.pow((1+dm), months);
        repay/=(Math.pow((1+dm), months)-1); //每月还贷
        //alert("downPay->"+downPay+" ret->"+ret);
        sum/=10000;
        var income=repay*2;
        $("#downPay").html(downPay.toFixed(2)+" 万元");
        $("#sum").html(sum.toFixed(2)+" 万元");
        $("#repay").html(repay.toFixed(2)+" 元");
        $("#income").html(income.toFixed(2)+" 元/月");

    });
   </script>
</body>
</html>