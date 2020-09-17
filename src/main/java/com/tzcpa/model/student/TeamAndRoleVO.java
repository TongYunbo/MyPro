package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 *
 * @author wangbj
 * <p>
 * 2019年4月28日
 */
@Data
@ToString
public class TeamAndRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    private Integer classId;
    /**
     * 团队id
     */
    private Integer teamId;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 团队描述(价值观）
     */
    private String teamDesc;
    /**
     * 我们的愿景
     */
    private String teamProspect;
    /**
     * CEO
     */
    private String ceoName;
    /**
     * CFO
     */
    private String cfoName;
    /**
     * CRO
     */
    private String croName;
    /**
     * COO
     */
    private String cooName;
    /**
     * CMO
     */
    private String cmoName;
    /**
     * 修改人
     */
    private String modifyPerson;

}
