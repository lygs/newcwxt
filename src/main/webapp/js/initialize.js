/**
 * Created by Administrator on 2017/10/23 0023.
 */

$(function(){
	
	 $("html,body").animate({"scrollTop": "400px"}, 1000);
	 $(".askSubmit textarea").focus();
     $(".askSubmit textarea").on('input',function(e) {
         $(".other").empty();
         var content = $.trim($(this).val());
         var num = content.length;
         if (num > 50) {
             $(this).val(content.substring(0, 50));
             return;
         }
         $(".askTj .text").html("您还可以输入<span style='color:red;'>" + (50 - num) + "</span>字");
     });
     getQuestionByComm();

    $(".askSubmit textarea").bind('keydown',function(event){
        if(event.keyCode == "13") {
            searchQuestion();
        }
    });
    // getHotQuestion();
	 $(".btn").on("click",function(){
         searchQuestion();
	 });



});

//根据问题id删除数组中相关的对象，返回余下的内容
function delquestion(qList,qid){
    for(var i=0;i<qList.length;i++){
        if(qList[i].id==qid){
            qList.splice(i,1);
            break;
        }
    }
    return qList;
}

function getList(qlist,type){
    var newArr = [];
    for(var i =0;i<qlist.length;i++){
        var flag = false;
        for(var j=0;j<newArr.length;j++){
            if(newArr[j].id==qlist[i].id && type==0){
                flag = true;break;
            }
            if(newArr[j].chnlname==qlist[i].chnlname && type==1){
                flag = true;break;
            }
        }
        if(!flag){
            newArr.push(qlist[i]);
        }

    }
    return newArr;
}

function saveRecord(id,question,answer,keyword){
    var cip = "127.0.0.1";
    if(typeof returnCitySN !="undefined")
        cip = returnCitySN["cip"];
	 $.ajax({
         type:"post",
         url : "/eprobot/record/saveRecord",
         dataType: "json",
         data:{"rQuestion":question,"rAnswer":answer,"rQuestionId":id,"ips":cip,"keyword":keyword},
         cache:false,
         success:function(data){
            // console.log(data);
         }
     });
}

function searchQuestion(){
    var inputVal,content = $.trim($(".askSubmit textarea").val());
    if(!content){
        //alert("请输入内容！");
        return false;
    }

    $.ajax({
        type:"post",
        url : "/eprobot/questionSearch/search",
        dataType: "json",
        data:{content:content},
        cache:false,
        success:function(msg){
            inputVal = $.trim($(".askSubmit textarea").val());
            $(".other").empty();
            var list1=msg.data;
                if(msg.status == 1 && list1.wordList && (list1.channelList.length>0 || list1.wordList.length>0)){
                    var list,wordslist=[],channelList=[];

                    if(list1.wordList.length>0){
                        wordslist = getList(list1.wordList,0);
                    }
                    if(list1.channelList.length>0){
                        channelList = getList(list1.channelList,1);
                    }
                    if(wordslist && wordslist.length>0)
                       list = wordslist;
                    var askdiv = $('<div class="ask"></div>');
                    var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
                    askdiv.append(askico);
                    var askcontent = $('<div class="text"></div>');
                    askcontent.append(content).append('<div class="jt"></div>');
                    askdiv.append(askcontent).append('<p class="clear"></p>');
                    $('#content').append(askdiv);
                    var answerdiv = $('<div class="answer"></div>');
                    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                    answerdiv.append(ansico);
                    var anscontent = $('<div class="text"></div>');
                    //清空
                    $(".askSubmit textarea").val("");
                    $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");
                    var kum=0;
                    var contentdiv = $('<div></div>');
                    contentdiv.addClass("qcontent");
                    if(list && list.length>0){
                        anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                        for(var j=0;j<list.length;j++){
                            if(j>7) break;
                            kum++;
                            var span = $('<span></span>')
                                .css("color","blue")
                                .css("cursor","pointer");
                            span.hover(function(){
                                $(this).css("color","red");
                            },function(){
                                $(this).removeAttr("style");
                                $(this) .css("color","blue").css("cursor","pointer")
                            });
                            span.append("【"+kum+"】"+list[j].qaQuestion);
                            span.attr("title",list[j].qaQuestion);
                            span.attr("lid","q"+list[j].id).attr("answer",list[j].qaAnswer);
                            span.on("click",function(){
                                questionAnswer($(this).attr("lid"), $(this).attr("title"), $(this).attr("answer"),0,inputVal);
                            });
                            span.append("<br>");
                            contentdiv.append(span);
                        }
                    }
                    if((!list || list.length<8) && channelList.length>0){
                        if(!list){
                            anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                        }
                        var clist = [];
                        if(list){
                            for(var k=0;k<channelList.length;k++){
                                var f = false;
                                for(var j=0;j<list.length;j++){
                                    if(channelList[k].chnlname==list[j].qaQuestion){
                                        f=true;break;
                                    }
                                }
                                if(!f){
                                    clist.push(channelList[k]);
                                }
                            }
                        }else{
                            clist = channelList;
                        }

                        for(var i=0;i<clist.length;i++){
                            if(kum>7) break;
                            kum++;
                            var span = $('<span></span>')
                                .css("color","blue")
                                .css("cursor","pointer");
                            span.hover(function(){
                                $(this).css("color","red");
                            },function(){
                                $(this).removeAttr("style");
                                $(this) .css("color","blue").css("cursor","pointer")
                            })

                            var churl = '<a href="'+clist[i].chnlurl+'" cid="c'+clist[i].channelid+'" target="_blank" style="color:blue;" title="'+clist[i].chnlname+'">'+clist[i].chnlname+'</a>';
                            span.append("【"+kum+"】"+churl);
                            $(span.find("a")).on("click",function(e){
                                saveRecord($(this).attr("cid"),$(this).attr("title"),$(this).attr("href"),inputVal);
                            });
                            span.attr("title",clist[i].chnlname);
                            span.append("<br>");
                            contentdiv.append(span);
                        }
                    }
                    anscontent.append(contentdiv);
                    anscontent.append('<div class="jt"></div>');
                    answerdiv.append(anscontent);
                    answerdiv.append('<p class="clear"></p>');
                    $('#content').append(answerdiv).append('<div style="height: 10px;"></div>');
                    if($("#content")[0].scrollHeight>521){
                        var scrollheight = $("#content")[0].scrollHeight-521;
                        $('#content').css("top",-scrollheight);
                    }
                    if(list && list.length>8){
                        var others = $("<div></div>");
                        others.addClass("Gd");
                        others.append('想了解更多具体内容，点击<span style="color: blue;display: inline;cursor: pointer;">这里</span>');
                        anscontent.append(others);
                        $(others.find("span")).on("click",function(){
                            var answerdiv1 = $('<div class="answer"></div>');
                            var ansico1 = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                            answerdiv1.append(ansico1);
                            var anscontent1 = $('<div class="text"></div>');
                            var contentdiv1 = $('<div></div>');
                            contentdiv1.addClass("qcontent");
                            if(channelList.length>0){
                                anscontent1.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                                var num=0;
                                for(var i=0;i<channelList.length;i++) {
                                    if (num > 8) break;
                                    num++;
                                    var span = $('<span></span>')
                                        .css("color","blue")
                                        .css("cursor", "pointer");
                                    span.hover(function () {
                                        $(this).css("color", "red");
                                    }, function () {
                                        $(this).removeAttr("style");
                                        $(this) .css("color","blue");
                                        $(this).css("cursor", "pointer");
                                    });
                                    var churl = '<a href="'+channelList[i].chnlurl+'" cid="c'+channelList[i].channelid+'" target="_blank" style="color:blue;" title="'+channelList[i].chnlname+'">'+channelList[i].chnlname+'</a>';
                                    span.append("【"+num+"】"+churl);
                                    $(span.find("a")).on("click",function(e){
                                        saveRecord($(this).attr("cid"),$(this).attr("title"),$(this).attr("href"),inputVal);
                                    });
                                    span.attr("title",channelList[i].chnlname);
                                    span.append("<br>");
                                    contentdiv1.append(span);
                                }

                            }

                            anscontent1.append(contentdiv1);
                            anscontent1.append('<div class="jt"></div>');
                            answerdiv1.append(anscontent1);
                            answerdiv1.append('<p class="clear"></p>');
                            $('#content').append(answerdiv1).append('<div style="height: 10px;"></div>');
                            if($("#content")[0].scrollHeight>541){
                                var scrollheight = $("#content")[0].scrollHeight-541;
                                $('#content').css("top",-scrollheight);
                            }
                        });
                    }
                
            }else{
                var askdiv = $('<div class="ask"></div>');
                var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
                askdiv.append(askico);
                var askcontent = $('<div class="text"></div>');
                askcontent.append(content).append('<div class="jt"></div>');
                askdiv.append(askcontent).append('<p class="clear"></p>');
                $('#content').append(askdiv);
                var answerdiv = $('<div class="answer"></div>');
                var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                answerdiv.append(ansico);
                var anscontent = $('<div class="text"></div>');
                anscontent.append("亲，没能为您找到答案，您的问题已经记录，您还可以联系办公室电话：028—80589003；值班电话：028—80589003(昼)、80589100 (夜/节假日)；<a href='http://www.schj.gov.cn/zmhd/zxzx/' target='_blank' style='color:blue;text-decoration:underline'>在线咨询</a>");
                anscontent.append('<div class="jt"></div>');
                answerdiv.append(anscontent).append('<p class="clear"></p>');
                $('#content').append(answerdiv).append('<div style="height: 10px;"></div>');
                if($("#content")[0].scrollHeight>521){
                    var scrollheight = $("#content")[0].scrollHeight-521;
                    $('#content').css("top",-scrollheight);
                }

                saveRecord("",content,$(anscontent).text(),$(".askSubmit textarea").val());
            }
            //清空
            $(".askSubmit textarea").val("");
            $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");
        }
    });
}

function getHotQuestion(){
    $.ajax({
        type:"post",
        url : "/eprobot/questionAnswer/getQuestionByClick.action",
        dataType: "json",
        data:{"pageSize":6},
        cache:false,
        success:function(data){
            var list=data.results;
            //console.log(JSON.stringify(list));
            if(list.length>0){
                var question =  new cpt_rob.CPT_CommQuestion_Main($("#knowledge"),list,"热点关注");
                question.onEvents("QMSGS",function(e){
                    questionAnswer("q"+e.id, e.qaQuestion,e.qaAnswer,1,"");
                });
            }
           
        }
    });
}

function getQuestionByComm(){
    $.ajax({
        type:"post",
        url : "/eprobot/questionAnswer/getQuestionByComm.action",
        dataType: "json",
        data:{"pageSize":6,"pageNumber":1},
        cache:false,
        success:function(data){
            var list=data.result;
            if(list.length>0){
                var question =  new cpt_rob.CPT_CommQuestion_Main($("#question"),list,"常见问题");
                question.onEvents("QMSGS",function(e){
                    questionAnswer("q"+e.id, e.qaQuestion, e.qaAnswer,1,"");
                });
            }
          getHotQuestion();
        }
    });
}

function questionAnswer(lid,questionstr,answerstr,num,kwords){

    //调用 统计记录
    saveRecord(lid,questionstr,answerstr,kwords);
    //清空
    $(".askSubmit textarea").val("");
    if(num==1){
        var askdiv = $('<div class="ask"></div>');
        var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
        askdiv.append(askico);
        var askcontent = $('<div class="text"></div>');
        askcontent.append(questionstr).append('<div class="jt"></div>');
        askdiv.append(askcontent).append('<p class="clear"></p>');
        $('#content').append(askdiv);
    }

    var answerdiv = $('<div class="answer"></div>');
    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
    answerdiv.append(ansico);
    var anscontent = $('<div class="text"></div>');
    anscontent.append(answerstr);
    if(anscontent.find("a")){
        var a = anscontent.find("a");
        a.attr("style","color:blue;text-decoration:underline");
    }
    anscontent.append('<div class="jt"></div>');
    answerdiv.append(anscontent).append('<p class="clear"></p>');
    var morediv =  $('<div class="more"></div>');
    var morul = $('<ul></ul>')
        .css("padding-top","10px")
        .css("padding-bottom","10px");
    morediv.append(morul);
    var li1 =  $('<li class="t1">不满意</li>');
    li1.attr("qid",lid);
    var li2 =  $('<li class="t2">满意</li>');
    li2.attr("qid",lid);
    var li3 = $('<li class="t3">您对此答案是否满意？</li>');
    li1.on("click",function(){
    	var lis = $(this).parent().find("li");
    	lis.each(function() {
    		$(this).unbind();
    		$(this).css("cursor","not-allowed");
		});
        $.ajax({
            type:"post",
            url:"/eprobot/praise/savePraise",
            dataType: "json",
            data:{pQaId:$(this).attr("qid"),pStatus:0},
            cache:false,
            success:function(data){
                alert("感谢您对我们的支持，我们会努力改进！");
            }
        });
    });
    li2.on("click",function(){
    	var lis = $(this).parent().find("li");
    	lis.each(function() {
    		$(this).unbind();
    		$(this).css("cursor","not-allowed");
		});
        $.ajax({
            type:"post",
            url:"/eprobot/praise/savePraise",
            dataType: "json",
            data:{pQaId:$(this).attr("qid"),pStatus:1},
            cache:false,
            success:function(data){
                alert("感谢您对我们的支持，我们会努力做到更好！");
            }
        });
    });
    morul.append(li1).append(li2).append(li3);
    answerdiv.append(morediv);
    $('#content').append(answerdiv).append('<div style="height: 10px;"></div>');
    if($("#content")[0].scrollHeight>521){
        var scrollheight = $("#content")[0].scrollHeight-521;
        $('#content').css("top",-scrollheight);
    }
   // $(".other").html("");
}
