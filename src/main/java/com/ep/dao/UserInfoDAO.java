package com.ep.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.Channels;
import com.ep.entity.Sysuser;

@Repository
@Transactional
public class UserInfoDAO {
	
	@Autowired
    private SessionFactory sessionFactory;
	
	//@SuppressWarnings("unused")
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	public int getCount(String hql){
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
	//	closeSess(sess);
		return nums.intValue();
	}
	
	/**
	 * 根据条件查询单条记录
	 * @param hql
	 * @param params
	 * @return
	 */
	public Object getObj(String hql,Object[] params){
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		if(params!=null && params.length>0){
			for(int i=0;i<params.length;i++){
				query.setParameter(String.valueOf(i), params[i]);
			}
		}
		query.setMaxResults(1);
		Object obj = query.uniqueResult();
	//	closeSess(sess);
		return obj;
	}
	/**
	 * 修改用户
	 * @param hql
	 * @param params
	 * @return
	 */
	public int updateUser(String hql,Object[] params) {
		Session sess = this.getSession();
//		int results = this.getSession().createQuery(sql).setParameter(0, id).executeUpdate();
		Query querys =sess.createQuery(hql);
		if(params!=null && params.length>0){
			for (int i = 0; i < params.length; i++) {
				querys.setParameter(i, params[i]);
			}
		}
		int results = querys.executeUpdate();
		//closeSess(sess);
		return results;
	}
	public int updataOrSaveUser(Sysuser obj) {
		Session session = this.getSession();
//		Transaction transaction = session.beginTransaction();
		int num = 0;
		try {
			session.saveOrUpdate(obj);
//			transaction.commit();
			num = 1;
		} catch (Exception e) {
//			transaction.rollback();
			e.printStackTrace();
		}finally {
			//closeSess(session);
		}
		return num;

	}
	

	
//	public Object getObjectById(Class classz,int id) {
//		Session session = this.getSession();
//		Object obj = session.get(classz, id);
//		closeSess(session);
//		return obj;
//	}
	  public Sysuser getObjectById(Class classz,int id) {
	        return (Sysuser) this.getSession().createQuery("from Sysuser where userId=?0").setParameter("0", id).uniqueResult();
	    }

	/**
     * 根据id查询
     * @param id
     * @return
     */
   /* public UserInfo getPersonById(String id) {
        return (UserInfo) this.getSession().createQuery("from UserInfo where uid=?0").setParameter("0", id).uniqueResult();
    }*/
    /**
     * 添加
     * @param person
     */
    /*public void addPerson(UserInfo userInfo) {
        this.getSession().save(userInfo);
    }*/
    //添加欄目
    public void addObject(Channels ch) {
        this.getSession().save(ch);
        Session sess = this.getSession();
		int nums = (int) sess.save(ch); 
		//closeSess(sess);
    }
    
    /**
     * 添加用户
     * @param sql
     * @return
     */
    public int addUser(Object obj){ //String sql
		Session sess = this.getSession();
		int results = (int) sess.save(obj);
//		int results =sess.createSQLQuery(sql).executeUpdate(); 
		//closeSess(sess);
		return results;
	}
    /**
     * 更新
     * @param person
     */
    /*public void updatePerson(UserInfo userInfo) {
        this.getSession().update(userInfo);
    }*/
    /**
     * 删除
     * @param id
     */
    public void deletePersonById(String id) {
        this.getSession().createQuery("delete UserInfo where id=?0").setParameter("0", id).executeUpdate();
    }
    public int deleteUserById(int id) {
        return this.getSession().createQuery("delete Sysuser where id=?0").setParameter("0", id).executeUpdate();
    }
    /**
     * 查询所有
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Sysuser> getUserInfoDatas() {
        return this.getSession().createCriteria(Sysuser.class).list();
    }
    
    public Integer save(Channels obj) {
		Session session = this.getSession();
//		Transaction transaction = session.beginTransaction();
		try {
			Serializable result = session.save(obj);
//			transaction.commit();
			Integer integer = (Integer) result;
			return integer;
		} catch (Exception e) {
//			transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
			//closeSess(session);
		}

	}
	public List<Sysuser> getUserAllList(String hql,
			String pageSize, String pageNum) {
			Session sess = this.getSession();
			Query query =sess.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setMaxResults(Integer.parseInt(pageSize));
			query.setFirstResult((Integer.parseInt(pageNum)-1)*Integer.parseInt(pageSize));
			 List list = query.list();
			 //closeSess(sess);
			 return list;
	}
}
