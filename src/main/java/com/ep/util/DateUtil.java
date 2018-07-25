package com.ep.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用于格式化时间类型或者时间类型字符串的工具
 * @author shimao
 *
 */
public class DateUtil {
	
	/**
	 * 加或减多少天
	 * @param specifiedDay  当前时间
	 * @param strFormat  时间格式
	 * @param days  天数
	 * @param fuhao  符号 + 或 -
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay,String strFormat,int days,String fuhao){ 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		String returnDate="";
		try { 
			date = new SimpleDateFormat(strFormat).parse(specifiedDay); 
			c.setTime(date); 
			int day=c.get(Calendar.DATE); 
			if(fuhao.equals("-")){
				c.set(Calendar.DATE,day-days); 
			}else{
				c.set(Calendar.DATE,day+days); 
			}
			returnDate = new SimpleDateFormat(strFormat).format(c.getTime()); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		
		return returnDate; 
	}
	
	
	/**
	 * 加或减多少小时
	 * @param dates  当前时间
	 * @param hours 小时数  
	 * @param addOrJian +  或  -
	 * @param strFormat  时间格式
	 * @return
	 */
	public static String addorjianDate(Date dates,int hours,String addOrJian,String strFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		 Calendar c = Calendar.getInstance();
		 Date d;
		 String Ctime=sdf.format(dates);
		try {
			d = sdf.parse(Ctime);
			c.setTimeInMillis(d.getTime());
			if(addOrJian.equals("+")){
				c.add(Calendar.HOUR_OF_DAY, +hours);
			}else{
				c.add(Calendar.HOUR_OF_DAY, -hours);
			}
			 Date Cdate = new Date(c.getTimeInMillis());
			  Ctime= sdf.format(Cdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Ctime;
	}
	
	
	/**
	 * 按标准格式来格式化时间类型参数
	 * @param date 时间类型参数
	 * @param strFormat 需要格式化的格式
	 * @return 生成的时间类型字符串
	 */
	public static String paseDate(Date date,String strFormat){
		if(strFormat==null||date==null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		return sdf.format(date);
	}
	
	/**
	 * 比较当前时间 是否在startdate,enddate其中
	 * 只比较日期
	 * @Description: TODO()
	 * @param startdate
	 * @param enddate
	 * @param nowdate
	 * @return
	 */
	public static boolean compareDate(Date startdate,Date enddate){
		return compareDate(startdate,enddate,new Date());
	}
	
	/**
	 * 比较当前时间 是否在startdate,enddate其中
	 * 只比较日期
	 * @Description: TODO()
	 * @param startdate
	 * @param enddate
	 * @param nowdate
	 * @return
	 */
	public static boolean compareDate(Date startdate,Date enddate,Date nowdate){
		Calendar nowCa = Calendar.getInstance(); 
		Calendar startCa = Calendar.getInstance(); 
		Calendar endCa = Calendar.getInstance(); 
		if (startdate != null && enddate != null) {
			nowCa.setTime(nowdate);
			startCa.setTime(startdate);
			endCa.setTime(enddate);
			//保留年月日
			nowCa.set(nowCa.get(Calendar.YEAR), nowCa.get(Calendar.MONTH), nowCa.get(Calendar.DATE), 0, 0, 0);
			startCa.set(startCa.get(Calendar.YEAR), startCa.get(Calendar.MONTH), startCa.get(Calendar.DATE), 0, 0, 0);
			endCa.set(endCa.get(Calendar.YEAR), endCa.get(Calendar.MONTH), endCa.get(Calendar.DATE), 0, 0, 0);
			nowCa.set(Calendar.MILLISECOND, 0);//设置毫秒
			startCa.set(Calendar.MILLISECOND, 0);//设置毫秒
			endCa.set(Calendar.MILLISECOND, 0);//设置毫秒
			/*
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println("当前时间："+sdf.format(nowCa.getTime()));
			System.out.println("开始时间："+sdf.format(startCa.getTime()));
			System.out.println("结束时间："+sdf.format(endCa.getTime()));
			System.out.println("当前时间："+nowCa.getTimeInMillis());
			System.out.println("开始时间："+startCa.getTimeInMillis());
			System.out.println("结束时间："+endCa.getTimeInMillis());
			*/
			if (nowCa.getTimeInMillis() >= startCa.getTimeInMillis() && nowCa.getTimeInMillis() <= endCa.getTimeInMillis()) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		
	}

}
