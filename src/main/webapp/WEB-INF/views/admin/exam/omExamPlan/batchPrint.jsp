<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/include.inc.jsp"%>
<%@ include file="/WEB-INF/views/common/create.inc.jsp"%>
<style type="text/css">
*{
padding:0px;
margin:0px;
}
@media print {#dybutton{display:none;}}

#juzhong{
text-align:center;
}
#juzuo{
text-align:left;
}

</style>

<div class="page-container">
<button class="layui-btn layui-btn-primary layui-btn-sm" style="float:right;margin-right:50px" onclick="printSomething();">
打印
</button>
<!--startprint--> 
<div style="width:210mm;background-color:#FFF;"> 
	<c:forEach items="${userDataList}" var="map">
		<div style="height:161mm">
		<table  border="3" width="95%" cellspacing="0" style="margin:10px;">
			<tr id="juzhong" height="100">
			<td colspan="2">
			<h1>准考证</h1>
			</td>
			</tr>
			<tr height="200">
			<td>
			学员姓名：  ${map.user.realName }<br/>
			身份证号：  ${map.user.userName }<br/>
			考试类别：  ${plan.courseName }<br/>
			准考证号：  ${map.examUser.examNum }<br/>
			考试时间：  ${plan.planTime}<br/>
			&nbsp;&nbsp;&nbsp;座位号：  ${map.examUser.seatNum }<br/>
			
			</td>
			<td style="border-style:none;border-wigth:0px;">
			<!-- <img src="1.jpg" width="150" height="150"> -->
			<c:if test="${not empty map.imgSrc}"><img src="${contextPath }${map.imgSrc }" width="150" height="150"></c:if>
			<c:if test="${empty map.imgSrc}"><img width="150" height="150"></c:if>
			</td>
			</tr>
			<tr id="juzhong" height="200">
			<td colspan="2">
			<p>考生须知</p>
			<div id="juzuo">
			1、考生应考时，严禁将书籍、笔记、移动电话、电子记事本等电子设备带至考场，一经发现，按违纪处理。<br/>
			2、考试前30分钟进入考场，监考人员核对准考证（包括电子准考证）、身份证后，学生对号入座将身份证放在桌角上备查（不携带身份证不容许参加考试）。<br/>
			3、考试开始后30分钟，迟到的考生不得进入考场参加考试。考生不得提前交卷。<br/>
			</div><br/>
			</td>
			</tr>
		</table>
		</div>
		
	</c:forEach>              
</div>
<!--endprint-->
</div>
<script>
  function printSomething() {
    // 获取当前页面html代码
    var currentHtml = window.document.body.innerHTML
    // 设置打印开始位置
    var start = '<!--startprint-->'
    // 设置打印结束位置
    var end = '<!--endprint-->'
    // 获取到要打印部分的代码
    var printHtml = currentHtml.substring(currentHtml.indexOf(start) + start.length, currentHtml.indexOf(end))
    
    // 打印
    window.document.body.innerHTML = printHtml
    window.print()
    // 返回原界面
    window.document.body.innerHTML = currentHtml
  }
  
</script>


