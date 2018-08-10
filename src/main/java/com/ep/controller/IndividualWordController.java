package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ep.entity.IndividualWord;
import com.ep.entity.Sysuser;
import com.ep.service.IndividualWordService;
import com.ep.util.CMyString;
import com.ep.util.FileUtil;

import net.sf.json.JSONObject;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月4日 下午4:22:35
* 联想词
*/
@Controller
@RequestMapping(value = "individualWord",produces = "application/json;charset=utf-8")
public class IndividualWordController {

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Resource
	private IndividualWordService iws;
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		this.request = request;
		this.response = response;
	}
	
	@ResponseBody
	@RequestMapping("/getDataByTitle")
	public String getDataByTitle(String title ,String pageSize, String pageNumber, String startTime, String endTime) {
		JSONObject json = new JSONObject();
		boolean flg = true;
		String str = null;
		try {
			if(!CMyString.isEmpty(pageSize) && !CMyString.isEmpty(pageNumber)) {
			if(!CMyString.isMatches(pageSize) || !CMyString.isMatches(pageNumber)) {
				flg = false;
				}
			}else {
				pageSize = "10"; 
				pageNumber = "1";
			}
			if(!flg) {
				json.put("msg", "参数错误");
				json.put("status","0");
				str = json.toString();
			}else {
				
				title = CMyString.filterForHTMLValue(title);
				
				
				List<IndividualWord> list= iws.getDataByTitle(title, pageSize, pageNumber, startTime, endTime);
				int total = iws.getTotal(title, startTime, endTime);
				json.put("msg", "操作成功");
				json.put("status","1");
				json.put("data",list);
				json.put("total",total);
				str = json.toString();
			}
			} catch (Exception e) {
				e.printStackTrace();
				json.put("msg", "程序异常");
				json.put("status","-1");
		}
		
		
		return str;
	}
	
	@ResponseBody
	@RequestMapping("/updataOrSave")
	public String updataOrSaveIndividualWord(IndividualWord obj, @RequestParam("file") MultipartFile file) {
		JSONObject json = new JSONObject();
		String str = null;
		boolean flg = true;
		String saveName = null;
		String upMsg = null;
		Sysuser user = (Sysuser) request.getSession().getAttribute("user");
		if(user != null) {
			obj.setQaFormat(0);
			if (file != null && file.getSize() > 0) {
				
				String path = request.getSession().getServletContext().getRealPath("/")+"video\\";
				String fileName = file.getOriginalFilename();
				
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				double fileSize = file.getSize() / 1024.0 / 1024.0;
				
				if(suffix.equals("mp3") || suffix.equals("mp4") || suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg")) {
					
					if (fileSize > 20) {
						json.put("status", 0);
						json.put("msg", "文件不能大于20M，请重新上传！");
						flg = false;
					}else {
						try {
							saveName = String.valueOf(System.currentTimeMillis()) + "." +suffix;
							flg = FileUtil.saveFile(path, saveName, file.getInputStream());
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						if(!flg) {
							upMsg = "文件上传失败";
							flg = false;
						}
					}
					
				}else {
					upMsg = "格式不符合要求，请重新上传！";
					flg = false;
				}
				
				if(flg) {
					
					obj.setQaAnswer(saveName);
					obj.setQaFormat(1);
				}
			}
		}
		
		//成功上传
		if(flg) {
			
			if(obj != null) {
				if(!CMyString.isEmpty(obj.getQaQuestion()) && !CMyString.isEmpty(obj.getQaAnswer())){
					if(user != null) {
						obj.setFounder(user.getUserName());
						obj.setLevel(0);
						str = iws.updataOrSaveIndividualWord(obj);
						
					}else {
						json.put("msg", "请登录后再操作");
						json.put("status","0");
						str = json.toString();
					}
				}else{
					json.put("msg", "参数错误");
					json.put("status","0");
					str = json.toString();
				}
			}else {
				json.put("msg", "参数错误");
				json.put("status","0");
				str = json.toString();
			}
		}else {
			json.put("msg", upMsg);
			json.put("status","0");
			str = json.toString();
		}
		
		return str;
		
	}
	
	@ResponseBody
	@RequestMapping("/del")
	public JSONObject delObjById(String id) {
		JSONObject json = new JSONObject();
		if(id != null && CMyString.isMatches(id)) {
			int r = iws.delObjById(Integer.parseInt(id));
			if(r > 0) {
				json.put("msg", "操作成功");
				json.put("status","1");
			}else {
				json.put("msg", "操作失败");
				json.put("status","-1");
			}
		}else {
			json.put("msg", "参数错误");
			json.put("status","0");
		}
		return json;
		
	}
	
	
	
	
}
