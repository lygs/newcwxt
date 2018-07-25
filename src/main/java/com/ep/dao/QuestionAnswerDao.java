package com.ep.dao;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.Channels;
import com.ep.entity.QuestionAnswerEntity;
import com.ep.util.DBUtil;

@Repository
@Transactional
public class QuestionAnswerDao {

	@Autowired
	private SessionFactory sessionFactory;

	//@SuppressWarnings("unused")
	private Session getSession() {
		
		return sessionFactory.getCurrentSession();
		//return sessionFactory.openSession();
	}

	/*public void closeSess(Session session) {
		if (session != null) {
			session.flush();
			session.clear();
			session.close();
		}
	}*/

	public int updateObj(String hql, Object[] objs) {
		Session sess = this.getSession();
//		int results = this.getSession().createQuery(sql).setParameter(0, id).executeUpdate();
		Query querys =sess.createQuery(hql);
		// Transaction tx = sess.beginTransaction();
		if(objs.length>0){
			for (int i = 0; i < objs.length; i++) {
				querys.setParameter(String.valueOf(i), objs[i]);
			}
		}
		int results = querys.executeUpdate();
		//tx.commit();
		//closeSess(sess);
		return results;
	}
	
	/**

	 * 查询所有问题

	 * 
	 * @param map
	 * @return
	 */
	public List getQuestionAnswerList(String hql,String pageSize,String pageNumber) {
		Session sess = this.getSession();
		Query query =sess.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(Integer.parseInt(pageSize));
		query.setFirstResult((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize));
		 List list = query.list();
		// closeSess(sess);
		 return list;
	}

	/**
	 * 查询常见问题
	 * @param map
	 * @return
	 */
	public List getQuestionAnswerByComm(Map map) {
		Session session = this.getSession();
		Query query = session.createQuery((String) map.get("hql"));
		query.setMaxResults((int) map.get("pageSize"));
		query.setFirstResult((int) map.get("page"));
		List list = query.list();
	//	closeSess(session);
		return list;

	}
	
	public List<QuestionAnswerEntity> findAllByAnswer(String hql){
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List list = query.list();
		//closeSess(session);
		return list;
	}
	
	/**
	 * 查询常见问题
	 * @param map
	 * @return
	 */
	public List getAllQuestion(String sql ,int pageSize,int pageNumber) {
		Connection conn = null;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			List list = new ArrayList();
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			Map map = null;
			while (rs.next()) {
				map = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					Object obj = rs.getObject(md.getColumnName(i));
					if (obj == null) {
						map.put(md.getColumnName(i), "");//为空时 返回字符串空""
					} else if ("CLOB".equals(obj.getClass().getSimpleName())) {
						Clob clob = (Clob) obj;
						map.put(md.getColumnName(i), clob.getSubString(1, (int)clob.length()));//clob字段的处理
					} else {
						map.put(md.getColumnName(i), obj);
					}
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(rs);//关闭ResultSet
			DBUtil.close(ps);//关闭Statement
			DBUtil.close(conn);//关闭数据库连接
		}
	}

	/**
	 * 查询总条数
	 * @param hql
	 * @return
	 */
	public int getAllCount(String hql){
		Session sess = this.getSession();
		Query query =sess.createQuery(hql);
		query.setMaxResults(1);
		Number nums = (Number) query.uniqueResult();
	//	closeSess(sess);
		return nums.intValue();
	}
	
	
	/**

	 * 根据id查询

	 * 
	 * @param id
	 * @return
	 */
	public Object getQuestionAnswerById(int id) {
		Session session = this.getSession();
		Object result = session.get(QuestionAnswerEntity.class, id);
		
		//closeSess(session);
		return result;

	}

	/**
	 * 总数
	 * @param hql
	 * @return
	 */
	public int getQuestionAnswerAllCount(String hql,String chnlId) {
		int num =0;
		if(StringUtils.isBlank(chnlId)){
			Session session = this.getSession();
			Query query = session.createQuery(hql);
			query.setMaxResults(1);
			Number nums = (Number) query.uniqueResult();
			num = nums.intValue();
			//closeSess(session);
		}else{
			num = getDocTotal(hql);
		}
		return num;
	}
	
	/**
	 * 總數
	 * <p>Title: getDocTotal</p>  
	 * <p>Description: </p>  
	 * @param sql
	 * @return
	 */
	public int getDocTotal(String sql){
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
            e.printStackTrace();
        }finally{
            DBUtil.close(rsss);//关闭记录集
            DBUtil.close(psss);//关闭声明
            DBUtil.close(connss);//关闭连接对象
        }
        return total;
    }

	/**

	 * 添加问题

	 * 
	 */
	public Integer saveQuestionAnswer(QuestionAnswerEntity obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			Serializable result = session.save(obj);
			//transaction.commit();
			Integer integer = (Integer) result;
			return integer;
		} catch (Exception e) {
			//transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
			//closeSess(session);
		}

	}

	/**

	 * 更新问题

	 */
	public int updataOrSaveQuestionAnswer(QuestionAnswerEntity obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			
			session.saveOrUpdate(obj);
			//transaction.commit();
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
	 * 删除问题
	 * @param obj
	 * @return
	 */
	public int delQuestionAnswer(QuestionAnswerEntity obj) {
		Session session = this.getSession();
	//	Transaction transaction = session.beginTransaction();
		try {
			
			session.delete(obj);
			//transaction.com mit();
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
	 * 根据知识点ID查询问题
	 * @param map
	 * @return
	 */
	public List getQuestionAnswerByKnowledgeId(Map map) {
		Session session = this.getSession();
		Query query = session.createQuery((String) map.get("hql"));
		query.setInteger("0", (int) map.get("id"));
		query.setMaxResults((int) map.get("pageSize"));
		query.setFirstResult((int) map.get("page"));
		List list = query.list();
	//	closeSess(session);
		return list;
	}
	
	/**
	 * 根据知识点ID查询问题
	 * @param map
	 * @return
	 */
	public List getQuestionAnswerByQuestion(Map map) {
		Session session = this.getSession();
		Query query = session.createQuery((String) map.get("hql"));
		query.setString(0, map.get("qaQuestion").toString());
		query.setString(1, map.get("keywords").toString());
		query.setString(2, map.get("resourse").toString());
		List list = query.list();
	//	closeSess(session);
		return list;
	}
	
	 
    public Integer save(Channels obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			Serializable result = session.save(obj);
		//	transaction.commit();
			Integer integer = (Integer) result;
			return integer;
		} catch (Exception e) {
			//transaction.rollback();
			e.printStackTrace();
			return 0;
		}finally {
			//closeSess(session);
		}

	}
    
    public Integer saveQa(QuestionAnswerEntity obj) {
  		Session session = this.getSession();
  		//Transaction transaction = session.beginTransaction();
  		try {
  			Serializable result = session.save(obj);
  		//	transaction.commit();
  			Integer integer = (Integer) result;
  			return integer;
  		} catch (Exception e) {
  			//transaction.rollback();
  			e.printStackTrace();
  			return 0;
  		}finally {
  		//	closeSess(session);
  		}
  	}
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
		//closeSess(sess);
		return obj;
	}
    public Integer updates(Channels obj) {
		Session session = this.getSession();
	//	Transaction transaction = session.beginTransaction();
		try {
			session.update(obj);
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
    
    public List<Map<String, Object>> getChnlBid(String sql) {
		Session session = this.getSession();
		Query query =  session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = query.list();
		//closeSess(session);
		return list;
		
	}

}
