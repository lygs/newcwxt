$(function(){
	$("input").keydown(function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e && e.keyCode ==13){
			logins();
		}
	});
	$("#login").click(logins);
});

function logins(){
	var name=$("#name").val();
	var pwd = $("#pwd").val();
	 var validateCode = $.trim($("#validateCode").val());
	if(name =="" || name ==null){
		alert("请输入用户名");
		return;
	}
	if(pwd =="" || pwd ==null){
		alert("请输入密码");
		return;
	}
	 if(validateCode.length==0){
	   	alert("请输入验证码");$("#validateCode").focus();return;
	 }
	$.ajax({
		url:"/eprobot/user/login.action",
		dataType:"json",
		data:{"name":name,"pwd":pwd,"validateCode":validateCode},
		success:function(data){
			if(data.results=='success'){
				window.location.href="/eprobot/admin/indexManager.html";
			}else if(data.results=="error"){
				changeCode();
				alert("用户名或密码错误");
			}else if(data.results=="codeError"){
				changeCode();
				alert("验证码错误");
			}else if(data.results =="null"){
				changeCode();
				alert("用户名或密码为空");
			}
		}
	});
}

//修改验证码
function changeCode(){
    document.getElementById('codeImg').src='/eprobot/codeMsg.jsp?' + Math.random();
    return false;
}
