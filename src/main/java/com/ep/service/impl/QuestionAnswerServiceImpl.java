package com.ep.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.QuestionAnswerDao;
import com.ep.entity.Channels;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.service.QuestionAnswerService;
import com.ep.util.DBUtil;
import com.ep.util.DBUtil2;

@Service("questionAnswerService")
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

	@Autowired
	QuestionAnswerDao questionAnswerDao;

	@Override
	public List<QuestionAnswerEntity> getQuestionAnswerAllList(int pageSize, int pageNumber, String qaQuestion,
			String chnlId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(
				" SELECT qa.ID as id, qa.QA_QUESTION as qaQuestion,qa.QA_CREATETIME as qaCreatetime,qa.QA_TYPE as qaType,qa.QA_KEYWORDS as qaKeywords,qa.QA_URL as qaUrl,qa.QA_ANSWER as qaAnswer,qa.QA_KNOWLEDGE as knowId,u.USERNAME AS userName,qa.QA_RESOURCE as qaResource,qa.QA_RESOURCETYPE as resourceType,qa.QA_FILENAME as fileName FROM QuestionAnswer qa LEFT JOIN SYSUSER u on qa.QA_CREATOR=u.USERID  where 1=1");
		if (StringUtils.isNotBlank(qaQuestion)) {
			hql.append(" and qa.QA_QUESTION like '%" + qaQuestion + "%'");
		}
		hql.append(" order by qa.QA_CREATETIME desc");
		if (StringUtils.isNotBlank(chnlId)) {
			String chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="
					+ Integer.parseInt(chnlId) + " UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"
					+ " INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";

			String sqlss = "SELECT qa.ID AS id,qa.QA_QUESTION AS qaQuestion,qa.QA_CREATETIME AS qaCreatetime,"
					+ "qa.QA_TYPE AS qaType,qa.QA_KEYWORDS AS qaKeywords,qa.QA_URL AS qaUrl,qa.QA_ANSWER AS qaAnswer,"
					+ "qa.QA_KNOWLEDGE AS knowId,u.USERNAME AS userName FROM	QuestionAnswer qa "
					+ "LEFT JOIN SYSUSER u ON qa.QA_CREATOR = u.USERID "
					+ "LEFT JOIN Knowledge k ON k.ID = qa.QA_KNOWLEDGE "
					+ "where k.K_EPCID in (SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID) ";// "+chnlId+"
			hql = new StringBuffer();
			hql.append(chnlSql + sqlss);
			if (StringUtils.isNotBlank(qaQuestion)) {
				hql.append(" and qa.QA_QUESTION like '%" + qaQuestion + "%'");
			}
			hql.append(" order by qa.QA_CREATETIME desc");

		}
		List<QuestionAnswerEntity> list = questionAnswerDao.getQuestionAnswerList(hql.toString(),
				String.valueOf(pageSize), String.valueOf(pageNumber));
		return list;
	}

	/**
	 * 查询总数
	 */
	@Override
	public int getQuestionAnswerTotal(String qaQuestion, String chnlId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select count(*) from QuestionAnswerEntity where 1=1");
		if (StringUtils.isNotBlank(qaQuestion)) {
			hql.append(" and qaQuestion like '%" + qaQuestion + "%'");
		}
		if (StringUtils.isNotBlank(chnlId)) {
			/*
			 * String sqlss
			 * =" WITH CTE AS (SELECT DOCCHNLID,PARENTID FROM CHANNELS WHERE DOCCHNLID = "
			 * +chnlId+" UNION ALL SELECT c.DOCCHNLID,c.PARENTID FROM CHANNELS c "+
			 * " INNER JOIN CTE b ON b.DOCCHNLID = c.PARENTID) SELECT count(*) num FROM QuestionAnswer qa WHERE qa.QA_CHNLID IN (SELECT a.DOCCHNLID FROM"
			 * + " CHANNELS a INNER JOIN CTE t ON a.DOCCHNLID = t.DOCCHNLID)";
			 */
			String chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="
					+ Integer.parseInt(chnlId) + " UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"
					+ " INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";

			String sqlss = "SELECT COUNT(*) as num FROM QuestionAnswer qa LEFT JOIN SYSUSER u ON qa.QA_CREATOR = u.USERID LEFT JOIN Knowledge k ON k.ID = qa.QA_KNOWLEDGE WHERE k.K_EPCID in(SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID) ";// +chnlId;
			hql = new StringBuffer();
			hql.append(chnlSql + sqlss);
			if (StringUtils.isNotBlank(qaQuestion)) {
				hql.append(" and QA_QUESTION like '%" + qaQuestion + "%'");
			}
		}
		return questionAnswerDao.getQuestionAnswerAllCount(hql.toString(), chnlId);
	}

	@Override
	public List<QuestionAnswerEntity> getQuestionAnswerByQuestion(String qaQuestion, String keywords, String resourse) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "from QuestionAnswerEntity where qaQuestion=? and qaKeywords=? and qaResource=?";
		map.put("qaQuestion", qaQuestion);
		map.put("keywords", keywords);
		map.put("resourse", resourse);
		map.put("hql", hql);
		return questionAnswerDao.getQuestionAnswerByQuestion(map);

	}

	/**
	 * 删除对象
	 */
	@Override
	public int delQuestionAnswerById(Integer id) {
		QuestionAnswerEntity obj = (QuestionAnswerEntity) questionAnswerDao.getQuestionAnswerById(id);
		if (obj != null) {
			return questionAnswerDao.delQuestionAnswer(obj);
		} else {
			return 0;
		}

	}

	@Override
	public int updateQuestionAnswerSUMById(int id) {
		return 0;// qaMapper.updateQuestionAnswerBadnumById(id);
	}

	@Override
	public int updateQuestionAnswerGoodnumById(int id) {
		return 0;// qaMapper.updateQuestionAnswerGoodnumById(id);
	}

	@Override
	public int updateQuestionAnswerBadnumById(int id) {
		return 0;// qaMapper.updateQuestionAnswerSUMById(id);
	}

	@Override
	public Integer saveQuestionAnswer(QuestionAnswerEntity questionAnswerEntity) {
		// String sql = "insert into QuestionAnswer
		// values('"+questionAnswerEntity.getQaQuestion()+"','"+questionAnswerEntity.getQaAnswer()+"','',1,'"+questionAnswerEntity.getQaCreatetime()+"')";
		return questionAnswerDao.saveQuestionAnswer(questionAnswerEntity);
	}

	/**
	 * 根据id获取对象
	 */
	@Override
	public QuestionAnswerEntity getQuestionAnswerById(Integer id) {
		return (QuestionAnswerEntity) questionAnswerDao.getQuestionAnswerById(id);

	}

	@Override
	public int updateQuestionAnswerById(QuestionAnswerEntity questionAnswerEntity) {
		return questionAnswerDao.updataOrSaveQuestionAnswer(questionAnswerEntity);
	}

	/**
	 * 修改是否是常见问题
	 */
	@Override
	public int updateProblemByChangj(String id, String isChangj) {
		QuestionAnswerEntity obj = (QuestionAnswerEntity) questionAnswerDao.getQuestionAnswerById(Integer.valueOf(id));
		if (obj != null) {
			obj.setQaType(Integer.valueOf(isChangj));
			return questionAnswerDao.updataOrSaveQuestionAnswer((obj));
		} else {
			return 0;
		}

	}

	/**
	 * 查询常见问题
	 */
	@Override
	public List getQuestionByComm(int pageSize, int pageNumber, String qaQuestion) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "from QuestionAnswerEntity where 1=1 and qaType=1";
		int page = pageSize * (pageNumber - 1);
		map.put("pageSize", pageSize);
		map.put("page", page);
		map.put("hql", hql);
		return questionAnswerDao.getQuestionAnswerByComm(map);

	}

	/**
	 * 根据知识点id查询问题
	 */
	@Override
	public List getQuestionAnswerByKnowledgeId(Integer qaKnowledgeId, Integer pageNumber, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "from QuestionAnswerEntity where 1=1 and qaKnowledge=?0";
		int page = pageSize * (pageNumber - 1);
		map.put("pageSize", pageSize);
		map.put("page", page);
		map.put("hql", hql);
		map.put("id", qaKnowledgeId);
		return questionAnswerDao.getQuestionAnswerByKnowledgeId(map);

	}

	/**
	 * 多词进行搜索
	 **/
	@Override
	public List<QuestionAnswerEntity> getQuestionAnswerListByWords(List<String> wordsList) {
		StringBuilder hqlString = new StringBuilder();
		hqlString.append("from QuestionAnswerEntity where 1=1");
		if (wordsList.size() > 0) {
			for (String str : wordsList) {
				hqlString.append(" and QA_QUESTION like '%" + str + "%'");
			}

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageSize", 9);
		map.put("page", 0);
		map.put("hql", hqlString.toString());
		return questionAnswerDao.getQuestionAnswerByComm(map);

	}

	/*
	 * @Override public void chnlSync(){ String sql =
	 * "select W.CHANNELID as docchnlId,W.CHNLNAME as chnlname,W.CHNLDESC as chnldesc, W.PARENTID as parentid, W.CHNLORDER as chnlorder ,W.CRUSER as cruser,W.CRTIME as crtime, W.STATUS as status ,W.CHNLTYPE as chnltype,W.SITEID as siteId,S.SITENAME as siteName,S.SITEDESC as siteDesc from WCMCHANNEL W,WCMWEBSITE S where S.SITEID = W.SITEID and S.STATUS=0 and W.STATUS=0"
	 * ; List<Channels> list = getList(sql); }
	 */

	/*
	 * public List<Channels> getList(String sql){ Connection
	 * connection=DBUtil2.getConnection(); PreparedStatement ps=null; ResultSet
	 * rs=null; List<Channels> list=new ArrayList<Channels>(); try {
	 * ps=connection.prepareStatement(sql); rs=ps.executeQuery(); while(rs.next()){
	 * Channels ch=new Channels(); ch.setChnlname(rs.getString("chnlname"));
	 * ch.setCrtime(rs.getTimestamp("crtime"));
	 * ch.setCruser(rs.getString("cruser")); ch.setParentid(rs.getInt("parentid"));
	 * ch.setStatus(rs.getInt("status")); String sqlss =
	 * "from Channels where docchnlId="+ch.getDocchnlId(); Channels chnl =
	 * (Channels) questionAnswerDao.getObj(sqlss,new Object[] {}); if(chnl!=null){
	 * ch.setChannelid(chnl.getChannelid()); questionAnswerDao.updates(ch); }else{
	 * questionAnswerDao.save(ch); } list.add(ch); } return list; } catch
	 * (SQLException e) { e.printStackTrace(); }finally{ try { if(rs!=null){
	 * rs.close(); } if(ps!=null){ ps.close(); } if(connection!=null){
	 * connection.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * return list; }
	 */

	/*
	 * @Override public String docSync(int times,String pageSize,String pageNumber)
	 * { //次數 第一次 根據配置文件的時間查詢改時間之前的所有數據 非第一次根據該時間查詢之後的所有數據 String keys =
	 * "currentTime"; //當前時間的key //第一次獲取根據默認的時間 String dateTime = ""; String
	 * sqlTime=""; if(times == 1){ Map maps
	 * =PropertiesUtil.getProperties_4("/fileUrl.properties",keys,""); //獲取配置文件中的時間
	 * dateTime = (String) maps.get("value");
	 * sqlTime=" and CONVERT(varchar(20), d.DOCRELTIME, 120)<='"+dateTime+"'";
	 * }else{ String qaSql =
	 * "select Max(QA_CREATETIME) as maxstr from QuestionAnswer"; String values
	 * =getMax(qaSql); Map maps
	 * =PropertiesUtil.getProperties_4("/fileUrl.properties",keys,values);
	 * //獲取配置文件中的時間 // dateTime = (String) maps.get("value"); dateTime = values;
	 * sqlTime=" and CONVERT(varchar(20), d.DOCRELTIME, 120)>'"+dateTime+"'"; }
	 * JSONObject obj = new JSONObject(); if(StringUtils.isNotBlank(pageNumber)){ //
	 * String docSql = "select top "
	 * +pageSize+" d.DOCCHANNEL as qaChnlid,d.DOCTITLE as qaQuestion,d.DOCHTMLCON as qaAnswer,d.DOCRELTIME as qaCreatetime,c.CHNLNAME as qaKeywords,d.DOCPUBURL as qaUrl,d.CRUSER as qaCreator,d.SITEID as siteId from WCMDOCUMENT as d left join WCMCHANNEL as c on d.DOCCHANNEL = c.CHANNELID  where  d.DOCSTATUS=10 and d.DOCID not in(select top "
	 * +(((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize)+1)-1)
	 * +" doc.DOCID from WCMDOCUMENT as doc where doc.DOCSTATUS=10)";
	 * 
	 * String docSql ="SELECT TOP "
	 * +pageSize+" d.DOCCHANNEL AS qaChnlid,d.DOCTITLE AS qaQuestion,d.DOCCONTENT AS qaAnswer,"
	 * +
	 * " d.DOCRELTIME AS qaCreatetime,c.CHNLNAME AS qaKeywords,d.DOCPUBURL AS qaUrl,d.CRUSER AS qaCreator,d.SITEID AS siteId"
	 * +
	 * " FROM WCMDOCUMENT d LEFT JOIN WCMCHANNEL c ON d.DOCCHANNEL = c.CHANNELID LEFT JOIN WCMWEBSITE s ON s.SITEID = c.SITEID"
	 * +
	 * " WHERE d.DOCSTATUS = 10 AND c.STATUS = 0 AND s.STATUS = 0 AND d.DOCID NOT IN (select top "
	 * +(((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize)+1)-1)
	 * +" dd.DOCID from" +
	 * " WCMDOCUMENT dd LEFT JOIN WCMCHANNEL cc ON dd.DOCCHANNEL = cc.CHANNELID LEFT JOIN WCMWEBSITE ss ON ss.SITEID = cc.SITEID WHERE"
	 * + " dd.DOCSTATUS = 10 AND cc.STATUS = 0 AND ss.STATUS = 0 " + sqlTime+
	 * " ORDER BY dd.DOCID ASC) "+sqlTime; List<QuestionAnswerEntity> list =
	 * getDocList(docSql); obj.put("results", "success"); } String sql =
	 * "SELECT COUNT(*) num FROM WCMDOCUMENT d LEFT JOIN WCMCHANNEL c ON d.DOCCHANNEL = c.CHANNELID LEFT JOIN WCMWEBSITE s ON s.SITEID = c.SITEID WHERE d.DOCSTATUS = 10 AND c.STATUS = 0 AND s.STATUS = 0"
	 * ; int counts = getDocTotal(sql); int totalPageNum=0; if(counts>0){
	 * totalPageNum= ((counts - 1) / Integer.parseInt(pageSize)) + 1;// 总页码 }
	 * 
	 * obj.put("totleRow", counts); obj.put("allPage", totalPageNum); return
	 * obj.toString(); }
	 */

	/**
	 * 獲取文檔并保存到問題表
	 * 
	 * @param sql
	 * @return
	 */
	/*
	 * public List<QuestionAnswerEntity> getDocList(String sql){ Connection
	 * connection=DBUtil2.getConnection(); PreparedStatement ps=null; ResultSet
	 * rs=null; List<QuestionAnswerEntity> list=new
	 * ArrayList<QuestionAnswerEntity>(); try { ps=connection.prepareStatement(sql);
	 * rs=ps.executeQuery(); SimpleDateFormat sf = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); while(rs.next()){
	 * QuestionAnswerEntity qa=new QuestionAnswerEntity(); String contents =
	 * HtmlRegexpUtil.filterHtml(rs.getString("qaAnswer"));
	 * qa.setQaAnswer(contents); qa.setQaChnlid(rs.getInt("qaChnlid"));
	 * qa.setQaCreatetime(sf.format(rs.getTimestamp("qaCreatetime")));
	 * qa.setQaCreator(rs.getString("qaCreator"));
	 * qa.setQaOther(String.valueOf(rs.getInt("siteId")));
	 * qa.setQaKeywords(rs.getString("qaKeywords")); qa.setQaKnowledge(-1);
	 * qa.setQaQuestion(rs.getString("qaQuestion")); qa.setQaResource("0");
	 * qa.setQaType(0); qa.setQaUrl(rs.getString("qaUrl"));
	 * if(StringUtils.isBlank(rs.getString("qaQuestion"))
	 * ||StringUtils.isBlank(contents)){ continue; } int id =
	 * questionAnswerDao.saveQa(qa); //創建索引 Map map = new HashMap();
	 * map.put("qaQuestion", qa.getQaQuestion()); map.put("qaAnswer",
	 * qa.getQaAnswer()); map.put("qaKeywords",qa.getQaKeywords()); map.put("id",
	 * String.valueOf(id)); LuceneUtil.createSingleIndex(map); list.add(qa); }
	 * return list; } catch (Exception e) { e.printStackTrace(); }finally{ try {
	 * if(rs!=null){ rs.close(); } if(ps!=null){ ps.close(); } if(connection!=null){
	 * connection.close(); } } catch (SQLException e) { e.printStackTrace(); } }
	 * return list; }
	 */

	public String getMax(String sql) {
		Connection connection = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str = "";
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				str = rs.getString("maxstr");
			}
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 總條數
	 * 
	 * @param sql
	 * @return
	 */
	public int getDocTotal(String sql) {
		int total = 0;
		Connection connss = DBUtil2.getConnection();
		PreparedStatement psss = null;
		ResultSet rsss = null;
		try {
			psss = connss.prepareStatement(sql);
			rsss = psss.executeQuery();
			if (rsss.next()) {
				total = rsss.getInt("num");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(rsss);// 关闭记录集
			DBUtil.close(psss);// 关闭声明
			DBUtil.close(connss);// 关闭连接对象
		}
		return total;
	}

	@Override
	public String getAllQa(String pageSize, String pageNumber, String keyword) {
		StringBuffer sb = new StringBuffer("select top " + Integer.parseInt(pageSize)
				+ " qa.ID as id, qa.QA_QUESTION as qaQuestion,qa.QA_CREATETIME as qaCreatetime,qa.QA_TYPE as qaType,qa.QA_KEYWORDS as qaKeywords,qa.QA_URL as qaUrl,k.K_NAME as knowName,qa.QA_ANSWER as qaAnswer,qa.QA_KNOWLEDGE as knowId  from QuestionAnswer qa left join Knowledge k on k.ID = qa.QA_KNOWLEDGE where 1=1");
		String sqlCount = "select count(*) from QuestionAnswerEntity where 1=1";
		if (StringUtils.isNotBlank(keyword)) {
			sb.append(" and qaQuestion like '%" + keyword + "%'");
			sqlCount += " and qaQuestion like '%" + keyword + "%'";
		}
		sb.append(" and qa.ID not in(select top "
				+ (((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize) + 1) - 1)
				+ " ID from QuestionAnswer) order by qa.QA_CREATETIME desc");
		List<Map<String, Object>> list = questionAnswerDao.getAllQuestion(sb.toString(), Integer.parseInt(pageSize),
				Integer.parseInt(pageNumber));
		int totle = questionAnswerDao.getAllCount(sqlCount);
		JSONObject obj = new JSONObject();
		if (list != null && list.size() > 0) {
			JSONArray arr = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject ob = new JSONObject();
				Map<String, Object> map = list.get(i);
				ob.put("id", map.get("id") + "");
				ob.put("qaQuestion", map.get("qaQuestion"));
				ob.put("qaCreatetime", map.get("qaCreatetime"));
				ob.put("qaType", map.get("qaType"));
				ob.put("qaKeywords", map.get("qaKeywords"));
				ob.put("knowName", map.get("knowName"));
				ob.put("qaUrl", map.get("qaUrl"));
				ob.put("qaAnswer", map.get("qaAnswer"));
				ob.put("knowId", map.get("knowId") + "");
				arr.add(ob);
			}
			obj.put("list", arr.toString());
			obj.put("totle", totle);
			obj.put("results", "success");
		} else {
			obj.put("results", "no");
		}
		return obj.toString();
	}

	@Override
	public String getQuestionByClick(String pageSize) {
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(q.id) as num ,q.id as qid,q.QA_QUESTION as question,q.QA_ANSWER as answer from QuestionAnswer q LEFT JOIN Record r on q.id = r.R_QUESTIONID GROUP BY q.id,q.QA_QUESTION,q.QA_ANSWER ORDER BY num desc";
		List<Map<String, Object>> list = questionAnswerDao.getQuestionAnswerList(sql, pageSize, "1");
		JSONArray arr = new JSONArray();
		if (list != null && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {
				JSONObject ob = new JSONObject();
				Map<String, Object> map = list.get(i);
				ob.put("id", map.get("qid") + "");
				ob.put("qaQuestion", map.get("question"));
				ob.put("qaAnswer", map.get("answer"));
				arr.add(ob);
			}
		}
		return arr.toString();
	}

	public List<QuestionAnswerEntity> findAllByAnswer() {
		String hql = "from QuestionAnswerEntity where qaAnswer=null or qaAnswer=''";

		return questionAnswerDao.findAllByAnswer(hql);

	}

	public List<QuestionAnswerEntity> findAll() {
		String hql = "from QuestionAnswerEntity";

		return questionAnswerDao.findAllByAnswer(hql);

	}
	public String getQaByChnlid(String pageSize, String pageNumber, String cid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM QuestionAnswer WHERE QA_KNOWLEDGE in ");
		sb.append("(SELECT id FROM Knowledge WHERE k_epcid = "+cid+") ORDER BY QA_CREATETIME DESC");
		List<Map<String, Object>> list = questionAnswerDao.getAllQuestion(sb.toString(), Integer.parseInt(pageSize),
				Integer.parseInt(pageNumber));
		JSONObject obj = new JSONObject();
		if (list != null && list.size() > 0) {
			JSONArray arr = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject ob = new JSONObject();
				Map<String, Object> map = list.get(i);
				ob.put("id", map.get("ID") + "");
				ob.put("qaQuestion", map.get("QA_QUESTION"));
				ob.put("qaCreatetime", map.get("QA_CREATETIME"));
				//ob.put("qaType", map.get("QA_TYPE"));
				//ob.put("qaKeywords", map.get("QA_KEYWORDS"));
				//ob.put("knowName", map.get("knowName"));
				ob.put("qaUrl", map.get("QA_URL"));
				ob.put("qaAnswer", map.get("QA_ANSWER"));
				//ob.put("knowId", map.get("knowId") + "");
				arr.add(ob);
			}
			obj.put("list", arr.toString());
			obj.put("results", "success");
		} else {
			obj.put("results", "no");
		}
		return obj.toString();
	}
	
	public String getChnlNameByQid(String id) {
		System.out.println(id);
		String sql = "SELECT * FROM CHANNELS WHERE CHANNELID=(SELECT k_epcid FROM Knowledge WHERE id=(SELECT qa_knowledge FROM QuestionAnswer WHERE id="+id+"))";
		List<Map<String, Object>> list = questionAnswerDao.getChnlBid(sql);
		JSONObject obj = new JSONObject();
		if(list.size()>0) {
			JSONArray arr = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject ob = new JSONObject();
				Map<String, Object> map = list.get(i);
				ob.put("id", map.get("CHANNELID") + "");
				ob.put("chnlname", map.get("CHNLNAME"));
				ob.put("parentid", map.get("PARENTID"));
				ob.put("chnlurl", map.get("CHNLURL"));
				arr.add(ob);
			}
			obj.put("list", list);
			obj.put("results", "success");
		}else {
			obj.put("results", "no");
		}
		
		return obj.toString();
	}

	@Override
	public List<QuestionAnswerEntity> getQuestionAnswerAllList(Integer pageSize, Integer pageNumber, String qaQuestion,
			String chnlId, String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(
				" SELECT qa.ID as id, qa.QA_QUESTION as qaQuestion,qa.QA_CREATETIME as qaCreatetime,qa.QA_TYPE as qaType,qa.QA_KEYWORDS as qaKeywords,qa.QA_URL as qaUrl,qa.QA_ANSWER as qaAnswer,qa.QA_KNOWLEDGE as knowId,u.USERNAME AS userName,qa.QA_RESOURCE as qaResource,qa.QA_RESOURCETYPE as resourceType,qa.QA_FILENAME as fileName FROM QuestionAnswer qa LEFT JOIN SYSUSER u on qa.QA_CREATOR=u.USERID  where 1=1");
		if (StringUtils.isNotBlank(qaQuestion)) {
			hql.append(" and qa.QA_QUESTION like '%" + qaQuestion + "%'");
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql.append(" and qa.QA_CREATETIME >= '" + startTime + "'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append(" and qa.QA_CREATETIME <= '" + endTime + "'");
		}
		hql.append(" order by qa.QA_CREATETIME desc");
		if (StringUtils.isNotBlank(chnlId)) {
			String chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="
					+ Integer.parseInt(chnlId) + " UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"
					+ " INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";

			String sqlss = "SELECT qa.ID AS id,qa.QA_QUESTION AS qaQuestion,qa.QA_CREATETIME AS qaCreatetime,"
					+ "qa.QA_TYPE AS qaType,qa.QA_KEYWORDS AS qaKeywords,qa.QA_URL AS qaUrl,qa.QA_ANSWER AS qaAnswer,"
					+ "qa.QA_KNOWLEDGE AS knowId,u.USERNAME AS userName FROM	QuestionAnswer qa "
					+ "LEFT JOIN SYSUSER u ON qa.QA_CREATOR = u.USERID "
					+ "LEFT JOIN Knowledge k ON k.ID = qa.QA_KNOWLEDGE "
					+ "where k.K_EPCID in (SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID) ";// "+chnlId+"
			hql = new StringBuffer();
			hql.append(chnlSql + sqlss);
			if (StringUtils.isNotBlank(qaQuestion)) {
				hql.append(" and qa.QA_QUESTION like '%" + qaQuestion + "%'");
			}
			if (StringUtils.isNotBlank(startTime)) {
				hql.append(" and qa.QA_CREATETIME >= '" + startTime + "'");
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and qa.QA_CREATETIME <= '" + endTime + "'");
			}
			hql.append(" order by qa.QA_CREATETIME desc");
		}
		List<QuestionAnswerEntity> list = questionAnswerDao.getQuestionAnswerList(hql.toString(),
				String.valueOf(pageSize), String.valueOf(pageNumber));
		return list;
	}

	@Override
	public int getQuestionAnswerTotal(String qaQuestion, String chnlId, String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select count(*) from QuestionAnswerEntity where 1=1");
		if (StringUtils.isNotBlank(qaQuestion)) {
			hql.append(" and qaQuestion like '%" + qaQuestion + "%'");
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql.append(" and qaCreatetime >= '" + startTime + "'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql.append(" and qaCreatetime <= '" + endTime + "'");
		}
		if (StringUtils.isNotBlank(chnlId)) {
			/*
			 * String sqlss
			 * =" WITH CTE AS (SELECT DOCCHNLID,PARENTID FROM CHANNELS WHERE DOCCHNLID = "
			 * +chnlId+" UNION ALL SELECT c.DOCCHNLID,c.PARENTID FROM CHANNELS c "+
			 * " INNER JOIN CTE b ON b.DOCCHNLID = c.PARENTID) SELECT count(*) num FROM QuestionAnswer qa WHERE qa.QA_CHNLID IN (SELECT a.DOCCHNLID FROM"
			 * + " CHANNELS a INNER JOIN CTE t ON a.DOCCHNLID = t.DOCCHNLID)";
			 */
			String chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="
					+ Integer.parseInt(chnlId) + " UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"
					+ " INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";

			String sqlss = "SELECT COUNT(*) as num FROM QuestionAnswer qa LEFT JOIN SYSUSER u ON qa.QA_CREATOR = u.USERID LEFT JOIN Knowledge k ON k.ID = qa.QA_KNOWLEDGE WHERE k.K_EPCID in(SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID) ";// +chnlId;
			hql = new StringBuffer();
			hql.append(chnlSql + sqlss);
			if (StringUtils.isNotBlank(qaQuestion)) {
				hql.append(" and QA_QUESTION like '%" + qaQuestion + "%'");
			}
			
			if (StringUtils.isNotBlank(startTime)) {
				hql.append(" and QA_CREATETIME >= '" + startTime + "'");
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and QA_CREATETIME <= '" + endTime + "'");
			}
		}
		return questionAnswerDao.getQuestionAnswerAllCount(hql.toString(), chnlId);
	}

}
