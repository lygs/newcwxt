/**
 * Created by Administrator on 2017/10/17 0017.
 */
(function(){
    if(!window.CPT) window.CPT={};
    CPT.CPT_Question_Main= function(){
       this.$el= $("<div></div>")
           .css("background-color","#0AD5C1");
        this.el =  this.$el[0];

        var question_View =  new CPT.CPT_Question_View();
        question_View.init();
        this.$el.append(question_View.el);
    }
    CPT.CPT_Question_Main.prototype.sendQuestion = function(question){
        var question_View =  new CPT.CPT_Question_View();
        question_View.sendQuestion(question);
        this.$el.append(question_View.el);
    }
    CPT.CPT_Question_Main.prototype.getAnwser = function(anwser){
        var question_View = new CPT.CPT_Question_View();
        question_View.getAnwser(anwser);
        this.$el.append(question_View.el);
    }

})();