package com.ep.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.LogsDao;
import com.ep.entity.Channels;
import com.ep.entity.IndividualWord;
import com.ep.entity.LogMessage;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.service.ChannelService;
import com.ep.service.IndividualWordService;
import com.ep.service.LuceneService;
import com.ep.service.QuestionAnswerService;
import com.ep.util.DateUtil;

@Service("luceneService")
public class LuceneServiceImpl implements LuceneService {
	@Resource
	private QuestionAnswerService questionAnswerService;
	
	@Autowired
    public LogsDao logsDao;
	
	@Resource
	private ChannelService channelService;
	@Resource
	private IndividualWordService individualWordService;
	//多词查询 1、完全匹配 （栏目/问题） 2、分词（栏目/问题）
	public List<QuestionAnswerEntity> getData(String qaQuestion,List<String> wordsList) {
		//1.完全匹配
		//联想词匹配
		
		//标准问题完全匹配
		List<QuestionAnswerEntity> qList = questionAnswerService.getQuestionAnswerAllList(8, 1, qaQuestion, "");
		//栏目完全匹配
		//List<Channels> cList = channelService.findByName(qaQuestion);
		//2、分词查询
		//分词标准问题完全匹配
		List<QuestionAnswerEntity> fqList  = questionAnswerService.getQuestionAnswerListByWords(wordsList);
		qList.addAll(fqList);
		//分词栏目完全匹配
		//List<Channels> fcList  = channelService.getQuestionAnswerListByWords(wordsList);
		//3.分词匹配单个栏目
		LogMessage logs = new LogMessage();
		logs.setClassMethod("getData");
		logs.setClassName("luceneService");
		logs.setContent("智能查询问题成功");
		logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		logsDao.saveDao(logs);
		return qList;
		
		
		
	}
}
