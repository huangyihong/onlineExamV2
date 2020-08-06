<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
String basePath = request.getContextPath();
String contextPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;	    
%>
<c:set var="contextPath" value="<%=contextPath %>"></c:set>

<script type="text/javascript" src="${contextPath }/static/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath }/static/lib/layer/2.1/layer.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/static/lib/layui/css/layui.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/static/lib/layui/css/modules/form.css" />
<script type="text/javascript" src="${contextPath }/static/lib/layui/layui.js"></script>
<link rel="stylesheet" href="${contextPath }/static/css/fonts/iconfont.css"/>
<script src="${contextPath }/static/css/fonts/iconfont.js"></script>
<!-- <link href="http://at.alicdn.com/t/font_1429101_zdoz8cz9d.css" rel="stylesheet" type="text/css"> -->
<link href="${contextPath }/static/css/public.css" rel="stylesheet" type="text/css">
<link href="${contextPath }/static/css/health.css" rel="stylesheet" type="text/css">
<link href="${contextPath }/static/css/developer.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	var WEBROOT = '${contextPath}';
</script>
<script type="application/javascript">
	var isIE = !!window.ActiveXObject || "ActiveXObject" in window
</script>
<title>在线考试系统</title>
<meta name="viewport" content="width=device-width, minimum-scale=0.5, maximum-scale=0.5, user-scalable=no"/>
<script>
//兼容ANDROID
var viewport = document.querySelector("meta[name=viewport]");
var winWidths=$(window).width();
var densityDpi=640/winWidths;
densityDpi= densityDpi>1?300*640*densityDpi/640:densityDpi;
viewport.setAttribute('content', 'width=640, target-densityDpi='+densityDpi+', initial-scale=0.5, maximum-scale=0.5');
</script>
<script type="text/javascript">
function IsPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
                "SymbianOS", "Windows Phone",
                "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}
</script>
