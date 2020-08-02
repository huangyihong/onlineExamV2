<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/include.inc.jsp"%>
<%@ include file="/WEB-INF/views/common/create.inc.jsp"%>
<style>
.layui-inline{
	width:90%
}
.layui-form-label{
	width:20%!important
}
.layui-input-inline{
	width:70%!important
}
</style>

<div class="page-container">
<form class="layui-form layui-form-pane" action="" id="form">
    <input type="hidden" id="fntype" name="fntype" value="${fntype }"/>
    <input type="hidden" id="questionId" name="questionId" value="${bean.questionId}"/>
    <input type="hidden" id="questionType" name="questionType" value="${questionType}"/>
    <br>
	<div class="layui-form-item layui-form-text">
		<label class="layui-form-label" style="width: 90%!important;"><span class="c-red">*</span>题目：</label>
		<div class="layui-input-inline" style="width: 90%!important;">
			<textarea name="questionName" class="layui-textarea" lay-verify="required" placeholder="请输入题目内容" >${bean.questionName }</textarea>
		</div>
	</div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项A：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionA" autocomplete="off" class="layui-input" lay-verify="required" value="${bean.optionA }">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项B：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionB" autocomplete="off" class="layui-input" lay-verify="required" value="${bean.optionB }">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项C：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionC" autocomplete="off" class="layui-input" lay-verify="required" value="${bean.optionC }">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项D：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionD" autocomplete="off" class="layui-input" lay-verify="required" value="${bean.optionD }">
            </div>
        </div>
    </div> 
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项E：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionE" autocomplete="off" class="layui-input" value="${bean.optionE }">
            </div>
        </div>
    </div>  
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项F：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionF" autocomplete="off" class="layui-input" value="${bean.optionF }">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项G：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionG" autocomplete="off" class="layui-input" value="${bean.optionG }">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">选项H：</label>
            <div class="layui-input-inline">
                <input type="text" name="optionH" autocomplete="off" class="layui-input" value="${bean.optionH }">
            </div>
        </div>
    </div> 
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>标准答案：</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="rightResult" value="A" title="A" <c:if test="${fn:contains(bean.rightResult,'A') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="B" title="B" <c:if test="${fn:contains(bean.rightResult,'B') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="C" title="C" <c:if test="${fn:contains(bean.rightResult,'C') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="D" title="D" <c:if test="${fn:contains(bean.rightResult,'D') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="E" title="E" <c:if test="${fn:contains(bean.rightResult,'E') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="F" title="F" <c:if test="${fn:contains(bean.rightResult,'F') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="G" title="G" <c:if test="${fn:contains(bean.rightResult,'G') }">checked</c:if>>
                <input type="checkbox" name="rightResult" value="H" title="H" <c:if test="${fn:contains(bean.rightResult,'H') }">checked</c:if>>
            </div>
        </div>
     </div>
     <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>题目分值：</label>
            <div class="layui-input-inline">
            	<input type="text" name="questionScore" autocomplete="off" class="layui-input" lay-verify="number" value="${bean.questionScore }">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span class="c-red">*</span>所属科目：</label>
            <div class="layui-input-inline">
            	<select id="courseId" name="courseId" value="${bean.courseId }" lay-verify="required" >
                	<option value="">--请选择--</option>
	                <c:forEach items="${courseList}" var="courseBean">
	                	<option value="${courseBean.courseId }" <c:if test="${courseBean.courseId == bean.courseId }">selected</c:if>>${courseBean.courseName }</option>
	                </c:forEach>
                </select>
            </div>
        </div>
     </div>
     <%@ include file="/WEB-INF/views/admin/exam/omQuestion/uploadImg.inc.jsp"%>
     

     <br>
     <div class="layui-form-item" style="text-align: center">
        <span>
            <c:if test="${fntype!='view' }">
                <a lay-submit lay-filter="formSubmit" id="btnSave" href="javascript:void(0)"
                   class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-primary">保存</a>
            </c:if>
            <a onclick="parent.layer.closeAll();parent.parent.layer.closeAll()"
               class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-default">关闭</a>
        </span>
     </div>   
</form>
</div>
<script type="text/javascript" src="${contextPath }/static/js/common/commonCreate.js"></script>
<script type="text/javascript" src="${contextPath }/static/js/admin/exam/omQuestion/create.js"></script>


