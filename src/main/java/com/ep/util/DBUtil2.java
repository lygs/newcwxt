package com.ep.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.ep.entity.Channels;
import com.mchange.v2.holders.ChangeNotifyingSynchronizedIntHolder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: DBUtil 
 * @Description: TODO() 
 * @author: ly
 */
public class DBUtil2 {
	
	public static void main(String[] args) {
		/*Connection conn = null;//连接对象
		PreparedStatement ps = null;//声明
		ResultSet rs = null;//记录集
		
		try {
			String sql = "";
			conn = DBUtil.getConnection();//获取数据库连接
			DBUtil.beginTransaction(conn);//开启事务
			DBUtil.commitTransaction(conn);//提交事务
			
			
		} catch (Exception e) {
			DBUtil.rollbackTransaction(conn);//回滚事务
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);//关闭记录集
			DBUtil.close(ps);//关闭声明
			DBUtil.close(conn);//关闭连接对象
		}*/
		Connection connection = getConnection(); 
		String sql=" SELECT c.channelid as cid,c.PARENTID as parentid,c.chnlname as chnlname,f.DATAPATH as datapath from WCMCHANNEL c LEFT JOIN WCMFOLDERPUBLISHCONFIG f on c.channelid=f.folderid WHERE c.SITEID=22";
		List<Channels> list = getChannelList(sql);
		List<Channels> cList = new ArrayList<Channels>();
		for(int i=0;i<list.size();i++ ) {
			cList = getData(list,list.get(i).getParentid(),list.get(i).getChannelid(),list.get(i).getChnlurl());
		}
		System.out.println(cList.size());
		JSONArray array =new JSONArray();
		for(int i=0;i<cList.size();i++ ) {
			JSONObject object = new JSONObject();
			object.put("channelid", cList.get(i).getChannelid());
			object.put("chnlname", cList.get(i).getChnlname());
			object.put("parentid", cList.get(i).getParentid());
			object.put("chnlUrl", cList.get(i).getChnlurl());
			array.add(object);
		}
		System.out.println(array.toString());
	}
	
	public static List<Channels> getData(List<Channels> list,int parentid,int channelid,String urls) {

		String url = "";
		int pid=-1;
		boolean f = false;
		if(parentid!=0) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getChannelid()==parentid) {
					url = list.get(i).getChnlurl()+"/"+urls;
					pid =list.get(i).getParentid();
					f = true;
					break;
				}	
				
			}
		}else {
			pid=0;f=true;url =urls;
		}
		
		if(pid!=0 && f) {
			getData(list,pid,channelid,url);
		}else {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getChannelid()==channelid && StringUtils.isNotBlank(url)) {
					list.get(i).setChnlurl(url);
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取数据库连接
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			String driverClass = null;
			String url = null;
			String username = null;
			String password = null;
			//从db-config.properties中获取数据库配置
			Properties p = new Properties();
			InputStream in = DBUtil.class.getResourceAsStream("/jdbc.properties");  
			p.load(in);
			driverClass = p.getProperty("jdbc.driver1");
			url = p.getProperty("jdbc.url1");
			username = p.getProperty("jdbc.username1");
			password = p.getProperty("jdbc.password1");	
			Class.forName(driverClass);
			conn =DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 开启事务
	 */
	public static void beginTransaction(Connection conn){
		if(conn != null){
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 提交事务
	 */
	public static void commitTransaction(Connection conn){
		if(conn != null){
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 事务回滚
	 */
	public static void rollbackTransaction(Connection conn){
		if(conn != null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 重置事务为默认状态
	 */
	public static void resetConnection(Connection conn){
		if(conn != null){
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭hibernate session
	 */
	public static void close(Session session){
		if(session != null){
			try {
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void close(Connection conn){
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭Statement
	 */
	public static void close(Statement ps){
		if(ps != null) {
			try {
				ps.close();//关闭Statement
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭ResultSet
	 */
	public static void close(ResultSet rs){
		if(rs != null) {
			try {
				rs.close();//关闭ResultSet
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<Channels> getChannelList(String sql){
	 	Connection connection=getConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Channels> list=new ArrayList<Channels>();
        try {
            ps=connection.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
            	Channels ch=new Channels();
            	ch.setChannelid(Integer.parseInt(rs.getString("cid")));
            	ch.setParentid(Integer.parseInt(rs.getString("parentid")));
                ch.setChnlname(rs.getString("chnlname"));
                ch.setChnlurl(rs.getString("datapath"));
                list.add(ch);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

	 public List<Channels> getList(String sql){
		 	Connection connection=getConnection();
	        PreparedStatement ps=null;
	        ResultSet rs=null;
	        List<Channels> list=new ArrayList<Channels>();
	        try {
	            ps=connection.prepareStatement(sql);
	            rs=ps.executeQuery();
	            while(rs.next()){
	            	Channels ch=new Channels();
	                ch.setChnlname(rs.getString("chnlname"));
	                ch.setCrtime(rs.getTimestamp("crtime"));
	                ch.setCruser(rs.getString("cruser"));
	                ch.setParentid(rs.getInt("parentid"));
	                ch.setStatus(rs.getInt("status"));
	                list.add(ch);
	            }
	            return list;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                if(rs!=null){
	                    rs.close();
	                }
	                if(ps!=null){
	                    ps.close();
	                }
	                if(connection!=null){
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return list;
	    }
	 
	
}
