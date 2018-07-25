package com.ep.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ep.entity.IndividualWord;
import com.ep.util.CMyString;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月4日 下午3:50:46
* 
*/
@Repository
@Transactional
public class IndividualWordDao {

	@Autowired
    private SessionFactory sessionFactory;
	
	//@SuppressWarnings("unused")
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	
	/**
	 * 
	* @author  Zhaoxh
	* @version 创建时间：2018年7月4日 下午4:03:10
	* @Description: TODO
	* @param hql 
	* @param pageSize
	* @param pageNum
	* @return 
	*/
	public List<IndividualWord> getDataByTitle( String hql, String pageSize, String pageNum){
		Session session = this.getSession();
		Query query =session.createQuery(hql);
		if(!CMyString.isEmpty(pageSize) && !CMyString.isEmpty(pageNum)) {
			query.setMaxResults(Integer.parseInt(pageSize));
			query.setFirstResult((Integer.parseInt(pageNum)-1)*Integer.parseInt(pageSize));
		}
		 List<IndividualWord> list = query.list();
		// closeSess(session);
		return list;
	}
	
	/**
	 * 
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月4日 下午4:05:01
	 * @Description: TODO
	 * @param obj
     * @return 返回对象的id
	 */
	public Integer updataOrSaveIndividualWord(IndividualWord obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			 session.saveOrUpdate(obj);
			//transaction.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			//this.closeSess(session);
		}
		
	}
	
	/**
	 * 查询总条数
	 * @param hql
	 * @return
	 */
	public int getTotal(String sql) {
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
			//closeSess(session);
		}
	}
	
	/**
	 * 根据对象删除
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月6日 上午9:50:18
	 * @Description: TODO
	 * @param obj
	 */
	public int delByObj(IndividualWord obj) {
		Session session = this.getSession();
		//Transaction transaction = session.beginTransaction();
		try {
			session.delete(obj);
		//	transaction.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		//	transaction.rollback();
			return -1;
		}finally {
			//closeSess(session);
		}
		
	}
	
	
}
