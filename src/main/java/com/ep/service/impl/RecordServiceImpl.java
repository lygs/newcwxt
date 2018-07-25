package com.ep.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.record.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.RecordDao;
import com.ep.entity.RecordEntity;
import com.ep.service.RecordService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("RecordService")
public class RecordServiceImpl implements RecordService {
	
	@Autowired
	RecordDao recordDao;
	
    /**
     * 保存或者更新聊天记录
     */
    @Override
    public int saveRecord(RecordEntity rEntity) {
    	return recordDao.updataOrSaveRecord(rEntity);
    }

	@Override
	public String getAllList(int pageSize,int pageNumber) {
		Map<String, Object> map =new HashMap<String, Object>();
		String hql = "SELECT CONVERT(varchar(25),r.ID) as id,CONVERT(varchar(1000),r.R_QUESTION) as question,"+
		          "CONVERT(varchar(2000),r.R_ANSWER) as answer,CONVERT(varchar(20),r.R_CREATETIME) as createtime,"+
				"CONVERT(varchar(200),c.CHNLNAME) as channelName,CONVERT(varchar(200),k.K_NAME) as knowName,CONVERT(varchar(200),r.R_CRITERION) as rCriterion,CONVERT(varchar(200),r.R_USERIP) as userip " + 
				"FROM Record AS r LEFT JOIN CHANNELS c ON c.CHANNELID = r.R_CHNNELID " + 
				"LEFT JOIN Knowledge k ON k.ID = r.R_KNOWLEDGEID order by r.R_CREATETIME desc";
        int page=pageSize*(pageNumber-1);
        map.put("pageSize", pageSize);
        map.put("page", page);
        map.put("hql", hql.toString());
        List<Map<String, Object>> list= recordDao.getAllList(map);
        JSONArray array = new JSONArray();
        for(int i=0;i<list.size();i++) {
        	JSONObject obj =new JSONObject();
        	obj.put("id", list.get(i).get("id"));
        	obj.put("rQuestion", list.get(i).get("question"));
        	obj.put("rAnswer", list.get(i).get("answer"));
        	obj.put("rCreatetime", list.get(i).get("createtime"));
        	obj.put("channelName", list.get(i).get("channelName"));
        	obj.put("knowName", list.get(i).get("knowName"));
        	obj.put("rCriterion", list.get(i).get("rCriterion"));
        	obj.put("userip", list.get(i).get("userip"));
        	array.add(obj);
        }
		return array.toString();
	}
	
	/**
	   * 查询总数
	   */
	   @Override
	   public int getRecordEntityTotal() {
	       Map<String, Object> map =new HashMap<String, Object>();
	       StringBuffer hql = new StringBuffer("select count(*) from RecordEntity where 1=1");
	       
	       return recordDao.getRecordEntityAllCount(hql.toString());
	   }
	   
	   

	/**
	 * 查询周 月 年 的浏览记录数量
	 */
	@Override
	public JSONObject getRecordTotal(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String sql = null;
		Map<Object, Object> weekCountMap = new LinkedHashMap<>();
		String date = DateUtil.paseDate(new Date(), "yyyy-MM-dd");//当前时间
		try {
			String day2 = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 1 , "+");
			
			
			String startDate = CMyString.filterForHTMLValue(request.getParameter("startDate"));
			String endDate = CMyString.filterForHTMLValue(request.getParameter("endDate"));
			if(startDate != null && startDate != "" && endDate != null && endDate != "") {
				endDate =  DateUtil.getSpecifiedDayBefore(endDate, "yyyy-MM-dd", 1, "+");
				String dateSql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '"+startDate+"' and rCreatetime < '"+endDate+"' ";
				int dateCount = recordDao.getRecordEntityAllCount(dateSql.toString());
				json.put("dateCount", dateCount);
				return json;
			}
			
			
			
			//日统计
			String daySql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '" + date +"'"
					+ " and rCreatetime < '" + day2 +"'";
			int dayCount = recordDao.getRecordEntityAllCount(daySql);
			
			//周统计 可查总数 然后在前端处理
			/*for (int i = 7; i > -1; i--) {
				String week = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", i-1, "-");// 此前 i 天
				String week1 = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", i +1 , "-");// 此前 i 天
					sql = "select count(*) from RecordEntity where 1=1 and rCreatetime < '" + week +"'"
							+ " and rCreatetime > '" + week1 +"'";
					System.out.println("day:"+sql);
				int weekCount = recordDao.getRecordEntityAllCount(sql);
				weekCountMap.put(week, weekCount);
			}*/
			for(int i=7; i >0; i--) {
				String week = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", i-2, "-");// 此前 i 天
				String week1 = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", i-1 , "-");// 此前 i 天
				sql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '" + week1 +"'"
						+ " and rCreatetime < '" + week +"'";
				int weekCount = recordDao.getRecordEntityAllCount(sql);
				weekCountMap.put(week, weekCount);
			}
			//周总数
			String week = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 7 , "-");
			String weekSql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '" + week +"'"
					+ " and rCreatetime < '" + day2 +"'";
			int weekCountNum = recordDao.getRecordEntityAllCount(weekSql);
			
			
			//30天统计
			String month = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 30, "-");
			String month1 = DateUtil.getSpecifiedDayBefore(date, "yyyy-MM-dd", 1, "+");
			String monthHql = "select rCreatetime from RecordEntity where 1=1 and rCreatetime < '" +month1+ "' and rCreatetime > '" + month +"'";
			List<Record> list = recordDao.selectRecordByHql(monthHql);
			//月总数
			String wmonthSql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '" + month +"'"
					+ " and rCreatetime < '" + day2 +"'";
			int monthCountNum = recordDao.getRecordEntityAllCount(wmonthSql);
			
			//年记录
			String year =  DateUtil.paseDate(new Date(), "yyyy");
			 String year1 = String.valueOf(Integer.valueOf(year)+1);
			String yearHql = "select count(*) from RecordEntity where 1=1 and rCreatetime >= '" +year+ "' and rCreatetime < '" + year1 +"'";

			int yearCount = recordDao.getRecordEntityAllCount(yearHql);
			
			int YCount = getRecordEntityTotal();
			if (weekCountMap.size() > 0) {
				json.put("dayConutn", dayCount);
				json.put("weekCountList", weekCountMap);//周每天
				json.put("weekCountNum",weekCountNum);//周总数
				json.put("monthCountNum",monthCountNum);//月总数
				json.put("recordList",list);//月每天
				json.put("yearCount", yearCount);
				json.put("YCount",YCount);//总数
				
			} else {
				json.put("result", "error");
			}
		} catch (Exception e) {
			json.put("result", "error");
			e.printStackTrace();
		}
		
		return json;
	}

	@Override
	public String delRecord(int parseInt) {
		String hql ="delete from RecordEntity where id =?0";
		int num = recordDao.delRecord(hql,parseInt); 
		String str = "";
		if(num>0) {
			str="SUCCESS";
		}else {
			str="error";
		}
		return str;
	}

	@Override
	public int updateObj(int ids) {
		String sql = "update RecordEntity set rCriterion=? where id=?";
		Object[] params = {"1",ids};
		return recordDao.updateObj(sql, params);
	}


}
