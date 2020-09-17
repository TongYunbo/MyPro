package com.tzcpa.model.teacher;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户管理---教师管理
 *
 * @author WangYao
 * <p>
 * 2019年4月28日
 */
@Data
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教师id
     */
    private Integer id;

    /**
     * 教师名称
     */
    private String name;

    /**
     * 教师账号
     */
    private String account;

    /**
     * 教师密码
     */
    private String password;

    /**
     * 是否删除：0：否 1：是
     */
    private Integer isDel;

    /**
     * 录入时间
     */
    private Date inputTime;

    /**
     * 录入人
     */
    private String inputPerson;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private String modifyPerson;
}
