(function(){
	if(!window.cpt_rob) window.cpt_rob = {};
	cpt_rob.CPT_CommQuestion_Content_Title = function(title){
		this.$el=$("<div></div>").addClass("title");
		this.el=this.$el[0];
		this.$el.append("<h2>"+title+"</h2>");
	}
	
})();