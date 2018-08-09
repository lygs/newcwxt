/**
 * Created by yang.yanxing on 2018/7/19.
 */
var channels = [];
$(function(){

    $.ajax({
        type:'post',
        url:"/eprobot/template/getSelectedTemp",
        dataType:"json",
        success:function(data){
            if(data.data.urlName){
                var temUrl = data.data.urlName;
                $("<link>")	.attr({ rel: "stylesheet",
                    type: "text/css",
                    href: "css/"+temUrl+".css"
                }).appendTo("head");
                $(".warp").show();
                $(".p_footer").show();
            }
        }
    });

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
    getAllChanell();
    $(".askSubmit textarea").bind('keydown',function(event){
        if(event.keyCode == "13") {
            searchQuestion();
        }
    });
    $(".btn").on("click",function(){
        searchQuestion();
    });



});


function searchQuestion(){
    var inputVal,content = $.trim($(".askSubmit textarea").val());
    if(!content){
        return false;
    }

    $.ajax({
        type:"post",
        url : "/eprobot/questionSearch/searchq",
        dataType: "json",
        data:{content:content},
        cache:false,
        success:function(msg){
            if(msg.status == 1){
                inputVal = $.trim($(".askSubmit textarea").val());
                $(".other").empty();
                content = msg.data.searchContent;
                var list1=msg.data,channelList=[],chlist=[];
                if(content=="于会文"){
                    chlist = getchnnellist(channels,"厅长");
                }else{
                    chlist = getchnnellist(channels,content);//完全匹配栏目
                }
                //var chlist = getchnnellist(channels,content);//完全匹配栏目
                //栏目完全匹配
                if(chlist>0){
                    //channels = channelList
                    channelList = getChannelsByName(chlist,content,list1.words);
                }else{
                    channelList = getChannelsByName(channels,content,list1.words);
                }

                if(list1.wordList && list1.wordList.length==0 && channelList.length==0){
                    var askdiv = $('<div class="ask"></div>');
                    var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
                    askdiv.append(askico);
                    var askcontent = $('<div class="text"></div>');
                    askcontent.append(inputVal).append('<div class="jt"></div>');
                    askdiv.append(askcontent).append('<p class="clear"></p>');
                    $('#content').append(askdiv);
                    var answerdiv = $('<div class="answer"></div>');
                    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                    answerdiv.append(ansico);
                    var anscontent = $('<div class="text"></div>');
                    anscontent.append("亲，没能为您找到答案，您的问题已经记录，我们将为您提供其他咨询方式：<br>1、在线人工咨询，<a href='http://www.schj.gov.cn/zmhd/zxzx/' target='_blank' style='color:blue;text-decoration:underline'>请点击查看</a>；<br>2、通过电话方式，联系电话：028—80589003；值班电话：028—80589003(昼)、80589100 (夜/节假日)。");
                   // anscontent.append("亲，没能为您找到答案，您的问题已经记录，您还可以联系办公室电话：028—80589003；值班电话：028—80589003(昼)、80589100 (夜/节假日)；<a href='http://www.schj.gov.cn/zmhd/zxzx/' target='_blank' style='color:blue;text-decoration:underline'>在线咨询</a>");
                    anscontent.append('<div class="jt"></div>');
                    answerdiv.append(anscontent).append('<p class="clear"></p>');
                    $('#content').append(answerdiv).append('<div style="height: 10px;"></div>');
                    if($("#content")[0].scrollHeight>521){
                        var scrollheight = $("#content")[0].scrollHeight-521;
                        $('#content').css("top",-scrollheight);
                    }

                    saveRecord("",content,$(anscontent).text(),$(".askSubmit textarea").val());
                }else{
                    var list=[],wordslist=[];
                    if(list1.wordList.length>0){
                        list = getList(list1.wordList);
                    }
                    var qwords = getWords(list,content);//完全匹配问题；
                    var askdiv = $('<div class="ask"></div>');
                    var askico = $('<div class="ico"><img src="images/jqr_ico_02.png" width="46" height="60" alt=""></div>');
                    askdiv.append(askico);
                    var askcontent = $('<div class="text"></div>');
                    askcontent.append(inputVal).append('<div class="jt"></div>');
                    askdiv.append(askcontent).append('<p class="clear"></p>');
                    $('#content').append(askdiv);
                    //清空
                    $(".askSubmit textarea").val("");
                    $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");

                    var answerdiv = $('<div class="answer"></div>');
                    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
                    answerdiv.append(ansico);
                    var anscontent = $('<div class="text"></div>');
                    var kum=0;
                    var contentdiv = $('<div></div>');
                    contentdiv.addClass("qcontent");

                    if(chlist.length>0){
                        if(!list){
                            anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                        }
                        for(var i=0;i<chlist.length;i++){
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
                            });
                            span.attr("chnlid",chlist[i].channelId);
                            span.attr("pid",chlist[i].parentid);
                            span.attr("title",chlist[i].chnlname);
                            span.attr("url",chlist[i].chnlurl);
                            var qstr = chlist[i].chnlname;
                            qstr = qstr.replace(content,content.fontcolor("red"));
                            span.append("【"+kum+"】"+qstr);
                            span.on("click",function(e){
                                var thisid = $(this).attr("chnlid");
                                // var thispid = $(this).attr("pid");
                                var chnlurl = $(this).attr("url");
                                var thischnlname = $(this).attr("title");
                                var chnles = getChannelsByChnlid(thisid);
                                if(chnles.length>0){
                                    //显示相关栏目。
                                    chanlAnswer(chnles);
                                }else{
                                    //查询相关问题
                                    getQAByCid(thisid,chnlurl,thischnlname);
                                }
                            });

                            span.append("<br>");
                            contentdiv.append(span);
                        }
                    }else if(qwords){
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
                        var qtitle = qwords.qaQuestion;
                        if(qtitle.length>26)
                            qtitle = qtitle.substring(0,26)+"...";
                        qtitle = qtitle.replace(content,content.fontcolor("red"));
                        span.append("【"+kum+"】"+qtitle);
                        span.attr("title",qwords.qaQuestion);
                        span.attr("lid","q"+qwords.id).attr("answer",qwords.qaAnswer);
                        span.attr("qastyle","q"+qwords.qaFormat);
                        span.on("click",function(){
                            questionAnswer($(this).attr("lid"), $(this).attr("title"), $(this).attr("answer"),0,content,$(this).attr("qastyle"));
                            saveRecord($(this).attr("lid").substring(1),$(this).attr("title"),$(this).attr("href"),content);
                        });
                        span.append("<br>");
                        contentdiv.append(span);
                    }else{
                        if( channelList.length>0){
                            if(!list){
                                anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                            }
                            for(var i=0;i<channelList.length;i++){
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
                                });
                                span.attr("chnlid",channelList[i].channelId);
                                span.attr("pid",channelList[i].parentid);
                                span.attr("title",channelList[i].chnlname);
                                span.attr("url",channelList[i].chnlurl);
                                var qstr = channelList[i].chnlname;
                                qstr = qstr.replace(content,content.fontcolor("red"));
                                span.append("【"+kum+"】"+qstr);
                                span.on("click",function(e){
                                    var thisid = $(this).attr("chnlid");
                                    // var thispid = $(this).attr("pid");
                                    var chnlurl = $(this).attr("url");
                                    var thischnlname = $(this).attr("title");
                                    var chnles = getChannelsByChnlid(thisid);
                                    if(chnles.length>0){
                                        //显示相关栏目。
                                        chanlAnswer(chnles);
                                    }else{
                                        //查询相关问题
                                        getQAByCid(thisid,chnlurl,thischnlname);
                                    }
                                });
                                span.append("<br>");
                                contentdiv.append(span);
                            }
                        }

                        if(list && list.length>0 && channelList.length==0){
                            anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
                            for(var j=0;j<list.length;j++){
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
                                });
                                var qtitle = list[j].qaQuestion;
                                if(qtitle.length>26)
                                    qtitle = qtitle.substring(0,26)+"...";
                                qtitle = qtitle.replace(content,content.fontcolor("red"));
                                span.append("【"+kum+"】"+qtitle);
                                span.attr("title",list[j].qaQuestion);
                                span.attr("lid","q"+list[j].id).attr("answer",list[j].qaAnswer);
                                span.attr("qastyle",list[j].qaFormat);
                                span.on("click",function(){
                                    questionAnswer($(this).attr("lid"), $(this).attr("title"), $(this).attr("answer"),0,inputVal, $(this).attr("qastyle"));
                                    saveRecord($(this).attr("lid").substring(1),$(this).attr("title"),$(this).attr("href"),inputVal);
                                });
                                span.append("<br>");
                                contentdiv.append(span);
                            }
                        }
                    }
                    if(kum<8){
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
                        span.append("【"+kum+"】其他");
                        span.on("click",function(){
                            window.open("http://www.schj.gov.cn/wzdt/");
                        });
                        span.append("<br>");
                        contentdiv.append(span);
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
                    if(list && list.length>8 && channelList.length==0 ){
                        var others = $("<div></div>");
                        others.addClass("Gd");
                        others.append('想了解更多具体内容，点击<span style="color: blue;display: inline;cursor: pointer;">这里</span>');
                        anscontent.append(others);
                        $(others.find("span")).on("click",function(){
                            getChnlNameByQid(list[0].id);
                        });
                    }
                }


            }else{
                alert(msg.msg);
            }
            //清空
            $(".askSubmit textarea").val("");
            $(".askTj .text").html("您还可以输入<span style='color:red;'>50</span>字");
        }
    });
}


//查询所有栏目
function getAllChanell(){
    $.ajax({
        type:'post',
        url:"/eprobot/chnl/getAll",
        dataType:"json",
        async: false,
        success:function(data){
            if(data.list!=null){
               // console.log(JSON.stringify(data))
                channels = data.list;
            }
        }
    });
}

function getQAByCid(cid,chnlurl,chnlName){
    $.ajax({
        type:"post",
        url:"/eprobot/questionAnswer/getQaByChnlid",
        dataType: "json",
        data:{"id":cid},
        cache:false,
        success:function(data){
            if(data.list && data.list.length>0){
                questionList(data.list,chnlurl,chnlName);
            }else{
                window.open(chnlurl);
            }

        }
    });
}

function getChnlNameByQid(qid){
    $.ajax({
        type:"post",
        url:"/eprobot/questionAnswer/getChnlNameByQid",
        dataType: "json",
        data:{"id":qid},
        cache:false,
        success:function(data){
            window.open(data.list[0].CHNLURL);
            //questionList(data.list,chnlurl,chnlName);
        }
    });
}
//完全匹配栏目
function getchnnellist(chlist,searchval){
    var chnnllist = [];
     for(var i=0;i<chlist.length;i++){
         if(chlist[i].chnlname==searchval){
             chnnllist.push(chlist[i]);
         }
     }
    return chnnllist;
}
function getChannelsByName(channelslist,channelName,wordslist){
    var chnlList = [];
    if(wordslist && wordslist.length>0){
       /* var  content = wordslist[0].content;
        if(content.indexOf("；")!=-1) {
            content = content.replace("；", ";");
        }*/
        var words = wordslist;
        var wlist = [];
        for(var k=0;k<words.length;k++){
            for(var i=0;i<channelslist.length;i++){
                if(channelslist[i].chnlname.indexOf(words[k])!=-1){
                    var chnlname=channelslist[i].chnlname,pid = channelslist[i].parentid;
                    for(var j=0;j<chnlList.length;j++){
                        if(chnlList[j].chnlname==channelslist[i].chnlname){
                            if(pid!=0){
                                var chnlobj = getParentChannelByPid(pid);
                                chnlname = chnlobj.chnlname+"-"+chnlname;
                                if(chnlobj.parentid!=0){
                                    var obj = getParentChannelByPid(chnlobj.parentid);
                                    chnlname = obj.chnlname+"-"+chnlname;
                                }
                            }
                            break;
                        }
                    }
                    channelslist[i].chnlname = chnlname;
                    wlist.push(channelslist[i])
                }
            }
        }
        for(var i =0;i<wlist.length;i++){
            var flag = false;
            for(var j=0;j<chnlList.length;j++){
                if(chnlList[j].channelId==wlist[i].channelId){
                    flag = true;break;
                }
            }
            if(!flag){
                chnlList.push(wlist[i]);
            }
        }

    }else{
        for(var i=0;i<channelslist.length;i++){
            if(channelslist[i].chnlname.indexOf(channelName)!=-1){
                var chnlname=channelslist[i].chnlname,pid = channelslist[i].parentid;
                for(var j=0;j<chnlList.length;j++){
                    if(chnlList[j].chnlname==channelslist[i].chnlname){
                        if(pid!=0){
                            var chnlobj = getParentChannelByPid(pid);
                            chnlname = chnlobj.chnlname+"-"+chnlname;
                            if(chnlobj.parentid!=0){
                                var obj = getParentChannelByPid(chnlobj.parentid);
                                chnlname = obj.chnlname+"-"+chnlname;
                            }
                        }
                        break;
                    }
                }
                channelslist[i].chnlname = chnlname;
                chnlList.push(channelslist[i])
            }

        }
    }

    return chnlList;

}

//根据父id获取父栏目
function getParentChannelByPid(pid){
    var chnlobj;
    for(var i=0;i<channels.length;i++){
        if(channels[i].channelId==pid){
            chnlobj=channels[i];break;
        }

    }
    return chnlobj;
}
//根据栏目id获取字栏目
function getChannelsByChnlid(cid){
    var chnls = [];
    for(var i=0;i<channels.length;i++){
        if(channels[i].parentid==cid){
            chnlobj=channels[i];
            chnls.push(channels[i]);
        }

    }
    return chnls;
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
                    questionAnswer("q"+e.id, e.qaQuestion, e.qaAnswer,1,"", e.qaFormat);
                });
            }
            getHotQuestion();
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
                   questionAnswer("q"+e.id, e.qaQuestion,e.qaAnswer,1,"",e.qaFormat);
                });
            }

        }
    });
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
    return newArr;
}
//完全匹配问题
function getWords(qlist,searchval){
    var qwords;
    for(var i =0;i<qlist.length;i++){
        if(qlist[i].qaQuestion==searchval){
            qwords = qlist[i];
            break;
        }
    }
    return qwords;
}

function questionAnswer(lid,questionstr,answerstr,num,kwords,qsstyle){

    //调用 统计记录
   // saveRecord(lid,questionstr,answerstr,kwords);
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
    //判断是图片还是音视频
    if(qsstyle && qsstyle==1){
         var index = answerstr .lastIndexOf(".");
         var stype = answerstr .substring(index + 1, answerstr .length);
         if(stype=="mp3"){
             var audio = $("<audio autoplay controls='controls' controlsList='nodownload'></audio>");
             audio.attr("src","video/"+answerstr);
             anscontent.append(audio);
         }else if(stype=="mp4"){
             var video = $("<video autoplay controls='controls' controlsList='nodownload'></video>");
             video.attr("src","video/"+answerstr);
             anscontent.append(video);
         }else{
             var qaimg = $("<img width='400px' height='300px'/>");
             qaimg.attr("src","video/"+answerstr);
             anscontent.append(qaimg);
         }
    }else{
        anscontent.append(answerstr);
    }


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
    if($("#content")[0].scrollHeight>511){
        var scrollheight = $("#content")[0].scrollHeight-511;
        $('#content').css("top",-scrollheight);
    }
    // $(".other").html("");
}


function chanlAnswer(clist){
    var answerdiv = $('<div class="answer"></div>');
    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
    answerdiv.append(ansico);
    var anscontent = $('<div class="text"></div>');
    var kum=0;
    var contentdiv = $('<div></div>');
    contentdiv.addClass("qcontent");
    anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
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
        });
        span.attr("chnlid",clist[i].channelId);
        span.attr("pid",clist[i].parentid);
        span.attr("title",clist[i].chnlname);
        span.attr("url",clist[i].chnlurl);
        span.append("【"+kum+"】"+clist[i].chnlname);
        span.on("click",function(e){
            var thisid = $(this).attr("chnlid");
           // var thispid = $(this).attr("pid");
            var chnlurl = $(this).attr("url");
            var thischnlname = $(this).attr("title");
            var chnles = getChannelsByChnlid(thisid);
            if(chnles.length>0){
                //显示相关栏目。
                chanlAnswer(chnles);
            }else{
                //查询相关问题
                getQAByCid(thisid,chnlurl,thischnlname);
            }
            //saveRecord($(this).attr("cid"),$(this).attr("title"),$(this).attr("href"),inputVal);
        });
        span.append("<br>");
        contentdiv.append(span);
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
}

function questionList(qlist,chnlurl,chnlName){
    var answerdiv = $('<div class="answer"></div>');
    var ansico = $('<div class="ico"><img src="images/jqr_ico_01.png" width="84" height="71" alt=""></div>');
    answerdiv.append(ansico);
    var anscontent = $('<div class="text"></div>');
    var kum=0;
    var contentdiv = $('<div></div>');
    contentdiv.addClass("qcontent");
    anscontent.append('<span style="font-weight: normal;">根据您的提问，为您筛选出以下情况，您可以点击查看：<br></span>');
    for(var j=0;j<qlist.length;j++){
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

        var qtitle = qlist[j].qaQuestion;
        if(qtitle.length>26)
            qtitle = qtitle.substring(0,26)+"...";
        span.append("【"+kum+"】"+qtitle);
        span.attr("title",qlist[j].qaQuestion);
        span.attr("lid","q"+qlist[j].id).attr("answer",qlist[j].qaAnswer);
        span.attr("qastyle",qlist[j].qaFormat);
        span.on("click",function(){
            questionAnswer($(this).attr("lid"), $(this).attr("title"), $(this).attr("answer"),0,"",$(this).attr("qastyle"));
            saveRecord($(this).attr("lid"),$(this).attr("title"),$(this).attr("answer"),chnlName);
        });
        span.append("<br>");
        contentdiv.append(span);
    }
    anscontent.append(contentdiv);
    if(qlist.length>8 && chnlurl.length>0){
        var others = $("<div></div>");
        others.addClass("Gd");
        others.append('想了解更多具体内容，点击<a style="color: blue;display: inline;cursor: pointer;" href="'+chnlurl+'" target="_blank">这里</a>');
        anscontent.append(others);
    }
    anscontent.append('<div class="jt"></div>');
    answerdiv.append(anscontent);
    answerdiv.append('<p class="clear"></p>');
    $('#content').append(answerdiv).append('<div style="height: 10px;"></div>');
    if($("#content")[0].scrollHeight>511){
        var scrollheight = $("#content")[0].scrollHeight-511;
        $('#content').css("top",-scrollheight);
    }
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