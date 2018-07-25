package com.ep.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.entity.HighFrequencyWordsEntity;
import com.ep.service.HighFrequencyWordsService;
import com.ep.util.CMyString;

import net.sf.json.JSONObject;

/**
* @author Zhaoxh
* @version 创建时间：2018年6月26日 下午1:12:18
* @about 关键字
*/
@Controller
@RequestMapping("/hfw")
public class HighFrequencyWordsController {

	@Autowired
	private HighFrequencyWordsService  hfwService;
	
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
	@ResponseBody
	@RequestMapping(value="/getTopEleven" ,produces = "application/json; charset=UTF-8")
	public List<HighFrequencyWordsEntity> getTopEleven() {
	
		try {
			List<HighFrequencyWordsEntity> list = hfwService.getTopEleven();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value="/getAllHighFre")
	public void getAllHighFre() {
		try {
			 String pageNum =CMyString.filterForHTMLValue( request.getParameter("pageNumber"));
		        String pageSize =CMyString.filterForHTMLValue( request.getParameter("pageSize"));
		        String name = CMyString.filterForHTMLValue(request.getParameter("searchName"));
		        boolean result = pageSize.matches("[0-9]+");
		        boolean result1 = pageNum.matches("[0-9]+");
		        String str = "";
		        if(result&&StringUtils.isNotBlank(pageSize)&&StringUtils.isNotBlank(pageNum)&&result1){
		        	str=hfwService.getGpcAllList(name,pageSize,pageNum);
		        	response.getWriter().println(str);
		        }else{
		        	JSONObject obj = new JSONObject();
		        	obj.put("results", "error");
		        	str = obj.toString();
		        	response.getWriter().println(str);
		        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
