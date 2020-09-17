package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.ClassQuestionOptionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 班级题目选项初始化
 *
 * @Author hanxf
 * @Date 16:30 2019/5/14
**/
public interface ClassQuestionOptionMapper {

    /**
     * 初始化 班级题目选项
     *
     * @Author hanxf
     * @Date 16:31 2019/5/14
     * @param classId 班级id
     * @return int
    **/
    int initClassQuestionOption(int classId);

    /**
     * 根据题目id 查询选项
     *
     * @Author hanxf
     * @Date 11:08 2019/5/21
     * @param questionIds
     * @param classId
     * @return java.util.List<com.tzcpa.model.treatment.ClassQuestionOptionDO>
    **/
    List<ClassQuestionOptionDO> getQuestionOptions(@Param("questionIds") List<Integer> questionIds, @Param("classId") int classId);

    int insertQuestionMonth( @Param("classId") int classId ,@Param("account") String account );

    List<ClassQuestionOptionDO> getQuestionOptionsById(@Param("questionId") Integer questionId,@Param("classId") int classId,
                                                       @Param("option") String option);

    List<ClassQuestionOptionDO> getThirdQuestionOptionsById(@Param("questionId") Integer questionId,@Param("classId") int classId,
                                                       @Param("option") String option);

    int insert(ClassQuestionOptionDO record);

    int update(@Param("record") ClassQuestionOptionDO record);
}