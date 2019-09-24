package com.jingshuiqi.util;

import java.math.BigDecimal;

public class ArithUtil {
	private static final int DEF_DIV_SCALE = 10;

	private ArithUtil() {
	}

	/**
	 * 精确加法运算
	 * @param d1 被加数
	 * @param d2 加数
	 * @return 两数之和
	 */
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 精确减法运算
	 * @param d1 被减数
	 * @param d2 减数
	 * @return 两数之差
	 */
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 精确乘法运算
	 * @param d1 被乘数
	 * @param d2 乘数
	 * @return 两数之积
	 */
	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 精确除法运算。<br>精确到小数点后10位，以后四舍五入。
	 * @param d1 被除数
	 * @param d2 除数
	 * @return 两数之商
	 */
	public static double div(double d1, double d2) {
		return div(d1, d2, DEF_DIV_SCALE);
	}

	/**
	 * 精确除法运算。<br>精确到小数点后8位，以后四舍五入。
	 * @param d1 被除数
	 * @param d2  除数
	 * @param scale 精确到小数点以后几位
	 * @return 两数之商
	 */
	public static double div(double d1, double d2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 比较两个数大小   1：val1大  0 ：相等  -1： val2大
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static Integer compare(Double val1, Double val2) {  
		Integer result = 0;
		BigDecimal b1 = new BigDecimal(Double.toString(val1));
		BigDecimal b2 = new BigDecimal(Double.toString(val2));
        if (b1.compareTo(b2) > 0) {  
        	result = 1;  
        }  
        if (b1.compareTo(b2) == 0) {  
        	result = 0;   
        }  
        if (b1.compareTo(b2) < 0) {  
        	result = -1;   
        } 
        return result;
	}  
	
	public static String getTwoMoney(Double price){
		Integer i = price.toString().lastIndexOf(".");
		Integer j = price.toString().length();
		if(i != -1 && (j-i) == 2){
			return price.toString() + "0";
		}else {
			return price.toString();
		}
	}

	public static double fun(double f) {
		BigDecimal bg = new BigDecimal(f);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
}
