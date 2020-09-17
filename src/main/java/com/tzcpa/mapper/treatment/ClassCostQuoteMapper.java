package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.ClassCostQuoteDO;

import java.util.List;
import java.util.Map;

/**
 * 班级成本引用
 *
 * @Author hanxf
 * @Date 11:11 2019/5/17
**/
public interface ClassCostQuoteMapper {


    /**
     * 根据班级id 查询班级成本引用的信息
     *
     * @Author hanxf
     * @Date 11:07 2019/5/17
     * @param map 参数
     * @return java.util.List<com.tzcpa.model.treatment.ClassCostQuoteDO>
    **/
    List<ClassCostQuoteDO>  selectClassCostQuoteByParam(Map<String,Integer> map);



    /**
     * 初始化班级成本引用的数据
     *
     * @Author hanxf
     * @Date 11:36 2019/5/17
     * @param classId  班级id
     * @return int
    **/
    int initClassCostQuote(int classId);
}