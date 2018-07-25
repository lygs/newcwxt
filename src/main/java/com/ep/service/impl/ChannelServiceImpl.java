package com.ep.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.ChannelDao;
import com.ep.entity.Channels;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.service.ChannelService;
@Service("channelService")
public class ChannelServiceImpl implements ChannelService{
	@Autowired
	public ChannelDao cdao;
	@Override
	public String getAllChnl() {
		String str = "";
//		String chnlSql = "select CHNLNAME as chnlname,PARENTID as parentid,DOCCHNLID as docchnlId from CHANNELS";
		String chnlhql = "from Channels"; 
		List<Channels> list = cdao.getAllList(chnlhql);
		if(list.size()>0){
			JSONObject objes = new JSONObject();
			JSONArray array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
//				Map<String,Object> map = list.get(i);
				JSONObject obj = new JSONObject();
				obj.put("chnlname",list.get(i).getChnlname()); //map.get("chnlname")
				obj.put("parentid", list.get(i).getParentid());//map.get("parentid")+""
				obj.put("chnlurl",list.get(i).getChnlurl()); //map.get("docchnlId")
				obj.put("channelId", list.get(i).getChannelid());
				//obj.put("siteId", list.get(i).getSiteId());
				//obj.put("siteName", list.get(i).getSiteName());
				//obj.put("siteDesc", list.get(i).getSiteDesc());
				array.add(obj);
			}
			objes.put("list", array);
			objes.put("results", "success");
			str = objes.toString();
		}
		return str;
	}
	@Override
	public String addChnl(String name, String parentId,String cruser,Timestamp crtime,String chnlurl) {
		Channels chnl = new Channels();
		chnl.setChnlname(name);
		//chnl.setChnldesc(name);
		chnl.setParentid(Integer.parseInt(parentId));
		chnl.setCruser(cruser);
		chnl.setCrtime(crtime);
		chnl.setChnlurl(chnlurl);
		//chnl.setChnlorder(0);
		//chnl.setChnltype(0);
		//chnl.setDocchnlId(0);
		//chnl.setSiteDesc("");
		//chnl.setSiteId(0);
		//chnl.setSiteName("");
		chnl.setStatus(0);
		int nums = cdao.saveDao(chnl);
		JSONObject obj = new JSONObject();
		if(nums>0) {
			obj.put("results","SUCCESS");
			obj.put("chnlId", nums);
		}else {
			obj.put("results","error");
		}
		return obj.toString();
		
	}
	@Override
	public String updateChnl(String id, String edit_name, String edit_channelId) {
		String hql = "update Channels set chnlname='"+edit_name+"' where channelid="+Integer.parseInt(id);
		Object[] objs = {}; 
    	int nums=cdao.updateChnl(hql,objs);
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
	
	public String upChannelPidById(String id,String pid) {
		String hql = "update Channels set parentid=?0 where channelid=?1";
    	//Object[] objs = {Integer.parseInt(pid),Integer.parseInt(id)}; 
        int num = cdao.upChannelPidById(hql,id,pid);
        JSONObject obj=new JSONObject();
        String string = "";
        if(num>0) {
        	string = "success";
        }
        return string;
    }
	
	@Override
	public String delChnlById(String id) {
		String hql = "delete from Channels where channelid=?0";
        int nums=cdao.delChnlById(hql,Integer.parseInt(id)); //String name
        String hql1 = "delete from KnowledgePointEntity where epcid=?0";
        int nums1=cdao.delChnlById(hql1,Integer.parseInt(id)); //String name
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
	
	@Override
	public Channels findById(Integer id) {
		// TODO Auto-generated method stub
		String hql = "from Channels where channelid=?0";
		Channels channels = cdao.getById(hql,id);
		return channels;
	}
	
	@Override
	public List findByName(String channelName,String parentid) {
		// TODO Auto-generated method stub
		String hql = "from Channels where chnlname=?0 and parentid=?1";
		List list = cdao.getAllChannelList(hql,channelName,parentid);
		return list;
	}
	
	@Override
	public List<Channels> findByName(String channelName) {
		// TODO Auto-generated method stub
		String hql = "from Channels where chnlname like ?0";
		List<Channels> list = cdao.getAllChannelList(hql,channelName);
		return list;
	}
	
	
	
	/**
	 * 多词进行搜索
	 **/
	@Override
	public List<Channels> getQuestionAnswerListByWords(List<String> wordsList) {
		StringBuilder hqlString = new StringBuilder();
		hqlString.append("from Channels where 1=1");
		if (wordsList.size() > 0) {
			for (String str : wordsList) {
				hqlString.append(" and chnlname like '%" + str + "%'");
			}

		}
		
		return cdao.getAllList(hqlString.toString());

	}
	
	
}
