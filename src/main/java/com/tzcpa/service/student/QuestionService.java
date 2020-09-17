package com.tzcpa.service.student;

import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.treatment.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.List;

/**
 * @ClassName QuestionService
 * @Description 班级题库初始化接口
 * @Author hanxf
 * @Date 2019/5/6 11:20
 * @Version 1.0
 **/
public interface QuestionService {


    /**
     * @author:     wangzhangju
     * @date:       2019/5/9 14:58
     * @param:      null
     * @return:
     * @exception:
     * @description: 答题并且获取下一题内容
     * @step:
     */
    Object answerAndGetQuestion(QuestionAnswerDTO questionAnswerDTO) throws Exception;

    /**
     * @author:     wangzhangju
     * @date:       2019/5/13 17:28
     * @param:      null
     * @return:
     * @exception:
     * @description: 查询小组沙盘数据
     * @step:
     */
    List<SandTableVO>  getSandTable(Integer classId, Integer teamId);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/14 9:20
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取已经答题的问题
     * @step:
     */
    QuestionAnswerDTO getAnswerdQuestion(QuestionAnswerdDTO questionAnswerdDTO) throws Exception;

    /**
     * @author:     wangzhangju
     * @date:       2019/5/20 9:51
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取倒计时 时间
     * @step:
     */
    CountDownDO getCountDownTime(Integer classId,Integer teamId);

    void addTime(int classId,long totalTime , long addTime,String account);
    /**
     *根据questionIds查询答案选项
     *
     * @Author hanxf
     * @Date 11:04 2019/5/21
     * @param  questionIds
     * @return java.util.List<com.tzcpa.model.treatment.ClassQuestionOptionDO>
     **/
    List<ClassQuestionOptionDO> getQuestionOptions(List<Integer> questionIds);

    int insertQuestionMonth(Integer classId,String account) throws Exception;

    String getAccountByClassId(Integer classId);

    int updateClassStatus (int classId ,int status ,String userName,Long remainTime ,Long bufferTime) throws Exception;

    Clazz getClazz(int classId);

    void autoAnswer(UserInfo userInfo ) throws Exception;

    /**
     * 获取月度利润数据
     * @param classId
     * @param teamId
     * @param currentDate
     */
      void setMonthlyProfit(Integer classId,Integer teamId,String currentDate,Long lastYear,Long lastMonth,List<String> dateStrs) throws Exception;

    /**
     *  设置年度利润数据
     * @param classId
     * @param teamId
     * @param currentDate
     * @throws Exception 
     */
      void setYearlyProfit(Integer classId,Integer teamId,Date currentDate) throws Exception;

    /**
     *
     * @param questionAnswerDTO
     * @return 保存 H6主题答案
     */
    int insertH6Answer(QuestionAnswerDTO questionAnswerDTO,UserInfo userInfo);

    /**
     * 获取班级下的未结束问题记录
     * @param ClassId
     * @return
     */
     int getUnAnswerd(int ClassId);

    /**
     *  跳转到下一个月
     */
     void nextMonth(UserInfo userInfo, ChannelHandlerContext ctx ) throws Exception;

     /**
      * @author:     wangzhangju
      * @date:       2019/7/15 15:25
      * @param:      null
      * @return:
      * @exception:
      * @description: 缓冲倒计时结束 通知跳到下一个月
      * @step:
      */
     void sendMsg(UserInfo userInfo) throws Exception;

     void errorPause(UserInfo userInfo)  ;

    void setNextYearDataAop(String thisTimeLine , Long nextYear , Long nextMonth, HseRequest hseRequest) throws Exception;
}
