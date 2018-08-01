package com.ep.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.entity.Sysuser;
import com.ep.entity.WordExpansion;
import com.ep.service.WordExpansionService;
import com.ep.util.CMyString;

import net.sf.json.JSONObject;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月5日 下午3:12:19
* 扩展词汇请求类
*/
@Controller
@RequestMapping(value = "wordExpansion",produces = "application/json;charset=utf-8")
public class WordExpansionController {

	@Resource
	private WordExpansionService wes;
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
	
	
	/**
	 * 查询
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月6日 下午3:14:15
	 * @Description: TODO
	 * @param content
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getData")
	public String getData(String content ,String pageSize, String pageNumber) {
		JSONObject json = new JSONObject();
		boolean flg = true;
		String str = null;
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
		}else {
			
			content = CMyString.filterForSQL(CMyString.filterForHTMLValue(content));
			List<WordExpansion> list = wes.getData(content, pageSize, pageNumber);
			int total = wes.getTotal();
			json.put("msg", "操作成功");
			json.put("status","1");
			json.put("data", list);
			json.put("total", total);
			
		}
		str = json.toString();
		return str;
	}
	/**
	 * 保存 OR 更新
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月6日 下午3:14:01
	 * @Description: TODO
	 * @param obj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updataOrSave")
	public String updataOrSaveWordExpansion(WordExpansion obj) {
		JSONObject json = new JSONObject();
		String str = null;
		if(obj != null) {
			if( !CMyString.isEmpty(obj.getContent())){
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
				if(user != null) {
					obj.setFounder(user.getUserName());
					str = wes.updataOrSaveWordExpansion(obj);
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
		return str;
		
	}
	
	/**
	 * 删除
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月6日 下午3:13:49
	 * @Description: TODO
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public JSONObject delObjById(String id) {
		JSONObject json = new JSONObject();
		if(id != null && CMyString.isMatches(id)) {
			int r = wes.delObjById(Integer.parseInt(id));
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
