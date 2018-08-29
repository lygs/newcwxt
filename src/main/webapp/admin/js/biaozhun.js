var qaQuestion = "";//保存搜索条件
var pageSize=15,searchPage=1;//每页数量
var prePageNum=10;iscj=0;//是否常见
var questionList;
var pageNavObj;
var totalPage;
var chnlIds="";
var endTimes="";
$(function(){
	
	parent.getKnowledge(-10);
	$(".lmjgqx").click(function(){
		chnlIds="";
		getData();
	});
    getData();
	$("#changj").on("click",function(){
		iscj = 1;
		getData();
	});
    $(".btn").on("click",function(){
		iscj = 0;
    	getData();
    });
    
    $(".lmjg").on("click",function(){
		parent.getAllChanell(1); //1代表点击标准问题的分类结构   2代表点击的是添加问题中的分类
		parent.$(".lm").show();
	});
    
	$(".tj").on("click",function(){
		if(parent.KnowAllList.length==0){
			alert("请先到知识点管理中添加知识点！");
			return;
		}
		parent.$(".addE").show();
	});
		
	$(".pl").on("click",function(){
		parent.$(".piLiang").show();
	});
	$(".shuaxinbtn").click(function(){
	    $("#searchName").val("");
	    $(".startTime").val("");
	    $(".endTime").val("");
		getData();
	});
});

function pageNavCallBack(clickPage){
    pageNavObj = new PageNavCreate("PageNavId",{
         pageCount:totalPage, 
         currentPage:clickPage, 
         perPageNum:prePageNum, 
    });
    pageNavObj.afterClick(pageNavCallBack);
    searchPage = clickPage;
    getData();
}
//获取分页数据
function getData(){ 
	
	$("#PageNavId").empty();
	$(".totalNum").empty();
	$("#listShow").empty();
    var tempUrl = '/eprobot/questionAnswer/getQuestionAnswerAllList.action';
    qaQuestion=$("#searchName").val();
    startTimes = $(".startTime").val();
    endTimes = $(".endTime").val();
    var queryString = "pageSize="+pageSize+"&pageNumber="+searchPage+"&qaQuestion="+qaQuestion+"&chnlId="+chnlIds+"&startTime="+startTimes+"&endTime="+endTimes+"&iscj="+iscj;
   // chnlIds = chnlId;
    $.ajax({
        type:'post',
        url:tempUrl,
        data:queryString,
        dataType: "json",
        cache:false,
        success:function(data){
        	if(data.result != null){
            var dataUl=$("#listShow");
            if(data.roleId==0){
            	$(".users").show();
            }
            if(data.result.length >0){
            	var lists=data.result;
				questionList = lists;
                var total = data.total;
                $(".totalNum").text("共"+total+"条");
                var pagecount = Math.ceil(total/pageSize);
                totalPage = pagecount;
               if(pagecount==1){
                   $("#PageNavId").html("");
               }else{
            	   pageNavObj = new PageNavCreate("PageNavId",{pageCount:pagecount,currentPage:searchPage,perPageNum:prePageNum});
                   pageNavObj.afterClick(pageNavCallBack);
               }
              /* var tr1 = $("<tr style='font-size: 14px; font-weight: bold;'></tr>");
               var td11 = $("<td width='5%' height='50' align='center' bgcolor='#F2F2F2'>序号</td>");
               var td22 =$("<td width='41%' height='50' align='center' bgcolor='#F2F2F2'>问题</td>");
               var td33 = $("<td width='11%' height='50' align='center' bgcolor='#F2F2F2'>创建时间</td>");
               var td44 = $("<td width='5%' height='50' align='center' bgcolor='#F2F2F2'>来源</td>");
               var td55 = $("<td width='20%' height='50' align='center' bgcolor='#F2F2F2'>关键字</td>");
               var td551 = $("<td width='5%' height='50' align='center' bgcolor='#F2F2F2'>创建人</td>");
               var td66 = $("<td width='5%' height='50' align='center' bgcolor='#F2F2F2'>是否常见</td>");
               var td77 = $("<td width='11%' height='50' align='center' bgcolor='#F2F2F2'>操作</td>");
               tr1.append(td11).append(td22).append(td33).append(td44).append(td55).append(td551).append(td66).append(td77);
           		$("#listShow").append(tr1);*/
                for ( var i = 0; i < lists.length; i++) {
                	var tr = $("<tr></tr>");
                	var td1 = $("<td height='44' align='center'></td>");
                	td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSize*searchPage-(pageSize-1): pageSize*searchPage-(pageSize-1)+i));
                	var td2=$("<td height='44' align='left' class='newList'></td>");
                	td2.attr("id",lists[i].id);
                	td2.attr("answers",lists[i].qaAnswer);
                	td2.attr("knowId",lists[i].qaKnowledge);
                	td2.attr("keyword",lists[i].qaKeywords);
                	td2.attr("resources",lists[i].qaResource);
                	td2.attr("knowId",lists[i].knowId);
                	var ii = $("<i class='iconfont icon-new'></i>");
                	var times = lists[i].qaCreatetime;
                	var date2 = new Date();    //当前时间
                	var date3 = date2.getTime() - new Date(times).getTime();   //时间差的毫秒数  
                	//计算出相差天数  
                	var days=Math.floor(date3/(24*3600*1000));
                	if(days<1){
                		td2.append(ii);
                	}
                	td2.append(lists[i].qaQuestion);
                	var td3=$("<td height='44' align='center'></td>");
                	td3.append(lists[i].qaCreatetime);
                	var td4=$("<td height='44' align='center'></td>"); //来源
                	td4.append(lists[i].qaResource);  
            		/*if(lists[i].qaResource=="1" || lists[i].qaResource==1){
                		td4.append("手工");  
                	}else{
                		td4.append("同步");  
                	}*/
                	
                	//关键字
                	var td5=$("<td height='44' align='left' style='text-indent:14px;'></td>");
                	td5.append(lists[i].qaKeywords);
                	var td51=$("<td height='44' align='left' style='text-indent:14px;'></td>");
                	td51.append(lists[i].userName);
                	var td6=$("<td height='44' align='center'></td>");
                	var checkBox=$("<input type='checkbox'/>");
                	if(lists[i].qaType==1){
                		checkBox.attr("checked","checked");
                	}
                	checkBox.on("click",function(){ //绑定checkbox点击事件  添加、取消常见问题
                		var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
                		var isChangj=0;
                		if($(this).is(":checked")){
                			isChangj=1;
                		}
                		$.ajax({
            				type:"post",
            				url:"/eprobot/questionAnswer/updateProblemByChangj.action",
            				data:{"id":ids,"isChangj":isChangj},
            				success:function(data){
            					if(data.result == "success"){
            						//alert("修改成功");
            					}else{
            						alert("修改失败!");
            						window.history.go(0);
            					}
            					
            				}
            			});
                	});
                	td6.append(checkBox); //常见问题 
                	var td7=$("<td height='44' align='center'></td>");
					var details =$("<a></a>").css("cursor","pointer");
					details.attr("qid",lists[i].id);
					details.append("详情 | ");
					details.on("click",function(){
						var question;
						if(questionList){
							for(var k=0;k<questionList.length;k++){
								if($(this).attr("qid")==questionList[k].id){
									question = questionList[k];
									break;
								}
							}
						}
						var kname="无",know = parent.KnowAllList;
						for(var m=0;m<know.length;m++){
							if(know[m].id==question.knowId){
								if(know[m].kname)
								kname = know[m].kname;
								break;
							}

						}
						parent.$(".question").text("问题: "+question.qaQuestion);
						parent.$(".keywords").text("关键字: "+question.qaKeywords);
						parent.$(".resourse").text("来源: "+question.qaResource);
						parent.$(".creator").text("创建人: "+question.userName);
						if(question.resourceType=="1")
							parent.$(".enterstutas").text("输入方式: 批量导入");
						else
							parent.$(".enterstutas").text("输入方式: 人工输入");
						if(question.fileName)
//							parent.$(".filename").text("批量导入文件名: "+question.fileName).css("display","none");
						parent.$(".knowledge").text("知识点: "+kname);
						parent.$(".createTime").text("创建时间: "+question.qaCreatetime.substring(0,10));
						parent.$(".answer").text(question.qaAnswer);
						parent.$(".details").show();
					});
                	var update =$("<a></a>").css("cursor","pointer"); //
                	update.append("编辑 | ");
                	update.on("click",function(){
                		var  str = $($($(this).parent().parent()).find("td")[1]).text(); 
                		var ids = $($($(this).parent().parent()).find("td")[1]).attr("id");
                		var answer = $($($(this).parent().parent()).find("td")[1]).attr("answers");
                		var knowIds = $($($(this).parent().parent()).find("td")[1]).attr("knowId");
                		var keyword_ = $($($(this).parent().parent()).find("td")[1]).attr("keyword");
                		var resources = $($($(this).parent().parent()).find("td")[1]).attr("resources");
                		var knowId = $($($(this).parent().parent()).find("td")[1]).attr("knowId");
                		parent.$(".edit_name").val(str);
                		parent.$(".edit_name").attr("ids",ids);
                		parent.$(".edit_answer").val(answer);
//                		parent.$("#edt_qaKnowledgePoint").val(knowIds).attr("selected","selected");
                		parent.$(".keyword1").val(keyword_);
                		parent.getKnowledge(knowIds);
                		parent.$(".resources1").val(resources);
                		parent.$(".editE").show();
                	});
                	var del=$("<a></a>").css("cursor","pointer");
                	del.append("删除");
                	del.on("click",function(){
                		var ids =   $($($(this).parent().parent()).find("td")[1]).attr("id");
                		if(confirm("是否确定删除！！！")){
                			$.ajax({
                				type:"post",
                				url:"/eprobot/questionAnswer/delQuestionAnswerById.action",
                				data:{"id":ids},
                				dataType:"json",
                				success:function(data){
                					if(data.result=="success"){
                						alert("删除成功！！！");
										iscj = 0;
										getData();
                					}else{
                						alert("删除失败！！！");
                					}
                				}
                			});
                		}
                	});
                	td7.append(details).append(update).append(del);
                	tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td51).append(td6).append(td7);
                	$("#listShow").append(tr);
                	
				}
            }else{
            	$("#listShow").append("暂无数据");
            }
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
