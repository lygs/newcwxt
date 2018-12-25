package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.entity.LogMessage;
import com.ep.entity.Sysuser;
import com.ep.entity.WordExpansion;
import com.ep.service.ChannelService;
import com.ep.service.LogsService;
import com.ep.util.CMyString;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * Title: ChannelController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author liuyao
 * @date 2018年5月14日
 */
@Controller
@RequestMapping("/logs")
public class LogsController {
	@Autowired
	private LogsService logService;

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
	 * 查询所有日志
	 */
	@ResponseBody
	@RequestMapping("/getData")
	public void getData(String pageSize, String pageNumber) {
		response.setContentType("text/html;charset=utf-8");
		try {
			JSONObject json = new JSONObject();
			boolean flg = true;
			String str = null;
			if (!CMyString.isEmpty(pageSize) && !CMyString.isEmpty(pageNumber)) {
				if (!CMyString.isMatches(pageSize) || !CMyString.isMatches(pageNumber)) {
					flg = false;
				}
			} else {
				pageSize = "10";
				pageNumber = "1";
			}
			if (!flg) {
				json.put("msg", "参数错误");
				json.put("status", "0");
			} else {

				List<LogMessage> list = logService.findAll(pageSize, pageNumber);
				int total = logService.getTotal();
				json.put("msg", "操作成功");
				json.put("status", "1");
				json.put("data", list);
				json.put("total", total);

			}
			str = json.toString();
			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
