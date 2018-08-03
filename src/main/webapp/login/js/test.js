var pageSize=1000;
$(function(){
	sures();
});

function sures(){
	var pageNumber = $("#pager").val();
	$.ajax({
		url:"/eprobot/questionAnswer/docSync.action",
		dataType:"json",
		data:{"times":1,"pageSize":pageSize,"pageNumber":pageNumber},
		success:function(data){
			console.log(JSON.stringify(data));
			$("span").text("共"+data.allPage+"页");
			if(data.results=="success"){
				alert("添加成功！");
			}
		}
	});
}
function delQuestion(){
	$.ajax({
		url:"/eprobot/questionAnswer/delQuestionAnswerByAnswer.action",
		dataType:"json",
		success:function(data){
			if(data.result=="success"){
				alert("删除成功！");
			}else{
				alert("删除失败！");
			}
		}
	});
}

function reQuestion(){
	$.ajax({
		url:"/eprobot/questionAnswer/findAllCreatDoc.action",
		dataType:"json",
		success:function(data){
			if(data.result=="success"){
				alert("创建完成！");
			}else{
				alert("创建失败！");
			}
		}
	});
}



function sycnChnl(){
	$.ajax({
		url:"/eprobot/questionAnswer/chnlSync.action",
		dataType:"json",
		type:"post",
		success:function(data){
			if(data.results=="success"){
				alert("执行成功！");
			}
		}
	});
}
