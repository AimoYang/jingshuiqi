package com.jingshuiqi.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class DateUtil {
	 private static DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	 private static DateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	 public static String getRandomFileName() {

	   SimpleDateFormat simpleDateFormat;

	   simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	   Date date = new Date();

	   String str = simpleDateFormat.format(date);

	   Random random = new Random();

	   int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数


	    return str+rannum ;// 当前时间 + 系统5随机生成位数

	  }
	 
	 /**获取当天结束*/
	 public static String getDayEnd(){
		 String dayEnd = formatDate.format(new Date()) + " 23:59:59";
		 return dayEnd;
	 }
	 
	 /**获取当天开始*/
	 public static String getDayBegin(){
		 String dayBegin = formatDate.format(new Date()) + " 00:00:00";
		 return dayBegin;
	 }
	 
	 /**获取当周第一天*/
	 public static String getFirstDayOfWeek(){
		Calendar cc = Calendar.getInstance();
        int day_of_week = cc.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
        day_of_week = 7;
        cc.add(Calendar.DATE, -day_of_week + 1);
        String monday = formatDate.format(cc.getTime()) + " 00:00:00" ;
        return monday;
	 }
	 
	 /**获取当周第一天*/
	 public static String getLastDayOfWeek(){
		Calendar cc7 = Calendar.getInstance();
        int day_of_week7 = cc7.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week7 == 0){
        	day_of_week7 = 7;
        }
        cc7.add(Calendar.DATE, -day_of_week7 + 7);
        String sunday = formatDate.format(cc7.getTime()) + " 23:59:59" ;
        return sunday;
	 }
	 
	 /**获取当月第一天*/
     public static String getFirstDayOfMonth(){
    	 Calendar c = Calendar.getInstance();   
         c.add(Calendar.MONTH, 0);
         c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
         String first = formatDate.format(c.getTime()) + " 00:00:00";
         return first;
     }
     
     /**获取当月最后一天*/
     public static String getLastDayOfMonth(){
    	 Calendar ca = Calendar.getInstance();   
         ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
         String last = formatDate.format(ca.getTime()) + " 23:59:59" ;
         return last;
     }
     
     /**获取每月第一天*/
     public static String getFirstDayOfMonth00(){
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 calendar.set(GregorianCalendar.DAY_OF_MONTH, 1); 
         String beginTime=sdf.format(calendar.getTime())+" 00:00:00";
    	 return beginTime;
     }
     /**获取每月最后一天*/
     public static String getLastDayOfMonth59(){
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 calendar.set( Calendar.DATE, 1 );
    	 calendar.roll(Calendar.DATE, - 1 );
         String endTime=sdf.format(calendar.getTime())+" 23:59:59";
    	 return endTime;
     }
     
     // 获取本年的开始时间
 	public static String getBeginDayOfYear() {
 		Calendar cal = Calendar.getInstance();
 		cal.set(Calendar.YEAR, getNowYear());
 		cal.set(Calendar.MONTH, Calendar.JANUARY);
 		cal.set(Calendar.DATE, 1);
 		String beginTime = formatDate.format(cal.getTime()) + " 00:00:00" ;
 		return beginTime;
 	}
  
 	// 获取本年的结束时间
 	public static String getEndDayOfYear() {
 		Calendar cal = Calendar.getInstance();
 		cal.set(Calendar.YEAR, getNowYear());
 		cal.set(Calendar.MONTH, Calendar.DECEMBER);
 		cal.set(Calendar.DATE, 31);
 		String endTime = formatDate.format(cal.getTime())+" 23:59:59";
 		return endTime;
 	}

 	// 获取今年是哪一年
 	public static Integer getNowYear() {
 		Date date = new Date();
 		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
 		gc.setTime(date);
 		return Integer.valueOf(gc.get(1));
 	}

     
     /**获取字符串时间00:00:00*/
     public static String getDate00(String dateStr){
    	 return dateStr+" 00:00:00";
     }
     /**获取字符串时间23:59:59*/
     public static String getDate59(String dateStr){
    	 return dateStr+" 23:59:59";
     }
     /**字符串转日期*/
	public static Date strToDate(String dateStr,int type){
    	Timestamp ts = null;
     	if(dateStr !=null){
     		if(type ==1){
         		dateStr +=" 00:00:00";
     		}else{
     			dateStr +=" 23:59:59";
     		}
     		ts =Timestamp.valueOf(dateStr);
     	}
     	return ts;
     }
     
	//12小时一间隔
    public static String interval12Hour(Date nowDate,int group){
    	Calendar nowCal = Calendar.getInstance();
    	nowCal.setTime(nowDate);
    	Calendar calendar59 = Calendar.getInstance();
    	calendar59.set(Calendar.HOUR_OF_DAY, 10);
    	calendar59.set(Calendar.MINUTE, 59);
    	calendar59.set(Calendar.SECOND, 59);
    	Calendar calendar00 = Calendar.getInstance();
    	calendar00.set(Calendar.HOUR_OF_DAY, 11);
    	calendar00.set(Calendar.MINUTE, 00);
    	calendar00.set(Calendar.SECOND, 00);
    	SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
    	int g = 1;
    	if(group>0){
    		g = group;
    	}
    	if(nowCal.compareTo(calendar59)<0){ // //下单时间小于当天上午10:59:59
		    calendar00.add(Calendar.HOUR_OF_DAY, -(g+1)*12);
		    calendar59.add(Calendar.HOUR_OF_DAY, -g*12);
    	}else{
    		calendar59.add(Calendar.HOUR_OF_DAY, 12);
    		if(nowCal.compareTo(calendar59)>0){
        		calendar00.add(Calendar.HOUR_OF_DAY, 12);
    			calendar59.add(Calendar.HOUR_OF_DAY, 12);
    		    calendar00.add(Calendar.HOUR_OF_DAY, -g*12);
    		    calendar59.add(Calendar.HOUR_OF_DAY, -g*12);
    		}else{	
    			calendar00.add(Calendar.HOUR_OF_DAY, -12);
    			calendar59.add(Calendar.HOUR_OF_DAY, -12);
    		    calendar00.add(Calendar.HOUR_OF_DAY, -(g-1)*12);
    		    calendar59.add(Calendar.HOUR_OF_DAY, -(g-1)*12);
    		}
    	}
    	return sdf.format(calendar00.getTime())+"~"+sdf.format(calendar59.getTime());
    }
    
    //一个月一间隔
    public static String intervalOneMonth(Date nowDate,int group){
    	Calendar nowCal = Calendar.getInstance();
    	nowCal.setTime(nowDate);
    	Calendar calendar00 = Calendar.getInstance();
    	calendar00.set(Calendar.HOUR_OF_DAY, 00);
    	calendar00.set(Calendar.MINUTE, 00);
    	calendar00.set(Calendar.SECOND, 00);
    	Calendar calendar59 = Calendar.getInstance();
    	calendar59.set(Calendar.HOUR_OF_DAY, 23);
    	calendar59.set(Calendar.MINUTE, 59);
    	calendar59.set(Calendar.SECOND, 59);
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	@SuppressWarnings("unused")
		int g = 1;
    	if(group>0){
    		g = group;
    	}
    	
    	return sdf.format(calendar00.getTime())+"~"+sdf.format(calendar59.getTime());
    }
    
	 //时间比较 
	    public static int compareTime(){
	    	int flag =0;
	    	Calendar cal1 = Calendar.getInstance(); //当前时间
	    	Calendar cal2 = Calendar.getInstance(); //当天11点
	    	cal2.set(Calendar.HOUR_OF_DAY, 11);
	    	cal2.set(Calendar.MINUTE, 00);
	    	cal2.set(Calendar.SECOND, 00);
	    	int t1 = cal1.compareTo(cal2); //1：当前时间大于当天11点
	    	cal2.add(Calendar.HOUR_OF_DAY, 12);
	    	int t2 = cal1.compareTo(cal2); //1：当前时间大于当天23点
	    	cal2.add(Calendar.HOUR_OF_DAY, -24);
	    	int t3 = cal1.compareTo(cal2);//1：当前时间大于前一天23点
	    	int t =t1+t2+t3;
	    	if((t==-1) || (t==3)){
	    		flag =1;
	    	}
	    	    return flag;
	    }
	     // N分钟之后
		 public static Date someMinuteAfter(Date date,int minute){
			    Calendar cal = Calendar.getInstance();
		    	cal.setTime(date);
		    	cal.add(Calendar.MINUTE, minute);
		    	return cal.getTime();
		  }
		
		//获取倒计时30分钟后的时间购物车商品删除
		 public static Date oneHourAfter(Date date,int type){
			    Calendar cal = Calendar.getInstance();
		    	cal.setTime(date);
		    	if(type ==1){
		    		//当前时间延后30分钟
				    cal.add(Calendar.MINUTE, 30);
		    	}else if(type ==2){
		    		//当前时间延后20分钟
				    cal.add(Calendar.MINUTE, 20);
		    	}else if(type ==3){ 
				    cal.add(Calendar.HOUR_OF_DAY, 1);
		    	}else{
		    		cal.add(Calendar.DAY_OF_MONTH, 1);
		    	}
		    	return cal.getTime();
		  }
	 
	 //日期转字符格式
	    public String getNowTimeToString(){
	    	return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
	    }
	    
	    public String getuserShipType(int type){
	    	Calendar calendar = Calendar.getInstance();
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String ust = "";
	    	if(type ==1){
	    		calendar.add(Calendar.DAY_OF_MONTH, 1);
	    		Date date = calendar.getTime();
	    		ust = sdf.format(date)+" 21：00";
	    	}else if(type==2 || type==3){
	    		calendar.add(Calendar.DAY_OF_MONTH, 2);
	    		Date date = calendar.getTime();
	    		ust = sdf.format(date)+" 21：00";
	    	}else{
	    		calendar.add(Calendar.DAY_OF_MONTH, 1);
	        	Date date = calendar.getTime();
	    		ust = sdf.format(date)+" 21：00";
	    	}
	    	return ust;
	    }
	    
	 public static String dateToString(Date date){
		return formatDate.format(date);
	 }
	 
	 public static Date stringToDate(String dateString){
		 try {
			 return formatDate.parse(dateString);
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }
		 return null;
	 }
	
	 public static String dateTimeToString(Date date){
		 return formatDateTime.format(date);
	 }
	 
	 public static boolean getIsWorkDay(){
		Calendar rightNow=Calendar.getInstance();  
	    @SuppressWarnings("static-access")
		int day=rightNow.get(rightNow.DAY_OF_WEEK);//获取时间 
	    day = day -1;
	    if (day > 0 && day < 6) {
			return true;
		}else {
			return false;
		}
	 }
	 
	 public static Date stringToDateTime(String dateString){
		 try {
			 return formatDateTime.parse(dateString);
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }
		 return null;
	 }
	 
	 //计算距离当前时间多久
	 public static String calTimeFromNow(Date time){
		 Date now = new Date();
		 long minuter = (now.getTime() - time.getTime()) /1000 /60;
		 if ((minuter > 0 || minuter == 0) && (minuter < 60 || minuter == 60)) {
			return "刚刚";
		 }
		 int hour = (int) Math.floor(minuter / 60);
		 if (hour < 24) {
			return hour + "小时以前";
		}
		 return dateTimeToString(time);
	 }
	 
	 //生成随机字符
	    public static String getRandomString(int length) { //length表示生成字符串的长度  
	        String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	        Random random = new Random();     
	        StringBuffer sb = new StringBuffer();     
	        for (int i = 0; i < length; i++) {     
	            int number = random.nextInt(base.length());     
	            sb.append(base.charAt(number));     
	        }     
	        return sb.toString();     
	     }     
	 
     public static void main(String[] args) {
    	 Date time = stringToDateTime("2016-10-27 10:28:00");
    	String result = calTimeFromNow(time);
    	System.out.println("show:" + result);
	}
    
}
