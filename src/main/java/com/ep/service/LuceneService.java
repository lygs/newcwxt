package com.ep.service;

import java.util.List;

import com.ep.entity.QuestionAnswerEntity;

public interface LuceneService {
	public List<QuestionAnswerEntity> getData(String qaQuestion,List<String> wordsList);
}
