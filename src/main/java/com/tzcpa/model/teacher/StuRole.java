package com.tzcpa.model.teacher;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户管理---学生角色
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
@Data
public class StuRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 学员名称
     */
    private String userName;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 班级id
     */
    private Integer classId;
}
