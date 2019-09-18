package com.jingshuiqi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomNum {
	
	//生成订单编号
	public static String getRandomFileName() {

		  SimpleDateFormat simpleDateFormat;

		  simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		  Date date = new Date();

		  String str = simpleDateFormat.format(date);

		  Random random = new Random();

		  int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;

		  return  str + rannum ;
		  }
}