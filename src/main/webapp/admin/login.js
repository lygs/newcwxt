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
			/*if(data.results=='success'){
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
			}*/
			var tempUrl;
			 if(data.results!=null && (data.results=="success" || data.results=="SUCCESS")){
				 tempUrl = "/eprobot/admin/indexManager.html";
	            	window.location.href=tempUrl;
				 window.location.href="/eprobot/admin/indexManager.html";
	         }else if(data.results && data.results.indexOf("codeerror")>-1){
	                   alert("验证码错误！");
	            	   changeCode();
	            	   $("#validateCode").val("");
	         }else if(data.results && data.results.indexOf("max_login")>-1){
	            	alert("错误登录次数超过10次，请30分钟后重试！");
	            	changeCode();
	         }else if(data.results && data.results.indexOf("loginin")>-1){
	            	if(confirm("该用户已经登录，是否强行登录?")){
	            		var queryString ="";
	           			 queryString += $('#loginForm').formSerialize();
	            			$.ajax({
	           				type : 'post',
	           				data:queryString,
	           				dataType:"text",
	           				url : "/eprobot/user/login.action?login_=1",
	           				cache : false,
	           				success : function(data) {
	           					tempUrl = "/eprobot/admin/indexManager.html";
	           	            	window.location.href=tempUrl;
	           				}
	           			});
	            	}
	            }else{
	                 alert("用户名或密码错误！");   
	                	changeCode();
	                	$("#name").val("");
	                	$("#pwd").val("");
		        }
		}
	});
}

//修改验证码
function changeCode(){
    document.getElementById('codeImg').src='/eprobot/codeMsg.jsp?' + Math.random();
    return false;
}
