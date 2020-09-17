package com.tzcpa.service.treatment;

/**
 * @ClassName QuestionService
 * @Description 班级题库初始化接口
 * @Author hanxf
 * @Date 2019/5/6 11:20
 * @Version 1.0
 **/
public interface InitQuestionDescService {

    /**
     * @return void
     * @Author hanxf
     * @Description 班级题库初始化实现
     * @Date 17:48 2019/5/6
     * @Param [classId] 班级id
     **/
    void initClassQuestionDesc(int classId);
}
