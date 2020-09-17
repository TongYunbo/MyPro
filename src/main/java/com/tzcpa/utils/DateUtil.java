package com.tzcpa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 *
 * @author 田庆新
 */
public class DateUtil {
	
	public static final String FORMART_1 = "YYYY";
	
	public static final String FORMART_2 = "yyyy";
	
	public static final String FORMART_3 = "YYYY-MM";
	
	public static final String FORMART_4 = "yyyy-MM";
	
	public static final String FORMART_5 = "yyyy-MM-dd";

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date getNow() {
        Date date = new Date();
        //long t=System.currentTimeMillis();
        //date.setTime(t);
        return date;
    }

    /**
     * 获取当前时间(格式为字符串)
     *
     * @return 当前时间的字符串格式
     */
    public static String getNowStr() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    /**
     * 根据format格式获得字符串日期
     *
     * @param format
     * @param operateTime
     * @return
     */
    public static String getDate(String format, String operateTime) {

        String strDate = new SimpleDateFormat(format).format(DateUtil.toDate("yyyy-MM-dd hh:mm:ss", operateTime));
        return strDate;
    }

    /**
     * 字符串转换成为java.util.Date
     *
     * @param sDate 字符串
     * @param sFmt  format
     * @return Date java.util.Date日期
     */
    public static Date toDate(String sFmt, String sDate) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat(sFmt).parse(sDate);
        } catch (ParseException e) {
            return dt;
        }
        return dt;
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getNowTimeInMillis() {
        return new Date().getTime();
    }

    /**
     * 获取当月第一天日期
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1 号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取上月最后一天
     *
     * @return
     */
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    /**
     * 日期格式转字符串
     * @param sFmt
     * @param sDate
     * @return
     */
    public static String dateToStr(String sFmt, Date sDate) {
		return new SimpleDateFormat(sFmt).format(sDate);
    }
    
    /**
     * 字符串转日期
     * @param sFmt
     * @param date
     * @return
     */
    public static Date strToDate(String sFmt, String date){
    	Date dateD = null;
    	try {
			dateD = new SimpleDateFormat(sFmt).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return dateD;
    }
    
    /**
     * 获取某个日期多长时间之后或之前日期 WTL
     * @param sFmt
     * @param date
     * @param filed
     * @param impactNum
     * @return
     */
    public static String getNextOrPrevDate(String sFmt, String date, int filed, int impactNum) {
    	Calendar ca = Calendar.getInstance();
		ca.setTime(toDate(sFmt, date));
		//判断为了兼容格式化2011-1转2011-01这种，这种的不进if
		if (impactNum != 0) {
			ca.add(filed, impactNum);
		}
		return dateToStr(sFmt, ca.getTime());
    }
    
    /**
     * 获取日期的属性，年，月，日等
     * @param
     * @param date
     * @param type
     * @return
     */
    public static int getDateAttr(Date date, int type){
    	Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		return instance.get(type);
    }
    
    /**
     * 判断两个字符串类型的日期谁小，并返回小的
     * @param date1
     * @param date2
     * @return
     */
    public static String getMinDate(String date1, String date2, String fType){
    	
    	if (date1 == null) {
			return date2;
		}
    	
    	if (date2 == null) {
			return date1;
		}
    	
    	SimpleDateFormat sdf1 = new SimpleDateFormat(fType);
    	SimpleDateFormat sdf2 = new SimpleDateFormat(fType);
    	try {
			if (sdf1.parse(date1).compareTo(sdf2.parse(date2)) == -1) {
				return date1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return date2;
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/19 9:39
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取两个日期间的所有年月 月末的下一个月 跟当前年月一定不同
     * @step:
     */
    public static List<String> getMonthBetween(String minDate,Long nextYear ,Long nextMonth)  throws ParseException {


        Long thisYear = Long.valueOf(minDate.substring(0,4));
        if(!nextYear.equals(thisYear)){
            nextMonth = 1l;
        }
                String maxMonth = String.valueOf(nextMonth);
                if(maxMonth.length() == 1){
                    maxMonth = "0"+maxMonth;
                }
            String maxDate =nextYear+ "-" + maxMonth;
            ArrayList<String> result = new ArrayList <>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            max.add(Calendar.MONTH, -1);

            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()) + "-01");
                curr.add(Calendar.MONTH, 1);
            }

            return result;
           }

    public static List<String> getLastMonthBetween(String maxDate,Long lastYear ,Long lastMonth)  throws ParseException {

        Long thisYear = Long.valueOf(maxDate.substring(0,4));
        Long thisLastYear = lastYear;
        if(!thisLastYear.equals(thisYear)){
            thisLastYear ++;
            lastMonth = 1l;
        }
        String maiMonth = String.valueOf(lastMonth);
        if(maiMonth.length() == 1){
            maiMonth = "0"+maiMonth;
        }
        String minDate =thisLastYear+ "-" + maiMonth;
        ArrayList<String> result = new ArrayList <>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()) + "-01");
            curr.add(Calendar.MONTH, 1);
        }

        //如果是今年的数据 删除第一条已经维护数据的年月
        if(lastYear.equals(thisYear) && result.size() > 1){
            result.remove(0);
        }

        return result;
    }

}
