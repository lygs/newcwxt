(function(){
	var Events = {
		SENDQMSG:0
	};
	if(!window.cpt_rob) window.cpt_rob = {};
	var EventsBase = new np.Utils.Event_Base(Events);
	cpt_rob.CPT_CommQuestion_Content_List=function(_list){
		EventsBase.call(this);
		this.$el = $("<ul></ul>");
		this.el = this.$el[0];
		for(var i=0;i<_list.length;i++){
	    	 this.commObj= new cpt_rob.CPT_CommQuestion_Content_View(_list[i]);
			 var that = this;
			 this.commObj.onEvents("QMSG",function(e){
				 that.fireEvents("SENDQMSG",e);
			 });
	    	 this.$el.append(this.commObj.el);
	     }
	}
})();