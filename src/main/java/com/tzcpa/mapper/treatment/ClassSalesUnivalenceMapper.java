package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.ClassSalesUnivalenceDO;
import com.tzcpa.model.treatment.ConfSalesUnivalenceDO;

import java.util.List;


/**
 * 班级销量单价基础配置表
 *
 * @Author hanxf
 * @Date 11:16 2019/5/17
**/
public interface ClassSalesUnivalenceMapper {

    /**
     * 根据班级id 查询销量 单价 销售额
     *
     * @Author hanxf
     * @Date 11:17 2019/5/17
     * @param classId 班级id
     * @return java.util.List<com.tzcpa.model.treatment.ClassSalesUnivalenceDO>
    **/
    List<ClassSalesUnivalenceDO> selectClassSaleUnivalence(int classId);


    /**
     * 初始化 班级销量单价
     *
     * @Author hanxf
     * @Date 11:56 2019/5/17
     * @param
     * @return int
    **/
    int initClassSaleUnivalence(int classId);
}