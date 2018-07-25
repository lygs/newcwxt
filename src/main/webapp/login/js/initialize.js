/**
 * Created by Administrator on 2017/10/23 0023.
 */
var questionList=[],questionList1=[];
$(function(){
	
	 $("html,body").animate({"scrollTop": "400px"}, 1000);
	 $(".askSubmit textarea").focus();

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

    $(".askSubmit textarea").on('input',function(e){
        var num = $(this).val().length;
        var content = $.trim($(this).val());
        if(num>50){
            $(this).val(content.substring(0,50));
            return;
        }
        $(".askTj .text").html("您还可以输入<span style='color:red;'>"+(50-num)+"</span>字");
        questionList = [];
        $.ajax({
            type:"post",
            url : "/eprobot/questionSearch/search",
            dataType: "json",
            data:{content:content},
            cache:false,
            success:function(data){
                var listdata=data.result;
                if(listdata!="no"){
                    var list = getList(listdata);
                    questionList = list;
                    var ul = $("<ul></ul>");
                    for(var i=0;i<list.length;i++){
                        if(i>15) break;
                        var li=$("<li></li>");
                        li.attr("lid",list[i].id);
                        li.attr("answer",list[i].qaAnswer);
                        li.append(list[i].qaQuestion);
                        li.on("click",function(){
                            questionList = delquestion(questionList,$(this).attr("lid"));
                        	 //调用 统计记录
                            saveRecord($(this).attr("lid"),$(this).text(),$(this).attr("answer"),$(".askSubmit textarea").val());
                          //清空
        	                $(".askSubmit textarea").val("");
                            $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");
                            var askdiv = $('<div class="ask"></div>');
                            var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
                            askdiv.append(askico);
                            var askcontent = $('<div class="text"></div>');
                            askcontent.append($(this).text()).append('<div class="jt"></div>');
                            askdiv.append(askcontent).append('<p class="clear"></p>');
                            $('#content').append(askdiv);

                            var answerdiv = $('<div class="answer"></div>');
                            var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                            answerdiv.append(ansico);
                            var anscontent = $('<div class="text"></div>');
                            anscontent.append($(this).attr("answer"));
                            if(anscontent.find("a")){
                                var a = anscontent.find("a");
                                a.attr("style","color:blue;text-decoration:underline");
                            }
                            var k=1;
                            if(questionList.length>1){
                                anscontent.append('<br><span style="font-weight: normal;">您可能在找以下问题：<br></span>');
                                var contentdiv = $('<div></div>');
                                contentdiv.addClass("qcontent");
                                for(var j=0;j<questionList.length;j++){

                                	if(j>9)
                                    	break;
                                    var span = $('<span></span>')
                                        .css("cursor","pointer");
                                    span.hover(function(){
                                        $(this).css("color","red");
                                    },function(){
                                        $(this).removeAttr("style");
                                        $(this).css("cursor","pointer")
                                    });

                                    if($(this).attr("lid")!=questionList[j].id){

                                        span.append(k+"."+questionList[j].qaQuestion);
                                        var f = $(span.find("font")[0]).text();

                                        var q  = $(span).text();
                                        span.attr("title",q);
                                        if(q.length>34){
                                            q = q.substring(0,33)+"...";
                                            if(q.indexOf(f)!=-1){
                                                q = q.replace(f,"<font color='red'>"+f+"</font>");
                                            }
                                            $(span).html(q);
                                        }

                                        span.attr("lid",questionList[j].id).attr("answer",questionList[j].qaAnswer);
                                        span.on("click",function(){
                                            questionAnswer($(this).attr("lid"), $(this).text(), $(this).attr("answer"));
                                        });
                                        span.append("<br>");
                                        contentdiv.append(span);
                                        k++;
                                    }
                                    questionList = delquestion(questionList,questionList[j].id);
                                }
                                anscontent.append(contentdiv);
                            }
                            var jt = $("<div></div>");
                            jt.addClass("jt");
                            anscontent.append(jt);
                            var others = $("<div></div>");
                            others.addClass("Gd");
                            others.append('<img src="images/Gd1.png">');
                            others.on("click",function(e){

                                for(var j=0;j<questionList.length;j++) {
                                    if (j > 9)
                                        break;
                                    var span = $('<span></span>')
                                        .css("cursor", "pointer");
                                    span.hover(function () {
                                        $(this).css("color", "red");
                                    }, function () {
                                        $(this).removeAttr("style");
                                        $(this).css("cursor", "pointer")
                                    });
                                    if ($(this).attr("lid") != questionList[j].id) {
                                        span.append(k+"."+questionList[j].qaQuestion);
                                        var f = $(span.find("font")[0]).text();
                                        var q  = $(span).text();
                                        span.attr("title",q);
                                        if(q.length>34){
                                            q = q.substring(0,33)+"...";
                                            if(q.indexOf(f)!=-1){
                                                q = q.replace(f,"<font color='red'>"+f+"</font>");
                                            }
                                            $(span).html(q);
                                        }
                                        span.attr("lid", questionList[j].id).attr("answer", questionList[j].qaAnswer);
                                        span.on("click", function () {
                                            questionAnswer($(this).attr("lid"), $(this).text(), $(this).attr("answer"));
                                        });
                                        span.append("<br>");
                                        $($(this).parent().find(".qcontent")).append(span);
                                        k++;
                                    }
                                    questionList = delquestion(questionList,questionList[j].id);
                                }
                                if(questionList.length==0){
                                    $(".Gd").hide();
                                }
                            });

                            if(questionList.length>1)
                                anscontent.append(others);
                            answerdiv.append(anscontent).append('<p class="clear"></p>');
                            var morediv =  $('<div class="more"></div>');
                            var morul = $('<ul></ul>');
                            morediv.append(morul);
                            var li1 =  $('<li class="t1">不满意</li>');
                            li1.attr("qid",$(this).attr("lid"));
                            var li2 =  $('<li class="t2">满意</li>');
                            li2.attr("qid",$(this).attr("lid"));
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
                            $(".other").html("");
                        });
                        ul.append(li);
                    }
                    $(".other").html(ul);
                }else{
                    $(".other").empty();
                }

            }
        });
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

function getList(qlist){
    var newArr = [];
    for(var i =0;i<qlist.length;i++){
        var flag = false;
        for(var j=0;j<newArr.length;j++){
            if(newArr[j].id==qlist[i].id){
                flag = true;break;
            }
        }
        if(!flag){
            newArr.push(qlist[i]);
        }

    }
   // if(newArr.length>15) newArr = newArr.slice(0,15);
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
    var content = $.trim($(".askSubmit textarea").val());
    if(!content){
        //alert("请输入内容！");
        return false;
    }
    questionList1 = [];
    $.ajax({
        type:"post",
        url : "/eprobot/questionSearch/search",
        dataType: "json",
        data:{content:content},
        cache:false,
        success:function(data){
            $(".other").empty();
            var list1=data.result;
            if(list1!="no"){
                if(list1.length>0){
                    var list = getList(list1);
                    questionList1 = list;
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
                    anscontent.append(list[0].qaAnswer);
                    if(anscontent.find("a")){
                        var a = anscontent.find("a");
                        a.attr("style","color:blue;text-decoration:underline");
                    }
                    //调用 统计记录
                    saveRecord(list[0].id,content,list[0].qaAnswer, $(".askSubmit textarea").val());
                    //清空
                    $(".askSubmit textarea").val("");
                    $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");
                    var k=1;
                    questionList1 = delquestion(questionList1,list[0].id);
                    if(questionList1.length>1){
                        anscontent.append('<br><span style="font-weight: normal;">您可能在找以下问题：<br></span>');
                        var contentdiv = $('<div></div>');
                        contentdiv.addClass("qcontent");
                        for(var j=1;j<questionList1.length;j++){
                            if(j>9)
                                break;
                            var span = $('<span></span>')
                                .css("cursor","pointer");
                            span.hover(function(){
                                $(this).css("color","red");
                            },function(){
                                $(this).removeAttr("style");
                                $(this).css("cursor","pointer")
                            });
                           // span.append(list[j].qaQuestion);
                            span.append(k+"."+questionList1[j].qaQuestion);
                            var f = $(span.find("font")[0]).text();

                            var q  = $(span).text();
                            span.attr("title",q);
                            if(q.length>34){
                                q = q.substring(0,33)+"...";
                                if(q.indexOf(f)!=-1){
                                    q = q.replace(f,"<font color='red'>"+f+"</font>");
                                }
                                $(span).html(q);
                            }


                            span.attr("lid",questionList1[j].id).attr("answer",questionList1[j].qaAnswer);
                            span.on("click",function(){
                                questionAnswer($(this).attr("lid"), $(this).text(), $(this).attr("answer"));
                            });
                            span.append("<br>");
                            contentdiv.append(span);
                            k++;
                            questionList1 = delquestion(questionList1,questionList1[j].id);
                        }
                    }
                    anscontent.append(contentdiv);
                    anscontent.append('<div class="jt"></div>');
                    answerdiv.append(anscontent);//
                    var others = $("<div></div>");
                    others.addClass("Gd");
                    others.append('<img src="images/Gd1.png">');
                    others.on("click",function(){
                        for(var j=0;j<questionList1.length;j++) {
                            if (j > 9)
                                break;
                            var span = $('<span></span>')
                                .css("cursor", "pointer");
                            span.hover(function () {
                                $(this).css("color", "red");
                            }, function () {
                                $(this).removeAttr("style");
                                $(this).css("cursor", "pointer")
                            });
                            if ($(this).attr("lid") != questionList1[j].id) {
                                span.append(k+"."+questionList1[j].qaQuestion);
                                var f = $(span.find("font")[0]).text();
                                var q  = $(span).text();
                                span.attr("title",q);
                                if(q.length>34){
                                    q = q.substring(0,33)+"...";
                                    if(q.indexOf(f)!=-1){
                                        q = q.replace(f,"<font color='red'>"+f+"</font>");
                                    }
                                    $(span).html(q);
                                }
                                span.attr("lid", questionList1[j].id).attr("answer", questionList1[j].qaAnswer);
                                span.on("click", function () {
                                    questionAnswer($(this).attr("lid"), $(this).text(), $(this).attr("answer"));
                                });
                                span.append("<br>");
                                $($(this).parent().find(".qcontent")).append(span);
                                k++;
                            }
                            questionList = delquestion(questionList1,questionList1[j].id);
                        }
                        if(questionList1.length==0){
                            $(".Gd").hide();
                        }
                    });
                    if(questionList1.length>1)
                        anscontent.append(others);
                    anscontent.append('<p class="clear"></p>');
                    var morediv =  $('<div class="more"></div>');
                    var morul = $('<ul></ul>');
                    morediv.append(morul);
                    var li1 =  $('<li class="t1">不满意</li>');
                    li1.attr("qid",list[0].id);
                    var li2 =  $('<li class="t2">满意</li>');
                    li2.attr("qid",list[0].id);
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
                anscontent.append("对不起，您需要了解的相关事项请联系管理员，办公室电话：028—80589003；值班电话：028—80589003(昼)、80589100 (夜/节假日)；在线咨询：http://www.schj.gov.cn/zmhd/zxzx/");
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
                    questionAnswer(e.id, e.qaQuestion,e.qaAnswer);
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
                    questionAnswer(e.id, e.qaQuestion, e.qaAnswer);
                });
            }
            getHotQuestion();
        }
    });
}

function questionAnswer(lid,questionstr,answerstr){
    //调用 统计记录
    saveRecord(lid,questionstr,answerstr,""); 
    //清空
    $(".askSubmit textarea").val("");
    var askdiv = $('<div class="ask"></div>');
    var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
    askdiv.append(askico);
    var askcontent = $('<div class="text"></div>');
    askcontent.append(questionstr).append('<div class="jt"></div>');
    askdiv.append(askcontent).append('<p class="clear"></p>');
    $('#content').append(askdiv);

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
    var morul = $('<ul></ul>');
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