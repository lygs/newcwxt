package com.ep.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.Channels;


@Repository
@Transactional
public class ChannelDao {

	@Autowired
	private SessionFactory sessionFactory;
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Channels getById(String sql,Integer id) {
		Session session = this.getSession();
		Query query = session.createQuery(sql).setParameter("0", id);
		List<Channels> list = query.list();
		return list.get(0);
	}
	
	public List getAllList(String sql) {
		Session session = this.getSession();
//		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//返回的是Map集合需要設置 setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Query query = session.createQuery(sql);
		List list = query.list();
		return list;
	}
	
	public List getAllChannelList(String sql,String channelName,String parentid) {
		Session session = this.getSession();
//		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//返回的是Map集合需要設置 setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Query query = session.createQuery(sql).setParameter("0", channelName).setParameter("1", Integer.parseInt(parentid));
		List list = query.list();
		return list;
	}
	
	public List getAllChannelList(String sql,String channelName) {
		Session session = this.getSession();
//		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//返回的是Map集合需要設置 setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Query query = session.createQuery(sql).setParameter("0",  "%" + channelName + "%");
		List list = query.list();
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

	public int saveDao(Object chnl) {
//		 this.getSession().save(chnl);
	        Session sess = this.getSession();
			int nums = (int) sess.save(chnl); 
			return nums;
	}

	public int upChannelPidById(String hql,String id,String pid) {
		Session sess = this.getSession();
		int results =sess.createQuery(hql).setParameter("0",Integer.parseInt(pid)).setParameter("1", Integer.parseInt(id)).executeUpdate();
		return results;
	}
	public int updateChnl(String hql, Object[] objs) {
		Session sess = this.getSession();
//		int results = this.getSession().createQuery(sql).setParameter(0, id).executeUpdate();
		Query querys =sess.createQuery(hql);
		if(objs.length>0){
			for (int i = 0; i < objs.length; i++) {
				querys.setParameter(String.valueOf(i), objs[i]);
			}
		}
		int results = querys.executeUpdate();
		return results;
	}

	public int delChnlById(String hql, int parseInt) {
		Session sess = this.getSession();
		int results =sess.createQuery(hql).setParameter("0",parseInt).executeUpdate();
		return results;
	}
}
