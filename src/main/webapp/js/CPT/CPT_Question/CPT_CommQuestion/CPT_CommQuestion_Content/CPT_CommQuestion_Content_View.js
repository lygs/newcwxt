(function(){
	var Events={
		QMSG:0,
	};
	if(!window.cpt_rob) window.cpt_rob = {};
	var EventBase = new np.Utils.Event_Base(Events);
	cpt_rob.CPT_CommQuestion_Content_View = function(_obj){
		EventBase.call(this);
		 this.$el=$("<li></li>").css("cursor","pointer");
	     this.el = this.$el[0];
	     this.obj=_obj; 
	     this.$el.attr("title",this.obj.qaQuestion);
		var that = this;
		this.$el.on("click",function(){
			that.fireEvents("QMSG", that.obj);
		});
	     //overflow: hidden;
	     //text-overflow: ellipsis;
	     //white-space: nowrap;
	     this.$el.append(this.obj.qaQuestion);
	}
	
})();