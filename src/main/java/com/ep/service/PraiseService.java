package com.ep.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;


public interface PraiseService {

	public JSONObject savePraise(HttpServletRequest request, HttpServletResponse response);
	
	public JSONObject selectPraiseTotal();
	
	public JSONObject selectPraise(HttpServletRequest request);
	
	public JSONObject selectPraiseTotalByDMY(HttpServletRequest request);
}
