package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ep.entity.LogMessage;
import com.ep.entity.Sysuser;
import com.ep.service.KnowledgePointService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;

@Controller
@RequestMapping("/know")
public class KnowPointController {
    @Autowired 
    private KnowledgePointService knowledgePointService;
    HttpServletRequest request;
    HttpServletResponse response;
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,
    HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        this.request = request;
        this.response = response;
    }
    /*
     *分页查询知识点
     * author:刘瑶
     */
    @RequestMapping("/getKnowAllList")
    public void getKnowAllList(){
    	try {
	        String pageNum =CMyString.filterForHTMLValue( request.getParameter("pageNumber"));
	        String pageSize =CMyString.filterForHTMLValue( request.getParameter("pageSize"));
	        String name = CMyString.filterForHTMLValue(request.getParameter("searchName"));
	        String chnlId = CMyString.filterForHTMLValue(request.getParameter("chnlId"));
	        boolean result = pageSize.matches("[0-9]+");
	        boolean result1 = pageNum.matches("[0-9]+");
	        String str = "";
	        if(result&&StringUtils.isNotBlank(pageSize)&&StringUtils.isNotBlank(pageNum)&&result1){
	        	if(chnlId.equals("0")) {
	        		chnlId = "";
	        	}
	        	str=knowledgePointService.getKnowAllList(name,pageSize,pageNum,chnlId,request);
	        	response.getWriter().println(str);
	        }else{
	        	JSONObject obj = new JSONObject();
	        	obj.put("results", "error");
	        	str = obj.toString();
	        	response.getWriter().println(str);
	        }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    /*
     *根据类型ID查询所有知识点
     * author:刘瑶
     */
    @RequestMapping("/getKnowAllByChnlId")
    public void getKnowAllByChnlId(){
        response.setCharacterEncoding("UTF-8");
        try {
        	 String chnlId = CMyString.filterForHTMLValue(request.getParameter("chnlId"));
 	        boolean result = chnlId.matches("[0-9]+");
 	        String str = "";
 	        if(result&&StringUtils.isNotBlank(chnlId)){
 	        	if(chnlId.equals("0")) {
 	        		chnlId = "";
 	        	}
 	        	str=knowledgePointService.getKnowAll(chnlId);
 	        	response.getWriter().println(str);
 	        }else{
 	        	JSONObject obj = new JSONObject();
 	        	obj.put("results", "error");
 	        	str = obj.toString();
 	        	response.getWriter().println(str);
 	        }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/getKnowAll")
    public void getKnowAll(){
        response.setCharacterEncoding("UTF-8");
        try {
 	        String str = "";
 	        	str=knowledgePointService.getKnowAll();
 	        	response.getWriter().println(str);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /*
     *添加知识点
     * author:刘瑶
     */
    @RequestMapping("/addKnows")
    public void addKnows(){
        String name = CMyString.filterForHTMLValue(request.getParameter("name")); //知识点名称
        String channelId = request.getParameter("channelId");//栏目名称 暂未使用
        try {
        	if(StringUtils.isNotBlank(name)){
        		Sysuser Sysuser = (Sysuser) request.getSession().getAttribute("user");
        		int id = Sysuser.getUserId();
        		String str= knowledgePointService.addKnows(name,channelId,id);
                response.getWriter().print(str);
        	}else{
        		JSONObject obj = new JSONObject();
        		obj.put("results", "error");//知识点为空
        		response.getWriter().print(obj.toString());
        	}
        	
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     *根据id删除知识点
     * author:刘瑶
     */
    @RequestMapping("/delKnowsById")
    public void delKnowsById(){
        String id = CMyString.filterForHTMLValue(request.getParameter("id"));
        try {
        	if(StringUtils.isNotBlank(id)){
        		  String str = knowledgePointService.delKnowsById(id);
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
    
    /*
     *修改知识点
     * author:刘瑶
     */
    @RequestMapping("/updateKnows")
    public void updateKnows(){
        String id =CMyString.filterForHTMLValue( request.getParameter("id"));
        String edit_name = CMyString.filterForHTMLValue(request.getParameter("edit_name"));
        String edit_channelId =CMyString.filterForHTMLValue(request.getParameter("edit_channelId"));
        try {
        	if(StringUtils.isNotBlank(id)){
        		 String str = knowledgePointService.updateKnows(id,edit_name, edit_channelId);
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
    /*
     *知识点设为热点
     * author:刘瑶
     */
    @RequestMapping("/updateKnowsByHot")
    public void updateKnowsByHot(){
        String id =CMyString.filterForHTMLValue(request.getParameter("id"));
        String isHot =CMyString.filterForHTMLValue(request.getParameter("isHot"));
        try {
        	if(StringUtils.isNotBlank(id)){
        		 String str = knowledgePointService.updateKnowsByHot(id,isHot);
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
    /*
     *查询热点知识点
     * author:刘瑶
     */
    @RequestMapping("/getHotKnowledge")
    public void getHotKnowledge(){
        String pageNum =CMyString.filterForHTMLValue( request.getParameter("pageNumber"));
        String pageSize =CMyString.filterForHTMLValue( request.getParameter("pageSize"));
        boolean result = pageSize.matches("[0-9]+");
        boolean result1 = pageNum.matches("[0-9]+");
        try{
        	 String str = "";
             if(result && result1){
             	str=knowledgePointService.getHotKnowledge(pageSize,pageNum);
             	response.getWriter().println(str);
             }else{
             	JSONObject obj = new JSONObject();
             	obj.put("results", "error");
             	str = obj.toString();
             	response.getWriter().println(str);
             }
        }catch(IOException e){
        	e.printStackTrace();
        }
       
    }
    
}
