<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/include.inc.jsp"%>
<%@ include file="/WEB-INF/views/common/create.inc.jsp"%>


<div class="page-container">
<form class="layui-form layui-form-pane" action="" id="form">
    <input type="hidden" id="fntype" name="fntype" value="${fntype }"/>
    <input type="hidden" id="planId" name="planId" value="${bean.planId}"/>
    <br>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>考试试卷：</label>
            <div class="layui-input-inline">
                <input type="hidden" id="paperId" name="paperId" value="${bean.paperId }"/>
                <input type="text" id="paperName" name="paperName" class="layui-input" lay-verify="required" value="${bean.paperName }" readonly>
            </div>
            <c:if test="${fntype!='view' }">
            <div class="layui-input-inline" style="width: 100px;">
            	<a class="layui-btn layui-btn-sm m-l-10" style="margin-top: 4px;" onclick="openSelectWin()">选择</a>
            </div>
            </c:if>
        </div>
         <div class="layui-inline">
            <label class="layui-form-label">试卷总分：</label>
            <div class="layui-input-inline">
                <input type="text" id="paperScore" autocomplete="off" class="layui-input" value="${bean.paperScore }" readonly>
            </div>
        </div> 
        <div class="layui-inline">
            <label class="layui-form-label">考试时长(分)：</label>
            <div class="layui-input-inline">
                <input type="text" id="paperTime" autocomplete="off" class="layui-input" value="${bean.paperTime }" readonly>
            </div>
        </div> 
        <div class="layui-inline">
            <label class="layui-form-label">题目数量：</label>
            <div class="layui-input-inline">
                <input type="text" id="singleCount" autocomplete="off" class="layui-input" value="${bean.singleCount+bean.multiCount+bean.judgeCount+bean.blankCount+bean.answerCount }" readonly>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>考试名称：</label>
            <div class="layui-input-inline">
                <input type="text" name="planName" autocomplete="off" class="layui-input" lay-verify="required|maxlength" value="${bean.planName }">
            </div>
        </div>
        <%-- <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>开考时间：</label>
            <div class="layui-input-inline">
                <input type="text" name="beginTime" autocomplete="off" class="layui-input dateInput" lay-verify="required" data-format="yyyy-MM-dd HH:mm:ss" data-datemin="0" value="${bean.beginTime }">
            </div>
        </div>  --%>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>及格分数：</label>
            <div class="layui-input-inline">
                <input type="text" name="passingScore" autocomplete="off" class="layui-input" lay-verify="number" value="${bean.passingScore }">
            </div>
            <div class="layui-input-inline" style="width: 100px;">
            	<select name="passingType" value="${bean.passingType }">
                	<option value="1" <c:if test="${bean.passingType == '1' }">selected</c:if>>分数</option>
                	<option value="2" <c:if test="${bean.passingType == '2' }">selected</c:if>>百分比</option>
                </select>
            </div>
        </div> 
        <div class="layui-inline">
            <label class="layui-form-label">是否自动阅卷：</label>
            <div class="layui-input-inline">
                <select name="autoMarkFlag" value="${bean.autoMarkFlag }">
                	<option value="1" <c:if test="${bean.autoMarkFlag == '1' }">selected</c:if>>是</option>
                	<option value="0" <c:if test="${bean.autoMarkFlag == '0' }">selected</c:if>>否</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">考试类别：</label>
            <div class="layui-input-inline">
                <select id="courseId" name="courseId" value="${bean.courseId }" lay-filter="courseId"  >
                	<option value="">--请选择--</option>
	                <c:forEach items="${courseList}" var="courseBean">
	                	<option value="${courseBean.courseId }" <c:if test="${courseBean.courseId == bean.courseId }">selected</c:if>>${courseBean.courseName }</option>
	                </c:forEach>
                </select>
            </div>
        </div>
        
	    <c:choose>
          <c:when test="${fn:length(bean.planTime) > 19}">
              <c:set var="planTime" value="${fn:substring(bean.planTime, 0, 19)}"></c:set>
          </c:when>
          <c:otherwise>
            <c:set var="planTime" value="${bean.planTime}"></c:set>
          </c:otherwise>
	    </c:choose>
        <div class="layui-inline">
            <label class="layui-form-label">考试时间：</label>
            <div class="layui-input-inline">
            	<c:if test="${fntype!='view' }">
            		<input type="text" id="planTime" name="planTime" autocomplete="off" class="layui-input" value="${planTime }">
            	</c:if>
            	<c:if test="${fntype=='view' }">
            		<input type="text" autocomplete="off" class="layui-input" value="${planTime }">
            	</c:if>
                
            </div>
        </div> 
   </div>     

    <br>
    <div class="layui-form-item" style="text-align: center">
        <span>
            <c:if test="${fntype!='view' }">
                <a lay-submit lay-filter="formSubmit" id="btnSave" href="javascript:void(0)"
                   class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-primary">保存</a>
            </c:if>
            <a onclick="parent.layer.closeAll()"
               class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-default">关闭</a>

        </span>
	</div>
</form>
</div>
<script type="text/javascript" src="${contextPath }/static/js/common/commonCreate.js"></script>
<script type="text/javascript" src="${contextPath }/static/js/admin/exam/omExamPlan/create.js"></script>
<script type="text/javascript">
function openSelectWin(){
	var url = WEBROOT + '/admin/exam/omPaper/selectPaper';
	commonSelectView('选择试卷',url,'','',1);	
}

function commonSelectView(title,url,width,height,isfull) {
	if(!width){
		width = 900
	}
	if(!height){
		height = 450
	}
    layui.use(['element'], function () {
        var index = top.layer.open({
            type: 2,
            title: title,
            skin: 'layui-layer-rim', //加上边框
            area: [width+'px', height+'px'], //宽高
            content: url,
            btn: ['确定', '取消'],
            maxmin: true, 
            yes: function (index) {
            	//当点击‘确定’按钮的时候，获取弹出层返回的值
                var res = top.window["layui-layer-iframe" + index].callbackdata();
                var msg = res.msg;
                if (msg != '') {
                    top.layer.alert(msg, {icon: 2, time: 2000, shade: 0.3});
                } else {
                    //选中的回写到页面
                    //setTab($("#questionType"+questionType).find('.multiSelect'),res.objs)
                    setPaperValue(res.objs)
                    top.layer.close(index);
                }
            },
            cancel: function () {
                //右上角关闭回调
            }
        });
        if(isfull){
        	top.layer.full(index);
   	    }
    });
}

function setPaperValue(objs){
	$.each(objs, function(i,bean){
		$("#paperId").val(bean.paperId);
		$("#paperName").val(bean.paperName);
		$("#paperScore").val(bean.paperScore);
		$("#paperTime").val(bean.paperTime);
		$("#singleCount").val(bean.singleCount+bean.multiCount+bean.judgeCount+bean.blankCount+bean.answerCount);
	});
}
</script>

