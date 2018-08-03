$(function(){
	$("#loginOut").click(function(){
		$.ajax({
	        type:'post',
	        url:"/eprobot/user/loginOut.action",
	        dataType:"json",
	        success:function(data){
	          if(data.results=="success"){
	        	 alert("成功退出");
	        	 window.location.href="/eprobot/login.jsp";
	          }
	        }
		});
	});
	
	$("#updatePwd").on("click",function(){
		var oldPwd = $("#oldPwd").val();
		var newPwd = $("#newPwd").val();
		var twonewPwd = $("#twonewPwd").val();
		if(oldPwd==null || oldPwd=="" || newPwd==null || newPwd=="" &&twonewPwd==null || twonewPwd==""){
			alert("密码不能为空");
			return;
		}
		if(newPwd != twonewPwd){
			alert("两次密码必须一致！");
			return;
		}
		$.ajax({
			type:'post',
	        url:"/eprobot/user/updatePwd",
	        data:{"oldPwd":oldPwd,"newPwd":newPwd,"ids":"1"},
	        dataType:'json',
	        success:function(data){
	        	alert(data);
	          if(data.results=="ERROR" || data.results == 'error'){
	        	  alert("修改失败！");
	          }else if(data.results=="oldPwdError"){
	        	  alert("原密码输入错误");
	          }else if(data.results=="succ"){
	        	  alert("修改成功");
	          }
	        }
		});
	});
});