<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>北京二手房</title>
<link rel="stylesheet" href="css/result.css"></link>
<script src="js/jquery-1.4.2.js"></script>
<script  src="js/dialog.js"></script> 
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
    
    <div id="theme-popover"  
    	onmousedown="mouseDown(this,event)"  onmousemove="mouseMove(event)" onmouseup="mouseUp(event)">
     	<div class="theme-poptit">
          <a href="javascript:;" title="关闭" class="close">×</a>
          <h3>房贷计算器</h3>
     	</div>
     	<div class="theme-popbod">
            <span>贷款类型:<select id="type">
                        <option value=1>商业贷款</option>
                        <option value=2>公积金贷款</option>
                    </select>
            </span><br/>
            <span>
             	  利率折扣:<select id="discount">
             	  	<option value=0.8 >8折</option>
             	  	<option value=0.85 selected="selected">85折</option>
             	  	<option value=0.9 >9折</option>
             	  	<option value=1.0>基准</option>
             	  	<option value=1.1>1.1倍</option>
             	  	<option value=1.2>1.2倍</option>
             	  </select>
              	  贷款年限:<select id="time">
              	  	<option value=360>30年</option>
              	  	<option value=300>25年</option>
              	  	<option value=240>20年</option>
              	  	<option value=180>15年</option>
              	  	<option value=120>10年</option>
              	  </select>
            </span><br/>
            <span id="s_price">
              	  成交价(万元):<input id="price" type="text" >
            	  房屋评估(折):<input id="eprice" type="text" value=0.9>
            </span><br/>
            
            <span>
               	  服务费点(%):<input id="service" type="text" value="2.7">
               	  契税费点(%):<input id="tax" type="text" value="1">
            </span><br/>
            <input type="button" id="btn" value="计算"><br/>
            <p>首付金额:<span id="downPay"></span></p>
            <p>贷款总额:<span id="sum"></span></p>
            <p>每月还款:<span id="repay"></span></p>
            <p>最低收入证明:<span id="income"></span></p>

        </div>
   </div>
   <script>
	  // 获取节点
	  var block = document.getElementById("theme-popover");
	  var oW,oH;
	  // 绑定touchstart事件
	  block.addEventListener("touchstart", function(e) {
	   console.log(e);
	   var touches = e.touches[0];
	   oW = touches.clientX - block.offsetLeft;
	   oH = touches.clientY - block.offsetTop;
	   //阻止页面的滑动默认事件
	   document.addEventListener("touchmove",defaultEvent,false);
	  },false)
	 
	  block.addEventListener("touchmove", function(e) {
	   var touches = e.touches[0];
	   var oLeft = touches.clientX - oW;
	   var oTop = touches.clientY - oH;
	   if(oLeft < 0) {
	    oLeft = 0;
	   }else if(oLeft > document.documentElement.clientWidth - block.offsetWidth) {
	    oLeft = (document.documentElement.clientWidth - block.offsetWidth);
	   }
	   block.style.left = oLeft + "px";
	   block.style.top = oTop + "px";
	  },false);
	   
	  block.addEventListener("touchend",function() {
	   document.removeEventListener("touchmove",defaultEvent,false);
	  },false);
	  function defaultEvent(e) {
	   e.preventDefault();
	  }
	</script>
   <script type="text/javascript">
  
    function setPrice(p){
    	
        $('#theme-popover').show(1,null);
        $("#price").val(p);
        $("#downPay").html("");
        $("#sum").html("");
        $("#repay").html("");
        $("#income").html("");
    }
    $('.theme-poptit .close').click(function(){

        $('#theme-popover').hide(1,null);
    });
    $("#type").change(function(){
    	var val=$(this).children('option:selected').val();
    	if(val==1){
        	$("#discount option").each(function(){
        		$(this).remove();
        	});
        	$("#discount").append("<option value=1>基准</option>"
        						 +"<option value=0.8>8折</option>"
        						 +"<option value=0.85 selected='selected'>85折</option>"
        						 +"<option value=0.9>9折</option>"
        						 +"<option value=1.1>1.1倍</option>"
        						 +"<option value=1.2>1.2倍</option>");
    	}else{
        	$("#discount option").each(function(){
        		$(this).remove();
        	});
        	$("#discount").append("<option value=1 >基准</option>"
        						 +"<option value=1.1 >1.1倍</option>"
        						 +"<option value=1.2>1.2倍</option>");
    	}
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
        if(type==1){
        	var interest=4.9;//银行基准利率
        	var ch=service;
	        var tch=tax;
	        var dy=4.9*discount/100;
	        var dm=dy/12; //每月贷款利率
	        var hep=price*10000*eprice; //0.9房评
	        var sum=hep*0.65;//可贷款总额
	        var months=time;
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
	        
	        $("#downPay").html("<span style='color:red;'>"+downPay.toFixed(2)+"</span> 万元");
	        $("#sum").html("<span style='color:red;'>"+sum.toFixed(2)+"</span> 万元");
	        $("#repay").html("<span style='color:red;'>"+repay.toFixed(2)+"</span> 元");
	        $("#income").html("<span style='color:red;'>"+income.toFixed(2)+"</span> 元/月");
        }else if(type==2){
        	var interest=3.25;//公积金基准利率
        	var ch=service;
	        var tch=tax;
	        var dy=interest*discount/100;
	        var dm=dy/12; //每月贷款利率
	        var hep=price*10000*eprice; //0.9房评
	        var sum=120*10000;//可贷款总额
	        var months=time;
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
	        
	        $("#downPay").html("<span style='color:red;'>"+downPay.toFixed(2)+"</span> 万元");
	        $("#sum").html("<span style='color:red;'>"+sum.toFixed(2)+"</span> 万元");
	        $("#repay").html("<span style='color:red;'>"+repay.toFixed(2)+"</span> 元");
	        $("#income").html("<span style='color:red;'>"+income.toFixed(2)+"</span> 元/月");
        }
    });
   </script>
</body>
</html>