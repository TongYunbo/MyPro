package com.tzcpa.service.question.impl;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName NoticeResultImpl
 * @Description
 * @Author wangzhangju
 * @Date 2019/6/21 18:19
 * @Version 6.0
 **/
@Slf4j
@Service
public class NoticeResultImpl extends AHseService {
    @Override
    public ResponseResult checkOS(List<HseRequest> hrList) {
        return ResponseResult.success();
    }
}
