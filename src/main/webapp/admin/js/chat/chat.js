/**
 * Created by yang.yanxing on 2018/5/15.
 */
var pageSize=15;//每页数量
var pageNavObj;
var totalPage;
var selectbtn;
$(function(){
    getData(1);//默认第一页
});

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

function selectedbtn(){
	$(selectbtn).text("已设置");//.css("cursor","on-allowed");
	$(selectbtn).unbind();
}
//获取分页数据
function getData(searchPage){
    var tempUrl = '/eprobot/record/getRecordList.action';
    var queryString = "pageSize="+pageSize+"&pageNumber="+searchPage;
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
            var list=data.result;
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
                var timess = list[i].rCreatetime;
                var tr = $("<tr></tr>");
                var td1 = $('<td height="44" align="center"></td>');
                td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSize*searchPage-(pageSize-1): pageSize*searchPage-(pageSize-1)+i));
                var td2 = $('<td height="44" align="left" class="newList"></td>');
                td2.attr("rid",list[i].id); 
                
                td2.append(list[i].rQuestion);
                var td3 = $('<td align="center"></td>');
                var answer = list[i].rAnswer;
                if(answer.length>100)
                   answer = answer.substring(0,100)+"...";
                td3.append(answer);
                
                
                var td4 = $('<td height="44" align="center"></td>');
                var knowName = "";
                if(list[i].knowName){
                    knowName = list[i].knowName;
                }
                if(list[i].channelName){
                    knowName = list[i].channelName;
                }
                td4.append(knowName);
                var td5 = $('<td height="44" align="center"></td>');
                td5.append(list[i].userip);
                
                var td6 = $('<td height="44" align="center"></td>');
                td6.append(timess.substring(0,10));
                var td7 = $('<td height="44" align="center"></td>');
                td7.attr("ids",list[i].id);
                td7.attr("rQuestion",list[i].rQuestion);
                td7.attr("rAnswer",list[i].rAnswer);
                td7.attr("knowName",list[i].knowName);
                var spanedit = $("<span style='margin-right: 5px;'></span>");
                if(list[i].rCriterion=="1"){
                	spanedit.append("已设置");
                }else{
                	spanedit.append("设置").css("cursor","pointer");
                	spanedit.on("click",function(){
                		selectbtn = this;
                		var ids = $($(this).parent()).attr("ids");
                		var rQuestion = $($(this).parent()).attr("rQuestion");
                		var rAnswer = $($(this).parent()).attr("rAnswer");
                		var knowName = $($(this).parent()).attr("knowName");
                		parent.getKnowledge(knowName);
                		parent.$(".edit_record").val(rQuestion);
                		parent.$(".recordanswer").val(rAnswer);
                		parent.$(".recordId").val(ids);
                		parent.$(".editR").show();
                	});
                }
            	var spandel = $("<span></span>").css("cursor","pointer");
            	spandel.append("删除");
            	spandel.on("click",function(){
            		var ids = $($(this).parent()).attr("ids");
            		if(confirm("是否确认删除？？")){
            		    $.ajax({
            		        type:'post',
            		        url:"/eprobot/record/delRecord.action",
            		        data:{"ids":ids},
            		        dataType: "json",
//            		        cache:false,
            		        success:function(data){
            		            var re=data.result;
            		            if(re =="SUCCESS"){
            		            	alert("删除成功！");
            		            	window.location.reload();
            		            }else{
            		            	alert("删除失败！");
            		            }
            		        }
            		    });
            		}
            	});
            	td7.append(spanedit).append(spandel);
                tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7);
                $("#listShow").append(tr);
            }
        }
    });
}
