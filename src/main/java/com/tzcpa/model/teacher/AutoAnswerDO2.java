package com.tzcpa.model.teacher;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AutoAnswerDO2
 * @Description
 * @Author wangzhangju
 * @Date 2019/6/18 19:54
 * @Version 6.0
 **/
@Data
public class AutoAnswerDO2 extends BaseAutoAnswer{

    List<BaseAutoAnswer> children;
}
