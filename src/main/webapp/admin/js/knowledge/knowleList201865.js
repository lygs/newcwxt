var queryName = "";//保存搜索条件
var pageSize=15;//每页数量
var pageNavObj;
var totalPage;
//欄目總條數
var allKnow;
var firstKnow = [];
var twoKnow = [];
var threeKnow = [];
$(function(){
	$(".knowjg").on("click",function(){
		parent.$(".knowjg").show();
		$.ajax({
	        type:'post',
	        url:"/eprobot/know/getKnowAll.action",
	        dataType:"json",
	        async: false,
	        success:function(data){
	        	if(data.list != null){
	        	var list = data.list;
	        	allKnow = list;
	        	if(list.length>0){
	        		parent.$("#know_slide").empty();
	        		var div1 = $("<div class='lm_menu fl hd' id='know_menu'></div>");
	        		var ul = $("<ul></ul>");
	        		var dive = $("<div class='bd'></div>");
	        		var dive2 = $("<div class='list'></div>");
	        		for(var i=0;i<list.length;i++){
	        			//dive.hide();
	        			if(list[i].parentId==0){
	        					var li = $("<li></li>");
	        					li.attr("knowid",list[i].id);
	        					li.on("mouseover",function(){
	        						dive.show();
		        					var knowid = $(this).attr("knowid");
	        						div11 = firstChnles(knowid);
	        						dive.empty();
	        						if(div11!=null){
	        							dive.append(div11);
	        	        			}
	        						console.log(div11);
	        					});
	        					li.on("dblclick",function(){
	        						var knowid = $(this).attr("knowid");
	        						console.log(knowid);
	        					});
	        					var aa = $("<a href='#'></a>");
	        					aa.append(list[i].kname);
	        					li.append(aa);
	        					ul.append(li);
	        				}
	        			}
	        			div1.append(ul);
	        			parent.$("#know_slide").append(div1);
	        		//	dive.append(dive2);
	        			parent.$("#know_slide").append(dive);
	        			
	        		}
	        	}
	        	parent.$("#know_slide").slide();
	        }
		});
	});
	parent.$(".guanbi6").click(function(){
		parent.$(".knowjg").hide();
		
	});
	
	$(".tj").on("click",function(){
		parent.$(".addK").show();
	});
    getData(1);//默认第一页
    $(".btn").click(function(){
    	getData(1);
    });
    
});
function firstChnles(knowId){
	var dive2 = $("<div class='list'></div>");
	if(allKnow.length>0){
		var div3 =$("<div class='lm_cont' id='know_cont_01'></div");
		var div4 =$("<div class='lm_cont' id='know_cont_02'></div");
		var ul = $("<ul></ul>");
		firstKnow = [];
		for(var i=0;i<allKnow.length;i++){
			div4.hide();
			if(allKnow[i].parentId==knowId){
				firstKnow.push(allKnow[i]);
				var li = $("<li></li>");
				li.attr("knowid",allKnow[i].id);
				li.on("mouseover",function(){
					div4.show();
					var knowid = $(this).attr("knowid");
					var div33 = twoChnles(knowid);
					if(div33!=null){
						div4.empty();
						div4.append(div33);
					}
				});
				
				li.on("dblclick",function(){
					var knowid = $(this).attr("knowid");
				});
				var aa = $("<a href='#'></a>");
				aa.append(allKnow[i].kname);
				li.append(aa);
				ul.append(li);
			}
		}
		div3.append(ul);
		dive2.append(div3);
		dive2.append(div4);
		if(firstKnow.length>0){
			return dive2;
		}else{
			return null;
		}
	}
	
	parent.$("#know_slide").slide();
}

function twoChnles(knowId){
	var ul = $("<ul></ul>");
	if(allKnow.length>0){
		twoKnow = [];
		for(var i=0;i<allKnow.length;i++){
			if(allKnow[i].parentId==knowId){
				twoKnow.push(allKnow[i]);
				var li = $("<li></li>");
				li.attr("knowid",allKnow[i].id);
				li.on("dblclick",function(){
					var knowid = $(this).attr("knowid");
				});
				var aa = $("<a href='#'></a>");
				aa.append(allKnow[i].kname);
				li.append(aa);
				ul.append(li);
			}
		}
		
	}
	if(twoKnow.length>0){
		return ul;
	}else{
		return null;
	}
	parent.$("#know_slide").slide();
}
function pageNavCallBack(clickPage){
	pageNavObj = new PageNavCreate("PageNavId",{
		pageCount:totalPage,
		currentPage:clickPage,
		perPageNum:pageSize,
	});
	pageNavObj.afterClick(pageNavCallBack);
	searchPage = clickPage;
	getData(searchPage);
}

//获取分页数据
function getData(searchPage){          
    var tempUrl = '/eprobot/know/getKnowAllList.action';
    queryName=$("#searchName").val();
    var queryString = "pageSize="+pageSize+"&pageNumber="+searchPage+"&searchName="+queryName+"";
    $.ajax({
        type:'post',
        url:tempUrl,
        data:queryString,
        dataType: "json",
//        cache:false,
        success:function(data){
            $("#listShow").empty();
            var dataUl=$("#listShow");
//            if(data.list.length >0){
            	var list=data.list;
                var total = data.total;
                var pagecount = Math.ceil(total/pageSize);
			    totalPage = pagecount;
                if(data.roleId==0){
                	$(".users").show();
                }

				if(pagecount==1){
					$("#PageNavId").html("");
				}else{
					if(!pageNavObj)
						pageNavObj = new PageNavCreate("PageNavId",{pageCount:pagecount,currentPage:searchPage,perPageNum:pageSize});
					pageNavObj.afterClick(pageNavCallBack);

				}
                var htmlStr="";
                for ( var i = 0; i < list.length; i++) {
                	 var timess = list[i].createTime;
                	 var crtimes = new Date(timess).format("yyyy-MM-dd hh:mm:ss");
                	var tr = $("<tr></tr>");
                	var td1 = $('<td height="44" align="center"></td>');
                	td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSize*searchPage-(pageSize-1): pageSize*searchPage-(pageSize-1)+i));
                	var td2 = $('<td height="44" align="left" class="newList"></td>');
                	td2.attr("id",list[i].id);
                	td2.append(list[i].kname);
                	var td3 = $('<td align="center"></td>');
                	td3.append(crtimes);
                	var td31 = $('<td align="center"></td>');
                	td31.append(list[i].userName);
                	var td4 = $('<td height="44" align="center"></td>');
                	var checkbox = $("<input type='checkbox' />");
                	if(list[i].isHot==0){
                		checkbox.attr("checked","checked");
                	}
                	checkbox.on("click",function(){ //绑定checkbox点击事件
                		var  str = $($($(this).parent().parent()).find("td")[1]).text(); 
                		var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
                		var isHot=1;
                		if($(this).is(":checked")){
                			isHot=0;
                		}
                		$.ajax({
            				type:"post",
            				url:"/eprobot/know/updateKnowsByHot.action",
            				data:{"id":ids,"isHot":isHot},
            				dataType:"json",
            				success:function(data){
            					console.log(JSON.stringify(data));
            					if(data.results=="SUCCESS"){
            						alert("设置成功！！！");
            					}else{
            						alert("设置失败！！！");
            					}
            				}
            			});
                	});
                	td4.append(checkbox);
                	var td5 = $('<td height="44" align="center"></td>');
                	var spanedit = $("<span style='margin-right: 5px;'></span>").css("cursor","pointer");
                	spanedit.append("编辑");
                	spanedit.on("click",function(){
                		var  str = $($($(this).parent().parent()).find("td")[1]).text(); 
                		var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
						parent.editKnowlegeShow(ids,str);
                		/*$("#edit_name").val(str);
                		$("#hidden_id").val(ids);
                		$("#edit_show").show();*/
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
//                						getData(1);//默认第一页
                						window.history.go(0);
                						alert("删除成功！！！");
                					}else{
                						alert("删除失败！！！");
                					}
                				}
                			});
                		}
                	});
                	td5.append(spanedit).append(spandel);
                	tr.append(td1).append(td2).append(td3).append(td31).append(td4).append(td5);
                	$("#listShow").append(tr);
				}
//            }
            
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