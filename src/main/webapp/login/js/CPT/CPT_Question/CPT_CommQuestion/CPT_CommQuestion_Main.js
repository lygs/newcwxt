(function(){
	var Events={
		QMSGS:0,
	};
	if(!window.cpt_rob) window.cpt_rob = {};
	var EventBase = new np.Utils.Event_Base(Events);
	cpt_rob.CPT_CommQuestion_Main = function(dom,_list,title){
		EventBase.call(this);
		this.$el=dom;
		this.listView=new cpt_rob.CPT_CommQuestion_Content_List(_list);
		var that = this;
		this.listView.onEvents("SENDQMSG",function(e){
			that.fireEvents("QMSGS",e);
		});
		this.title=new cpt_rob.CPT_CommQuestion_Content_Title(title);
		var list = $("<div></div>").addClass("contMain");
		list.append(this.listView.el);
		this.$el.append(this.title.el);
		this.$el.append(list);
	}
	
})();