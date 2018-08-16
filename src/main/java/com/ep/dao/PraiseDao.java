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

import com.ep.entity.PraiseEntity;

@Repository
@Transactional
public class PraiseDao {

	@Autowired
    private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * ���桢���µ��޼�¼
	 * @param praise
	 * @return
	 */
	public int updataOrSavePraise(PraiseEntity praise){
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(praise);
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
	
	/**
	 * ����SQL��ѯ����
	 * @param sql
	 * @return
	 */
	public int selectPraiseTotal(String sql) {
		Session session = this.getSession();
		try {
			//Query query = session.createQuery(sql);
			Query query = session.createSQLQuery(sql);
			/*List<BigInteger> list = query.list();
	        int count = list.get(0).intValue();
	        return count;*/
			query.setMaxResults(1);
			Number nums = (Number) query.uniqueResult();
			return nums.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
		//	closeSess(session);
		}
	}
	
	/**
	 * ��ҳ��ѯ���޼�¼
	 * @param map
	 * @return
	 */
	public List selectPraise(Map map) {
		Session session = this.getSession();
		Query query = session.createQuery((String) map.get("hql"));
		//Query query =  session.createSQLQuery((String) map.get("hql")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		int pageSize = (int) map.get("pageSize");
		int curPage = (int) map.get("pageNumber");
		query.setFirstResult(pageSize * (curPage - 1));
		query.setMaxResults(pageSize);
		List list = query.list();
		
		//closeSess(session);
		return list;
		
	}
	
}
