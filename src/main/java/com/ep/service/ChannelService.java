package com.ep.service;

import java.sql.Timestamp;
import java.util.List;

import com.ep.entity.Channels;

/**
 * 
 * @author admin
 *
 */
public interface ChannelService {
	public String getAllChnl();
	public String addChnl(String name, String parentId, String cruser, Timestamp crtime,String kurl);
	public String updateChnl(String id, String edit_name, String edit_channelId,String chnlurl);
	public String delChnlById(String id);
	public Channels findById(Integer id);
	public List<Channels> findByName(String channelName,String parentid);
	public List<Channels> findByName(String channelName);
	public String upChannelPidById(String id,String pid) ;
	//输入多词查询
	public List<Channels> getQuestionAnswerListByWords(List<String> wordsList);
	
}
