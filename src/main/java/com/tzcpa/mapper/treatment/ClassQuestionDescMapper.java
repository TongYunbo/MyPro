package com.tzcpa.mapper.treatment;

import com.tzcpa.model.teacher.AutoAnswerDO;
import com.tzcpa.model.treatment.CountDownDO;
import com.tzcpa.model.treatment.QuestionAnswerDTO;
import com.tzcpa.model.treatment.SandTableVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClassQuestionDescMapper {

    /**
     * @return int
     * @Author hanxf
     * @Description 初始化班级问题
     * @Date 17:36 2019/5/6
     * @Param [classId] 班级id
     **/
    int initClassQuestionDesc(@Param(value = "classId") int classId);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/9 16:15
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取当前班级的 当前未答问题
     * @step:
     */
    QuestionAnswerDTO getQuestions(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId,
                                   @Param("currentMonth") Date currentMonth);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/13 17:30
     * @param:      null
     * @return:
     * @exception:
     * @description:
     * @step:
     */
    List<SandTableVO>  getSandTable(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/13 17:45
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取当前题目的答题记录
     * @step:
     */
    QuestionAnswerDTO getResponsed(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId, @Param("questionId") int questionId);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/20 10:14
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取倒计时时间戳
     * @step:
     */
    CountDownDO getCountDownTime(@Param("classId") Integer classId,
                                 @Param("currentMonth") Date currentMonth);

    CountDownDO getCountDownTimeTeam(@Param("classId") Integer classId, @Param("teamId") Integer teamId,
                                 @Param("currentMonth") Date currentMonth);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/21 14:23
     * @param:      null
     * @return:
     * @exception:
     * @description: 添加时间
     * @step:
     */
    int addTime(Map<String, Object> paramMap);

    Date getLocalMonth(@Param("classId") int classId,@Param(value = "nextMonth") int nextMonth);

    List<AutoAnswerDO> getAutoQuestion(@Param("classId") Integer classId, @Param("currentMonth") Date currentMonth);

    boolean checkNextYear(@Param("classId") Integer classId,@Param("currentMonth") Date currentMonth);

    Integer getLastYear(@Param("currentDate") Date currentDate,@Param("classId") Integer classId);

    Integer getLastYearByYear(@Param("localYear") Integer localYear,@Param("classId") Integer classId);

    Map<String,Long> checkThisMonth(@Param("classId") Integer classId,@Param("questionId") Integer questionId );

    int  getQuesUnAnswerd(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId);

    int  getQues16UnAnswerd(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId);

    /**
     * @author:     wangzhangju
     * @date:       2019/6/25 19:07
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取未回答问题数
     * @step:
     */
    int getUnAnswerd(@Param(value = "classId") int classId,@Param(value = "teamId") Integer teamId,
                     @Param(value = "currentMonth") Date currentMonth);
}