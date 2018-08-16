var pageSize = 10;
var pageNumber = 1;
var pageNavObj;
var url = window.location.search;
var pStatus = url.substring(1);
$(function(){
	if(!pStatus){
		pStatus = 0;
	}
	if(pStatus == 0){
		$($("thead td")[2]).text("不满意数");
	}else{
		$($("thead td")[2]).text("满意数");
	}
	getData(1);
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

function getData(searchPage){
	$.ajax({
		type:"post",
		url:"/eprobot/praise/selectPraise",
		data:{"pageNumber":searchPage,"pageSize":pageSize,"pStatus":pStatus},
		success:function(data){
			if(data.result=="success"){
				$(".ptotal").empty();
				$(".ptotal").append(data.total);
	            $("#listShow").empty();
	            var dataUl=$("#listShow");
	            	var list=data.list;
	                var total = data.total;
	                var pagecount = Math.ceil(total/pageSize);
				    totalPage = pagecount;
	                
					if(pagecount==1){
						$("#PageNavId").html("");
					}else{
							pageNavObj = new PageNavCreate("PageNavId",{pageCount:pagecount,currentPage:searchPage,perPageNum:pageSize});
						pageNavObj.afterClick(pageNavCallBack);

					}
	                var htmlStr="";
	                for ( var i = 0; i < list.length; i++) {
	                	var timess = list[i].createtime;
	                	var tr = $("<tr></tr>");
	                	var td1 = $('<td height="44" align="center"></td>');
	                	td1.append((searchPage == 1) ? i+1: ((i == 0)?pageSize*searchPage-(pageSize-1): pageSize*searchPage-(pageSize-1)+i));
	                	var td2 = $('<td height="44" align="left" class="newList"></td>');
	                	td2.attr("id",list[i].id);
	                	td2.append(list[i].question);
	                	//var td3 = $('<td align="center"></td>');
	                	//var resource="";
	                	//td3.append(list[i].resource==0?"同步":"手工");
	                	var td4 = $('<td height="44" align="center"></td>');
	                	td4.append(list[i].qacount);

	                	var td5 = $('<td height="44" align="center"></td>');
	                	td5.append(timess);
	                	
	                	tr.append(td1).append(td2).append(td4).append(td5);
	                	$("#listShow").append(tr);
					}
	        
		         
			}else{
				alert("查询失败！");
			}
		}
	});
}