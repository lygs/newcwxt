package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ep.entity.Channels;
import com.ep.entity.KnowledgePointEntity;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.entity.Sysuser;
import com.ep.service.ChannelService;
import com.ep.service.KnowledgePointService;
import com.ep.service.QuestionAnswerService;
import com.ep.service.RecordService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;
import com.ep.util.LuceneUtil;
import com.ep.util.PropertiesUtil;
import com.ep.util.ReadExcel;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/questionAnswer")
public class QuestionAnswerController {
	@Autowired
	private QuestionAnswerService qaService;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private RecordService recordService;

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
	 * 后台查询所有的问题记录 author:陈杰
	 */
	@RequestMapping("/getQuestionAnswerAllList")
	public void getQuestionAnswerAllList() {
		JSONObject json = new JSONObject();
		try {
			String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));
			String qaQuestion = CMyString.filterForHTMLValue(request.getParameter("qaQuestion"));
			String chnlId = CMyString.filterForHTMLValue(request.getParameter("chnlId"));
			String startTime = CMyString.filterForHTMLValue(request.getParameter("startTime"));
			String endTime = CMyString.filterForHTMLValue(request.getParameter("endTime"));
			boolean num = pageSize.matches("[0-9]+");
			boolean pagesize = pageNumber.matches("[0-9]+");
			if (num && pagesize) {
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
				List<QuestionAnswerEntity> list = qaService.getQuestionAnswerAllList(Integer.valueOf(pageSize),
						Integer.valueOf(pageNumber), qaQuestion, chnlId,startTime,endTime);
				int total = qaService.getQuestionAnswerTotal(qaQuestion, chnlId,startTime,endTime);
				if (user != null) {
					json.put("roleId", user.getRoleId());
				}
				json.put("result", list);
				json.put("total", total);
			} else {
				json.put("result", "error");
			}
			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 前端进行提问（依据问题进行查询） author:陈杰
	 * 
	 * @RequestMapping("/getQuestionAnswerByQuestion") public void
	 * getQuestionAnswerByQuestion() { String qaQuestion =
	 * request.getParameter("qaQuestion"); QuestionAnswerEntity qaEntity =
	 * qaService.getQuestionAnswerByQuestion(qaQuestion); String qaAnswer =
	 * qaEntity.getQaAnswer(); try { response.getWriter().println(qaAnswer); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

	/**
	 * 根据问题id删除问题
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/delQuestionAnswerById", produces = "application/json;charset=utf-8")
	public void delQuestionAnswerById() throws IOException {
		JSONObject json = new JSONObject();
		try {
			String id = CMyString.filterForHTMLValue(request.getParameter("id"));
			boolean num = id.matches("[0-9]+");
			if (num) {
				int count = qaService.delQuestionAnswerById(Integer.valueOf(id));
				if (count > 0) {
					//LuceneUtil.deleteDoc("id", id);
					json.put("result", "success");
				} else {
					json.put("result", "error");
				}
			} else {
				json.put("result", "error");
			}

		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		response.getWriter().println(json.toString());

	}
	
	/**
	 * 根据答案为空删除问题
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/delQuestionAnswerByAnswer", produces = "application/json;charset=utf-8")
	public void delQuestionAnswerByAnswer() throws IOException {
		JSONObject json = new JSONObject();
		try {
			//String id = CMyString.filterForHTMLValue(request.getParameter("id"));
			List<QuestionAnswerEntity> list = qaService.findAllByAnswer();
			if (list.size()>0) {
				for(int i=0;i<list.size();i++) {
					qaService.delQuestionAnswerById(list.get(i).getId());
					//LuceneUtil.deleteDoc("id", list.get(i).getId().toString());
				}
				
			
				json.put("result", "success");
				
			} else {
				json.put("result", "error");
			}

		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		response.getWriter().println(json.toString());

	}
	
	
	

	/*
	 * 修改访问量（访问一次+1） author:陈杰
	 */
	@RequestMapping("/updateQuestionAnswerSUMById")
	public void updateQuestionAnswerSUMById() {
		int id = Integer.valueOf(request.getParameter("id"));
		int count = qaService.updateQuestionAnswerSUMById(id);
		
		if (count > 0) {
			// System.out.println("访问量增加1");
		}
	}

	/*
	 * 保存管理添加的标准问题 author:陈杰
	 */
	@RequestMapping("/saveQuestionAnswer")
	public void saveQuestionAnswer() {
		JSONObject json = new JSONObject();
		try {
			String qaQuestion = CMyString.filterForHTMLValue(request.getParameter("qaQuestion"));
			String qaAnswer = request.getParameter("qaAnswer");
			String knowledgePoint = CMyString.filterForHTMLValue(request.getParameter("qaKnowledgePoint"));
			String kewwords = CMyString.filterForHTMLValue(request.getParameter("kewwords"));
			String resource = CMyString.filterForHTMLValue(request.getParameter("resource"));
			//记录ID   
			String recordId = CMyString.filterForHTMLValue(request.getParameter("recordId"));
			if (!CMyString.isEmpty(qaQuestion) && !CMyString.isEmpty(qaAnswer) && !CMyString.isEmpty(knowledgePoint)
					&& !CMyString.isEmpty(kewwords) && StringUtils.isNotBlank(resource)) {
				String date = DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				QuestionAnswerEntity qaEntity = new QuestionAnswerEntity();
				Sysuser sysuser = (Sysuser) request.getSession().getAttribute("user");
				qaEntity.setQaAnswer(qaAnswer);
				qaEntity.setQaQuestion(qaQuestion);
				qaEntity.setQaCreatetime(date);
				qaEntity.setQaCreator(String.valueOf(sysuser.getUserId()));
				qaEntity.setQaKnowledge(Integer.parseInt(knowledgePoint));
				qaEntity.setQaType(0); // 0普通 1常见问题
				qaEntity.setQaResource(resource); // 信息来源
				qaEntity.setQaResourceType("0");
				qaEntity.setQaChnlid(-1);
				qaEntity.setQaKeywords(kewwords);
				int id = qaService.saveQuestionAnswer(qaEntity);
				if (StringUtils.isNotBlank(recordId)) { 
					//当添加的问题是从记录表里设置的，就需要将记录的状态 修改为 已设置1
					recordService.updateObj(Integer.parseInt(recordId));
				}
				if (id > 0) {
					// 添加问题序列化
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(id));
					map.put("qaQuestion", qaQuestion);
					map.put("qaAnswer", qaAnswer);
					map.put("qaKeywords", kewwords);
					/*try {
						LuceneUtil.createSingleIndex(map);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					json.put("result", "success");
				} else {
					json.put("result", "error");
				}
			} else {
				json.put("result", "error");
			}

			response.getWriter().print(json.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			json.put("result", "error");
			try {
				response.getWriter().print(json.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	/*
	 * 添加扩展问题 author:刘瑶
	 */
	@RequestMapping("/saveExtendQuestionAnswer")
	public void saveExtendQuestionAnswer(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String qaQuestion = request.getParameter("problemName"); // 问题
		String qaAnswer = request.getParameter("problemAnswer");// 答案
		String knowledgePoint = request.getParameter("knowledgePoint"); // 知识点
		QuestionAnswerEntity qaEntity = new QuestionAnswerEntity();
		qaEntity.setQaAnswer(qaAnswer);
		qaEntity.setQaQuestion(qaQuestion);
		qaEntity.setQaCreatetime(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		qaEntity.setQaKnowledge(Integer.parseInt(knowledgePoint));
		qaEntity.setQaType(0); // 0普通 1常见问题
		int b = qaService.saveQuestionAnswer(qaEntity);
		/*
		 * try { if (b) { response.getWriter().print(b); } } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
	}

	/*
	 * 根据ID获取单条数据集 author:陈杰
	 */
	@RequestMapping("/getQuestionAnswerById")
	public void getQuestionAnswerById() {
		JSONObject json = new JSONObject();
		try {
			if (!CMyString.isEmpty(request.getParameter("id"))) {
				String id = CMyString.filterForHTMLValue(request.getParameter("id"));
				QuestionAnswerEntity list = qaService.getQuestionAnswerById(Integer.valueOf(id));
				json.put("result", list);
			} else {
				json.put("result", "error");
			}
			response.getWriter().print(json.toString());
		} catch (Exception e) {
			json.put("result", "error");
			try {
				response.getWriter().print(json.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	/*
	 * 依据ID修改问题 author:陈杰
	 */
	@RequestMapping("/updateQuestionAnswerById")
	public void updateQuestionAnswerById() {
		JSONObject json = new JSONObject();
		try {
			String id = CMyString.filterForHTMLValue(request.getParameter("id"));
			String qaQuestion = CMyString.filterForHTMLValue(request.getParameter("qaQuestion"));
			String qaAnswer = request.getParameter("qaAnswer");
			String qaKnowledge = CMyString.filterForHTMLValue(request.getParameter("qaKnowledge"));
			String kewwords = CMyString.filterForHTMLValue(request.getParameter("kewwords"));
			String resources1 = CMyString.filterForHTMLValue(request.getParameter("resources1"));
			if (!CMyString.isEmpty(id) && !CMyString.isEmpty(qaQuestion) && !CMyString.isEmpty(qaAnswer)
					&& !CMyString.isEmpty(qaKnowledge) && !CMyString.isEmpty(kewwords)
					&& StringUtils.isNotBlank(resources1)) {
				QuestionAnswerEntity qaEntity = qaService.getQuestionAnswerById(Integer.valueOf(id));
				if (qaEntity != null) {
					Sysuser sysuser = (Sysuser) request.getSession().getAttribute("user");
					qaEntity.setQaCreator(String.valueOf(sysuser.getUserId()));
					qaEntity.setQaAnswer(qaAnswer);
					qaEntity.setQaQuestion(qaQuestion);
					qaEntity.setQaKnowledge(Integer.valueOf(qaKnowledge));
					qaEntity.setQaKeywords(kewwords);
					qaEntity.setQaResource(resources1);
					int count = qaService.updateQuestionAnswerById(qaEntity);
					if (count > 0) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("id", id);
						map.put("qaQuestion", qaQuestion);
						map.put("qaAnswer", qaAnswer);
						map.put("qaKeywords", kewwords);
						json.put("result", "success");
						response.getWriter().print(json.toString());
					} else {
						json.put("result", "error");
						response.getWriter().print(json.toString());
					}
				} else {
					json.put("result", "error");
				}

			} else {
				json.put("result", "error");
				response.getWriter().print(json.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				json.put("result", "error");
				response.getWriter().print(json.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/*
	 * 前端查询常见问题 author:刘瑶
	 */
	@RequestMapping("/getQuestionByComm")
	public void getQuestionByComm() {
		JSONObject json = new JSONObject();
		try {
			String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));
			boolean num = pageSize.matches("[0-9]+");
			boolean pagesize = pageNumber.matches("[0-9]+");
			if (num && pagesize) {
				List<QuestionAnswerEntity> list = qaService.getQuestionByComm(Integer.valueOf(pageSize),Integer.valueOf(pageNumber), "");
				// String str = qaService.getQuestionByClick(pageSize);
				json.put("result", list);

			} else {
				json.put("result", "error");
			}

			response.getWriter().println(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据知识点查询问题
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getQuestionByKnowledge", produces = "application/json;charset=utf-8")
	public JSONObject getQuestionByKnowledge() {
		JSONObject json = new JSONObject();
		try {
			String qaKnowledgeId = CMyString.filterForHTMLValue(request.getParameter("qaKnowledgeId"));
			String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));
			// System.out.println(qaKnowledgeId+"---"+pageNumber+"====="+pageSize);
			boolean qaKnId = qaKnowledgeId.matches("[0-9]+");
			boolean num = pageNumber.matches("[0-9]+");
			boolean size = pageSize.matches("[0-9]+");
			if (qaKnId && num && size) {
				List list = qaService.getQuestionAnswerByKnowledgeId(Integer.valueOf(qaKnowledgeId),
						Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
				json.put("result", "success");
				json.put("list", list);
			} else {
				json.put("result", "error");
			}
		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * 设为常见问题 author:刘瑶
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateProblemByChangj")
	public JSONObject updateProblemByChangj() {
		JSONObject json = new JSONObject();
		try {
			String id = CMyString.filterForHTMLValue(request.getParameter("id"));
			// 1 设为常见问题 0 取消常见
			String isChangj = CMyString.filterForHTMLValue(request.getParameter("isChangj"));
			boolean num = id.matches("[0-9]+");
			if (num && !CMyString.isEmpty(isChangj)) {
				int count = qaService.updateProblemByChangj(id, isChangj);
				if (count > 0) {
					json.put("result", "success");
				} else {
					json.put("result", "error");
				}
			} else {
				json.put("result", "error");
			}
		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		return json;

	}

	/**
	 * 通过Excel批量添加问题 添加的字段有 问题 答案 创建时间 关键字
	 * 
	 * @param file
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/addQuestionByExcel")
	public void addQuestionByExcel(@RequestParam("file") MultipartFile file) throws IOException {
		JSONObject json = new JSONObject();
		Sysuser sysuser = (Sysuser) request.getSession().getAttribute("user");
		try {
			List<Map<String, String>> list = ReadExcel.getExcelDate(file);
			if (list != null) {
				boolean flag = false;
				for (Map map : list) {
					try {
						String qaQuestion = CMyString.filterForSQL(CMyString.filterForHTMLValue((String) map.get("qaQuestion")));
						String qaAnswer = (String) map.get("qaAnswer");
						String kewwords = CMyString.filterForSQL(CMyString.filterForHTMLValue((String) map.get("qaKeywords")));
						String qaResource = CMyString.filterForSQL(CMyString.filterForHTMLValue((String) map.get("qaResource"))); // 信息来源
						String qaKnowledge = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("qaKnowledge")));// 知识点
						String epcid1 = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid1")));// 知识点栏目1
						String epcid1url = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid1url")));// 知识点栏目1
						String epcid2 = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid2")));// 知识点栏目2
						String epcid2url = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid2url")));// 知识点栏目1
						String epcid3 = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid3")));// 知识点栏目3
						String epcid3url = CMyString
								.filterForSQL(CMyString.filterForHTMLValue((String) map.get("epcid3url")));// 知识点栏目1
						/*System.out.println("qaQuestion:"+qaQuestion);
						System.out.println("qaAnswer:"+qaAnswer);
						System.out.println("kewwords:"+kewwords);
						System.out.println("qaKnowledge:"+qaKnowledge);
						System.out.println("epcid1:"+epcid1);
						System.out.println("epcid1url:"+epcid1url);
						System.out.println("epcid2:"+epcid2);
						System.out.println("epcid2url:"+epcid2url);
						System.out.println("epcid3:"+epcid3);
						System.out.println("epcid3url:"+epcid3url);*/
						String chnnelid = "";
						if(CMyString.isEmpty(qaQuestion)) {
							break;
						}
						
						List<QuestionAnswerEntity> qaList = qaService.getQuestionAnswerByQuestion(qaQuestion, kewwords,qaResource);
						if (qaList.size() > 0) {
							flag =true;
							json.put("result", "success");
						} else {
							if (!CMyString.isEmpty(epcid1)) {
								
								List<Channels> clist = channelService.findByName(epcid1,"0");
								if (clist.size() > 0) {
									chnnelid = clist.get(0).getChannelid().toString();
								} else {
									String channelobj = channelService.addChnl(epcid1, "0", sysuser.getUserName(),new Timestamp(System.currentTimeMillis()),epcid1url);
									JSONObject obj = JSONObject.fromObject(channelobj);
									chnnelid = obj.getString("chnlId");
								}
								
								if (!CMyString.isEmpty(epcid2)) {
									List<Channels> clist1 = channelService.findByName(epcid2,chnnelid);
									if (clist1.size() > 0) {
										chnnelid = clist1.get(0).getChannelid().toString();

									} else {
										String channelobj1 = channelService.addChnl(epcid2,chnnelid, sysuser.getUserName(),
												new Timestamp(System.currentTimeMillis()),epcid2url);
										JSONObject obj1 = JSONObject.fromObject(channelobj1);
										chnnelid = obj1.getString("chnlId");
									}
								}
								
								if (!CMyString.isEmpty(epcid3)) {
									List<Channels> clist2 = channelService.findByName(epcid3,chnnelid);
									if (clist2.size() > 0) {
										chnnelid = clist2.get(0).getChannelid().toString();
									}else{
										String channelobj2 = channelService.addChnl(epcid3,	chnnelid, sysuser.getUserName(),
												new Timestamp(System.currentTimeMillis()),epcid3url);
										JSONObject obj2 = JSONObject.fromObject(channelobj2);
										chnnelid = obj2.getString("chnlId");
									}
								}
								
							}else {
								json.put("result", "error");break;
							}
							int kid = 0;
							List<KnowledgePointEntity> kList = knowledgePointService.getByEpcidAndKname(chnnelid,qaKnowledge);
							if (kList.size() > 0) {
								kid = kList.get(0).getId();
							} else {
								String string = knowledgePointService.addKnows(qaKnowledge, chnnelid, sysuser.getUserId());
								JSONObject object = JSONObject.fromObject(string);
								kid = Integer.parseInt(object.getString("kid"));
							}
							String date = DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
							QuestionAnswerEntity qaEntity = new QuestionAnswerEntity();
							qaEntity.setQaCreator(String.valueOf(sysuser.getUserId()));
							qaEntity.setQaAnswer(qaAnswer);
							qaEntity.setQaQuestion(qaQuestion);
							qaEntity.setQaCreatetime(date);
							qaEntity.setQaKnowledge(kid);
							qaEntity.setQaType(0); // 0普通 1常见问题
							qaEntity.setQaKeywords(kewwords);
							qaEntity.setQaResource(qaResource);
							qaEntity.setQaFileName(file.getOriginalFilename());
							qaEntity.setQaResourceType("1");
							qaEntity.setQaChnlid(-1);
							int id = qaService.saveQuestionAnswer(qaEntity);
							//map.put("id", String.valueOf(id));
							//LuceneUtil.createSingleIndex(map);
							flag =true;
						}
						
					} catch (Exception e) {
						continue;
					}

					
				}
				if(flag)
					json.put("result", "success");
			} else {
				json.put("result", "no");
			}
		} catch (Exception e) {
			json.put("result", "error");
		}
		response.getWriter().println(json.toString());
	}

	/* *//**
			 * 欄目同步
			 *//*
				 * @RequestMapping("/chnlSync") public void chnlSync(){ try {
				 * qaService.chnlSync(); JSONObject obj = new JSONObject(); obj.put("results",
				 * "success"); String str = obj.toString(); response.getWriter().println(str); }
				 * catch (IOException e){ e.printStackTrace(); } }
				 */

	/**
	 * 文檔同步
	 */
	/*
	 * @RequestMapping("/docSync") public void docSync(){ try { String times =
	 * request.getParameter("times");//次數 String pageSize =
	 * request.getParameter("pageSize");// String pageNumber =
	 * request.getParameter("pageNumber");// if(StringUtils.isBlank(times)){ times =
	 * "1"; } String str =
	 * qaService.docSync(Integer.parseInt(times),pageSize,pageNumber);
	 * response.getWriter().println(str); } catch (IOException e){
	 * e.printStackTrace(); } }
	 */
	@RequestMapping("/getAllQa")
	public void getAllQa() {
		try {
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));//
			String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));//
			String keywords = CMyString.filterForHTMLValue(request.getParameter("keyword"));
			String str = "";
			JSONObject obj = new JSONObject();
			if (StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(pageNumber)) {
				boolean flag1 = pageSize.matches("[0-9]+");
				boolean flag2 = pageNumber.matches("[0-9]+");
				if (flag1 && flag2) {
					str = qaService.getAllQa(pageSize, pageNumber, keywords);
				} else {
					obj.put("results", "error");
					str = obj.toString();
				}
			} else {
				obj.put("results", "paramNull");
				str = obj.toString();
			}
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*****
	 * 查询所有问题根据点击率排序
	 * 
	 * 
	 */
	@RequestMapping("/getQuestionByClick")
	public void getQuestionByClick() {
		try {
			String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));//
			String str = "";
			JSONObject obj = new JSONObject();
			if (StringUtils.isNotBlank(pageSize)) {
				boolean flag2 = pageSize.matches("[0-9]+");
				if (flag2) {
					str = qaService.getQuestionByClick(pageSize);
					obj.put("results", str);
				} else {
					obj.put("results", "error");
				}
			} else {
				obj.put("results", "paramNull");
			}
			response.getWriter().println(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据数据库表重新创建索引
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllCreatDoc", produces = "application/json;charset=utf-8")
	public void findAllCreatDoc() throws IOException {
		JSONObject json = new JSONObject();
		Map<String, String> filemap = PropertiesUtil.getProperties_3("/fileUrl.properties");  
		boolean flag = PropertiesUtil.delAllFile(filemap.get("value"));
		try {
			//String id = CMyString.filterForHTMLValue(request.getParameter("id"));
			List<QuestionAnswerEntity> list = qaService.findAll();
			if (list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(list.get(i).getId()));
					map.put("qaQuestion", list.get(i).getQaQuestion());
					//map.put("qaAnswer", list.get(i).getQaAnswer());
					map.put("qaKeywords", list.get(i).getQaKeywords());
					LuceneUtil.createSingleIndex(map);
				}
				json.put("result", "success");
				
			} else {
				json.put("result", "error");
			}

		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		response.getWriter().println(json.toString());

	}
	
	@RequestMapping("/getQaByChnlid")
	public void getQaByChnlid() {
		try {
			//String pageSize = CMyString.filterForHTMLValue(request.getParameter("pageSize"));//
			//String pageNumber = CMyString.filterForHTMLValue(request.getParameter("pageNumber"));//
			String cid = CMyString.filterForHTMLValue(request.getParameter("id"));
			String str = "";
			JSONObject obj = new JSONObject();
			//if (StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(pageNumber)&& StringUtils.isNotBlank(cid)) {
			if (StringUtils.isNotBlank(cid)) {
				/*boolean flag1 = pageSize.matches("[0-9]+");
				boolean flag2 = pageNumber.matches("[0-9]+");*/
				boolean flag3 = cid.matches("[0-9]+");
				if (flag3) {
					str = qaService.getQaByChnlid("9", "1", cid);
				} else {
					obj.put("results", "error");
					str = obj.toString();
				}
			} else {
				obj.put("results", "paramNull");
				str = obj.toString();
			}
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/getChnlNameByQid")
	public void getChnlNameByQid() {
		String id = CMyString.filterForHTMLValue(request.getParameter("id"));
		JSONObject obj = new JSONObject();
		String str = "";
		if(StringUtils.isNotBlank(id)) {
			str = qaService.getChnlNameByQid(id);
		}else {
			obj.put("results", "error");
			str = obj.toString();
		}
		try {
			response.getWriter().println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Map<String, String> filemap = PropertiesUtil.getProperties_3("/fileUrl.properties");  
		System.out.println(filemap.get("value"));
	}
}
