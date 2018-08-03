package com.ep.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.HighFrequencyWordsEntity;
import com.ep.entity.Sysuser;


/**
* @author Zhaoxh
* @version 创建时间：2018年6月26日 上午9:44:31
* @about 关键词
*/
@Repository
@Transactional
public class HighFrequencyWordsDao {

	@Autowired
    private SessionFactory sessionFactory;
	
	//@SuppressWarnings("unused")
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	
	/**
	 * 根据关键字查询
	 * @param words 关键字
	 * @return 实体
	 */
	public HighFrequencyWordsEntity getHFWByWord(String words) {
		    HighFrequencyWordsEntity entity = (HighFrequencyWordsEntity)this.getSession().createQuery("from HighFrequencyWordsEntity where words= '"+words+"'").uniqueResult();
		return entity;
		
	}
	
	/**
	 * 更新保存
	 * @param obj
	 * @return
	 */
	public int updataOrSaveHFW(HighFrequencyWordsEntity obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
		//	transaction.commit();
			return 1;
		} catch (Exception e) {
		//	transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
			//closeSess(session);
		}
	}
	
	/**
	 * 查询前20
	 * @return
	 */
	public List<HighFrequencyWordsEntity> getHFWByTop() {
		Session session = this.getSession();
		Query query = session.createQuery("from HighFrequencyWordsEntity order by num desc ");
		query.setMaxResults(20);
		query.setFirstResult(0);
		List<HighFrequencyWordsEntity> list = query.list();
		//closeSess(session);
		return list;
		
	}

	public List<Sysuser> getGpcAllList(String hql, String pageSize, String pageNum) {
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		query.setMaxResults(Integer.parseInt(pageSize));
		query.setFirstResult((Integer.parseInt(pageNum)-1)*Integer.parseInt(pageSize));
		 List list = query.list();
		// closeSess(sess);
		 return list;
	}

	public int getCount(String hql) {
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
		//closeSess(sess);
		return nums.intValue();
	}
	
}
