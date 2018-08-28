package com.ep.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class PropertiesUtil {
	
	/**
	 24      * 
	 25      * @Title: printAllProperty   
	 26      * @Description: 输出所有配置信息  
	 27      * @param props
	 28      * @return void  
	 29      * @throws
	 30      */
	      private static  Map<String, String> printAllProperty(Properties props)  
	      {  
	          @SuppressWarnings("rawtypes")  
	          Enumeration en = props.propertyNames(); 
	          Map<String, String> map = new HashMap<String, String>();
	          while (en.hasMoreElements())  
	          {  
	              String keyss = (String) en.nextElement();
	              String value = props.getProperty(keyss);
	              if("saveAddress".equals(keyss)) {
	            	  map.put("key", keyss);
		              map.put("value", value);
		              break;
	              }
	             
	          }  
	          return map;
	      }
	      
	      private static  Map<String, String> printAllProperty_1(String filePath,Properties props,String keys,String currentTime) throws IOException  
	      {  
	          @SuppressWarnings("rawtypes")  
	          Enumeration en = props.propertyNames(); 
	          Map<String, String> map = new HashMap<String, String>();
	          while (en.hasMoreElements())  
	          {  
	        	  String key = (String) en.nextElement();  
	        	  if(keys.equals(key)){
	        		  if(StringUtils.isNotBlank(currentTime)){
		            	  OutputStream fos = new FileOutputStream(filePath);  
		            	  props.setProperty(key, currentTime);
		                  props.store(fos, "Update '" + key + "' value"); 
		              /*    byte[] bf = new String(currentTime).getBytes();
		                  fos.write(bf, 0, bf.length);*/
		                  fos.close();
		              }
		              String value = props.getProperty(key);
		              map.put("key", key);
		              map.put("value", value);
		              break;
	        	  }
	          }  
	          return map;
	      }
	      /**
	 44      * 根据key读取value
	       * 
	       * @Title: getProperties_1   
	 47      * @Description: 第一种方式：根据文件名使用spring中的工具类进行解析  
	 48      *                  filePath是相对路劲，文件需在classpath目录下
	 49      *                   比如：config.properties在包com.test.config下， 
	 50      *                路径就是com/test/config/config.properties    
	 51      *          
	 52      * @param filePath 
	 53      * @param keyWord      
	 54      * @return
	 55      * @return String  
	 56      * @throws
	 57      */
	      public static String getProperties_1(String filePath, String keyWord){
	          Properties prop = null;
	          String value = null;
	          try {
	              // 通过Spring中的PropertiesLoaderUtils工具类进行获取
	        	 // System.out.println(PropertiesUtil.class.getResource(filePath).getPath());
	              prop = PropertiesLoaderUtils.loadAllProperties(PropertiesUtil.class.getResource(filePath).getPath());
	              // 根据关键字查询相应的值
	              value = prop.getProperty(keyWord);
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          return value;
	      }
	      
	      /**
	 73      * 读取配置文件所有信息
	 74      * 
	 75      * @Title: getProperties_1   
	 76      * @Description: 第一种方式：根据文件名使用Spring中的工具类进行解析  
	 77      *                  filePath是相对路劲，文件需在classpath目录下
	 78      *                   比如：config.properties在包com.test.config下， 
	 79      *                路径就是com/test/config/config.properties
	 80      *              
	 81      * @param filePath 
	 82      * @return void  
	 83      * @throws
	 84      */
	      public static void getProperties_1(String filePath){
	          Properties prop = null;
	          try {
	              // 通过Spring中的PropertiesLoaderUtils工具类进行获取
	              prop = PropertiesLoaderUtils.loadAllProperties(filePath);
	              printAllProperty(prop);
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	      }
	      
	      /**
	 97      * 根据key读取value
	 98      * 
	 99      * @Title: getProperties_2   
	100      * @Description: 第二种方式：使用缓冲输入流读取配置文件，然后将其加载，再按需操作
	101      *                    绝对路径或相对路径， 如果是相对路径，则从当前项目下的目录开始计算， 
	102      *                  如：当前项目路径/config/config.properties, 
	103      *                  相对路径就是config/config.properties   
	104      *           
	105      * @param filePath
	106      * @param keyWord
	107      * @return
	108      * @return String  
	109      * @throws
	110      */
	     public static String getProperties_2(String filePath, String keyWord){
	         Properties prop = new Properties();
	         String value = null;
	         try {
	             // 通过输入缓冲流进行读取配置文件
	        	 String path = PropertiesUtil.class.getResource("/").getPath();
	        	// System.out.println(path);
	             InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(path+filePath)));
	             // 加载输入流
	             prop.load(InputStream);
	             // 根据关键字获取value值
	             value = prop.getProperty(keyWord);
	            // System.out.println("value:"+value);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	         return value;
	     }
	     
	     /**
	128      * 读取配置文件所有信息
	129      * 
	130      * @Title: getProperties_2   
	131      * @Description: 第二种方式：使用缓冲输入流读取配置文件，然后将其加载，再按需操作
	132      *                    绝对路径或相对路径， 如果是相对路径，则从当前项目下的目录开始计算， 
	133      *                  如：当前项目路径/config/config.properties, 
	134      *                  相对路径就是config/config.properties   
	135      *           
	136      * @param filePath
	137      * @return void
	138      * @throws
	139      */
	     public static void getProperties_2(String filePath){
	         Properties prop = new Properties();
	         try {
	             // 通过输入缓冲流进行读取配置文件
	             InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
	             // 加载输入流
	             prop.load(InputStream);
	             printAllProperty(prop);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	     }
	     
	     /**
	154      * 根据key读取value
	155      * 
	156      * @Title: getProperties_3   
	157      * @Description: 第三种方式：
	158      *                    相对路径， properties文件需在classpath目录下， 
	159      *                  比如：config.properties在包com.test.config下， 
	160      *                  路径就是/com/test/config/config.properties 
	161      * @param filePath
	162      * @param keyWord
	163      * @return
	164      * @return String  
	165      * @throws
	166      */
	     public static String getProperties_3(String filePath, String keyWord){
	         Properties prop = new Properties();
	         String value = null;
	         try {
	             InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
	             prop.load(inputStream);
	             value = prop.getProperty(keyWord);
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         return value;
	     }
	     
	     /**
	181      * 读取配置文件所有信息
	182      * 
	183      * @Title: getProperties_3   
	184      * @Description: 第三种方式：
	185      *                    相对路径， properties文件需在classpath目录下， 
	186      *                  比如：config.properties在包com.test.config下， 
	187      *                  路径就是/com/test/config/config.properties 
	188      * @param filePath
	189      * @return
	190      * @throws
	191      */
	     public static Map<String, String> getProperties_3(String filePath){
	         Properties prop = new Properties();
	         Map<String, String> map = new HashMap<String, String>();
	         try {
	             InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
	             prop.load(inputStream);
	             map = printAllProperty(prop);
	         } catch (IOException e) {
	             e.printStackTrace();
	             map = null;
	         }
	         return map;
	     }
	     
	     
	     public static String getProperties_5(String path,String key) {
	    	 Resource resource =  new ClassPathResource(path);  
		     Properties props = null ;
				try {
					props = PropertiesLoaderUtils.loadProperties(resource);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	    	 return props.getProperty(key);
	     }
	     
	     public static Map<String, String> getProperties_4(String filePath,String keys,String currentTime){
	         Properties prop = new Properties();
	         Map<String, String> map = new HashMap<String, String>();
	         try {
	             InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
	             prop.load(inputStream);
	             map = printAllProperty_1(filePath,prop,keys,currentTime);
	         } catch (IOException e) {
	             e.printStackTrace();
	             map = null;
	         }
	         return map;
	     }
	     
	     //删除文件夹所有文件
	     public static boolean delAllFile(String path) {
	         boolean flag = false;
	         File file = new File(path);
	         if (!file.exists()) {
	           return flag;
	         }
	         if (!file.isDirectory()) {
	           return flag;
	         }
	         String[] tempList = file.list();
	         File temp = null;
	         if(tempList.length==0) {
	        	 flag = true;
	         }else {
	        	 for (int i = 0; i < tempList.length; i++) {
	 	            if (path.endsWith(File.separator)) {
	 	               temp = new File(path + tempList[i]);
	 	            } else {
	 	                temp = new File(path + File.separator + tempList[i]);
	 	            }
	 	            if (temp.isFile()) {
	 	               temp.delete();
	 	               flag = true;
	 	            }
	 	            
	 	         }
	         }
	         return flag;
	       }
	     public static void main(String[] args) {
	         // 注意路径问题
	         // 注意路径问题
	    	 String timess ="2018-05-09 00:00:00";//當前時間的value
	    	 String keys = "currentTime"; //當前時間的key 
	         Map maps =getProperties_4("/fileUrl.properties",keys,timess);
	        // System.out.println(maps.get("key"));
	         //System.out.println(maps.get("value"));
	     }
}
