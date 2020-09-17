package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.ClassIntermediateDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级中间表初始化
 *
 * @Author hanxf
 * @Date 18:42 2019/5/20
**/
public interface ClassIntermediateMapper {


    /**
     * 初始化中间表基准数据
     *
     * @Author hanxf
     * @Date 10:13 2019/5/20
     * @param classId 班级id
     * @return int
    **/
    int initClassIntermediate(int classId);

}