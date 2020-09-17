package com.tzcpa.mapper.treatment;

/**
 * @Author hanxf
 * @Description 标准答案得分配置
 * @Date 14:42 2019/5/14
**/
public interface ClassAnswerScoreMapper {
    /**
     * 初始化班级标准答案得分
     *
     * @Author hanxf
     * @Date 14:47 2019/5/14
     * @param classId 班级Id
     * @return int
    **/
    int initClassAnswerScore(int classId);
}