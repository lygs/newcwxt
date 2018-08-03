package com.ep.service;

import java.util.List;


import com.ep.entity.QuestionAnswerEntity;

public interface QuestionAnswerService {
    public List<QuestionAnswerEntity> getQuestionAnswerAllList(int pageSize,int pageNumber,String qaQuestion, String chnlId);//查询所有的问题记录
    
    public List getQuestionByComm(int pageSize,int pageNumber,String qaQuestion); //查询常见问题
    
    public List<QuestionAnswerEntity>  getQuestionAnswerByQuestion(String qaQuestion,String keywords,String resourse);//前端进行提问（依据问题进行查询）
    
    public QuestionAnswerEntity getQuestionAnswerById(Integer id);//根据ID获取单条数据集
    
    public int updateQuestionAnswerById(QuestionAnswerEntity questionAnswerEntity);//依据ID修改问题
    
    public int delQuestionAnswerById(Integer id);//删除问题
    
    public int updateQuestionAnswerSUMById(int id);//修改访问量（访问一次+1）
    
    public List<QuestionAnswerEntity> findAllByAnswer();
    public List<QuestionAnswerEntity> findAll();
    
    public int updateQuestionAnswerGoodnumById(int id);//修改点赞量（点赞一次+1）
    
    public int updateQuestionAnswerBadnumById(int id);//修改不点赞量（点不赞一次+1）
    
    public Integer saveQuestionAnswer(QuestionAnswerEntity questionAnswerEntity);//保存管理添加的标准问题
    public int updateProblemByChangj(String id, String isChangj); //设为常见

    public int getQuestionAnswerTotal(String qaQuestion, String chnlId);

	public List getQuestionAnswerByKnowledgeId(Integer qaKnowledgeId,Integer pageNumber,Integer pageSize);

	//public String docSync(int parseInt, String pageSize, String pageNumber);

	//public void chnlSync();

	public String getAllQa(String pageSize, String pageNumber,String keyword);
	public String getQuestionByClick(String pageNumber);
	//输入多词查询
	public List<QuestionAnswerEntity> getQuestionAnswerListByWords(List<String> wordsList);
	public String getQaByChnlid(String pageSize, String pageNumber, String cid);
	public String getChnlNameByQid(String cid);
}
