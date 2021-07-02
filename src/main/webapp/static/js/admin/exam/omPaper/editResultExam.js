var form = null;
layui.use('form', function(){
	  form = layui.form;
	  form.render();
	  
	//监听提交
	  form.on('submit(formSubmit)', function(data){
		save(data);	
		return false;
	  });
});

var totalScore = 0;
$(function () {
	$(".questionscore_span").each(function(i, o){
		var questionScore = $(o).text();
		totalScore += parseInt(questionScore.trim());
	});
	$("#totalScore").html("&emsp;"+totalScore+"&emsp;");
	
	//监控修改作答
	$("input:text[name='editResult']").blur(function () {
		var editTotal = 0;
		$("input:text[name='editResult']").each(function(i, o){
			var val = $(o).val().trim();
			if(val&&val!=''){
				var questionScore = parseInt($(o).closest('.layui-form-item').find("[name='questionScore']").val());//题目分数
				var answerScore = parseInt($(o).closest('.layui-form-item').find("[name='answerScore']").val());//原来得分
				var questionType =$(o).closest('.layui-form-item').find("[name='questionType']").val();//题目类型
				var answerResult =$(o).closest('.layui-form-item').find("[name='answerResult']").val();//作答
				var rightResult =$(o).closest('.layui-form-item').find("[name='rightResult']").val();//正确答案
			    //得到更正的分数
				if(questionType=='1'||questionType=='2'||questionType=='6'){
					val = val.toUpperCase();
				}
				if(questionType=='3'){
					if(val=='正确'){
						val = '1';
					}
					if(val=='错误'){
						val = '0';
					}
				}
				var editScore=0;
				if(val==rightResult){
					editScore = questionScore;
				}
				editTotal += editScore-answerScore
			}
		})
		$("#totalScore").html("&emsp;"+(totalScore+editTotal)+"&emsp;");
		
	});
});


function save(data){
	var url = WEBROOT + '/admin/exam/omExamSubmit/editResultExam';
	var omExamAnswerList = new Array();
	var editTotal = 0;
	$("input:text[name='editResult']").each(function(i, o){
		var val = $(o).val().trim();
		if(val){
			var questionScore = parseInt($(o).closest('.layui-form-item').find("[name='questionScore']").val());//题目分数
			var answerScore = parseInt($(o).closest('.layui-form-item').find("[name='answerScore']").val());//原来得分
			var questionType =$(o).closest('.layui-form-item').find("[name='questionType']").val();//题目类型
			var answerResult =$(o).closest('.layui-form-item').find("[name='answerResult']").val();//作答
			var rightResult =$(o).closest('.layui-form-item').find("[name='rightResult']").val();//正确答案
			var answerId = $(this).closest('.layui-form-item').find("[name='answerId']").val();
			var questionId = $(this).closest('.layui-form-item').find("[name='questionId']").val();
		    //得到更正的分数
			if(questionType=='1'||questionType=='2'||questionType=='6'){
				val = val.toUpperCase();
			}
			if(questionType=='3'){
				if(val=='正确'){
					val = '1';
				}
				if(val=='错误'){
					val = '0';
				}
			}
			var editScore=0;
			if(val==rightResult){
				editScore = questionScore;
			}
			editTotal += editScore-answerScore
			
			var omExamAnswer = new Object();
			omExamAnswer.questionId=questionId;
			omExamAnswer.answerId=answerId;
			omExamAnswer.answerResult=val;
			omExamAnswerList.push(omExamAnswer);
		}
	});
	data.field.totalScore = totalScore+editTotal;
	data.field.omExamAnswerListJson=JSON.stringify(omExamAnswerList);
	commonSave(data,url);	
}