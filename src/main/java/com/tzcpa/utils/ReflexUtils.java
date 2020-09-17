package com.tzcpa.utils;

import com.tzcpa.constant.EventMethodEnum;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.alibaba.druid.sql.ast.SQLPartitionValue.Operator.List;

/**
 * @ClassName ReflexUtils
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/13 14:45
 * @Version 6.0
 **/
public class ReflexUtils {

    /**
     * 构造函数
     * java.lang.Error
     */
    private ReflexUtils() {
        throw new Error("不能实例化该类！");
    }


    //通过反射获取实例对象
    public static <S> S reflexObject(Class clas, S obj) throws Exception {
        //转义指定类型class对象
        Class<S> classObject = clas;
        //反射获取实例对象
        S s = classObject.getDeclaredConstructor().newInstance();
        return s;
    }

    /**
     * 通过反射获取实例对象的指定方法对象
     * 调用方通过invoke方法使用，如果方法有返回值，就返回值，没有返回值，比如void就返回null
     * Object invoke = ctd.invoke(实例对象obj,方法参数等);
     */
    public static <S> Method reflexMethod(Class clas, String methodName, Object object) throws Exception{
        //转义指定类型class对象
        Method resultMethod = null;
        Method[] methods = clas.getMethods();
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                method.setAccessible(true);
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }

    //通过反射修改实例对象的私有属性值
    public static <S> void  reflexProperty(Class clas, String field, Object fieldValue, S obj) throws Exception{
        Class<S> classObject = clas;
        //获取字段对象
        Field fieldObj = classObject.getDeclaredField(field);
        //设置私有属性可以修改
        fieldObj.setAccessible(true);
        //修改私有属性值
        fieldObj.set(obj,fieldValue);
    }

    public static ResponseResult callingMethod(String eventCode, java.util.List<HseRequest> answers) throws Exception {
        String classURL = EventMethodEnum.getBeanPositionByKey( eventCode);
        try {
            if (null == classURL) {
                return ResponseResult.success();
            }
            Class<?> cls = Class.forName(classURL); // 取得Class对象,传入一个包名+类名的字符串得到Class对象
            Object bean = SpringUtils.getBean(cls);
            Method method = ReflexUtils.reflexMethod(bean.getClass(), EventMethodEnum.getMethodNameByKey(eventCode), answers);
            Object getObj = method.invoke(bean, answers);
            ResponseResult responseResult = (ResponseResult) getObj;
            return responseResult;
        }catch (InvocationTargetException e){
            throw e;
        }
    }

}
