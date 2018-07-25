var queryName = "";//保存搜索条件
var pageSize=15;//每页数量
var pageNavObj;
var totalPage;
var currentPage=1;
//欄目總條數
var chnlId="";
var obj ={};
var selectChanne,dragobj;
var allData=0;
$(function(){
	loadChannel();

	parent.$(".guanbi6").click(function(){
		parent.$(".knowjg").hide();

	});

	$(".delChnl").click(function(){
		var chnlId = $("#parentId").val();
		if(obj[chnlId]==undefined || obj[chnlId] =='undefined'){
			if(allData==0){
				if(confirm("是否确定删除??")){
					$.ajax({
						type:"post",
						url:"/eprobot/chnl/delChnlById.action",
						data:{"id":chnlId},
						dataType:"json",
						success:function(data){
							if(data.results=="SUCCESS"){
								$("#parentId").val("");
								if($($(selectChanne).parent().parent()).find("li").length==1){
									$($(selectChanne).parent().parent().parent().find("a")).removeClass();
									$(selectChanne).parent().parent().remove();
								}else{
									$(selectChanne).parent().remove();
								}
							}else{
								alert("删除失败！！！");
							}
						}
					});
				}
			}else{
				alert("该类别有数据不能删除");
			}
		}else{
			alert("有子栏目不能被删除！！");
		}
	});

	$(".updateChnl").click(function(){
		var chnlId = $("#parentId").val();
		var str = $("#chnlname").val();
		var chnlurl = $("#chnlurl").val();

		var chnlIdss = "";
		parent.$("#types").val(1);
		parent.editKnowlegeShow(chnlId,str,chnlIdss,chnlurl);
	});


	$(".addChnl").on("click",function(){
		if($("#parentId").val().length>0) {
			$(parent.$(".addK")).find(".title").text("添加分类");
			$($(parent.$(".addK")).find(".text")[0]).text("请输入类别名称");
			parent.$("#types").val(1);
			parent.$(".addK").show();
			parent.$("#parentId").val($("#parentId").val());
		}else{
			alert("请选择分类名称");
		}
	});
	$(".tj").on("click",function(){
		if($("#parentId").val().length>0 && $("#parentId").val()!=0) {
			$(parent.$(".addK")).find(".title").text("添加知识点");
			$($(parent.$(".addK")).find(".text")[0]).text("请输入知识点名称");
			parent.$(".addK").show();
			parent.$("#types").val(0);
			parent.$("#parentId").val($("#parentId").val());
		}else{
			alert("请选择分类名称");
		}

	});
	getData(1,chnlId);//默认第一页
	$(".btn").click(function(){
		getData(1,chnlId);
	});

});

function loadChannel(){
	$.ajax({
		type:'post',
		url:"/eprobot/chnl/getAll.action",
		dataType:"json",
		async: false,
		success:function(data){
			$(".allknow ul").empty();
			if(data.results!=null){
				for(var i=0;i<data.list.length;i++){
					if(!obj[data.list[i].parentid]){
						obj[data.list[i].parentid] = [];
					}
					if(obj[data.list[i].parentid].indexOf(data.list[i])==-1)
						obj[data.list[i].parentid].push(data.list[i]);
				}
				var ul = commMethod(0,obj);
				$(".allknow").append(ul);
				$(".zs_menu ul li").menu();
				$(".allknow a").click();
			}
		}
	});
}


function allowDrop(ev)
{
	ev.preventDefault();
}

function drag(ev)
{
	dragobj = $(ev.target);
}

function drop(ev)
{
	ev.preventDefault();
	$(ev.target).css("background-color","#fff");
	if(!dragobj) return;
	if(!$(dragobj).find("a")){
		var li = $("<li></li>");
		li.addClass(""+$(dragobj).attr("docchnlId"));
		li.append("<a href='#'></a>");
		li.append($(dragobj));
		dragobj = li;
	}

	if($(ev.target).attr("pid")==0){
		$("."+$($(dragobj).find("span")[0]).attr("docchnlId")).remove();
	}

	var ul1 = $($(ev.target).parent()[0]).find("ul");
	if(ul1.length>0){
		$(ul1[0]).append(dragobj);
	}else{
		var ul = $("<ul></ul>").css("display","block");
		$($(ev.target).parent()[0]).append(ul);
		ul.append(dragobj);
	}
	var uls = $(".zs_menu ul li").find("ul");
	uls.each(function(k,e){
		if($(e).find("li").length==0){
			$(e).parent().find("a").removeAttr("class");
			$(e).remove();
		}


	});

	$(".zs_menu ul li").menu();
	if($($(".allknow a")[0]).hasClass("inactive")){
		$($(".allknow a")[0]).removeClass();
		$($(".allknow a")[0]).addClass("active");
	}
	if($($($(ev.target).parent()[0]).find("a")[0]).hasClass("inactive"))
	{
		$($($(ev.target).parent()[0]).find("a")[0]).removeClass();
		$($($(ev.target).parent()[0]).find("a")[0]).addClass("active");
	}
	if($($($(ev.target).parent().parent().parent()[0]).find("a")[0])&&$($($(this).parent().parent().parent()[0]).find("a")[0]).hasClass("inactive")){
		$($($(ev.target).parent().parent().parent()[0]).find("a")[0]).removeClass();
		$($($(ev.target).parent().parent().parent()[0]).find("a")[0]).addClass("active");
	}

	channelPidById($($(dragobj).find("span")[0]).attr("docchnlid"),$(ev.target).attr("docchnlId"));
	dragobj = null;
}

function commMethod(parentId,arrs){
	var ul = $("<ul></ul>").css("display","block");
	if(arrs[parentId].length>0){
		for(var i=0;i<arrs[parentId].length;i++){
			var arr = arrs[parentId];
			var li = $("<li></li>");
			li.addClass(""+arr[i].channelId);
			if(arr[i].parentid!=0){
				li.attr("draggable","true");
				li.attr("ondragstart","drag(event)");
			}
			var a1=$("<a href='#'></a>");
			li.append(a1);
			var aa = $("<span></span>");//.css("cursor","pointer");
			/*aa.attr("ondragover","allowDrop(event)");
			aa.attr("ondrop","drop(event)");*/
            var chnl = arr[i].chnlname;
			if(chnl.length>9)
			   chnl = chnl.substring(0,9)+"..";
			aa.append(chnl);

			aa.attr("docchnlId",arr[i].channelId);
			aa.attr("pid",arr[i].parentid);
			aa.attr("chnlurl",arr[i].chnlurl);
			aa.attr("title",arr[i].chnlname);
			aa.on("click",function(){ // dblclick双击
				var span = $(".zs_menu").find("span");
				span.each(function() {
					if ($(this).attr("style")) {
						$(this).removeAttr("style");
					}
				});
				$(this).css("color","#217CD5");
				var docchnlId = $(this).attr("docchnlId");
				var chnlname = $(this).attr("title");
				var chnlurl = $(this).attr("chnlurl");
				chnlId = docchnlId;
				$("#parentId").val(docchnlId);
				$("#chnlname").val(chnlname);
				$("#chnlurl").val(chnlurl);
				selectChanne = this;
				parent.$("#parentId").val(docchnlId);
				getData(1,docchnlId);
			});
			li.append(aa);
			aa.on("dragover",function(evt){
				$(this).css("background-color","green");
				event.preventDefault();
			});
			aa.on("dragleave", function(event) {
				$(this).css("background-color","#fff");
				event.preventDefault();
			});
			var parentids =arr[i].channelId || arr[i].docchnlId;
			if(arrs[parentids]){
				var ul1 = commMethod(parentids,arrs);
				li.append(ul1);
			}
			ul.append(li);
		}
		return ul;
	}else{
		return null;
	}

}

function channelPidById(thisid,pid){
	$.ajax({
		type:"post",
		url:"/eprobot/chnl/upChannelPidById.action",
		data:{"id":thisid,"pid":pid},
		dataType:"json",
		success:function(data){
			if(data.result!="success"){
				alert("拖动分类失败");
				window.location.reload();
			}


		}
	});
}
//添加
function appendChannel(thisobj){
	var ul = $($(selectChanne).parent()[0]).find("ul");
	if(!selectChanne){
		selectChanne = $(".allknow span")[0];
	}
	if(ul.length>0){
		var pid = thisobj.parentId;
		var li = $("<li></li>");
		li.addClass(""+thisobj.chnlId);
		li.attr("draggable","true");
		li.attr("ondragstart","drag(event)");
		var a1=$("<a href='#'></a>");
		li.append(a1);
		var aa = $("<span></span>").css("cursor","pointer");
		aa.append(thisobj.channelName);
		li.append(aa);
		aa.attr("docchnlId",thisobj.chnlId);
		aa.attr("chnlname",thisobj.channelName);
		aa.attr("ondragover","allowDrop(event)");
		aa.attr("ondrop","drop(event)");
		aa.on("click",function(){ // dblclick双击
			var span = $(".zs_menu").find("span");
			span.each(function() {
				if ($(this).attr("style")) {
					$(this).removeAttr("style");
				}
			});
			$(this).css("color","#217CD5");
			chnlId = thisobj.chnlId;
			$("#parentId").val(thisobj.chnlId);
			$("#chnlname").val(thisobj.channelName);
			selectChanne = this;
			parent.$("#parentId").val(thisobj.chnlId);
			getData(1,thisobj.chnlId);
		});
		$(ul[0]).append(li);
	}else{
		var ul1 = $("<ul></ul>").css("display","block");
		var li = $("<li></li>");
		li.addClass(""+thisobj.chnlId);
		li.attr("draggable","true");
		li.attr("ondragstart","drag(event)");
		var a1=$("<a href='#'></a>");
		li.append(a1);
		var aa = $("<span></span>").css("cursor","pointer");
		aa.append(thisobj.channelName);
		li.append(aa);
		aa.attr("docchnlId",thisobj.chnlId);
		aa.attr("chnlname",thisobj.channelName);
		aa.attr("ondragover","allowDrop(event)");
		aa.attr("ondrop","drop(event)");
		aa.on("click",function(){ // dblclick双击
			var span = $(".zs_menu").find("span");
			span.each(function() {
				if ($(this).attr("style")) {
					$(this).removeAttr("style");
				}
			});
			aa.css("color","#217CD5");
			chnlId = thisobj.chnlId;
			$("#parentId").val(thisobj.chnlId);
			$("#chnlname").val(thisobj.channelName);
			selectChanne = this;
			parent.$("#parentId").val(thisobj.chnlId);
			getData(1,thisobj.chnlId);
		});
		ul1.append(li);
		$($(selectChanne).parent()[0]).append(ul1);
		$(".zs_menu ul li").menu();
		if($($(".allknow a")[0]).hasClass("inactive")){
			$($(".allknow a")[0]).removeClass();
			$($(".allknow a")[0]).addClass("active");
		}
		if($($($(selectChanne).parent()[0]).find("a")[0]).hasClass("inactive"))
		{
			$($($(selectChanne).parent()[0]).find("a")[0]).removeClass();
			$($($(selectChanne).parent()[0]).find("a")[0]).addClass("active");
		}
		//$($($(selectChanne).parent().parent().parent()[0]).find("a")[0])
		if($($($(selectChanne).parent().parent().parent()[0]).find("a")[0])&&$($($(selectChanne).parent().parent().parent()[0]).find("a")[0]).hasClass("inactive")){
			$($($(selectChanne).parent().parent().parent()[0]).find("a")[0]).removeClass();
			$($($(selectChanne).parent().parent().parent()[0]).find("a")[0]).addClass("active");
		}

	}

}

//修改
function  editKnowldge(chanellName,chnlurl){

	if($(selectChanne).attr("chnlname")) {
		$(selectChanne).removeAttr("chnlname");
		$(selectChanne).removeAttr("chnlurl");
	}
	$(selectChanne).attr("chnlname",chanellName);
	$(selectChanne).attr("chnlurl",chnlurl);
	$(selectChanne).html(chanellName);
}
function pageNavCallBack(clickPage){
	pageNavObj = new PageNavCreate("PageNavId",{
		pageCount:totalPage,
		currentPage:clickPage,
		perPageNum:pageSize,
	});
	pageNavObj.afterClick(pageNavCallBack);
	searchPage = clickPage;
	currentPage = searchPage;
	getData(searchPage,chnlId);
}
function allChnl(){
	selectChanne = $(".allknow span")[0];
	var span = $(".zs_menu").find("span");
	span.each(function() {
		if ($(this).attr("style")) {
			$(this).removeAttr("style");
		}
	});
	$(selectChanne).css("color","#217CD5").css("cursor","pointer");
	$("#parentId").val(0);
	getData(currentPage,0)
}
//获取分页数据
function getData(searchPage,chnlId){
	var tempUrl = '/eprobot/know/getKnowAllList.action';
	queryName=$("#searchName").val();
	var queryString = "pageSize="+pageSize+"&pageNumber="+searchPage+"&searchName="+queryName+"&chnlId="+chnlId+"";
	$.ajax({
		type:'post',
		url:tempUrl,
		data:queryString,
		dataType: "json",
		success:function(data){
			$("#listShow").empty();
			$("#PageNavId").empty();
			var dataUl=$("#listShow");
			var list=data.list;
			var total = data.total;
			allData = total;
			var pagecount = Math.ceil(total/pageSize);
			totalPage = pagecount;
			if(data.roleId==0){
				$(".users").show();
			}

			if(pagecount==1){
				$("#PageNavId").html("");
			}else{
				pageNavObj = new PageNavCreate("PageNavId",{pageCount:pagecount,currentPage:searchPage,perPageNum:pageSize});
				pageNavObj.afterClick(pageNavCallBack);

			}
			var htmlStr="";
			for ( var i = 0; i < list.length; i++) {
				var timess = list[i].createTime;
				//var crtimes = new Date(timess).format("yyyy-MM-dd hh:mm:ss");
				var tr = $("<tr></tr>");
				var td1 = $('<td height="44" align="center"></td>');
				td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSize*searchPage-(pageSize-1): pageSize*searchPage-(pageSize-1)+i));
				var td2 = $('<td height="44" align="left" class="newList"></td>');
				td2.attr("id",list[i].id);
				td2.append(list[i].kname);
				var td3 = $('<td align="center"></td>');
				td3.append(timess);
				var td31 = $('<td align="center"></td>');
				td31.append(list[i].userName);
				var td5 = $('<td height="44" align="center"></td>');
				var spanedit = $("<span style='margin-right: 5px;'></span>").css("cursor","pointer");
				spanedit.append("编辑");
				spanedit.attr("chnlId",list[i].chnlId)
				spanedit.on("click",function(){
					var  str = $($($(this).parent().parent()).find("td")[1]).text();
					var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
					var chnlIdss = $(this).attr("chnlId");
					parent.editKnowlegeShow(ids,str,chnlIdss);
				});

				var spandel = $("<span></span>").css("cursor","pointer");
				spandel.append("删除");
				spandel.on("click",function(){
					var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
					if(confirm("是否确定删除！！！")){
						$.ajax({
							type:"post",
							url:"/eprobot/know/delKnowsById.action",
							data:{"id":ids},
							dataType:"json",
							success:function(data){
								if(data.results=="SUCCESS"){
									$("#add_show").hide();
									getData(currentPage,chnlId);
									alert("删除成功！！！");
								}else{
									alert("删除失败！！！");
								}
							}
						});
					}
				});
				td5.append(spanedit).append(spandel);
				tr.append(td1).append(td2).append(td3).append(td31).append(td5);
				$("#listShow").append(tr);
			}
		}
	});
}

Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth()+1,                 //月份
		"d+" : this.getDate(),                    //日
		"h+" : this.getHours(),                   //小时
		"m+" : this.getMinutes(),                 //分
		"s+" : this.getSeconds(),                 //秒
		"q+" : Math.floor((this.getMonth()+3)/3), //季度
		"S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt)) {
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("("+ k +")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		}
	}
	return fmt;
}