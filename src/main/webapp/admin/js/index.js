var KnowAllList =[];
//欄目總條數
var allChnl;
var knowlegeid;
var flType;//知识点分类 类型
var rcId;
	$(function(){
		islogin();
		$("#rcAdd").on("click",function(){
			addRC();
		})
		$("#rcChange").on("click",function(){
			rcChange();
			
		})

		$(".upfile").on("click",function(){
			var fname = $(".fileval").val();
			if(fname){
				var formData = new FormData($('#fileform')[0]);
				$.ajax({
					type: 'post',
					url: "/eprobot/questionAnswer/addQuestionByExcel",
					data: formData,
					dataType:"json",
					cache: false,
					processData: false,
					contentType: false,
				}).success(function (data) {
					if(data.result=="success"){
						alert("上传成功！");
						$(".fileval").val("");
					}else{
						alert("上传失败");
					}

				}).error(function () {
					alert("上传失败");
				});
			}else{
				alert("请选择上传文件");
			}

		});


		
		$(".ul1 li").on("click",function(){
			$(".ul1 li").removeClass("on");
			$(this).addClass("on");
			if($(".ul1 li:eq(0)").hasClass("on")){
				$("iframe").attr("src","biaozhun.html");
			}
            if($(".ul1 li:eq(1)").hasClass("on")){
            	 $("iframe").attr("src","zhishi.html");
            }
			if($(".ul1 li:eq(2)").hasClass("on")){
				$("iframe").attr("src","lianxiang.html");
			}

			if($(".ul1 li:eq(3)").hasClass("on")){
				$("iframe").attr("src","kuozhan.html");
			}

            if($(".ul1 li:eq(4)").hasClass("on")){
                $("iframe").attr("src","chatRecord.html");
            }

			if($(".ul1 li:eq(5)").hasClass("on")){
				$("iframe").attr("src","sysuser.html");
			}
			if($(".ul1 li:eq(6)").hasClass("on")){
				$("iframe").attr("src","tongji.html");
			}
			if($(".ul1 li:eq(7)").hasClass("on")){
				$("iframe").attr("src","muban.html");
			}
		});
		
		$(".passWord").on("click",function(){
			$(".editP").show();
		});
		
		//添加知识点
//		$($(".addK").find(".on")).on("click",function(){
		$(".addKnows").on("click",function(){
			var name= $.trim($(".addKnowss").val());
			var kurl= $.trim($(".addurl").val());
			if(name==null || name == ""){
				alert("请输入名称！！");
				return;
			}

			var types =$("#types").val();  //0添加知识点    1添加栏目
			var parentId = $("#parentId").val();
			var urls ="";
			
			if(types==0){
				urls = "/eprobot/know/addKnows.action";
			}else{
				if(kurl==null || kurl == ""){
					alert("请输入分类访问地址！！");
					return;
				}
				var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
				if(!reg.test(kurl)){
					alert("不是正确的访问地址，请重新输入!");
				    return;            
				}
				urls = "/eprobot/chnl/addChnl.action?kurl="+kurl;
			}
			$.ajax({
				type:"post",
				url:urls,
				data:{"name":name,"channelId":parentId},
				dataType:"json",
				success:function(data){
					if(data.results=="SUCCESS"){
						alert("添加成功！！！");
						$(".addK").hide();
						var obj = {};
						obj.channelName = name;
						obj.chnlId = data.chnlId;
						obj.parentId = parentId;
//						$("iframe")[0].contentWindow.pageNavCallBack(1);
						//$("iframe")[0].contentWindow.getData(1,parentId);
						if(types!=0)
							$("iframe")[0].contentWindow.appendChannel(obj);
						else
							$("iframe")[0].contentWindow.getData(1,parentId);
						//$(".addsuccess").show();
					}else{
						alert("添加失败！！！");
					}
					$(".addK").hide();
					//$("iframe")[0].contentWindow.getData(1,parentId);
					$(".addKnowss").val("");
				}
			});

		});
		$($(".editK li")[1]).on("click",function(){
			$(".editK").hide();
		});
		$(".editK .close").on("click",function(){
			$(".editK").hide();
		});
		$($(".addK li")[1]).on("click",function(){
			$(".addK").hide();
		});
		$(".modelAskMainK .close").on("click",function(){
			$(".addK").hide();
		});
		
		//安全退出
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
		 
		 
		//修改密码	
			 $("#updatePwd").on("click",function(){
				var oldPwd = $("#oldPwd").val();
				var newPwd = $("#newPwd").val();
				var twonewPwd = $("#twonewPwd").val();
				if(oldPwd==null || oldPwd=="" || newPwd==null || newPwd=="" &&twonewPwd==null || twonewPwd==""){
					alert("密码不能为空");
					return false;
				}
				if(newPwd != twonewPwd){
					alert("两次密码必须一致！");
					return  false;
				}
				$.ajax({
			        url:"/eprobot/user/updatePwd",
			        data:{"oldPwd":oldPwd,"newPwd":newPwd,"ids":"1"},
			        dataType:'json',
			        success:function(data){
			          if(data.results=="ERROR" || data.results == 'error'){
			        	  alert("修改失败！");
						  return false;
			          }else if(data.results=="oldPwdError"){
			        	  alert("原密码输入错误");
						  return false;
			          }else if(data.results=="succ"){
			        	  $(".editP").hide();
			        	  alert("修改成功");
			          }
			        }
				});
			}); 
		
			
			 
			//修改问题
			$("#edt_qa").click(function(){
				var edt_qaQuestion=$(".edit_name").val();
				var edt_qaAnswer=$(".edit_answer").val();  
				var edt_knowledgePoint = $("#edt_qaKnowledgePoint").val();
				var edt_kewwords = $(".keyword1").val();
				var resources1 = $(".resources1").val();
				var id = $(".edit_name").attr("ids");
				if(edt_qaQuestion != null && edt_qaQuestion !=""  && edt_knowledgePoint != null && edt_knowledgePoint !="" && edt_kewwords != null && edt_kewwords !="" && resources1 !=null && resources1!=""&&edt_knowledgePoint!="-10"){
					if($(".radio_xgwt:checked").val() == 1){
						if(!(edt_qaQuestion.length >0)){
							alert("请输入答案！！");
							return;
						}
					}else{
						if(!($(".qafile").val().length) > 0 ){
							alert("请选择上传文件！！");
							return;
						}
					}
					var from = $("#editAs");
					from.append("<input  type='hidden' name='id' value='"+id+"'>");
					var formData = new FormData(from[0]);
					$.ajax({
						type:"post",
						url:"/eprobot/questionAnswer/updateQuestionAnswerById",
						//data:{"id":id,"qaQuestion":edt_qaQuestion,"qaAnswer":edt_qaAnswer,"qaKnowledge":edt_knowledgePoint,"kewwords":edt_kewwords,"resources1":resources1},
						data:formData,
						dataType:"json",
						processData: false ,
			    		contentType : false,
						async: false,
						success:function(data){
							if(data.status==1){
								alert("修改成功！");
								$(".editE").hide();
								
								$("iframe")[0].contentWindow.getData();
								//window.history.go(0);
							}else{
								alert(data.result);
								$(".editE").hide();
								return;
							}
						}
					});
				}else{
					alert("请填完所有选项！");
					return false;
				}
				
			});
			
			//关闭或x
			$(".guanbi1").click(function(){
				$(".addQafl").val("");
				$("#qaKnowledgePoint").empty();
				$(".editE").hide();
				$(".addE").hide();
				$(".details").hide();
			});
			
			//关闭或x
			$(".guanbi2").click(function(){
				$(".editU").hide();
				$(".addU").hide();
			});
			//关闭或x
			$(".guanbi3").click(function(){
				$(".editP").hide();
			});
			//关闭或x
			$(".guanbi4").click(function(){
				$(".piLiang").hide();
			});
			//关闭或x
			$(".guanbi5").click(function(){
				$(".lm").hide();
			});
			
			$(".guanbi7").click(function(){
				$(".setQafl").val("");
				$(".editR").hide();
			});
			
			//添加问题
			$(".saveQa").click(function(){
				
				
				var add_qaQuestion=$("#qaQuestion").val();
		    	var add_qaAnswer=$("#qaAnswer").val();  
		    	var add_knowledgePoint = $("#qaKnowledgePoint").val();
		    	var add_kewwords = $("#kewwords").val();
		    	var resource = $("#resource").val();
		    	
		    	
		    	if(add_qaQuestion==null || add_qaQuestion == ""){
		    		alert("请输入问题！！");
		    		return;
		    	}
		    	
		    	
		    	if($('input:radio[name="fi"]:checked').val() ==1){
		    		if(add_qaAnswer==null || add_qaAnswer == ""){
			    		alert("请输入答案！！");
			    		return;
			    	}
		    	}else{
		    		var filepath = $(".radio_div input").val();
		    		if(filepath==null || filepath == ""){
			    		alert("请选择上传的文件！！");
			    		return;
			    	}
		    		
		    	}
		    	
		    	if(add_knowledgePoint==null || add_knowledgePoint == "" || add_knowledgePoint=="-10"){
		    		alert("请输入知识点！！");
		    		return;
		    	}
		    	if(add_kewwords==null || add_kewwords == ""){
		    		alert("请输入关键字！！");
		    		return;
		    	}
		    	if(resource==null || resource == ""){
		    		alert("请输入来源！！");
		    		return;
		    	}
		    	var formData = new FormData($("#addQuestion")[0]);
		    	/*var formData = new FormData();
		    	formData.append("qaQuestion",add_qaQuestion);
		    	formData.append("qaAnswer",add_qaAnswer);
		    	formData.append("qaKnowledgePoint",add_knowledgePoint);
		    	formData.append("kewwords",add_kewwords);
		    	formData.append("resource",resource);*/
		    	
		    	
		    	$.ajax({
		    		type:"post",
		    		url:"/eprobot/questionAnswer/saveQuestionAnswer.action",
		    	//	data:{"qaQuestion":add_qaQuestion,"qaAnswer":add_qaAnswer,"qaKnowledgePoint":add_knowledgePoint,"kewwords":add_kewwords,"resource":resource},
		    		data:formData,
		    		dataType:"json",
		    		processData: false ,
		    		contentType : false,
		    		success:function(data){
		    			if(data.status==1){
		    				$(".addE").hide();
		    				alert("添加成功！");
		    				//window.history.go(0);
							$("iframe")[0].contentWindow.getData();
							$("#qaQuestion").val("");
					    	$("#qaAnswer").val("");  
					    	$("#kewwords").val("");
					    	$("#resource").val("");
					    	$(".radio_div input").val("");
		    			}else {
		    				alert(data.msg);
		    			}
		    		}
		    	});
				
			});
			
			//记录设置为标准问题
			$(".edt_re").click(function(){
				var add_qaQuestion=$(".edit_record").val();
		    	var add_qaAnswer=$(".recordanswer").val();  
		    	var add_knowledgePoint = $("#edt_recordknow").val();
		    	var add_kewwords = $(".recordkey").val();
		    	var recordresource = $(".recordresource").val();
		    	if(add_qaQuestion==null || add_qaQuestion == ""){
		    		alert("请输入问题！！");
		    		return;
		    	}
		    	if(add_qaAnswer ==null || add_qaAnswer == ""){
		    		alert("请输入答案！！");
		    		return;
		    	}
		    	if(add_knowledgePoint==null || add_knowledgePoint == "" || add_knowledgePoint=="-10"){
		    		alert("请输入知识点！！");
		    		return;
		    	}
		    	if(add_kewwords==null || add_kewwords == ""){
		    		alert("请输入关键字！！");
		    		return;
		    	}
		    	if(recordresource==null || recordresource == ""){
		    		alert("请输入来源！！");
		    		return;
		    	}
		    	var recordId = $(".recordId").val();
		    	var form = $("#editRecord");
		    	var formData = new FormData(form[0]);
		    	$.ajax({
		    		type:"post",
		    		url:"/eprobot/questionAnswer/saveQuestionAnswer.action",
		    	//	data:{"qaQuestion":add_qaQuestion,"qaAnswer":add_qaAnswer,"qaKnowledgePoint":add_knowledgePoint,"kewwords":add_kewwords,"resource":recordresource,"recordId":recordId},
		    		data:formData,
		    		dataType:"json",
		    		processData: false ,
		    		contentType : false,
		    		success:function(data){
		    			if(data.status== 1){
		    				$(".editR").hide();
		    				alert("设置成功！");
							$("iframe")[0].contentWindow.selectedbtn();
		    				//window.history.go(0);
		    			}else{
		    				alert(data.msg);	
		    			}
		    		}
		    	});
			});
			
		$($(".editK li")[0]).click(function(){
			var edit_name=$(".updateKnows").val();
			var chnlId = $("#parentId").val();

			var ids = knowlegeid;
			var types = $("#types").val(); //0修改知识点  1修改栏目
			var urls = "",chnlurl="";
			
			if(types==0){
				urls ="/eprobot/know/updateKnows.action";
			}else{
				chnlurl = $.trim($(".updateurl").val());
				var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
				if(!reg.test(chnlurl)){
					alert("不是正确的访问地址，请重新输入!");
				    return;            
				}
				urls ="/eprobot/chnl/updateChnl.action?chnlurl="+chnlurl;
			}
		
			$.ajax({
				type:"post",
				url:urls,
				data:{"edit_name":edit_name,"edit_channelId":chnlId,"id":ids},
				dataType:"json",
				success:function(data){
					if(data.results =="SUCCESS"){
						alert("修改成功！！！");
						if(types!=0){
							$("iframe")[0].contentWindow.editKnowldge(edit_name,chnlurl);
						}else{
							$("iframe")[0].contentWindow.getData(1,chnlId);
						}

						$(".editK").hide();
						//	getData(1);//默认第一页
					}else{
						alert("修改失败！！！");
					}
					$(".editK").hide();
					//if(types!=0)
					//	$("iframe")[0].contentWindow.getData(1,chnlId);
				}
			});
		});
		
		//添加用戶
		
	    
		//修改用戶
	    $("#updateUser").click(function(){
	    	var edit_name=$("#edit_name").val();
	    	var roleId=$("#edt_roleId").val();
	    	var ids = $("#hidden_id").val();
	    	$.ajax({
	    		type:"post",
	    		url:"/eprobot/user/updateUser.action",
	    		data:{"edit_name":edit_name,"edt_roleId":roleId,"ids":ids},
	    		dataType:"json",
	    		success:function(data){
	    			if(data.results =="succ"){
	    				alert("修改成功！！！");
	    				$(".editU").hide();
						$("iframe")[0].contentWindow.getData(1);
	    			}else{
	    				alert("修改失败！！！");
	    			}
	    		}
	    	});
	    });
	    
	    //点击添加问题中的分类
	    $(".addQafl").click(function(){ 
	    	$(".addQafl").val("");
	    	getAllChanell(2); //1代表点击标准问题的分类结构   2代表点击的是添加问题中的分类   3修改问题中的分类   4记录设为问题中的分类
			$(".lm").show();
	    });
	    
	    //点击修改问题中的分类
	    $(".updateQafl").click(function(){
	    	$(".updateQafl").val("");
	    	getAllChanell(3); //1代表点击标准问题的分类结构   2代表点击的是添加问题中的分类
			$(".lm").show();
	    });
	    
	    //点击记录设问题中的分类
	    $(".setQafl").click(function(){
	    	$(".setQafl").val("");
	    	getAllChanell(4); //1代表点击标准问题的分类结构   2代表点击的是添加问题中的分类
			$(".lm").show();
	    });
		//热词管理关闭
		$(".addR .close").click(function(){
			$(".addR").hide();
		})
		$(" .guanbi6").click(function(){
			$(".addR").hide();
		})
		$(".editRC .close").click(function(){
			$(".editRC").hide();
		})
		$(" .guanbi7").click(function(){
			$(".editRC").hide();
		})
	});

function islogin(){
	$.ajax({
		type:'post',
		url:"/eprobot/user/isLogin.action",
		dataType:"json",
		success:function(data){
			if(!data.results){
				if(data.roleId=="0"){
					$(".users").show();
				}
				$(".userNamess").text(data.userName);
			}
		}
	});
}
function editKnowlegeShow(id,title,chnlId,chnlurl){
	$("#parentId").val(chnlId);
	knowlegeid = id;
	$(".updateKnows").val(title);
	$(".updateurl").val(chnlurl);
	$(".editK").show();
}

function saveUser(){
	var name=$("#name").val();
	var pwd=$("#pwd").val();
	var twoPwd=$("#twoPwd").val();
	var email=$("#email").val();
	var roleId=$("#roleId").val();
	if(name==null || name == ""){
		alert("请输入名称！！");
		return;
	}
	if(pwd!=twoPwd){
		alert("两次密码不一致！");
		return;
	}
	$.ajax({
		type:"post",
		url:"/eprobot/user/addUser.action",
		data:{"name":name,"pwd":pwd,"email":email,"roleId":roleId},
		dataType:"json",
		success:function(data){
			if(data.results=="success"){
				alert("添加成功！！！");
				$(".addU").hide();
				$("iframe")[0].contentWindow.getData(1);
			}else{
				alert("添加失败！！！");
			}
		},error:function(e){
			console.log(JSON.stringify(e));
		}
	});
};


//第一級欄目
function firstChnles(chList){
	$("#lm_cont_01 ul").empty();
	if(chList.length>0){
		for(var i=0;i<chList.length;i++){
			if(chList[i].parentid=="0"){
				var li = $("<li></li>");
				li.attr("docchnl",chList[i].channelId);
				li.attr("chnlName",chList[i].chnlname);
				li.on("mouseover",function(){
					var docchnl = $(this).attr("docchnl");
					$("#lm_cont_02").show();
					$("#lm_cont_03").hide();
					twoChnles(docchnl);
				});
				
				li.on("dblclick",function(){
					if(flType==1){//标准问题中的分类结构
						var docchnl = $(this).attr("docchnl");
						$(".lm").hide();
						$("iframe")[0].contentWindow.chnlIds = docchnl;
						$("iframe")[0].contentWindow.getData();
					}else if(flType==2){//添加问题中的分类知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".addQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//添加问题
										var opt=$("<option></option>");
										opt.append(list[i].kname);
										opt.attr("value",list[i].id);
										$("#qaKnowledgePoint").append(opt);
									}
								}
							}
						});
					}else if(flType==3){//修改问题中的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".updateQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//修改问题
										var opt2=$("<option></option>");
										opt2.append(list[i].kname);
										opt2.attr("value",list[i].id);
										$("#edt_qaKnowledgePoint").append(opt2);

									}
								}
							}
						});
					}else if(flType==4){ //记录设置为问题时的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".setQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_recordknow").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_recordknow").append(opt1);
									for(var i=0;i<list.length;i++){
										//记录
										var opt1=$("<option></option>");
										opt1.append(list[i].kname);
										opt1.attr("value",list[i].id);
										$("#edt_recordknow").append(opt1);
									}
								}
							}
						});
					}
					
				});
				var aa = $("<a href='#'></a>");
				aa.append(chList[i].chnlname);
				li.append(aa);
				$("#lm_cont_01 ul").append(li);
			}
		}
	}
	$("#lm_slide").slide();
}

//查询所有分类
function getAllChanell(types){ //1代表点击的是标准问题中的分类结构   2代表的是点击添加问题中的分类
	flType = types;
	$.ajax({
		type:'post',
		url:"/eprobot/chnl/getAll.action",
		dataType:"json",
		async: false,
		success:function(data){
			if(data.list!=null){
				//console.log(JSON.stringify(data))
				allChnl = data.list;
				firstChnles(allChnl);
			}
		}
	});
}

function getKnowledge(knowId){
	/**
	 * 查询知识点
	 */
	$.ajax({
		type:"post",
		url:"/eprobot/know/getKnowAll.action",
		dataType:"json",
		//data:queryString,
		success:function(data){
			if(data.list){ //!=null
				var list = data.list;
				KnowAllList = list;
				$("#edt_qaKnowledgePoint").empty();
				$("#edt_recordknow").empty();
				//$("#qaKnowledgePoint").empty();
				if(list.length>0){
					//拼接知识点
					for(var i=0;i<list.length;i++){
						if(list[i].id==knowId){
							var opt2=$("<option></option>");
							opt2.append(list[i].kname);
							opt2.attr("value",list[i].id);
							$("#edt_qaKnowledgePoint").append(opt2);
							$(".updateQafl").val("");
						}else if(list[i].kname==knowId){
							var opt2=$("<option></option>");
							opt2.append(list[i].kname);
							opt2.attr("value",list[i].id);
							$("#edt_recordknow").append(opt2);
							$(".setQafl").val("");
						}
						/*//添加问题
						var opt=$("<option></option>");
						opt.append(list[i].kname);
						opt.attr("value",list[i].id);
						$("#qaKnowledgePoint").append(opt);
						//记录
						var opt1=$("<option></option>");
						opt1.append(list[i].kname);
						opt1.attr("value",list[i].id);
						$("#edt_recordknow").append(opt1);*/
						//修改问题
					}
				}
			}
		}
	});

}


//第二級欄目
function twoChnles(parentId){
	$("#lm_cont_02 ul").empty();
	if(allChnl.length>0){
		for(var i=0;i<allChnl.length;i++){
			if(allChnl[i].parentid==parentId){
				var li = $("<li></li>");
				li.attr("docchnl",allChnl[i].channelId);
				li.attr("chnlName",allChnl[i].chnlname);
				li.on("mouseover",function(){
					var docchnl = $(this).attr("docchnl");
					$("#lm_cont_03").show();
					threeChnles(docchnl);
				});
				
				li.on("dblclick",function(){
					if(flType==1){//标准问题中的类别
						var docchnl = $(this).attr("docchnl");
						$(".lm").hide();
						$("iframe")[0].contentWindow.chnlIds = docchnl;
						$("iframe")[0].contentWindow.getData();
					}else if(flType==2){//添加问题中的分类知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".addQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//添加问题
										var opt=$("<option></option>");
										opt.append(list[i].kname);
										opt.attr("value",list[i].id);
										$("#qaKnowledgePoint").append(opt);
									}
								}
							}
						});
					}else if(flType==3){//修改问题中的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".updateQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//修改问题
										var opt2=$("<option></option>");
										opt2.append(list[i].kname);
										opt2.attr("value",list[i].id);
										$("#edt_qaKnowledgePoint").append(opt2);

									}
								}
							}
						});
					}else if(flType==4){ //记录设置为问题时的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".setQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_recordknow").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_recordknow").append(opt1);
									for(var i=0;i<list.length;i++){
										//记录
										var opt1=$("<option></option>");
										opt1.append(list[i].kname);
										opt1.attr("value",list[i].id);
										$("#edt_recordknow").append(opt1);
									

									}
								}
							}
						});
					}
				});
				
				var aa = $("<a href='#'></a>");
				aa.append(allChnl[i].chnlname);
				li.append(aa);
				$("#lm_cont_02 ul").append(li);
			}
		}
	}
	$("#lm_slide").slide();
}

//第三級欄目
function threeChnles(parentId){
	$("#lm_cont_03 ul").empty();
	if(allChnl.length>0){
		for(var i=0;i<allChnl.length;i++){
			if(allChnl[i].parentid==parentId){
				var li = $("<li></li>");
				li.attr("docchnl",allChnl[i].channelId);
				li.attr("chnlName",allChnl[i].chnlname);
				li.on("dblclick",function(){
					if(flType==1){//标准问题中的类别
						var docchnl = $(this).attr("docchnl");
						$(".lm").hide();
						$("iframe")[0].contentWindow.chnlIds = docchnl;
						$("iframe")[0].contentWindow.getData();
					}else if(flType==2){//添加问题中的分类知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".addQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//添加问题
										var opt=$("<option></option>");
										opt.append(list[i].kname);
										opt.attr("value",list[i].id);
										$("#qaKnowledgePoint").append(opt);
									}
								}
							}
						});
					}else if(flType==3){//修改问题中的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".updateQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_qaKnowledgePoint").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_qaKnowledgePoint").append(opt1);
									for(var i=0;i<list.length;i++){
										//修改问题
										var opt2=$("<option></option>");
										opt2.append(list[i].kname);
										opt2.attr("value",list[i].id);
										$("#edt_qaKnowledgePoint").append(opt2);

									}
								}
							}
						});
					}else if(flType==4){ //记录设置为问题时的知识点
						var docchnl = $(this).attr("docchnl");
						var chnlName = $(this).attr("chnlName");
						$(".lm").hide();
						$(".setQafl").val(chnlName);
						$.ajax({
							url:"/eprobot/know/getKnowAllByChnlId.action",
							type:"post",
							data:{"chnlId":docchnl},
							dataType:"json",
							success:function(data){
								var list=data.list;
								$("#edt_recordknow").empty();
								if(list.length>0){
									var opt1 = $("<option value='-10'></option>");
									opt1.append("--请选择--");
									$("#edt_recordknow").append(opt1);
									for(var i=0;i<list.length;i++){
										//记录
										var opt1=$("<option></option>");
										opt1.append(list[i].kname);
										opt1.attr("value",list[i].id);
										$("#edt_recordknow").append(opt1);
									

									}
								}
							}
						});
					}
				});
				
				var aa = $("<a href='#'></a>");
				aa.append(allChnl[i].chnlname);
				li.append(aa);
				$("#lm_cont_03 ul").append(li);
			}
		}
	}
	$("#lm_slide").slide();
}

function addRC(){
	var title= $.trim($(".addR .cRc").val());
	var content= $.trim($(".addR #r1").val());
	
	if($('input:radio[name="rc"]:checked').val() ==1){
		if(content==null || content == ""){
			alert("请输入内容！");
    		return;
    	}
	}else{
		var filepath = $(".radio_div_r input").val();
		if(filepath==null || filepath == ""){
    		alert("请选择上传的文件！！");
    		return;
    	}
		
	}
	
	if(title==""){
		alert("请输入标题！");
		return;
	}
	else {
		var tempUrl = '/eprobot/individualWord/updataOrSave';
		//var queryString = {"qaQuestion":title,"qaAnswer":content};
		var formData = new FormData($('#addRC')[0]);
		$.ajax({
			type: 'post',
			url: tempUrl,
			data: formData,
			dataType: "json",
			processData: false ,
    		contentType : false,
			cache: false,
			success: function (data) {
				//console.log(JSON.stringify(data))
				if(data.status==1){
					alert(data.msg);
					$(".addR").hide();
					$(".addR .cRc").val("");
					$(".radio_div_r input").val("");
					
					$("iframe")[0].contentWindow.pageNavCallBack(1);
				}
				else {
					alert(data.msg)
				}
			}
		});
	}
}

//修改热词
function rcChange(){
	var title= $.trim($(".editRC .cRc").val());
	var content= $.trim($(".editRC #r3").val());
	
	if(title==""){
		alert("请输入标题！");
		return;
	}
	
	if($(".radio_xgrc:checked").val() == 0){
		if(content==""){
			alert("请输入内容！");
			return;
		}
	}
	
	if($(".radio_xgrc:checked").val() == 1){
		if($("#rcFile").val().length == 0){
			alert("请选择要上传的文件！");
			return;
		}
	}
	
	
		var tempUrl = '/eprobot/individualWord/updataOrSave';
		//var form = $("#xgRC");
		var formData = new FormData($("#xgRC")[0]);
		$.ajax({
			type: 'post',
			url: tempUrl,
			data: formData,
			dataType: "json",
			processData: false ,
    		contentType : false,
			cache: false,
			success: function (data) {
				//console.log(JSON.stringify(data))
				if(data.status==1){
					alert(data.msg);
					$(".editRC").hide();
					var pageN=$("iframe")[0].contentWindow.currentPage;
					$("iframe")[0].contentWindow.getData(pageN);
				}
				else {
					alert(data.msg)
				}
			}
		});
	
}



