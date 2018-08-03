package com.ep.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface KnowledgePointService {
  public String getKnowAllList(String name,String pageSize,String pageNum,String chnlId,HttpServletRequest requests);
  public String addKnows(String name,String channelId,int uid);
  public String updateKnows(String id,String name,String channelId);
  public String updateKnowsByHot(String id,String isHot);
  public String getHotKnowledge(String pageSize,String pageNumber);
  public String delKnowsById(String id);
  public String getKnowAll(String chnlId);
  public String getKnowAll();
  public List getByEpcidAndKname(String epcid,String kname);

}
