package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.StrategicParametersDTO;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * 班级战略参数
 *
 * @Author hanxf
 * @Date 17:06 2019/5/16
**/
public interface ClassStrategicParametersMapper {

    /**
     * 初始化班级战略参数
     *
     * @Author hanxf
     * @Date 17:12 2019/5/16
     * @param classId 班级id
     * @return void
    **/
    int initClassStrategicParameters(int classId);


    /**
     *根据id查询班级战略参数
     *
     * @Author hanxf
     * @Date 19:12 2019/5/16
     * @param classId 班级id
     * @param year 年份
     * @param strategicSelect 选择的战略
     * @return java.util.List<com.tzcpa.model.treatment.ClassStrategicParametersDO>
    **/
    @MapKey("groupKey")
    Map<String,Map<String,Long>> selectClassStrategicParametersByClassYearStrategic(int classId, int year, String strategicSelect);

}