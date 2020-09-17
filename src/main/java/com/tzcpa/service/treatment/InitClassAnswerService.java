package com.tzcpa.service.treatment;

/**
 * @ClassName InitClassAnswerService
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 17:13
 * @Version 1.0
 **/
public interface InitClassAnswerService {


    /**
     * 初始化班级答案影响
     *
     * @Author hanxf
     * @Date 17:14 2019/5/14
     * @param classId 班级id
     * @return void
    **/
    void initClassAnswerImfact(int classId);

    /**
     * 初始化班级标准答案得分
     *
     * @Author hanxf
     * @Date 15:06 2019/5/14
     * @param classId 班级Id
     * @return void
     **/
    void initClassAnswerScore(int classId);
}
