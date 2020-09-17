package com.tzcpa.model.teacher;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户管理--学员管理
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
@Data
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 团队账号
     */
    private String account;

    /**
     * 团队密码
     */
    private String password;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 团队描述
     */
    private String teamDesc;

    /**
     * 团队愿景
     */
    private String teamProspect;

    /**
     * 账号可用结束日期
     */
    private Date endDate;

    /**
     * 团队id
     */
    private Integer classId;

    /**
     * 是否删除：0：否 1：是 (不展示在团队列表上)2:账号不可用(不可以查看该团队的信息)
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
    /**
     * 用户名
     */
    private String userName;
}
