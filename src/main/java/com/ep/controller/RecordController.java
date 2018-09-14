package com.ep.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.entity.QuestionAnswerEntity;
import com.ep.entity.RecordEntity;
import com.ep.service.HighFrequencyWordsService;
import com.ep.service.QuestionAnswerService;
import com.ep.service.RecordService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;
import com.ep.util.LuceneUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/record")
public class RecordController {
	@Autowired
	private RecordService recordService;
	@Autowired
	private QuestionAnswerService qaService;
	@Autowired
	private HighFrequencyWordsService  hfwService;
	
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
	 * 插入聊天记录
	 */
	@ResponseBody
	@RequestMapping(value="/saveRecord",method=RequestMethod.POST)
	public JSONObject saveRecord() {
		RecordEntity rEntity = new RecordEntity();
		JSONObject json = new JSONObject();
		try {
			String rQuestion = CMyString.filterForHTMLValue(request.getParameter("rQuestion"));// 问
			String rAnswer = CMyString.filterForHTMLValue(request.getParameter("rAnswer"));// 答
			String ips = CMyString.filterForHTMLValue(request.getParameter("ips"));// ip
			String keyword = CMyString.filterForHTMLValue(request.getParameter("keyword"));//关键词

			String rQuestionId = CMyString.filterForHTMLValue(request.getParameter("rQuestionId"));// 问题id
			
			if (StringUtils.isNotBlank(rQuestion) &&StringUtils.isNotBlank(rAnswer)){
				if(StringUtils.isNotBlank(rQuestionId)) {
					String s = rQuestionId.substring(0, 1);
					if(s.equals("c")) {
						rEntity.setrChnnelid(rQuestionId.substring(1));
					}
					if(s.equals("q")) {
						rEntity.setrQuestionId(rQuestionId.substring(1));
					}
				}
				
				rEntity.setrQuestion(rQuestion);
				rEntity.setrAnswer(rAnswer);
				String date = DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				rEntity.setrCreatetime(date);
				rEntity.setUserIp(ips); //网友IP
				rEntity.setKeyword(keyword);
				int resutl = recordService.saveRecord(rEntity);
				if(resutl > 0) {
					json.put("result", "success");
				}else {
					json.put("result", "error");
					System.out.println("保存记录失败！");
				}
			} else {
				json.put("result", "error");
			}
			
			//关键字保存
			if(keyword.length() >0) {
				List<String> words = LuceneUtil.getStrinsByWords(keyword);
				if(words.size() > 0) {
					for(int i=0; i<words.size(); i++) {
						hfwService.updataOrSaveHFW(words.get(i));
					}
				}
					
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", "error");
			return json;
		}
		
		return json;
	}
	
	/**
	 * 后台查询所有记录
	 */
	@RequestMapping("/getRecordList")
	public void getRecordList() {
		JSONObject json = new JSONObject();
		try {
			String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));
			
			String startDate = CMyString.filterForHTMLValue(request.getParameter("startDate"));
			String endDate = CMyString.filterForHTMLValue(request.getParameter("endDate"));
			String searchTitle = CMyString.filterForHTMLValue(request.getParameter("searchTitle"));
			String order = CMyString.filterForHTMLValue(request.getParameter("order"));
			
			boolean num = pageSize.matches("[0-9]+");
			boolean pagesize = pageNumber.matches("[0-9]+");
			if (num && pagesize) {
				 String string = recordService.getAllList(Integer.valueOf(pageSize),Integer.valueOf(pageNumber), startDate,  endDate, searchTitle,Integer.valueOf(order));
				int total = recordService.getRecordEntityTotal( startDate,  endDate, searchTitle,Integer.valueOf(order));
				json.put("result", string);
				json.put("total", total);
			} else {
				json.put("result", "error");
			}
			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	@RequestMapping("/delRecord")
	public void delRecord() {
		JSONObject json = new JSONObject();
		try {
			String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
			boolean num = ids.matches("[0-9]+");
			if (num) {
				 String string = recordService.delRecord(Integer.parseInt(ids));
				json.put("result", string);
			} else {
				json.put("result", "error");
			}
			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 查询周 月 年 的浏览记录数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRecordTotalList")
	public JSONObject getRecordTotal() {
		
		return recordService.getRecordTotal(request);
		
	}
	
	@RequestMapping("/updRecord")
	public void updRecord() {
		JSONObject json = new JSONObject();
		try {
			String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
			String rCriterion = CMyString.filterForHTMLValue(request.getParameter("rCriterion"));
			
			boolean num = ids.matches("[0-9]+");
			if (num) {
				 String string = recordService.delRecord(Integer.parseInt(ids));
				json.put("result", string);
			} else {
				json.put("result", "error");
			}
			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
