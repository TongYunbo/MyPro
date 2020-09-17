package com.tzcpa.service.question.impl;

import com.tzcpa.constant.EventMethodEnum;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.CommonService;
import com.tzcpa.utils.ReflexUtils;
import com.tzcpa.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName CommonServiceImpl
 * @Description
 * @Author wangzhangju
 * @Date 2019/7/17 11:16
 * @Version 6.0
 **/
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private  CommonService commonService;

    @Autowired
    PlatformTransactionManager platformTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult callingMethod(String eventCode, List<HseRequest> answers) throws Exception {



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
        }catch (Exception e){
//            log.error("callingMethod异常",e);
            throw e;
        }
    }
    @Override
    public ResponseResult calling(String eventCode, List<HseRequest> answers) throws Exception{

        return commonService.callingMethod(eventCode,answers);
    }
}
