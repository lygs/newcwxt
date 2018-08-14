package com.ep.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ep.entity.Channels;
import com.ep.entity.IndividualWord;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.entity.WordExpansion;
import com.ep.service.ChannelService;
import com.ep.service.IndividualWordService;
import com.ep.service.LuceneService;
import com.ep.service.QuestionAnswerService;
import com.ep.service.WordExpansionService;
import com.ep.util.CMyString;
import com.ep.util.LuceneUtil;
import com.ep.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/questionSearch")
public class LuceneController {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Resource
	private IndividualWordService iws;
	@Resource
	private WordExpansionService wes;
	@Resource
	private ChannelService channelService;
	@Resource
	private QuestionAnswerService questionAnswerService;
	@Resource
	private LuceneService luceneService;
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		this.request = request;
		this.response = response;
	}
	
	@RequestMapping("/searchq")
	public void searchq() throws Exception{
		String searchContent = CMyString.filterForHTMLValue(request.getParameter("content"));
		JSONObject resultjson = new JSONObject();
		if(!CMyString.isEmpty(searchContent)) {
			/*String words = LuceneUtil.search(searchContent, 5);
			if(words.length()>0 && words.length()==searchContent.length())
				searchContent = words;*/
			resultjson =  getwords(searchContent);
		}else {
			 resultjson.put("status","0");
			 resultjson.put("msg", "参数错误");
		}
		response.getWriter().println(resultjson.toString());
	}
	
	public JSONObject getwords(String searchContent) {
		JSONObject resultjson = new JSONObject();
		 List<WordExpansion> list = wes.getData(searchContent, "9", "1"); 
		 if(list.size() > 0) {
				List<String> liststr = new  ArrayList<String>();
				for(int i=0; i<list.size(); i++) {
					String content = list.get(i).getContent();
					if(content.indexOf("；")!=-1) {
						content = content.replace("；", ";");
					}
					String[] s = content.split(";");
					boolean f = false;
					for(int j=0; j<s.length; j++) {
						if(searchContent.equals(s[j])) {
							f = true;break;
						}
					}
					if(f) {
						for(int k=0; k<s.length; k++) {
							liststr.add(s[k]);
						}
					}
					
				}
				List<IndividualWord> individualWordLists = new  ArrayList<IndividualWord>();
				for(int i=0; i<liststr.size(); i++) {
					List<IndividualWord> iwl = iws.getDataByTitle(liststr.get(i), "9", "1", "", "");
					individualWordLists.addAll(iwl);
				}
				List<QuestionAnswerEntity> qaLists = new  ArrayList<QuestionAnswerEntity>();
				for(int i=0; i<liststr.size(); i++) {
					List<QuestionAnswerEntity> qaList = questionAnswerService.getQuestionAnswerAllList(9, 1, liststr.get(i), "");
					qaLists.addAll(qaList);
				}
				JSONObject jsonObject = new JSONObject();
				if(individualWordLists.size()>0) {
					jsonObject.put("wordList", individualWordLists);
				 }else if(qaLists.size()>0){
					 jsonObject.put("wordList", qaLists);
				 }
				jsonObject.put("words", liststr);
				jsonObject.put("searchContent", searchContent);
				resultjson.put("data", jsonObject);
				resultjson.put("status","1");
				resultjson.put("msg", "操作成功");
		 }else {
			 //分词返回多个单词集合
			 boolean isnum = searchContent.matches("[0-9]+"),flag =false;
			 //1、5字以下完全匹配   
			 JSONObject jsonObject = new JSONObject();
			 //联想词完全匹配
			 List<IndividualWord> inList = iws.getDataByTitle(searchContent, "9", "1");
			 if(inList.size()>0) {
				 jsonObject.put("words", "");
				 jsonObject.put("wordList", inList);
				 jsonObject.put("searchContent", searchContent);
				 resultjson.put("data", jsonObject);
				 resultjson.put("status","1");
				 resultjson.put("msg", "操作成功");
				 flag =true;
			 }else {
				 //标 准问题完全匹配
				 List<QuestionAnswerEntity> qaList = questionAnswerService.getQuestionAnswerAllList("9", "1", searchContent);
				 if(qaList.size()>0) {
					 jsonObject.put("words", "");
					 jsonObject.put("wordList", qaList);
					 jsonObject.put("searchContent", searchContent);
					 resultjson.put("data", jsonObject);
					 resultjson.put("status","1");
					 resultjson.put("msg", "操作成功");
					 flag =true;
				 }
				 
			 }			 
			 if(!flag) {
				 //先查栏目是否有，如果没有再纠错
				 List<Channels> listchnl = channelService.findByName(searchContent);
				 if(listchnl.size()==0) {
					 String words = LuceneUtil.search(searchContent, 5);
					 if(words.length()>0 && words.length()==searchContent.length())
						 searchContent = words;
				 }
				 List<IndividualWord> inList2 = iws.getDataByTitle(searchContent, "9", "1");
				 if(inList2.size()>0) {
					 jsonObject.put("words", "");
					 jsonObject.put("wordList", inList2);
					 jsonObject.put("searchContent", searchContent);
					 resultjson.put("data", jsonObject);
					 resultjson.put("status","1");
					 resultjson.put("msg", "操作成功");
					 flag =true;
				 }else {
					 //标 准问题完全匹配
					 List<QuestionAnswerEntity> qaList = questionAnswerService.getQuestionAnswerAllList("9", "1", searchContent);
					 if(qaList.size()>0) {
						 jsonObject.put("words", "");
						 jsonObject.put("wordList", qaList);
						 jsonObject.put("searchContent", searchContent);
						 resultjson.put("data", jsonObject);
						 resultjson.put("status","1");
						 resultjson.put("msg", "操作成功");
						 flag =true;
					 }
				 }	
				 if(!flag) {
					 List<String> wordsList = LuceneUtil.getStrinsByWords(searchContent);
					 if(wordsList.size()==1 && !isnum) {
						 //JSONObject jsonObject = new JSONObject();
						 //单词查询
						 List<IndividualWord> inList1 = iws.getDataByTitle(searchContent, "9", "1", "", "");
						 if(inList1.size()>0) {
							 jsonObject.put("wordList", inList1);
						 }else {
							 List<QuestionAnswerEntity> qaList = luceneService.getData(searchContent, wordsList);
							 jsonObject.put("wordList", qaList);
						 }
						 jsonObject.put("words", "");
						 jsonObject.put("searchContent", searchContent);
						 resultjson.put("data", jsonObject);
						 resultjson.put("status","1");
						 resultjson.put("msg", "操作成功");
					 }else {
						 //多词查询 1、完全匹配 （栏目/问题） 2、分词（栏目/问题）
						 List<IndividualWord> inList1 = iws.getDataByTitle(searchContent, "9", "1", "" ,"");
						 if(inList1.size()>0) {
							 jsonObject.put("wordList", inList1);
						 }else {
							 List<QuestionAnswerEntity> qaList = luceneService.getData(searchContent, wordsList);
							 jsonObject.put("wordList", qaList);
						 }
						 jsonObject.put("words", "");
						 jsonObject.put("searchContent", searchContent);
						 resultjson.put("data", jsonObject);
						 resultjson.put("status","1");
						 resultjson.put("msg", "操作成功");
					 }
				 }
				 
				 
			 } 
		 }
		 return resultjson;
		
	}
	
	@RequestMapping("/search")
	public void search() throws Exception{
		String searchContent = CMyString.filterForHTMLValue(request.getParameter("content"));
		JSONObject resultjson = new JSONObject();
		if(!CMyString.isEmpty(searchContent)) {
			 /**查询同意词*/
			 List<WordExpansion> list = wes.getData(searchContent, "9", "1");
			 if(list.size() > 0) {
					List<String> liststr = new  ArrayList<String>();
					for(int i=0; i<list.size(); i++) {
						String[] s = list.get(i).getContent().split(";");
						for(int j=0; j<s.length; j++) {
							liststr.add(s[j]);
						}
					}
					List<IndividualWord> IndividualWordLists = new  ArrayList<IndividualWord>();
					List<Channels> channelLists = new  ArrayList<Channels>();
					for(int i=0; i<liststr.size(); i++) {
						List<IndividualWord> iwl = iws.getDataByTitle(liststr.get(i), "9", "1", "", "");
						for(int k=0; k<iwl.size(); k++) {
							IndividualWordLists.add(iwl.get(k));
						}
						List<Channels> channelList = channelService.findByName(liststr.get(i));
						for(int j=0; j<channelList.size(); j++) {
							channelLists.add(channelList.get(j));
						}
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("wordList", IndividualWordLists);
					jsonObject.put("channelList", channelLists);
					resultjson.put("data", jsonObject);
					resultjson.put("status","1");
					resultjson.put("msg", "操作成功");
			 }else {
				 //分词返回多个单词集合
				 List<String> wordsList = LuceneUtil.getStrinsByWords(searchContent);
				 if(wordsList.size() > 1) {
					 try {
						 /*查询标准问题*/
						 List<QuestionAnswerEntity> qaList  = questionAnswerService.getQuestionAnswerListByWords(wordsList);
						 List<Channels> channelLists = new  ArrayList<Channels>();
						 if(qaList.size()>8) {
							 List<Integer> idlist = new ArrayList<Integer>();
							 for(int i=0;i<qaList.size();i++) {
								 if(qaList.get(i).getQaChnlid()<1) continue;
								 if(idlist.size()>0) {
									 boolean flag = false;
									 for (Integer id:idlist) {
										if(id==qaList.get(i).getQaChnlid()) {
											flag=true;break;
										}
									  }
									  if(!flag) idlist.add(qaList.get(i).getQaChnlid());
								 }else {
									 idlist.add(qaList.get(i).getQaChnlid()); 
								 }
								 
								 
							 }
							 if(idlist.size()>0) {
								 for (Integer id:idlist) {
									 Channels channels = channelService.findById(id);
									 channelLists.add(channels);
								  }
							 }
						 }
						 
						 /**查询相关栏目*/
						 for (String str : wordsList) {
							 List<Channels> channelList = channelService.findByName(str);
							 for(Channels c:channelList) {
								 channelLists.add(c);
							 }
						 }
						 JSONObject jsonObject = new JSONObject();
						 jsonObject.put("wordList", qaList);
						 jsonObject.put("channelList", channelLists);
						 resultjson.put("data", jsonObject);
						 resultjson.put("status","1");
						 resultjson.put("msg", "操作成功");
					} catch (Exception e) {
						// TODO: handle exception
						resultjson.put("status","0");
						resultjson.put("msg", "参数错误");
					}
									 
				 }else if(wordsList.size() == 1){
					 //单词查询
					 List<IndividualWord> inList = iws.getDataByTitle(searchContent, "9", "1", "", "");
					 List<Channels> channelList = channelService.findByName(searchContent);
					 JSONObject jsonObject = new JSONObject();
					 jsonObject.put("wordList", inList);
					 jsonObject.put("channelList", channelList);
					 resultjson.put("data", jsonObject);
					 resultjson.put("status","1");
					 resultjson.put("msg", "操作成功");
				 }else {
					 resultjson.put("data", "");
					 resultjson.put("status","1");
					 resultjson.put("msg", "没有数据");
				 }
			 }
			 
		}else {
			 resultjson.put("status","0");
			 resultjson.put("msg", "参数错误");
		}
		response.getWriter().println(resultjson.toString());
	}
}
