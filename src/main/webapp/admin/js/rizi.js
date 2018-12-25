/**
 * Created by yang.yanxing on 2018/5/15.
 */
var pageSize=15;//每页数量
var pageNavObj;
var totalPage;
var selectbtn;
var curpage = 1;

$(function(){
	order = 0;
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
    curpage =searchPage; 
    getData(searchPage);
}


//获取分页数据
function getData(searchPage){
	curpage =searchPage ;
	var tempUrl = '/eprobot/logs/getData.action';
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
            var list=data.data;
            var total = data.total;
            $(".total").text(total);
            var pagecount = Math.ceil(total/pageSize);
            totalPage = pagecount;
           /* if(data.roleId==0){
                $(".users").show();
            }*/

            if(pagecount==1){
                $("#PageNavId").html("");
            }else{
              // if(!pageNavObj)
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
                
                td2.append(list[i].className);
                var td3 = $('<td align="center"></td>');
                var answer = list[i].classMethod;
                td3.append(answer);
                var td4 = $('<td height="44" align="center"></td>');
                
                td4.append(list[i].content);
                var td5 = $('<td height="44" align="center"></td>');
                td5.append(list[i].createdate);
                tr.append(td1).append(td2).append(td3).append(td4).append(td5);
                $("#listShow").append(tr);
            }
        }
    });
}
