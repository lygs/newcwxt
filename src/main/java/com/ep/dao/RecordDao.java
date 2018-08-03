package com.ep.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.RecordEntity;

@Repository
@Transactional
public class RecordDao {

	@Autowired
	private SessionFactory sessionFactory;

	//@SuppressWarnings("unused")
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	
	/**
	 * 修改记录
	 * @param hql
	 * @param objs
	 * @return
	 */
	public int updateObj(String hql, Object[] objs) {
		Session sess = this.getSession();
//		int results = this.getSession().createQuery(sql).setParameter(0, id).executeUpdate();
		Query querys =sess.createQuery(hql);
		if(objs.length>0){
			for (int i = 0; i < objs.length; i++) {
				querys.setParameter(i, objs[i]);
			}
		}
		int results = querys.executeUpdate();
		//closeSess(sess);
		return results;
	}
	
	/**
	 * 保存记录
	 * @return
	 */
	public int updataOrSaveRecord(RecordEntity rEntity){
		Session session = this.getSession();
	//	Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(rEntity);
		//	transaction.commit();
			return 1;
		} catch (Exception e) {
			//transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
			//closeSess(session);
		}
		
	}
	
	public List getAllList(Map map) {
		Session session = this.getSession();
		Query query = session.createSQLQuery((String) map.get("hql")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults((int) map.get("pageSize"));
		query.setFirstResult((int) map.get("page"));
		List list = query.list();
		//closeSess(session);
		return list;

	}
	
	/**
	 * 总数
	 * @param hql
	 * @return
	 */
	public int getRecordEntityAllCount(String hql) {
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
		//closeSess(session);
		return nums.intValue();
	}
	
	
	public List selectRecordByHql(String hql) {
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List list = query.list();
		//closeSess(session);
		return list;
		
	}

	public int delRecord(String hql,int parseInt) {
		Session sess = this.getSession();
		int results =sess.createQuery(hql).setParameter("0",parseInt).executeUpdate();
		//closeSess(sess);
		return results;
	}
}
