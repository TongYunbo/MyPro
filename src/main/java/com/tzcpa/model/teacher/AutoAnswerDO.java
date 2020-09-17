package com.tzcpa.model.teacher;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AutoAnswerDO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/27 14:04
 * @Version 6.0
 **/
@Data
public class AutoAnswerDO extends BaseAutoAnswer{

    private List<AutoAnswerDO2> children;
}
