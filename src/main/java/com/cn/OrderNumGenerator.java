package com.cn;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生成器
 * (时间戳+业务id)
 * @author Administrator
 *
 */
public class OrderNumGenerator {
	// 业务ID
	private static int count = 0;

	// 生成订单号
	public String getNumber() {

		SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return simpt.format(new Date()) + "-" + ++count;

	}
	public static void main(String[] args) {
		 long totalMilliSeconds = System.currentTimeMillis();
	      long totalSeconds = totalMilliSeconds / 1000;
		System.out.println(totalSeconds);
	}
}
