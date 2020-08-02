var form = null;
var element = null;
var transfer = null;
layui.use(['form','element','transfer'], function(){
	  element = layui.element;
	  form = layui.form;
	  transfer = layui.transfer
	  form.render();
	  
	//监听提交
	  form.on('submit(formSubmit)', function(data){
		var fntype = $("#fntype").val();  
		save(data);	
		return false;
	 });
	  
	  //初始右侧数据
	  transfer.render({
	    elem: '#userSelect'
	    ,title: ['可授权的用户', '已授权的用户']  //自定义标题
	    ,id: 'userSelect' //定义唯一索引
	    ,showSearch: true
	    ,data: getAllUserList()
	    ,value: []
	  })
});

//用户列表
function getAllUserList(){
	var dataUser = []
	$.ajax({
		type:"POST",
		async:false,
		url:WEBROOT + "/admin/exam/omExamPlan/getMakeupExamUserList",
		data:{'planId':$("#oldPlanId").val()},
		success:function (data) {
			if (data.code == "200") {
				var userList = data.data;
				$.each(userList, function(i,user){      
					dataUser[i] = {value:user.userId , title: user.realName+'('+user.userName+')'};
				});
			}else{
				layer.msg('查询失败：'+data.messgae, {icon: 5});
			}
		}
	});
	return dataUser;
}

function save(data){
	var url = WEBROOT + '/admin/exam/omExamPlan/saveMakeupPlan';
	var selectData = transfer.getData('userSelect'); //获取右侧数据
	var userIdArr = [];
	$.each(selectData, function(i,data){      
		userIdArr.push(data.value);
	}); 
	if(userIdArr.length==0){
		layer.msg('请选择授权用户', {icon: 5});
		return;
	}
	data.field.examUserType = '1';
	data.field.userIds = userIdArr.join(',');
	commonSave(data,url);	
	/*// 按钮禁用
    var isDisabled = $("#btnSave").hasClass('layui-btn-disabled');
    if (isDisabled) {
        return false;
    }
	$.ajax({
		type:"POST", 
		async:true, 
		url:url,
		data:data.field,
		beforeSend: function () {
            $("#btnSave").addClass('layui-btn-disabled');// 禁用
        },
        complete: function () {
            $("#btnSave").removeClass('layui-btn-disabled');// 启用
        },
		success:function (data) {
			if (data.code == "200") {
				$("#planId").val(data.data.planId);
			}else{
				layer.msg('保存失败：'+data.message, {icon: 5});
			}
		}
	});	*/
}

function saveExamUser(){
	var planId = $("#planId").val();
	if(!planId){
		layer.msg('请先保存补考安排', {icon: 5});
		return;
	}
	var selectData = transfer.getData('userSelect'); //获取右侧数据
	var userIdArr = [];
	$.each(selectData, function(i,data){      
		userIdArr.push(data.value);
	}); 
	if(userIdArr.length==0){
		layer.msg('请选择授权用户', {icon: 5});
		return;
	}
	$.ajax({
		type:"POST", 
		async:true, 
		url:WEBROOT + "/admin/exam/omExamPlan/saveExamUser",
		data:{
				planId:$("#planId").val(),
				paperId:$("#paperId").val(),
				examUserType:'1',
				userIds:userIdArr.join(',')
			 },
		beforeSend: function () {
            $("#btnSave2").addClass('layui-btn-disabled');// 禁用
        },
        complete: function () {
            $("#btnSave2").removeClass('layui-btn-disabled');// 启用
        },
		success:function (data) {
			if (data.code == "200") {
				layer.msg('保存成功', {icon: 1});
				parent.layer.closeAll()
			}else{
				layer.msg('保存失败：'+data.ERRINFO, {icon: 5});
			}
		}
	});
}