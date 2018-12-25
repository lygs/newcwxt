package com.ep.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.Channels;
import com.ep.entity.LogMessage;


@Repository
@Transactional
public class LogsDao {

	@Autowired
	private SessionFactory sessionFactory;
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	public List<LogMessage> getAllData(String hql, String pageSize, String pageNumber) {
		Session sess = this.getSession();
		Query query = sess.createQuery(hql);
		query.setMaxResults(Integer.parseInt(pageSize));
		query.setFirstResult((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize));
		List<LogMessage> list = query.list();
		return list;
	}

	
	
	/**
	 * 总数
	 * @param hql
	 * @return
	 */
	public int getAllCount(String hql) {
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
		return nums.intValue();
	}

	public int saveDao(Object logs) {
	        Session sess = this.getSession();
			int nums = (int) sess.save(logs); 
			return nums;
	}

	
}
