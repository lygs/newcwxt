package com.ep.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.PraiseDao;
import com.ep.entity.PraiseEntity;
import com.ep.service.PraiseService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("praiseService")
public class PraiseServiceImpl implements PraiseService {

	@Autowired
	PraiseDao praisedao;

	/**
	 * 保存、更新点赞记录
	 */
	@Override
	public JSONObject savePraise(HttpServletRequest request, HttpServletResponse response) {
		PraiseEntity praise = new PraiseEntity();
		JSONObject json = new JSONObject();
		try {
			String pQaId = CMyString.filterForHTMLValue(request.getParameter("pQaId"));// 问题ID
			String pStatus = CMyString.filterForHTMLValue(request.getParameter("pStatus"));// 状态 0 赞 1 不满意
			String pDate = DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotBlank(pQaId) && pStatus != null && pStatus != "") {
				String s = pQaId.substring(0, 1);
				praise.setPtype(s);
				praise.setpQaId(Integer.parseInt(pQaId.substring(1)));
				praise.setpStatus(pStatus);
				praise.setpDate(pDate);
				int i = praisedao.updataOrSavePraise(praise);
				if (i < 0) {
					json.put("result", "error");
				} else {
					json.put("result", "success");
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
	 * 查询赞和不攒的总数
	 */
	@Override
	public JSONObject selectPraiseTotal() {
		JSONObject json = new JSONObject();
		// String sql = "SELECT COUNT(p.num) from (SELECT COUNT(P_QAID) num FROM Praise
		// WHERE P_STATUS = 0 GROUP BY P_QAID) p";// 不满意
		// String sql2 = "SELECT COUNT(p.num) from (SELECT COUNT(P_QAID) num FROM Praise
		// WHERE P_STATUS = 1 GROUP BY P_QAID) p";// 赞
		String sql = "SELECT COUNT(ID) num FROM Praise WHERE P_STATUS = 0";// 不满意
		String sql2 = "SELECT COUNT(ID) num FROM Praise WHERE P_STATUS = 1";// 赞
		int satisfy = praisedao.selectPraiseTotal(sql2);
		int unsatisfy = praisedao.selectPraiseTotal(sql);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("satisfy", satisfy);
		map.put("unsatisfy", unsatisfy);
		if (satisfy > -1 && unsatisfy > -1) {
			json.put("result", "success");
			json.put("list", map);
		} else {
			json.put("result", "error");
		}
		return json;
	}

	/**
	 * 根据状态查询点赞记录  
	 */
	public JSONObject selectPraise(HttpServletRequest request) {

		JSONObject json = new JSONObject();
		try {

			Integer pageNumber = Integer.valueOf(CMyString.filterForHTMLValue(request.getParameter("pageNumber")));
			Integer pageSize = Integer.valueOf(CMyString.filterForHTMLValue(request.getParameter("pageSize")));
			Integer pStatus = Integer.valueOf(CMyString.filterForHTMLValue(request.getParameter("pStatus")));// 0不满意 1满意
			
			StringBuffer hql = new StringBuffer("select CONVERT(varchar(200),p.P_QAID) as pqaid, CONVERT(varchar(200),q.QA_QUESTION) as question,CONVERT(varchar(50),MAX(p.P_DATE)) as createtime,CONVERT(varchar(50),q.QA_RESOURCE) as resource from Praise p left join QuestionAnswer q on q.ID = p.P_QAID where ");
			hql = (pStatus == 0) ? hql.append(" p.P_STATUS=0") : hql.append(" p.P_STATUS=1");
			hql = hql.append(" GROUP BY p.P_QAID,q.QA_QUESTION,q.QA_RESOURCE");
			if (pageNumber > 0 && pageSize > 0) {
				Map<Object, Object> map = new HashMap<>();
				String sql = "SELECT COUNT(p.num) from (SELECT COUNT(P_QAID) num FROM Praise WHERE P_STATUS = "	+ pStatus + " GROUP BY P_QAID) p";
				int total = praisedao.selectPraiseTotal(sql);
				map.put("pageSize", pageSize);
				map.put("pageNumber", pageNumber);
				map.put("hql", hql.toString());
				List<Map<String, Object>> list = praisedao.selectPraise(map);
				JSONArray array = new JSONArray();
				for (int i = 0; i < list.size(); i++) {
					JSONObject obj = new JSONObject();
					if (list.get(i).get("question") == null || list.get(i).get("question") == "") {
						continue;
					}
					int num = praisedao.selectPraiseTotal("select count(P_QAID) from Praise where P_STATUS= " + pStatus
							+ " and P_QAID = " + list.get(i).get("pqaid") +" and P_type='q'");
					
					obj.put("qacount", num);
					obj.put("question", list.get(i).get("question"));
					obj.put("resource", list.get(i).get("resource"));
					obj.put("createtime", list.get(i).get("createtime"));
					array.add(obj);
				}
				json.put("total", total);
				json.put("result", "success");
				json.put("list", array);
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
	 * 查询 年 月 周 日 点赞数量
	 */
	@Override
	public JSONObject selectPraiseTotalByDMY(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			String date = DateUtil.paseDate(new Date(), "yyyy-MM-dd");// 当前时间
			String day2 = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 1, "+");

			String startDate = CMyString.filterForHTMLValue(request.getParameter("startDate"));
			String endDate = CMyString.filterForHTMLValue(request.getParameter("endDate"));
			if(startDate != null && startDate != "" && endDate != null && endDate != "") {
				endDate =  DateUtil.getSpecifiedDayBefore(endDate, "yyyy-MM-dd", 1, "+");
				String sql = "SELECT COUNT (ID) as 'num',SUM (CASE P_STATUS WHEN '1' THEN	1	ELSE	0	END) AS 'm', "
						+ " SUM (	CASE P_STATUS	WHEN '0' THEN	1	ELSE	0	END	) AS 'b' "
						+ " FROM Praise WHERE 1 = 1 AND P_DATE >= '" + startDate + "' AND P_DATE < '" + endDate + "'";
				Map<Object, Object> map = new HashMap<>();
				map.put("pageSize", 1);
				map.put("pageNumber", 1);
				map.put("hql", sql);
				List<Map<String, Object>> dateCount = praisedao.selectPraise(map);
				json.put("dateCount", getList(dateCount));
				return json;
			}
			
			// 日
			String daySql = "SELECT COUNT (ID) as 'num',SUM (CASE P_STATUS WHEN '1' THEN	1	ELSE	0	END) AS 'm', "
					+ " SUM (	CASE P_STATUS	WHEN '0' THEN	1	ELSE	0	END	) AS 'b' "
					+ " FROM Praise WHERE 1 = 1 AND P_DATE >= '" + date + "' AND P_DATE < '" + day2 + "'";
			Map<Object, Object> map = new HashMap<>();
			map.put("pageSize", 1);
			map.put("pageNumber", 1);
			map.put("hql", daySql);
			List<Map<String, Object>>  dayCount = praisedao.selectPraise(map);

			// 周
			String week = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 7, "-");
			/*
			 * String weekSql1 = "select count(ID) from Praise where 1=1 and P_DATE >= '" +
			 * week +"'" + " and P_DATE < '" + day2 +"' and p.P_STATUS=1 ";
			 */

			String weekSql = "SELECT COUNT (ID) as 'num',SUM (CASE P_STATUS WHEN '1' THEN	1	ELSE	0	END) AS 'm', "
					+ " SUM (	CASE P_STATUS	WHEN '0' THEN	1	ELSE	0	END	) AS 'b' "
					+ " FROM Praise WHERE 1 = 1 AND P_DATE >= '" + week + "' AND P_DATE < '" + day2 + "'";
			Map<Object, Object> weekmap = new HashMap<>();
			weekmap.put("pageSize", 1);
			weekmap.put("pageNumber", 1);
			weekmap.put("hql", weekSql);
			List weekCount = praisedao.selectPraise(weekmap);

			// 月
			String month = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 30, "-");
			/*
			 * String monthSql1 = "select count(ID) from Praise where 1=1 and P_DATE >= '" +
			 * month +"'" + " and P_DATE < '" + day2 +"' and p.P_STATUS=1 ";
			 */

			String monthSql = "SELECT COUNT (ID) as 'num',SUM (CASE P_STATUS WHEN '1' THEN	1 ELSE 0 END) AS 'm', "
					+ " SUM (	CASE P_STATUS WHEN '0' THEN 1 ELSE 0 END	) AS 'b' "
					+ " FROM Praise WHERE 1 = 1 AND P_DATE >= '" + month + "' AND P_DATE < '" + day2 + "'";
			Map<Object, Object> monthmap = new HashMap<>();
			monthmap.put("pageSize", 1);
			monthmap.put("pageNumber", 1);
			monthmap.put("hql", monthSql);
			List monthCount = praisedao.selectPraise(monthmap);

			// 年
			String year = DateUtil.paseDate(new Date(), "yyyy");
			String year1 = String.valueOf(Integer.valueOf(year) + 1);
			// String yearHql1 = "select count(ID) from Praise where 1=1 and P_DATE >= '"
			// +year+ "' and P_DATE < '" + year1 +"' and p.P_STATUS=1 ";
			String yearHql = "SELECT COUNT (ID) as 'num',SUM (CASE P_STATUS WHEN '1' THEN 1 ELSE 0	END) AS 'm', "
					+ " SUM (	CASE P_STATUS	WHEN '0' THEN	1	ELSE	0	END	) AS 'b' "
					+ " FROM Praise WHERE 1 = 1 AND P_DATE >= '" + year + "' AND P_DATE < '" + year1 + "'";

			Map<String, Object> yearmap = new HashMap<>();
			yearmap.put("pageSize", 1);
			yearmap.put("pageNumber", 1);
			yearmap.put("hql", yearHql);
			List yearCount = praisedao.selectPraise(yearmap);

			// int yearCount = praisedao.selectPraiseTotal(yearHql);
           
			json.put("dayCount", getList(dayCount));
			json.put("weekCount", getList(weekCount));
			json.put("monthCount", getList(monthCount));
			json.put("yearCount", getList(yearCount));

		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "查询出错！");
		}
		return json;
	}
	
	public JSONObject getList(List<Map<String, Object>> list) {
		JSONObject object=new JSONObject();
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				object.put("num", list.get(i).get("num"));
				if(list.get(i).get("m")==null) {
					object.put("m", "0");
				}else {
					object.put("m", list.get(i).get("m"));
				}
				if(list.get(i).get("b")==null) {
					object.put("b", "0");
				}else {
					object.put("b", list.get(i).get("b"));
				}
            }
		}
		return object;
		
	}

}
