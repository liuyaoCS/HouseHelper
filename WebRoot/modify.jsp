<%@ page language="java" contentType="text/html; charset=utf8"
    pageEncoding="utf8"
    import="java.util.List"
    import="com.ly.spider.bean.HouseInfoData"
    import="net.sf.json.JSONObject"
    import="net.sf.json.JSONArray"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>报价变动</title>
<script src="js/jquery-1.4.2.js"></script>
<style  type="text/css">
	#left{
		float:left; 
		text-align:center;
	}
	#right{
		float:left; 
		text-align:center;
	}
	.count{
		text-align:center;
	}
	.list{
		list-style:none;
		width:500px; 
		margin:auto;
		text-align:left;
	}
	.search{
		text-align:center;
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
	<% ServletContext context=getServletContext();
 	   JSONArray uobj=(JSONArray)context.getAttribute("upDatas");
 	   JSONArray dobj=(JSONArray)context.getAttribute("downDatas");
 	 %>
 	 <div id="left">
 	 	<div class="count" id="upcount"></div>
 	 	<span class="search" id="upsearch">
 	 		<input id="upsearchtxt" type="text" name="address" placeholder="望京"/>
 	 		<input id="upsearchbtn" type="button" value="查询"/>
	 	</span>
    	<ul class="list" id="uplist"></ul>
 	 </div>
	<div id="right">
 	 	<div class="count" id="downcount"></div>
 	 	<span class="search" id="downsearch">
 	 		<input id="downsearchtxt" type="text" name="address" placeholder="望京"/>
 	 		<input id="downsearchbtn" type="button" value="查询"/>
 		</span>
    	<ul class="list" id="downlist"></ul>
 	 </div>
    
    
     <script type="text/javascript" charset="UTF-8">
    	var ujson = <%=uobj%>;    
    	var djson = <%=dobj%>; 	
    	///////////////up///////////////////
        $("#upcount").html("涨价房源:<h3 style='display : inline'> "+ujson.length+"</h3> 套");
      	for(var i=0;i<ujson.length;i++){
      		  var jsonArray = eval(ujson[i].history);//有的时候必须要eval一下，否则有问题
      		  //var jsonArray = ujson[i].history;
              var historyprice="";
			  for(var j=0;j<jsonArray.length;j++) {
				 // historyprice=historyprice+jsonArray[i]["price"]+"万 "+jsonArray[i]["date"]+"; ";
				  historyprice+=jsonArray[j].price+"万 "+jsonArray[j].date+"</br>&nbsp;&nbsp;&nbsp;&nbsp;"; 
			  } 
              $("#uplist").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src=http://read.html5.qq.com/image?imageUrl="+ujson[i].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+ujson[i].linkUrl+">"+ujson[i].title+"</a></h3>"
							+       "<div><span>"+ujson[i].area+"."+ujson[i].address+"</span></div>"
							+       "<div ><h4>"+ujson[i].price+"万</h4>&nbsp;单价:"+ujson[i].unitPrice+"元/平米</div>"
							+       "<div ><h5>历史报价:"+historyprice+"</h5></div>"
							+       "<div><span>涨价<h4>"+ujson[i].gap+"</h4>万元</span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
        }
        $("#upsearchbtn").click(function(){
        	// $("#uplist").remove();
        	$("#uplist li").each(function(){
  				 $(this).remove();
			}); 
        	var address=$("#upsearchtxt").val();
        	var ucount=0;
        	for(var i=0;i<ujson.length;i++){
        		if(ujson[i].address.indexOf(address)>=0 || ujson[i].area.indexOf(address)>=0){
        		  ucount++;
        		  
        		 var jsonArray = eval(ujson[i].history);//有的时候必须要eval一下，否则有问题
      		  	 //var jsonArray = ujson[i].history;
              	 var historyprice="";
			 	 for(var j=0;j<jsonArray.length;j++) {
				 // historyprice=historyprice+jsonArray[i]["price"]+"万 "+jsonArray[i]["date"]+"; ";
				  historyprice+=jsonArray[j].price+"万 "+jsonArray[j].date+"</br>&nbsp;&nbsp;&nbsp;&nbsp;"; 
			 	 } 
            	  $("#uplist").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src=http://read.html5.qq.com/image?imageUrl="+ujson[i].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+ujson[i].linkUrl+">"+ujson[i].title+"</a></h3>"
							+       "<div><span>"+ujson[i].area+"."+ujson[i].address+"</span></div>"
							+       "<div ><h4>"+ujson[i].price+"万</h4>&nbsp;单价:"+ujson[i].unitPrice+"元/平米</div>"
							+       "<div ><h5>历史报价:"+historyprice+"</h5></div>"
							+       "<div><span>涨价<h4>"+ujson[i].gap+"</h4>万元</span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
      		   }
      	    }
      	    $("#upcount").html("涨价房源:<h3 style='display : inline'> "+ucount+"</h3> 套");
        });
        //////////////down//////////////////
        $("#downcount").html("降价房源:<h3 style='display : inline'> "+djson.length+"</h3> 套");
        for(var j=0;j<djson.length;j++){
        	  var jsonArray = eval(djson[j].history);//有的时候必须要eval一下，否则有问题
      		  //var jsonArray = ujson[i].history;
              var historyprice="";
			  for(var k=0;k<jsonArray.length;k++) {
				 // historyprice=historyprice+jsonArray[i]["price"]+"万 "+jsonArray[i]["date"]+"; ";
				  historyprice+=jsonArray[k].price+"万 "+jsonArray[k].date+"</br>&nbsp;&nbsp;&nbsp;&nbsp;"; 
			  } 
              $("#downlist").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src=http://read.html5.qq.com/image?imageUrl="+djson[j].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+djson[j].linkUrl+">"+djson[j].title+"</a></h3>"
							+       "<div><span>"+djson[j].area+"."+djson[j].address+"</span></div>"
							+       "<div ><h4>"+djson[j].price+"万</h4>&nbsp;单价:"+djson[j].unitPrice+"元/平米</div>"
							+       "<div ><h5>历史报价:"+historyprice+"</h5></div>"
							+       "<div><span>降价<h4>"+djson[j].gap*(-1)+"</h4>万元</span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
        }
       
        $("#downsearchbtn").click(function(){
        	// $("#uplist").remove();
        	$("#downlist li").each(function(){
  				 $(this).remove();
			}); 
        	var address=$("#downsearchtxt").val();
        	//alert(address);
        	var dcount=0;
        	for(var j=0;j<djson.length;j++){
        		
        		if(djson[j].address.indexOf(address)>=0 || djson[j].area.indexOf(address)>=0){
        			 dcount++;
        			  var jsonArray = eval(djson[j].history);//有的时候必须要eval一下，否则有问题
		      		  //var jsonArray = ujson[i].history;
		              var historyprice="";
					  for(var k=0;k<jsonArray.length;k++) {
						 // historyprice=historyprice+jsonArray[i]["price"]+"万 "+jsonArray[i]["date"]+"; ";
						  historyprice+=jsonArray[k].price+"万 "+jsonArray[k].date+"</br>&nbsp;&nbsp;&nbsp;&nbsp;"; 
					  } 
             		 $("#downlist").append("<li>"
        	  				+"<div class='item'>"
							+   "<a href='#'><img src=http://read.html5.qq.com/image?imageUrl="+djson[j].picUrl+"/></a>"
							+   "<div class='right'>"
							+       "<h3><a href="+djson[j].linkUrl+">"+djson[j].title+"</a></h3>"
							+       "<div><span>"+djson[j].area+"."+djson[j].address+"</span></div>"
							+       "<div ><h4>"+djson[j].price+"万</h4>&nbsp;单价:"+djson[j].unitPrice+"元/平米</div>"
							+       "<div ><h5>历史报价:"+historyprice+"</h5></div>"
							+       "<div><span>降价<h4>"+djson[j].gap*(-1)+"</h4>万元</span></div>"
							+    "</div>"
							+ "</div>"
                            +"</li>");
       			 }
       		}
       		$("#downcount").html("降价房源:<h3 style='display : inline'> "+dcount+"</h3> 套");
        });
    </script>
</body>
</html>