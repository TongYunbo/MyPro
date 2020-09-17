package com.tzcpa.model.teacher;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName RoleScore
 * @Description
 * @Author hanxf
 * @Date 2019/5/29 17:10
 * @Version 1.0
 **/
@Data
@ToString
public class RoleQuestionScore implements Serializable {

    private static final long serialVersionUID = 5255632718429178127L;
    /**
     * 题目名称
     */
    private String questionName;

    /**
     * 题目名称
     */
    private String roleName;

    /**
     * 角色id
     */
    private String teamId;

    /**
     * 得分
     */
    private Float score;

    /**
     * 团队名称
     */
    private String teamName;
}
