package com.tzcpa.utils;


/**
 * <p>Description: 数字格式化</p>
 * @author WTL
 * @date 2019年5月28日
 */
public class FormatNumberUtils {
	
	/**
	 * 格式化double类型保留零位小数
	 */
	public static final int FORMAT_DOUBLE_DECIMAL_ZERO = 0;
	
	/**
	 * 格式化double类型保留两位小数
	 */
	public static final int FORMAT_DOUBLE_DECIMAL_TWO = 2;
	
	/**
	 * 保留小数
	 * @param d
	 * @param fdd
	 * @return
	 */
	public static String formatDouble(double d, int fdd) {
//        NumberFormat nf = NumberFormat.getInstance();
//        //设置保留多少位小数
//        nf.setMaximumFractionDigits(fdd);
//        //取消科学计数法
//        nf.setGroupingUsed(false);
//        //返回结果
//        return nf.format(d);
		return String.format("%."+ fdd +"f", d);
    }
	
}

