package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ep.service.TemplateService;
import com.ep.util.CMyString;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/template")
public class TemplateController {
	@Autowired
	private TemplateService tempService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	@ModelAttribute
	public void reqAndRes(HttpServletRequest req,HttpServletResponse resp) throws UnsupportedEncodingException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		this.request = req;
		this.response = resp;
	}
	
	/**
	 * 查询所有的模板
	 */
	@RequestMapping("/getAll")
	public void getAll() {
		List list = tempService.getAll();
		JSONObject obj = new JSONObject();
		if(list!=null && list.size()>0) {
			obj.put("data", list);
			obj.put("results", "success");
		}else {
			obj.put("data", "null");
			obj.put("results", "null");
		}
		try {
			response.getWriter().print(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 选中模板
	 * 
	 */
	@RequestMapping("/updateSelectedStatus")
	public void updateSelectedStatus() {
		String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
		boolean flag = ids.matches("[0-9]+");
		String str = "";
		JSONObject obj = new JSONObject();
		if(StringUtils.isNotBlank(ids) && flag) {
			tempService.updateOldStatus();
			str = tempService.updateSelectedStatus(ids);
			obj.put("results", str);
			
		}else {
			obj.put("results", "error");
		}
		str = obj.toString();
		try {
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
