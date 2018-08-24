var pageSize=15;
var pageNavObj,totalPage=0;
var currentPage=1;
$(function(){
	$(".shuaxinbtn").click(function(){
		$("#searchName").val("");
		$("#startDate").val("");
		$("#endDate").val("");
		getData(1);
	});
    $(".tj").on("click",function(){
        parent.$(".addR").show();
    })
    getData(1);
    $(".btn").on("click",function(){
    	getData(1);
    })
});
function pageNavCallBack(clickPage){
    pageNavObj = new PageNavCreate("PageNavId",{
        pageCount:totalPage,
        currentPage:clickPage,
        perPageNum:pageSize,
    });
    pageNavObj.afterClick(pageNavCallBack);
    currentPage = clickPage;
    getData(clickPage);
}

function getData(pageNumber){
    $("#listShow").empty();
    var tempUrl = '/eprobot/individualWord/getDataByTitle';
    var queryString = "pageSize="+pageSize+"&pageNumber="+pageNumber+"&title="+$("#searchName").val()+"&startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val();
    $.ajax({
        type: 'post',
        url: tempUrl,
        data: queryString,
        dataType: "json",
        cache: false,
        success: function (data) {
        	//获取分页数据
        	if(data.status=1){
                if(data.data.length>0){
                    var list=data.data;
                    var pageCount = Math.ceil(data.total/pageSize);
                    $("#total").text("总数： "+data.total);
                    totalPage = pageCount;
                    //if(!pageNavObj)
                    pageNavObj = new PageNavCreate("PageNavId",{pageCount:pageCount,currentPage:pageNumber,perPageNum:pageSize});
                    pageNavObj.afterClick(pageNavCallBack);
                    //添加
                    for (var i=0;i<list.length;i++){
                        var tr=$("<tr></tr>");
                        var td1=$('<td></td>');
                        td1.attr("align","center").attr("height","50px");
                        //编号
                        td1.append((pageNumber == 1) ? i+1: ((i == 0)?pageSize*pageNumber-(pageSize-1): pageSize*pageNumber-(pageSize-1)+i));
                        //td1.append(i+1);
                        var td2=$('<td></td>').css("padding-left","5px");
                        td2.append(list[i].qaQuestion);
                        var td3=$('<td></td>');
                        td3.append(list[i].founder);
                        td3.attr("align","center");
                        var td4=$('<td></td>');
                        td4.append(list[i].cretime);
                        td4.attr("align","center");
                        var td5=$('<td></td>');
                        var spanedit = $("<span style='margin-right: 5px;'></span>").css("cursor","pointer");
                        spanedit.append("编辑");
                        spanedit.attr("rcId",list[i].id);
                        spanedit.attr("rcanswer",list[i].qaAnswer);
                        spanedit.on("click",function(){
                        	parent.$(".editRC").show();
                        	var  str1 = $($($(this).parent().parent()).find("td")[1]).text();
                        	var  str2 = $(this).attr("rcanswer");;
        					var ids =   $(this).attr("rcId");
        					parent.$(".editRC .cRc").val(str1);
        					parent.$(".editRC #r3").val(str2);
        					parent.rcId=ids;
                        })
                        var spanedel = $("<span style='margin-right: 5px;'></span>").css("cursor","pointer");
                        spanedel.append("删除");
                        spanedel.on("click",function(){
                    		var ids =   $($(this).parent().find("span")[0]).attr("rcId");
                    		if(confirm('确实要删除该内容吗?')){
                    			$.ajax({
                    				url:"/eprobot/individualWord/del",
                    				dataType:"json",
                    				data:{"id":ids},
                    				success:function(data){
                    					pageNavCallBack(1);
                    					alert(data.msg);
                    				}
                    			})
                    		}
                    	});
                        td5.append(spanedit).append(spanedel);
                        td5.attr("align","center");
                        var td6=$('<td></td>').css("padding-left","5px");
                        td6.append(list[i].qaAnswer);
                        tr.append(td1).append(td2).append(td6).append(td3).append(td4).append(td5);
                        $("#listShow").append(tr);
                    }
                }
                else {}
            }
            else {
                alert(data.msg);
            }
        },
        error:function(data){
            alert("请求错误！");
        },
    });
}
