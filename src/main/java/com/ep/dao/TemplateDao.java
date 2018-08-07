package com.ep.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author admin
 *
 */
@Repository
@Transactional
public class TemplateDao {	
	@Autowired
    private SessionFactory sessionFactory;
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

	
	/**
	 * 查询所有模板
	 * @param hql
	 * @return
	 */
	public List getList(String hql){
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		 List list = query.list();
		 return list;
	}
	/**
	 * 修改知识点
	 * @param sql
	 * @param id
	 * @return
	 */
	public int updateSelectedStatus(String hql,Object[] params) {
		Session sess = this.getSession();
		Query querys =sess.createQuery(hql);
		if(params.length>0){
			for (int i = 0; i < params.length; i++) {
				querys.setParameter(String.valueOf(i), params[i]);
			}
		}
		int results = querys.executeUpdate();
		return results;
	}
	
}
