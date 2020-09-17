package com.tzcpa.model.treatment;

import lombok.Data;

/**
 * @ClassName QuestionAnswerdDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/14 9:16
 * @Version 6.0
 **/
@Data
public class QuestionAnswerdDTO {

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 小组id
     */
    private Integer teamId;

    /**
     * 问题id
     */
    private Integer thisItemId;
    /**
     * 标准分
     */
    private Double score;
}
