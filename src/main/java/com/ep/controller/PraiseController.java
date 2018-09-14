package com.ep.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.service.PraiseService;

import net.sf.json.JSONObject;

/**
 * 点赞记录
 * @author cpt
 *
 */
@Controller
@RequestMapping("/praise")
public class PraiseController {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Autowired
	private PraiseService praiseServiceImpl;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 保存点赞记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/savePraise",method=RequestMethod.POST)
	public JSONObject savePraise() {
		return praiseServiceImpl.savePraise(request, response);
	}
	
	/**
	 * 查询点赞和不满意总数，
	 * @return
	 */
	@ResponseBody
	@RequestMapping("selectPraiseTotal")
	public JSONObject selectPraiseTotal() {
		return praiseServiceImpl.selectPraiseTotal();
	}
	
	/**
	 * 查询年月周的满意不满意数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("selectPraiseTotalByDMY")
	public JSONObject selectPraiseTotalByDMY() {
		return praiseServiceImpl.selectPraiseTotalByDMY(request);
	}
	
	
	/**
	 * 分页查询 点赞记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("selectPraise")
	public JSONObject selectPraise() {
		return praiseServiceImpl.selectPraise(request);
		
	}
	
	@ResponseBody
	@RequestMapping("test")
	public JSONObject test() {
		//String a = new ThirdInterfaceAction().sendGet("http://www.zhxhu.com", "1");
		//System.out.println(a);
		return null;
		
	}
}
