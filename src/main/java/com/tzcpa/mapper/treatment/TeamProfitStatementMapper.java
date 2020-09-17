package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.TeamProfitStatementDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Date;
import java.util.List;

/**
 * 初始化团队利润表
 *
 * @author WangYao
 * 2019年5月22日
 */
public interface TeamProfitStatementMapper {

    /**
     * 初始化团队利润表
     *
     * @param classId
     * @param teamId
     * @return
     * @author WangYao
     * @date 2019年5月20日
     */
    int initTeamProfitStatement(@Param(value = "classId") int classId, @Param(value = "teamId") int teamId);

    /**
     * 查询营业成本
     *
     * @param param
     * @return
     */
    Long getOperatingCost(Map<String, Object> param);


    Integer getLastYear(@Param("classId") Integer classId, @Param("teamId") Integer teamId, @Param("year") Integer year);

    /**
     * @author: wangzhangju
     * @date: 2019/5/29 14:17
     * @param: null
     * @return:
     * @exception:
     * @description: 获取年度利润表数据
     * @step:
     */
    List<TeamProfitStatementDO> getAnnualForm(@Param("classId") Integer classId, @Param("teamId") Integer teamId,
                                              @Param("year") Integer year, @Param("lastYear") Integer lastYear);


    List<TeamProfitStatementDO> getDataFromMonthly(@Param("classId") Integer classId,@Param("teamId") Integer teamId, @Param("currentDate") Date currentDate,
                                                   @Param("lastYear") Integer lastYear);


    int insertForBatch(List<TeamProfitStatementDO> list);

    /**
     * 查询利润表 wangbj
     *
     * @param map
     * @return
     */
    Map<String, Object> selectTeamProfitStatement(Map<String, Object> map);
    
    /**
     * 查询存货周转天数
     * @param classId
     * @param teamId
     * @return
     */
    Double getCHZZTS(@Param("classId") Integer classId, @Param("teamId") Integer teamId);
}