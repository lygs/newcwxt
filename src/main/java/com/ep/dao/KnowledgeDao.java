package com.ep.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.util.DBUtil;


/**
 * 
 * @author admin
 *
 */
@Repository
@Transactional
public class KnowledgeDao {	
	@Autowired
    private SessionFactory sessionFactory;
	
	//@SuppressWarnings("unused")
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	/*public void closeSession(){
		if(this.getSession()!=null){
			this.getSession().flush();
			this.getSession().clear();
			this.getSession().close();
		}
	}*/
	
	/**
	 * 查询知识点 、热点
	 * @param hql
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List getKnowAllList(String hql,String pageSize,String pageNumber){
		Session sess = this.getSession();
		Query query =sess.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(Integer.parseInt(pageSize));
		query.setFirstResult((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize));
		 List list = query.list();
		 return list;
	}
	
	public List getKnowAllList(String hql){
		 Session sess = this.getSession();
		 Query query =sess.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List list = query.list();
		 return list;
	}
	
	public List getByEpcidAndKname(String hql,String epcid,String kname){
		 Session sess = this.getSession();
		 Query query =sess.createQuery(hql);
		 query.setInteger(0, Integer.parseInt(epcid));
		 query.setString(1, kname);
		 List list = query.list();
		 return list;
	}
	
	
	/**
	 * 查询总条数
	 * @param hql
	 * @return
	 */
	public int getKnowAllCount(String hql){
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
		return nums.intValue();
	}
	
	 public int getSqlTotal(String sql){
	        int total=0;
	        Connection connss = DBUtil.getConnection();
	        PreparedStatement psss = null;
	        ResultSet rsss = null;
	        try {
	            psss = connss.prepareStatement(sql);
	            rsss = psss.executeQuery();
	            if(rsss.next()) {
	               total=rsss.getInt("num");
	            }
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }finally{
	            DBUtil.close(rsss);//关闭记录集
	            DBUtil.close(psss);//关闭声明
	            DBUtil.close(connss);//关闭连接对象
	        }
	        return total;
	    }
	
	
	
	/**
	 * 添加知识点
	 * @param hql
	 * @return
	 */
	public Integer addKnows(Object obj){ //KnowledgePointEntity
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			Serializable result = session.save(obj);
			transaction.commit();
			Integer integer = (Integer) result;
			return integer;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
		}
		
	}
	
	/**
	 * 根据id删除知识点
	 * @param class1
	 * @param id
	 * @return
	 */
	public int delKnowsById(String sql,int id) {
		Session sess = this.getSession();
		int results =sess.createQuery(sql).setParameter("0", id).executeUpdate();
		return results;
	}
	/**
	 * 修改知识点
	 * @param sql
	 * @param id
	 * @return
	 */
	public int updateKnows(String hql,Object[] params) {
		Session sess = this.getSession();
//		int results = this.getSession().createQuery(sql).setParameter(0, id).executeUpdate();
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
