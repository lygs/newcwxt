var pageSize = 10;
var pageNumber = 1;
var searchContent ;
var prePageNum=10;
var questionList;
var pageNavObj;
var totalPage;

/**
 * 初始化
 * @returns
 */
$(function(){
	 getData("",10,1);
})

/**
 * 添加
 * @returns
 */
function add(id,selected){
	var content = $.trim(selected.find("textarea").val());
	var data;
	if(id > 0){
		data = {"content":content,"id":id};
	}else{
		data = {"content":content};
	}
	if(checkisNotEmpty(content)){
		$.ajax({
			url:"/eprobot/wordExpansion/updataOrSave",
			dataType:"json",
			data:data,
			success:function(data){
				if(data.status > 0){
					selected.hide();
					selected.find("textarea").val("");
					//刷新
					 getData("",pageSize,pageNumber);
					alert("操作成功");
					
				}else{
					alert(data.msg);
				}
			}
		});
	}else{
		alert("请输入内容！");
	}
	
}

/**
 * 获取分页数据
 * @returns
 */
function getData(content,pageSize,pageNumber){
	searchContent = content;
	$("#PageNavId").empty();
	$(".totalNum").empty();
	$("#listShow tbody").empty();
	$.ajax({
		url:"/eprobot/wordExpansion/getData",
		type:'post',
		dataType:"json",
		data:{"content":content,"pageSize":pageSize,"pageNumber":pageNumber},
		async: false,
		success:function(data){
			if(data.status > 0){
				var total = data.total;
				 $(".totalNum").text("共"+total+"条");
	             var pagecount = Math.ceil(total/pageSize);
	             totalPage = pagecount;
	             if(pagecount==1){
	                   $("#PageNavId").html("");
	              }else{
	            	   pageNavObj = new PageNavCreate("PageNavId",{pageCount:pagecount,currentPage:pageNumber,perPageNum:prePageNum});
	                   pageNavObj.afterClick(pageNavCallBack);
	             }
				pushDataToDom(data.data,pageSize,pageNumber);
			}else{
				alert(data.msg);
			}
		}
	});
}

/**
 * 向页面添加数据
 * @returns
 */
function pushDataToDom(data,pageSize,pageNumber){
		for(var i=0; i< data.length; i++){
			var tr = $("<tr align='center'></tr>");
			var td1 = $("<td height='40px'></td>");
			td1.append((pageNumber == 1) ? i+1: ((i == 0)?pageSize*pageNumber-(pageSize-1): pageSize*pageNumber-(pageSize-1)+i));
			var tdContent = $("<td></td>").text(data[i].content);
			var tdCretime = $("<td align='center'></td>").text(data[i].cretime);
			var tdFounder = $("<td align='center'></td>").text(data[i].founder);
			var operation = '<td  align="center"><a onclick="showEdit(this,'+data[i].id+');" style="cursor: pointer;">编辑 | </a><a onclick="del('+data[i].id+');" style="cursor: pointer;">删除</a></td>';
			tr.append(td1).append(tdContent).append(tdCretime).append(tdFounder).append(operation)
			$("#listShow tbody").append(tr);
		}
}

/**
 * 验证是否为空
 * @param e
 * @returns 不为空返回true
 */
function checkisNotEmpty(i){
	if(i != null && i != "" && i.length >0){
		return true;
	}else{
		return false;
	}
}

/**
 * 编辑
 * @returns
 */
function edit(e){
	
	add($(".editId").val(),e);
}

function showEdit(e,id){
	$(".edit .textArea textarea").val("");
	$(".edit .textArea textarea").val($(e).parent().prev().prev().prev().text());
	$(".editId").val(id);
	$(".edit").show();
}

/**
 * 删除
 * @returns
 */
function del(id){
	if(confirm('确实要删除该内容吗?')){
		$.ajax({
			url:"/eprobot/wordExpansion/del",
			dataType:"json",
			data:{"id":id},
			success:function(data){
				 getData("",10,1);
				alert(data.msg);
			}
		})
	}
}

function pageNavCallBack(clickPage){
    pageNavObj = new PageNavCreate("PageNavId",{
         pageCount:totalPage, 
         currentPage:clickPage, 
         perPageNum:prePageNum, 
    });
    pageNavObj.afterClick(pageNavCallBack);
    searchPage = clickPage;
    getData(searchContent,pageSize,clickPage);
}
