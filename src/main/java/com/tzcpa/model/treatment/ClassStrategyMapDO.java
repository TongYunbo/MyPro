package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 *
 * @Author hanxf
 * @Date 17:21 2019/5/27
**/
@Data
@ToString
public class ClassStrategyMapDO implements Serializable {
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

    private static final long serialVersionUID = 1L;
}