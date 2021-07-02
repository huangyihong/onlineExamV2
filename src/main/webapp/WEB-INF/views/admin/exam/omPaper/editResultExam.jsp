<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/include.inc.jsp"%>
<%@ include file="/WEB-INF/views/common/create.inc.jsp"%>
<style>
.layadmin-font-em {
    font-size: 13px;
    color: #758697;
}
.layadmin-serach-list {
    margin-bottom: 10px;
    padding: 10px 0;
    border-bottom: 1px solid #f6f6f6;
}
.correct_span{
	color:green;
	padding-left:5px
}
.error_span{
	color: red;
	padding-left:5px
}
.rightresult_span{
	color:#F1AF00;
	padding-left:5px
}
.questionscore_span{
	color:green;
	padding-left:5px
}
</style>
<style>
.layadmin-font-em {font-size: 18px;}
.layui-serachlist-test h4 {font-size:30px; margin-top:30px;  font-weight:bold;}
.questionName {font-size:18px; font-weight:bold; margin-top:30px;}
.layui-form-item p input {display: inline-block; text-align: left;}
</style>

<div class="page-container">
<form class="layui-form layui-form-pane" action="" id="form">
<c:if test="${plan!=null }">
<input type="hidden" name="planId" id="planId" value="${plan.planId }"/>
</c:if>
<input type="hidden" name="paperId" id="paperId" value="${bean.paperId }"/>
<input type="hidden" name="submitId" id="submitId" value="${examSubmit.submitId }"/>
<div class="layui-card-header" style="text-align: center;">
         <p style="font-size: 30px;">
           <strong>${bean.paperName }</strong> 
         </p>
         <c:if test="${type=='monitorExam' }">
         	<p class="layadmin-font-em">开始考试时间：<fmt:formatDate value="${examSubmit.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/><span class="p-l-50"></span>考试时长：<span id="examTime">${bean.paperTime }</span></p>
	         <p>姓名： <u> &emsp;${examSubmit.realName} &emsp;</u>	
			 </p>
         </c:if>
         <c:if test="${type!='monitorExam' }">
         	<p class="layadmin-font-em">总分：${bean.paperScore }<span class="p-l-50"></span>考试时长：<span id="examTime">${bean.paperTime }</span></p>
	         <p>姓名： <u> &emsp;${examSubmit.realName} &emsp;</u>
	            &emsp;最终得分： <u id="totalScore"> &emsp;${examSubmit.totalScore} &emsp;</u>	
	            &emsp;评卷人： <u> &emsp;${examSubmit.markUser} &emsp;</u>	
			 </p>
         </c:if>
         
</div>
<br>
<br>
<div class="layui-card-body">
	<div class="layui-serachlist-text">
	    <c:set var="questionTypeNum" value="0"/>
	    <c:if test="${not empty questionList1 }">
	    <div id="questionType1">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、单项选择题</strong></h4>
	    	<c:forEach items="${questionList1}" var="question" varStatus="status">
	    	    <div class="layui-form-item">
	    	    	<input type="hidden" name="questionId" value="${question.questionId }"/>
	    	    	<input type="hidden" name="answerId" value="${question.answerId }"/>
		    		<p class="questionName">${status.index+1 }、${question.questionName }<span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
		            <div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
		            <c:if test="${not empty question.optionA}">
		            	<p>A、${question.optionA }</p>
		            </c:if>
		            <c:if test="${not empty question.optionB}">
		            	<p>B、${question.optionB }</p>
		            </c:if>
		            <c:if test="${not empty question.optionC}">
		            	<p>C、${question.optionC }</p>
		            </c:if>
		            <c:if test="${not empty question.optionD}">
		            	<p>D、${question.optionD }</p>
		            </c:if>
		            <c:if test="${not empty question.optionE}">
		            	<p>E、${question.optionE }</p>
		            </c:if>
		            <c:if test="${not empty question.optionF}">
		            	<p>F、${question.optionF }</p>
		            </c:if>
		            <c:if test="${not empty question.optionG}">
		            	<p>G、${question.optionG }</p>
		            </c:if>
		            <c:if test="${not empty question.optionH}">
		            	<p>H、${question.optionH }</p>
		            </c:if>
		            <c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${question.questionScore }"/>
		            <input type="hidden" name="answerScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	      作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>					     
						</p>
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	        标准答案:<b class="rightresult_span"> ${question.rightResult }</b>
		                   &emsp; 作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					       <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
						</p>
		            </c:if>
	            </div>
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
    	</div>
    	</c:if>
    	
    	<c:if test="${not empty questionList2 }">
	    <div id="questionType2">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、多项选择题</strong></h4>
	    	<c:forEach items="${questionList2}" var="question" varStatus="status">
	    	    <div class="layui-form-item">
	    	        <input type="hidden" name="questionId" value="${question.questionId }"/>
	    	        <input type="hidden" name="answerId" value="${question.answerId }"/>
		    		<p class="questionName">${status.index+1 }、${question.questionName }<span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
		    		<div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
		    		<c:if test="${not empty question.optionA}">
		            	<p>A、${question.optionA }</p>
		            </c:if>
		            <c:if test="${not empty question.optionB}">
		            	<p>B、${question.optionB }</p>
		            </c:if>
		            <c:if test="${not empty question.optionC}">
		            	<p>C、${question.optionC }</p>
		            </c:if>
		            <c:if test="${not empty question.optionD}">
		            	<p>D、${question.optionD }</p>
		            </c:if>
		            <c:if test="${not empty question.optionE}">
		            	<p>E、${question.optionE }</p>
		            </c:if>
		            <c:if test="${not empty question.optionF}">
		            	<p>F、${question.optionF }</p>
		            </c:if>
		            <c:if test="${not empty question.optionG}">
		            	<p>G、${question.optionG }</p>
		            </c:if>
		            <c:if test="${not empty question.optionH}">
		            	<p>H、${question.optionH }</p>
		            </c:if>
		            <c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${question.questionScore }"/>
		            <input type="hidden" name="answerScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	      作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>					     
						</p>
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	        标准答案:<b class="rightresult_span"> ${question.rightResult }</b>
		                   &emsp; 作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					       <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
						</p>
		            </c:if>
		            
		    	</div>
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
    	</div>
    	</c:if>
    	
    	<c:if test="${not empty questionList3 }">
    	<div id="questionType3">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、判断题</strong></h4>
	    	<c:forEach items="${questionList3}" var="question" varStatus="status">
	    		<div class="layui-form-item">
	    		    <input type="hidden" name="questionId" value="${question.questionId }"/>
	    		    <input type="hidden" name="answerId" value="${question.answerId }"/>
		    		<p class="questionName">${status.index+1 }、${question.questionName }<span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
	            	<div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
	            	<c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${question.questionScore }"/>
		            <input type="hidden" name="answerScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	      作答: <b class="${color}"> <c:if test="${not empty question.answerResult }"><c:if test="${question.rightResult=='1' }">正确</c:if><c:if test="${question.rightResult=='0' }">错误</c:if></c:if><c:if test="${empty question.answerResult }">未做</c:if></b>					     
						</p>
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	        标准答案:<b class="rightresult_span"> <c:if test="${question.rightResult=='1' }">正确</c:if><c:if test="${question.rightResult=='0' }">错误</c:if></b>
		                   &emsp; 作答: <b class="${color}"> <c:if test="${not empty question.answerResult }"><c:if test="${question.answerResult=='1' }">正确</c:if><c:if test="${question.answerResult=='0' }">错误</c:if></c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					       <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
						</p>
		            </c:if>
		            
	            </div>
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
    	</div>
    	</c:if>
    	
    	<c:if test="${not empty questionList4 }">
    	<div id="questionType4">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、填空题</strong></h4>
	    	<c:forEach items="${questionList4}" var="question" varStatus="status">
	    		<div class="layui-form-item">
	    		    <input type="hidden" name="answerResult" value="${question.answerResult }"/>
	    		    <input type="hidden" name="questionId" value="${question.questionId }"/>
	    		    <input type="hidden" name="answerId" value="${question.answerId }"/>
	    		    <input type="hidden" name="questionType" value="${question.questionType }"/>
	    			<p><span class="questionName">${status.index+1 }、${question.questionName }</span> <span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
	    			<div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
	    			<c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	      作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>					     
						</p>
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            </c:if>
		            <p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	        标准答案:<b class="rightresult_span"> ${question.rightResult }</b>
		                   &emsp; 作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					       <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
					</p>
	    		</div>
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
    	</div>
    	</c:if>
    	
    	<c:if test="${not empty questionList5 }">
    	<div id="questionType5">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、简答题</strong></h4>
	    	<c:forEach items="${questionList5}" var="question" varStatus="status">
	    		<div class="layui-form-item">
	    		    <input type="hidden" name="questionId" value="${question.questionId }"/>
	    		    <input type="hidden" name="answerId" value="${question.answerId }"/>
		    		<p class="questionName">${status.index+1 }、${question.questionName }<span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
	    		    <div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
	    		    <c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            		作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					    </p>  
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            		 标准答案:<b class="rightresult_span"> ${question.rightResult }</b>
		            		<br>作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					        <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
					     </p>  
		            </c:if>
		            
	    		</div>
	    		
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
	    </div>	
    	</c:if>
    	
    	<c:if test="${not empty questionList6 }">
	    <div id="questionType6">
	    	<h4><strong>${questionTypeArr[questionTypeNum] }、案例题</strong></h4>
	    	<c:forEach items="${questionList6}" var="question" varStatus="status">
	    	    <div class="layui-form-item">
	    	        <input type="hidden" name="questionId" value="${question.questionId }"/>
	    	        <input type="hidden" name="answerId" value="${question.answerId }"/>
		    		<p class="questionName">${status.index+1 }、${question.questionName }<span style="font-weight: bold;">( ${question.questionScore} 分 )</span></p>
		    		<div class="layui-upload-list" id="imgList">
		            <c:forEach items="${question.imgList}" var="imgBean" varStatus="status">
				      	<div class="imgdiv">
				      		<img src="${contextPath }${imgBean.imgSrc }" class="layui-upload-img" onclick="previewImg(this)"/>
				      	</div>
				    </c:forEach>
				    </div>
		    		<c:if test="${not empty question.optionA}">
		            	<p>A、${question.optionA }</p>
		            </c:if>
		            <c:if test="${not empty question.optionB}">
		            	<p>B、${question.optionB }</p>
		            </c:if>
		            <c:if test="${not empty question.optionC}">
		            	<p>C、${question.optionC }</p>
		            </c:if>
		            <c:if test="${not empty question.optionD}">
		            	<p>D、${question.optionD }</p>
		            </c:if>
		            <c:if test="${not empty question.optionE}">
		            	<p>E、${question.optionE }</p>
		            </c:if>
		            <c:if test="${not empty question.optionF}">
		            	<p>F、${question.optionF }</p>
		            </c:if>
		            <c:if test="${not empty question.optionG}">
		            	<p>G、${question.optionG }</p>
		            </c:if>
		            <c:if test="${not empty question.optionH}">
		            	<p>H、${question.optionH }</p>
		            </c:if>
		            <c:set var="color" value="error_span"/>
		            <c:set var="questionScore" value="0"/>
		            <c:if test="${question.answerResult==question.rightResult }">
		            	<c:set var="color" value="correct_span"/>
		            	<c:set var="questionScore" value="${question.questionScore}"/>
		            </c:if>
		            <c:if test="${not empty question.markScore}">
		            	<c:set var="questionScore" value="${question.markScore}"/>
		            </c:if>
		            <input type="hidden" name="questionScore" value="${question.questionScore }"/>
		            <input type="hidden" name="answerScore" value="${questionScore }"/>
		            <input type="hidden" name="answerResult" value="${question.answerResult }"/>
		            <input type="hidden" name="rightResult" value="${question.rightResult }"/>
		            <input type="hidden" name="questionType" value="${question.questionType }"/>
		            <c:if test="${type=='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	      作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>					     
						</p>
		            </c:if>
		            <c:if test="${type!='monitorExam' }">
		            	<p style="border-bottom: 1px solid #e6e6e6;padding:2px">
		            	        标准答案:<b class="rightresult_span"> ${question.rightResult }</b>
		                   &emsp; 作答: <b class="${color}"> <c:if test="${not empty question.answerResult }">${question.answerResult }</c:if><c:if test="${empty question.answerResult }">未做</c:if></b>
					       <c:if test="${empty question.markScore}">
					       &emsp;自动阅卷得分:<b class="questionscore_span"> ${questionScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markScore}">
					       &emsp;人工评卷打分:<b class="rightresult_span">${question.markScore }</b>
					       </c:if>
					       <c:if test="${not empty question.markText}">
					       	&emsp;备注:<b class="rightresult_span">${question.markText }</b>
					       </c:if>
					       &emsp;修改作答:&emsp;<input type="text" name="editResult" autocomplete="off" value="">
						</p>
		            </c:if>
		            
		    	</div>
	    	</c:forEach>
	    	<c:set var="questionTypeNum" value="${questionTypeNum+1 }"/>
    	</div>
    	</c:if>
   </div>
</div>
<br>
<div class="layui-form-item" style="text-align: center">
    <span>
        <a lay-submit lay-filter="formSubmit" id="btnSave" href="javascript:void(0)"
               class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-primary">修改提交</a>  
        <a onclick="parent.layer.closeAll()"
           class="fm-button ui-state-default ui-corner-all fm-button-icon-left ui-reset btn btn-sm btn-default">关闭</a>
       </span>
</div>
</form>
</div>
<script type="text/javascript" src="${contextPath }/static/js/common/commonCreate.js?v=<%=Math.random()%>"></script>
<script type="text/javascript" src="${contextPath }/static/js/admin/exam/omPaper/editResultExam.js?v=<%=Math.random()%>"></script>
<%@ include file="/WEB-INF/views/admin/exam/omPaper/showImg.inc.jsp"%>

