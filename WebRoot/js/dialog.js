var mouseX, mouseY;  
var objX, objY;  
var isDowm = false;  //是否按下鼠标  
function mouseDown(obj, e) {  
    obj.style.cursor = "move";  
    var div = document.getElementById("theme-popover");  
    objX = div.style.left;  
    //因为js不能读取引用的css属性，所以这里设置初值
    if(isNull(objX)){
    	objX=parseInt(document.body.clientWidth)*0.25+"px";
    }
    objY = div.style.top; 
    if(isNull(objY)){
    	objY=parseInt(window.screen.availHeight)*0.2-20+"px";
    }
    mouseX = e.clientX;  
    mouseY = e.clientY;  
    isDowm = true;  
    console.log(objX+","+objY);
}  
function mouseMove(e) {  
    var div = document.getElementById("theme-popover");  
    var x = e.clientX;  
    var y = e.clientY;  
    if (isDowm) {  
    	console.log(objX+","+objY);
        div.style.left = parseInt(objX) + parseInt(x) - parseInt(mouseX) + "px";  
        div.style.top = parseInt(objY) + parseInt(y) - parseInt(mouseY) + "px";   
        //console.log(parseInt(objX)+","+ parseInt(x)+","+parseInt(mouseX) );
    }  
}  
function mouseUp(e) {  
    if (isDowm) {  
        var x = e.clientX;  
        var y = e.clientY;  
        var div = document.getElementById("theme-popover");  
        div.style.left = (parseInt(x) - parseInt(mouseX) + parseInt(objX)) + "px";  
        div.style.top = (parseInt(y) - parseInt(mouseY) + parseInt(objY)) + "px";  
        mouseX = x;  
        rewmouseY = y;  
        div.style.cursor = "default";  
        isDowm = false;  
    }  
}
function  isNull(obj){
	if(obj==null || obj==undefined || obj=='')return true;
	else return false;
}  