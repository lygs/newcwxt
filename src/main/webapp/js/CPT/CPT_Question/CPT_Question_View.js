/**
 * Created by Administrator on 2017/10/18 0018.
 */
(function(){
    if(!window.CPT) window.CPT={};
    CPT.CPT_Question_View=function(){
            this.$el=$("<div></div>");
            this.el =  this.$el[0];
            this.robotimg = "<img src='./images/roobt.png'>";
            this.renImg = "<img src='./images/ren.png'>";
            this.content = "你好！我是智能机器小冰，请问你想咨询点什么呢?";
        }
    CPT.CPT_Question_View.prototype.init = function(){
        this.$el.append(this.robotimg).append(this.content);
    }
    CPT.CPT_Question_View.prototype. sendQuestion = function(question){
        this.content = question;
        this.$el.css("float","right");
        this.$el.append(this.content).append(this.renImg);
    }

    CPT.CPT_Question_View.prototype. getAnwser = function (anwser){
        this.content = anwser;
        this.$el.append(this.robotimg).append(this.content);
            }
})()