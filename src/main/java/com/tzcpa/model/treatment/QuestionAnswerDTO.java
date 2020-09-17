package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.util.List;

import com.tzcpa.model.teacher.Mark;

/**
 * @ClassName QuestionAnswerDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/9 13:44
 * @Version 6.0
 **/
@Data
@ToString
public class QuestionAnswerDTO extends BaseQuestionDTO {


    /**
     * 当前题目的小题
     */
    private List<SecondQuestionDTO> thisChildrenItem;
    private List<Mark> scoreList;


}
