var form = null;
var table = null;
var element = null;
layui.use('form', function(){
	  form = layui.form;
	  form.render();
});
var baseUrl = WEBROOT + '/admin/exam/omExamPlan';
var listUrl = baseUrl +'/getMakeupExamList';
var createMakeupPlanUrl = baseUrl +'/createMakeupPlan';
var showUrl = WEBROOT + '/admin/exam/omPaper/show';

$(function () {
	//初始列表
	initGrid();
});

//初始列表
function initGrid() {
	var cols = [[{field:'submitStatus', width:300, title: '操作',templet:function(d){
					 if(d.nopassNum>0){//不及格人数
			   		  //练习过
			   		 return "<div><a href='javascript:void(0)' class='layui-table-link' style='margin-left:14px'  onclick=createMakeupPlan('"+d.paperId+"','"+d.planId+"') >补考</a></div>";
					 }
					 return '<div style="font-size: 10px;">无需补考</div>'
			     }}
				 ,{field:'planName', width:300, title: '考试名称'}
	             ,{field:'paperName', width:200, title: '试卷名称',templet:function(d){
	                 return "<div><a href='javascript:void(0)' class='layui-table-link'  onclick=showPaper('"+d.paperId+"') >"+d.paperName+"</a></div>"
	              }}
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
	             ,{field:'userNum', width:100, title: '考试人数'}
	             ,{field:'passNum', width:100, title: '及格人数'}
	             ,{field:'nopassNum', width:100, title: '不及格人数'}
	           ]];
	var keyFiled= "planId";//主键
	var limit = 10;//分页
	commonInitGrid(listUrl,cols,keyFiled,limit)
}

//预览
function showPaper(id){
	 var url =showUrl+'?paperId='+id;
	 var title = '试卷预览';
	 var isreload = 0;
	 commonCreateView(title,url,isreload,'500','500',1);
}

//补考安排
function createMakeupPlan(paperId,planId){
    var url =createMakeupPlanUrl+'?paperId='+paperId+'&planId='+planId;
    var title = '补考安排';
    commonCreateView(title,url,0,'800','600',1);
}


