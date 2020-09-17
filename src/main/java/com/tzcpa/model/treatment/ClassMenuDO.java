package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ClassMenuDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 菜单编码
     */
    private Integer menuId;

    /**
     * 菜单条目
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String menuUrl;

    /**
     * 是否显示
     */
    private Integer isShow;

    private static final long serialVersionUID = 1L;


}