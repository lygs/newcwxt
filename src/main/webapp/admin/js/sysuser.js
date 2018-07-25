var queryName = "";//保存搜索条件
var pageSizes=15;//每页数量
$(function(){
	$(".tj").on("click",function(){
    	parent.$(".addU").show();
    });
	
    getData(1);//默认第一页
    $(".btn").on("click",function(){
    	getData(1);
    });
});

//获取分页数据
function getData(searchPage){          
    var tempUrl = '/eprobot/user/getUserAllList.action';
    queryName=$("#searchName").val();
    var queryString = "pageSize="+pageSizes+"&pageNumber="+searchPage+"&searchName="+queryName+"";
    $.ajax({
        type:'post',
        url:tempUrl,
        data:queryString,
        dataType: "json",
        success:function(data){
            $("#listShow").empty();
        	var list=data.list;
            var total = data.total;
            var pagecount = Math.ceil(total/pageSizes);
            if(pagecount==1){
                $("#PageNavId").html("");
            }else{
            	
            }
            var htmlStr="";
            for ( var i = 0; i < list.length; i++) {
            	var tr = $("<tr></tr>");
            	var td1 = $("<td height='50' align='center'></td>");
            	td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSizes*searchPage-(pageSizes-1): pageSizes*searchPage-(pageSizes-1)+i));
            	var td2 = $("<td height='50' align='center'></td>");
            	td2.attr("id",list[i].userId);
            	td2.attr("userName",list[i].userName);
            	td2.append(list[i].userName);
            	var td3 = $("<td height='50' align='center'></td>");
            	td3.append(list[i].createTime);
            	var td4 = $("<td height='50' align='center'></td>");
            	if(list[i].roleId=="0"){
            		td4.append("管理员");
            	}else{
            		td4.append("普通用户");
            	}
            	var td5 = $("<td height='50' align='center'></td>");
            	var spanedit = $("<span style='margin-right: 5px;'></span>").css("cursor","pointer");
            	spanedit.append("编辑  |");
            	spanedit.on("click",function(){
            		var  str = $($($(this).parent().parent()).find("td")[1]).text(); 
            		var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
            		if(str=="admin"){
        	    		alert("超级管理员不能被修改！");
        	    		return;
        	    	}
            		parent.$("#edit_name").val(str);
            		parent.$("#hidden_id").val(ids);
            		parent.$(".editU").show();
            	});
            	
            	var spandel = $("<span></span>").css("cursor","pointer");
            	spandel.append(" 删除");
            	spandel.on("click",function(){
            		var idss =   $($($(this).parent().parent()).find("td")[1]).attr("id");
            		var userNam =   $($($(this).parent().parent()).find("td")[1]).attr("userName");
            		if(userNam=='admin'){
            			alert("超级管理员不能删除！！");
            			return;
            		}
            		if(confirm("是否确定删除！！！")){
            			$.ajax({
            				type:"post",
            				url:"/eprobot/user/deleteUser.action",
            				data:{"ids":idss},
            				dataType:"json",
            				success:function(data){
            					if(data.results=="success"){
            						$("#add_show").hide();
            						getData(1);
//            						window.history.go(0);
            						alert("删除成功！！！");
            					}else{
            						alert("删除失败！！！");
            					}
            				}
            			});
            		}
            	});
            	td5.append(spanedit).append(spandel);
            	tr.append(td1).append(td2).append(td3).append(td4).append(td5);
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