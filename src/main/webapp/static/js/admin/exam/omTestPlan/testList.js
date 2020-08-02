var form = null;
var table = null;
layui.use('form', function(){
	  form = layui.form;
	  form.render();
});
var baseUrl = WEBROOT + '/admin/exam/omTestPlan';
var listUrl = baseUrl +'/getTestList';
var showUrl = WEBROOT + '/admin/exam/omPaper/show';

$(function () {
	//初始列表
	initGrid();
});

//初始列表
function initGrid() {
	var cols = [[{field:'submitStatus', width:300, title: '操作',templet:function(d){
					 if(d.submitStatus=='2'||d.submitStatus=='3'||d.submitStatus=='4'){
			    		  //练习过
			    		 return "<div>" +
			    		 		"<a href='javascript:void(0)' class='layui-table-link'  onclick=showPaper('"+d.paperId+"','"+d.planId+"','"+d.userId+"','historyExam')>回顾</a>" +
			    		 		"<a href='javascript:void(0)' class='layui-table-link' style='margin-left:14px'  onclick=reStartTest('"+d.paperId+"','"+d.planId+"') >再次练习</a>" +
			    		 		"</div>";
			    	 }else if(d.submitStatus=='1'){
			    		 return "<div><a href='javascript:void(0)' class='layui-table-link'  onclick=startExam('"+d.paperId+"','"+d.planId+"') >继续练习</a></div>"
			    	 }
					 return "<div><a href='javascript:void(0)' class='layui-table-link'  onclick=startExam('"+d.paperId+"','"+d.planId+"') >开始练习</a></div>"
	              }}
				,{field:'submitStatus', width:200, title: '状态',templet:function(d){
			   	 var html = '还未练习';
			   	 if(d.submitStatus=='1'){
			   		 html = '正在练习';
			   	 }else if(d.submitStatus=='2'||d.submitStatus=='3'||d.submitStatus=='4'){
			   		 html = '练习过';
			   	 }
			        return '<div>'+html+'</div>'
			     }}
				 ,{field:'planName', width:300, title: '练习名称'}
	             ,{field:'paperName', width:200, title: '试卷名称'}
	             
	             ,{field:'paperTime', width:120, title: '练习时长(分)'}
	             ,{field:'paperScore', width:100, title: '总分'}
	             ,{field:'passingScore', width:100, title: '及格分',templet:function(d){
	            	 var html = '';
	            	 if(d.passingType=='1'){
	            		 html = d.passingScore+'分';
	            	 }
	            	 if(d.passingType=='2'){
	            		 html = d.passingScore+'%';
	            	 }
	            	 
	                 return '<div>'+html+'</div>'
	              }}
	             ,{field:'singleCount', width:100, title: '题目数量',templet:function(d){
	            	 var num = d.singleCount+d.multiCount+d.judgeCount+d.blankCount+d.answerCount
	                 return '<div>'+num+'</div>'
	              }}
	             //,{field:'courseName', width:100, title: '科目'}
	           ]];
	var keyFiled= "planId";//主键
	var limit = 10;//分页
	commonInitGrid(listUrl,cols,keyFiled,limit)
}

function startExam(paperId,planId){
	$.ajax({
		type:"POST",
		async:false,
		data:{paperId:paperId,planId:planId},
		url:WEBROOT + "/admin/exam/omExamPlan/doStartExam",
		success:function (data) {
			if (data.code == "200") {
				var flag = data.data;
				if(flag){
					showPaper(paperId,planId,'','');
				}else{
					layer.msg(data.message, function(){
						//关闭后的操作
						window.location.reload();
					});
				}
			}else{
				layer.msg('查询失败：'+data.messgae, {icon: 5});
			}
		}
	});
}

function reStartTest(paperId,planId){
	$.ajax({
		type:"POST",
		async:false,
		data:{paperId:paperId,planId:planId},
		url:WEBROOT + "/admin/exam/omExamPlan/reStartTest",
		success:function (data) {
			if (data.code == "200") {
				showPaper(paperId,planId,'','');
			}else{
				layer.msg('查询失败：'+data.messgae, {icon: 5});
			}
		}
	});
}

function showPaper(paperId,planId,userId,type){
	var baseUrl = WEBROOT + '/admin/exam/omPaper';
	var showUrl = baseUrl +'/exam';
	 var url =showUrl+'?paperId='+paperId+'&planId='+planId;
	 var title = '练习';
	 var isreload = 1;
	 if(type=='historyExam'){
		 url +='&type='+type+'&answerUserId='+userId;
		 title = '回顾练习';
		 isreload = 0;
	 }
	 commonCreateView(title,url,isreload,'500','500',1);
}




