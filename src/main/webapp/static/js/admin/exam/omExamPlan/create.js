var form = null;
var laydate = null;
layui.use(['form','laydate'], function(){
	  form = layui.form;
	  laydate = layui.laydate;
	  form.render();
	  
	  laydate.render({
		  elem: '#planTime'
		 ,position: 'fixed'		  
	     ,type: 'datetime'		  
		 ,format: 'yyyy-MM-dd HH:mm:ss'
	  });
	  
	  //监听提交
	 form.on('submit(formSubmit)', function(data){
		var fntype = $("#fntype").val(); 
		var courseName = $("#courseId option:selected").text();
		data.field.courseName = courseName;
		save(data);	
		return false;
	 });
	  
	  
});

function save(data){
	var url = WEBROOT + '/admin/exam/omExamPlan/save';
	commonSave(data,url);	
}