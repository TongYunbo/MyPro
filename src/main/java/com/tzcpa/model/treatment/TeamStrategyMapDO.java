package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
/**
 * 团队战略地图
 *
 * @Author hanxf
 * @Date 9:57 2019/5/29
**/
@Data
@ToString
public class TeamStrategyMapDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 地图年份
     */
    private Integer year;

    /**
     * 战略选择
     */
    private String strategicSelect;

    /**
     * 地图地址
     */
    private String mapUrl;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 团队id
     */
    private Integer teamId;


    private static final long serialVersionUID = 1L;

}