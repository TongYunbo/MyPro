package com.tzcpa.model.teacher;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用户管理---班级管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@Data
public class Clazz implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    private Integer id;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 班级描述
     */
    private String classDesc;

    /**
     * 是否删除：0：否 1：是
     */
    private Integer isDel;

    /**
     * 班级对应的教师列表
     */
    private List<Teacher> teacherLists;

    /**
     * 班级所对应的的教师名称
     */
    private String teachers;

    /**
     *  答题状态  0-未开始  2-开始 3-暂停 4-已结束
     */
    private int answerState;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private String modifyPerson;

    /**
     * 剩余时间
     */
    private Long timeRemain;

    /**
     * 缓冲剩余时间
     */
    private Long bufferRemain;
    
    /**
     * 教师id
     */
    private Integer tId;
    /**
     * 教师名称
     */
    private String name;
}
