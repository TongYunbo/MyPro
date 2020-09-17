package com.tzcpa.mapper.treatment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tzcpa.model.question.TBalanceSheetImpactDO;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.DelayedUpdateDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.SearchFinanceDTO;
import com.tzcpa.model.student.StuScoreDTO;
import com.tzcpa.model.student.UacDO;

/**
 * <p>Description: </p>
 *
 * @author WTL
 * @date 2019年5月8日
 */
public interface OSMapper {

    /**
     * 查询此题的标准答案及需要给角色加的分数,查询出来的结果去查询答案影响进行操作（有多级答案的题只会查询出选择正确的结果）
     *
     * @param asd
     * @return
     */
    List<Map<String, Object>> checkRes(@Param("asd") AnswerScoreDO asd);

    /**
     * 批量添加学生成绩
     *
     * @param sl
     */
    void addStuScore(@Param("scoreList") ArrayList<StuScoreDTO> sl);

    /**
     * 查询出此选项要执行的影响任务
     *
     * @param asd
     * @return
     */
    List<Map<String, Object>> getImpactCodes(@Param("asd") AnswerScoreDO asd);

    /**
     * 查询出选择的战略
     *
     * @param hse
     * @return
     */
    String getStrategic(@Param("hse") HseRequest hse, @Param("vehicleModel") String vehicleModel);

    /**
     * 添加战略选择
     *
     * @param classId
     * @param teamId
     * @param strategySelect
     * @param year
     * @param vehicleModel
     */
    void addStrategic(@Param("classId") Integer classId, @Param("teamId") Integer teamId, @Param("strategySelect") String strategySelect, @Param("year") Integer year, @Param("vehicleModel") String vehicleModel);

    /**
     * 获取所有车型的选择的战略
     *
     * @param classId
     * @param teamId
     * @return
     */
    List<String> getAllVMSC(@Param("classId") Integer classId, @Param("teamId") Integer teamId);

    /**
     * 查询出指标所对应的影响
     *
     * @param questionId
     * @param isk
     * @return
     */
    List<UacDO> findCodesImpactData(@Param("taskId") String taskId, @Param("classId") Integer classId);

    /**
     * 修改调整字段数据
     *
     * @param uac
     */
    void updateAdjustedColumn(@Param("uac") UacDO uac);
    
    /**
     * 查询将要修改的值的结果
     * @param uac
     * @return
     */
    List<String> updateAdjustedColumnVAL(@Param("uac") UacDO uac);
    
    /**
     * 批量修改调整字段数据
     * @param uacList
     */
    void batchUpdateAdjustedColumn(@Param("list")List<UacDO> uacList);

    /**
     * 获取变量信息
     *
     * @param variableName
     * @param optionVal
     * @param strategicChoice
     * @return
     */
    BalanceVariableDO getVariableInfo(@Param("variableName") String variableName, @Param("optionVal") String optionVal,
                                      @Param("strategicChoice") String strategicChoice, @Param("classId") Integer classId);

    /**
     * 批量添加平衡积分卡纪录
     *
     * @param bvList
     */
    void batchIBalancedVariable(@Param("bvList") List<BalanceVariableDO> bvList);

    /**
     * 单个添加平衡积分卡纪录
     *
     * @param bvList
     */
    void addBalancedVariable(@Param("bv") BalanceVariableDO bv);

    /**
     * 获取延迟更新的任务IDS
     *
     * @param teamId
     * @param classId
     * @return
     */
    List<DelayedUpdateDO> findDelayedUpdateTasks(@Param("du") DelayedUpdateDO du);

    /**
     * 修改延迟任务所需要执行的次数
     *
     * @param executionTimes
     */
    void updateETAndBTime(@Param("executionTimes") Integer executionTimes, @Param("bTime") String bTime, @Param("id") Integer id);

    /**
     * 删除团队所需要执行的延迟任务
     *
     * @param teamId
     * @param classId
     */
    void deleteDelayedUpdate(@Param("ids") String ids);

    /**
     * 添加团队的延迟执行任务
     *
     * @param du
     */
    void addDelayedUpdate(@Param("du") DelayedUpdateDO du);

    /**
     * 批量添加延迟任务
     *
     * @param du
     */
    void addBatchDelayedUpdate(@Param("duList") List<DelayedUpdateDO> du);

    /**
     * 获取相对应的财务值
     *
     * @param sfd
     * @return
     */
    Long getFinanceVal(@Param("sfd") SearchFinanceDTO sfd);

    /**
     * 获取变量的纪录值
     *
     * @param classId
     * @param teamId
     * @param variableName
     * @return
     */
    Map<String, Object> getVariableRecord(@Param("classId") Integer classId, @Param("teamId") Integer teamId,
                                          @Param("variableName") String variableName);

    /**
     * 查询某一年所有的车型的某个比例
     *
     * @param year
     * @param classId
     * @param targetColumn 所要查询的比例字段
     * @return
     */
    List<Map<String, Object>> findVehicleModelRatio(@Param("year") Integer year, @Param("classId") Integer classId,
                                                    @Param("targetColumn") String targetColumn);

    /**
     * 获取变量的区间取值
     *
     * @param year
     * @return
     */
    Map<String, BigDecimal> getVariableSection(@Param("year") Integer year);

    /**
     * 添加填报研发费用
     *
     * @param classId
     * @param teamId
     * @param year
     * @param vCode
     * @param vValue
     */
    void addRDV(@Param("classId") Integer classId, @Param("teamId") Integer teamId, @Param("year") Integer year,
                @Param("vCode") String vCode, @Param("vValue") BigDecimal vValue);

    /**
     * 查询配置变量
     *
     * @param map
     * @return
     */
    String selectTeamRdv(Map<String, Object> map);

    /**
     * 更新调整后销量为战略基准销量
     *
     * @param map
     * @return
     */
    int updateAdjustedSales(@Param("uac") UacDO uac);

    /**
     * 更新调整后单价为战略基准单价
     *
     * @param map
     * @return
     */
    int updateAdjustedUnitPrice(@Param("uac") UacDO uac);
    /**
     * 查询2018年1-6月营业利润
     *
     * @param map
     * @return
     */
	Double findYyLr(@Param("hse")HseRequest hse);
	/**
     * 查询2018年1-6月销量
     *
     * @param map
     * @return
     */
	Integer findSale(@Param("hse")HseRequest hse);
	/**
     * 查询单车毛利在答案表里面
     *
     * @param hse
	 * @param interpdMappingQuestionId 
     * @return
     */
	String findDanMaoLi(@Param("hse")HseRequest hse,@Param("qid")Integer interpdMappingQuestionId);
	/**
     * 查询营业利润来自option表
     *
     * @param hse
     * @return
     */
	Double findLRFromOption(@Param("hseT")HseRequest hseT);
	/**
     * 查询答案分数表
     * @param hse questionIds
     * @return
     */
	List<Map<String, Object>> findAnswerScoreByQuestionIds(@Param("hse")HseRequest hseRequest,@Param("questionIds")String questionIds);
	/**
     * 查询我公司数据从答案表
     *
     * @param hseRequest
	 * @param weCompanyQuestionIds 
     * @return
     */
	List<Map<String, Object>> findWeCompanyDate(@Param("hse")HseRequest hseRequest,@Param("qid")String weCompanyQuestionIds);

    /**
     * 查询系统变量值
     * @param vCode
     * @return
     */
    String selectConfVariable(String vCode);
    
    /**
     * 查询此年份都含有哪些车型
     * @param classId
     * @param year
     * @return
     */
    Set<String> findVMByYear(@Param("classId")Integer classId, @Param("year")Integer year);
    /**
     * 查询2018年1-6月营业毛利
     *
     * @param map
     * @return
     */
	Double findYyLrT(@Param("hse")HseRequest hse);

	Long getBufferTime();

	/**
	 * 查询选择外购的条数
	 * @param teamId
	 * @param classId
	 * @return
	 */
	Integer getWGCount(@Param("teamId")Integer teamId, @Param("classId")Integer classId);
	
	/**
	 * 批量添加资产负债表的影响
	 * @param zcList
	 */
	void addBatchTBSI(@Param("zcList")List<TBalanceSheetImpactDO> zcList);
	
	/**
	 * 查询题的答案
	 * @param hse
	 * @param questionId
	 * @return
	 */
	String getAnswerByQID(@Param("hse")HseRequest hse, @Param("questionId")Integer questionId);
	/**
	 * 查询题的答案
	 * @param hse
	 * @param questionId
	 * @return
	 */
	Integer getcountDateAnswer(@Param("teamId")Integer teamId,@Param("rootId")Integer rootId);
	
	/**
	 * 查询他公司销量
	 * @return
	 */
	Double findXL(@Param("hseT")HseRequest hseT);
	
	/**
	 * 查询营销的满分
	 * @param classId
	 * @param questionIds
	 * @return
	 */
	Double getYXMF(@Param("classId")Integer classId,@Param("questionIds")String questionIds);
	
	/**
	 * 查询配置的变量信息
	 * @param vCode
	 * @param year
	 * @return
	 */
	String getConfVariable(@Param("vCode")String vCode,@Param("year")Integer year);
	
	/**
	 * 查询他公司数据（盈利能力判断）
	 * @param classId
	 * @return
	 */
	@Select("SELECT question_id questionId, quesion_option_desc QOD FROM `t_class_question_option` WHERE class_id = #{classId} AND question_id BETWEEN 450 AND 458;")
	List<Map<String, Object>> getTGSData(@Param("classId")Integer classId);
}

