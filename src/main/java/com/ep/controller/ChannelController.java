package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.entity.Sysuser;
import com.ep.service.ChannelService;
import com.ep.util.CMyString;

import net.sf.json.JSONObject;
/**
 * 
* <p>Title: ChannelController</p>  
* <p>Description: </p>  
* @author liuyao  
* @date 2018年5月14日
 */
@Controller
@RequestMapping("/chnl")
public class ChannelController {
	@Autowired
	private ChannelService chnlService;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		this.request = request;
		this.response = response;
	}

	/**
	 *查詢所有欄目
	 */
	@ResponseBody
	@RequestMapping("/getAll")
	public void getAll() {
		try {
			String str = chnlService.getAllChnl();
			if(StringUtils.isBlank(str)){
				JSONObject obj = new JSONObject();
				obj.put("results", "null");
				str = obj.toString();
			}
			response.getWriter().print(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 @RequestMapping("/addChnl")
	    public void addChnl(){
	        String name = CMyString.filterForHTMLValue(request.getParameter("name")); //知识点名称
	        String channelId =CMyString.filterForHTMLValue(request.getParameter("channelId"));//栏目名称 暂未使用
	        String kurl =CMyString.filterForHTMLValue(request.getParameter("kurl"));
	        try {
	        	String str = "";
	        	if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(channelId)&&StringUtils.isNotBlank(kurl)){
	        		Sysuser sysuser = (Sysuser) request.getSession().getAttribute("user");
	        		str= chnlService.addChnl(name, channelId, sysuser.getUserName(), new Timestamp(System.currentTimeMillis()),kurl);
	        	}else{
	        		JSONObject obj = new JSONObject();
	        		obj.put("results", "error");//知识点为空
	        		str = obj.toString();
	        	}
	        	response.getWriter().print(str);
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 /*
	     *修改知识点父级ID
	     * author:yyx
	     */
	    @RequestMapping("/upChannelPidById")
	    public void upChannelPidById(){
	    	 String thisid =CMyString.filterForHTMLValue( request.getParameter("id"));
		     String pid =CMyString.filterForHTMLValue( request.getParameter("pid"));
		     JSONObject jsonObject = new JSONObject();
		     if(!CMyString.isEmpty(thisid) && !CMyString.isEmpty(pid)) {
		    	 String str=chnlService.upChannelPidById(thisid,pid);
		    	 jsonObject.put("result", str);
		     }else {
		    	 jsonObject.put("result", "error");
		     }
	        response.setCharacterEncoding("UTF-8");
	        try {
	            response.getWriter().println(jsonObject.toString());
	        } catch (IOException e){
	            e.printStackTrace();
	        }
	    }
	 
	 
	 	@RequestMapping("/updateChnl")
	    public void updateChnl(){
	        String id =CMyString.filterForHTMLValue( request.getParameter("id"));
	        String edit_name = CMyString.filterForHTMLValue(request.getParameter("edit_name"));
	        String edit_channelId =CMyString.filterForHTMLValue(request.getParameter("edit_channelId"));
	        String chnlurl =CMyString.filterForHTMLValue(request.getParameter("chnlurl"));
	        try {
	        	if(StringUtils.isNotBlank(id)){
	        		 String str = chnlService.updateChnl(id,edit_name, edit_channelId,chnlurl);
	                  response.getWriter().print(str);
	        	}else{
	        		JSONObject obj = new JSONObject();
	        		obj.put("results", "error");//知识点为空
	        		String str = obj.toString();
	        		response.getWriter().print(str);
	        	}
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
    }
	 	
	 	 @RequestMapping("/delChnlById")
	    public void delChnlById(){
	        String id = CMyString.filterForHTMLValue(request.getParameter("id"));
	        try {
	        	if(StringUtils.isNotBlank(id)){
	        		  String str = chnlService.delChnlById(id);
	                  response.getWriter().print(str);
	        	}else{
	        		JSONObject obj = new JSONObject();
	        		obj.put("results", "error");//知识点为空
	        		String str = obj.toString();
	        		response.getWriter().print(str);
	        	}
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
