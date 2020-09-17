package com.tzcpa.utils;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 通用工具类
 *
 * @author 田庆新
 */
public class ConstentUtil {

    /**
     * JSONObject 转化为对象
     *
     * @param json JSONObject 格式
     * @param pojo 相应对象
     * @return
     * @throws Exception
     */
    public static Object json2Object(JSONObject json, Class pojo) throws Exception {
        // 首先得到pojo所定义的字段
        Field[] fields = pojo.getDeclaredFields();
        // 根据传入的Class动态生成pojo对象
        Object obj = pojo.newInstance();
        for (Field field : fields) {
            // 设置字段可访问（必须，否则报错）
            field.setAccessible(true);
            // 得到字段的属性名
            String name = field.getName();
            // 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
            try {
                json.get(name);
            } catch (Exception ex) {
                continue;
            }
            if (json.get(name) != null && !"".equals(json.getString(name))) {
                // 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
                if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    field.set(obj, Long.parseLong(json.getString(name)));
                } else if (field.getType().equals(String.class)) {
                    field.set(obj, json.getString(name));
                } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                    field.set(obj, Double.parseDouble(json.getString(name)));
                } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    field.set(obj, Integer.parseInt(json.getString(name)));
                } else if (field.getType().equals(java.util.Date.class)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    field.set(obj, sdf.parse(json.getString(name)));
                } else {
                    continue;
                }
            }
        }
        return obj;
    }

    /**
     * 判断是否为空
     *
     * @param o
     * @return 为空返回true，不为空返回false
     */
    public static boolean isEmpty(Object o) {
        boolean b = false;
        if (null == o || "" == o || "".equals(o)) {
            b = true;
        }
        return b;
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return 为空返回true，不为空返回false
     */
    public static boolean isEmpty(String str) {
        boolean b = false;
        if (null == str) {
            b = true;
        } else if (str.trim().length() == 0) {
            b = true;
        }
        return b;
    }

    /**
     * 判断是否为空
     *
     * @param list
     * @return 为空返回true，不为空返回false
     */
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(List list) {
        boolean b = false;
        if (null == list) {
            b = true;
        } else if (list.size() == 0) {
            b = true;
        }
        return b;
    }

    /**
     * string转化为List集合
     *
     * @param str 集合格式的字符串
     * @return
     */
    public static List strToList(String str) {
        String st[] = str.split(",");
        return Arrays.asList(st);
    }
}
