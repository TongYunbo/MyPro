package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName ChildrenQuestionDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/10 15:38
 * @Version 6.0
 **/
@Data
@ToString
public class SecondQuestionDTO extends BaseQuestionDTO {
    /**
     * 当前题目的小题
     */
    private List<ThirdQuestionDTO> thisChildrenItem;
}
