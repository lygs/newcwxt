package com.ep.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.KnowledgeDao;
import com.ep.entity.KnowledgePointEntity;
import com.ep.entity.Sysuser;
import com.ep.service.KnowledgePointService;
@Service("knowledgePointService")
public class KnowledgePointServiceImpl implements KnowledgePointService {
	
	@Autowired
	public KnowledgeDao kdao;
   /**
    * 鏌ヨ鎵�鏈夌殑鐭ヨ瘑鐐�
    */
    public String getKnowAllList(String name,String pageSize,String pageNum,String chnlId,HttpServletRequest requests) {
    	String chnlSql = "";
    	String sql = "";
    	String sqlCount = "";
    	StringBuffer hql = new StringBuffer("select k.ID as id,k.K_NAME as kname,k.K_ISHOT as isHot,k.K_CREATETIME AS createTime,u.USERNAME as userName,k.K_EPCID as chnlId from Knowledge k left join SYSUSER  u on k.K_CREATOR=u.USERID where 1=1");
        StringBuffer hql_ = new StringBuffer("select count(*) num from Knowledge where 1=1");
        if(StringUtils.isNotBlank(name)){
        	hql.append(" and k.K_NAME like '%"+name+"%'");
        	hql_.append(" and K_NAME like '%"+name+"%'");
        }
        if(StringUtils.isNotBlank(chnlId)){
        	chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="+Integer.parseInt(chnlId)+
        	" UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"+
        	" INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";
        	hql.append(" and k.K_EPCID in (SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID)");
        	hql_.append(" and K_EPCID in (SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID) ");
        	
        
        	
//        	hql.append(" and k.K_EPCID = "+Integer.parseInt(chnlId));
//        	hql_.append(" and epcid ="+Integer.parseInt(chnlId));
        }
        
        hql.append(" order by k.K_CREATETIME desc,k.K_ISHOT asc");
        sql = chnlSql + hql.toString();
        sqlCount = chnlSql + hql_.toString();
        List<KnowledgePointEntity> list=kdao.getKnowAllList(sql,pageSize,pageNum);
//        int counts = kdao.getKnowAllCount(sqlCount);
        int counts = kdao.getSqlTotal(sqlCount);
        JSONObject obj=new JSONObject();
        Sysuser user =(Sysuser) requests.getSession().getAttribute("user");
        if(user!=null){
        	obj.put("roleId", user.getRoleId());
        }
        obj.put("list", list);
        obj.put("total",counts);
        kdao.closeSession();
        return obj.toString();
    }
    
    
    
    public String getKnowAll() {
    	StringBuffer hql = new StringBuffer("select k.ID as id,k.K_NAME as kname,k.K_ISHOT as isHot,k.K_CREATETIME AS createTime,u.USERNAME as userName from Knowledge k left join SYSUSER  u on k.K_CREATOR=u.USERID where 1=1 order by createTime desc,isHot asc");
        List<KnowledgePointEntity> list=kdao.getKnowAllList(hql.toString());
        JSONObject obj=new JSONObject();
        if(list.size()>0) {
        	 obj.put("list", list);
        }else {
        	 obj.put("list", null);
        }
        return obj.toString();
    }
    
    public String getKnowAll(String chnlId) {
    	StringBuffer hql = new StringBuffer("select k.ID as id,k.K_NAME as kname,k.K_ISHOT as isHot,k.K_CREATETIME AS createTime,u.USERNAME as userName,k.K_EPCID as chnlId from Knowledge k left join SYSUSER  u on k.K_CREATOR=u.USERID where 1=1");
    	String chnlSql="";
    	if(StringUtils.isNotBlank(chnlId)){
          	chnlSql = "WITH CTE AS (SELECT CHANNELID,PARENTID FROM CHANNELS WHERE CHANNELID="+Integer.parseInt(chnlId)+
          	" UNION ALL SELECT a.CHANNELID,a.PARENTID FROM CHANNELS a"+
          	" INNER JOIN CTE b ON b.CHANNELID=a.PARENTID) ";
          	hql.append(" and k.K_EPCID in (SELECT a.CHANNELID from CHANNELS a INNER JOIN CTE t ON a.CHANNELID=t.CHANNELID)");
          }
          
          hql.append(" order by k.K_CREATETIME desc,k.K_ISHOT asc");
         String sql = chnlSql + hql.toString();
        List<KnowledgePointEntity> list=kdao.getKnowAllList(sql.toString());
        JSONObject obj=new JSONObject();
        if(list.size()>0) {
        	 obj.put("list", list);
        }else {
        	 obj.put("list", null);
        }
        return obj.toString();
    }
    
    /**
     * 娣诲姞鐭ヨ瘑鐐�
     */
    @Override
    public String addKnows(String name,String channelId,int uid) {
//        KnowledgePointEntity knowsle = new KnowledgePointEntity();
//        knowsle.setKnowName(name);
//        if(StringUtils.isNotBlank(channelId)){
//        	knowsle.setEpcid(Integer.parseInt(channelId));
//        }else{
//        	knowsle.setEpcid(0); //0琛ㄧず娌℃湁鏍忕洰
//        }
//        knowsle.setIsHot(1);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//璁剧疆鏃ユ湡鏍煎紡
//        String date = df.format(new Date());// new Date()涓鸿幏鍙栧綋鍓嶇郴缁熸椂闂达紝涔熷彲浣跨敤褰撳墠鏃堕棿鎴�
//        try {
//			knowsle.setCreateTime(df.parse(date));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
    	
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//璁剧疆鏃ユ湡鏍煎紡
        String date = df.format(new Date());// new Date()涓鸿幏鍙栧綋鍓嶇郴缁熸椂闂达紝涔熷彲浣跨敤褰撳墠鏃堕棿鎴�
        KnowledgePointEntity knowsle = new KnowledgePointEntity();
   	    knowsle.setCreateTime(date);
   	    knowsle.setCreator(String.valueOf(uid));
   	    knowsle.setEpcid(Integer.parseInt(channelId));
   	    knowsle.setIsHot(1);//1默认   0热点
   	    knowsle.setKnowName(name);
//        String sql = "insert into Knowledge values('"+name+"',"+Integer.parseInt(channelId)+",'',1,'"+date+"','"+uid+"',0)";
		int nums=kdao.addKnows(knowsle); //knowsle
		String str="";
		JSONObject obj = new JSONObject();
		if(nums>0){
			obj.put("results", "SUCCESS");
			obj.put("kid", nums);
		}else{
			obj.put("results", "ERROR");
		}
        str = obj.toString();
        return str;
    }
    
    @Override
    public String delKnowsById(String id) {
    	String hql = "delete from KnowledgePointEntity where id=?0";
        int nums=kdao.delKnowsById(hql,Integer.parseInt(id)); //String name
    	String str=""; 
		JSONObject obj = new JSONObject();
		if(nums>0){
			obj.put("results", "SUCCESS");
		}else{
			obj.put("results", "ERROR");
		}
        str = obj.toString();
        return str;
    }
    
    /**
     * 淇敼鐭ヨ瘑鐐瑰唴瀹�
     */
    @Override
    public String updateKnows(String id,String edit_name,String edit_channelId) {
    	String hql = "update KnowledgePointEntity set knowName=?0,epcid=?1 where id=?2";
    	Object[] objs = {edit_name,Integer.parseInt(edit_channelId),Integer.parseInt(id)};
        int nums=kdao.updateKnows(hql,objs);
        String str=""; 
		JSONObject obj = new JSONObject();
		if(nums>0){
			obj.put("results", "SUCCESS");
		}else{
			obj.put("results", "ERROR");
		}
        str = obj.toString();
        return str;
    }
    /**
     * 璁句负鐑偣
     */
    @Override
    public String updateKnowsByHot(String id,String isHot) {
    	String hql = "update KnowledgePointEntity set isHot=?0 where id=?1";
    	Object[] objs = {Integer.parseInt(isHot),Integer.parseInt(id)};
        int nums=kdao.updateKnows(hql,objs);
        String str=""; 
		JSONObject obj = new JSONObject();
		if(nums>0){
			obj.put("results", "SUCCESS");
		}else{
			obj.put("results", "ERROR");
		}
        str = obj.toString();
        return str;
    }
    
    
    /**
     * 鑾峰彇鐑偣鐭ヨ瘑鐐�
     */
    @Override
    public String getHotKnowledge(String pageSize,String pageNum) {
//	  StringBuffer hql = new StringBuffer("from KnowledgePointEntity where 1=1 and isHot=0");
      StringBuffer hql = new StringBuffer("select k.ID as id,k.K_NAME as knowName,k.K_ISHOT as isHot,k.K_CREATETIME AS createTime,u.USERNAME as userName from Knowledge k left join SYSUSER  u on k.K_CREATOR=u.USERID where 1=1 and k.K_ISHOT=0");
      hql.append(" order by createTime desc,isHot asc"); 
      List<KnowledgePointEntity> list=kdao.getKnowAllList(hql.toString(),pageSize,pageNum);
      JSONObject obj=new JSONObject();
      String str="";
      obj.put("list", list);
      str=obj.toString();
      return str;
    }
    
    /**
     * 根据知识点ID和知识点名称查询是否有记录
     */
    @Override
    public List getByEpcidAndKname(String epcid,String kname) {
//	  StringBuffer hql = new StringBuffer("from KnowledgePointEntity where 1=1 and isHot=0");
      String sql = "from KnowledgePointEntity where epcid=? and knowName=?";
      List list = kdao.getByEpcidAndKname(sql,epcid,kname);
     /* List<KnowledgePointEntity> list=kdao.getKnowAllList(hql.toString(),pageSize,pageNum);
      JSONObject obj=new JSONObject();
      String str="";
      obj.put("list", list);
      str=obj.toString();*/
      return list;
    }


}
