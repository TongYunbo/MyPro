package com.tzcpa.mapper.question;

import com.tzcpa.model.question.TeamBidding;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TeamBiddingMapper {

    /**
     * 新增投标得分
     */
    int insertTeamBidding(TeamBidding record);

    /**
     * 更新投标得分
     */
    int updateTeamBidding(TeamBidding teamBidding);

    /**
     * 查询投标信息
     *
     * @param classId
     * @return
     */
    List<TeamBidding> selectTeamBidding(int classId);

    /**
     * 查询最高分竞标信息
     *
     * @param teamBidding
     * @return
     */
    TeamBidding findTeamBiddingTop(TeamBidding teamBidding);

    /**
     * 查询车型的单价
     *
     * @param ym_date
     * @param teamBidding
     * @return
     */
    Double findDJ(@Param("bidd") TeamBidding bidd, @Param("month") Integer month, @Param("year") Integer year);

    /**
     * 修改最终销售费用-其他
     *
     * @param ym_date
     * @param teamBidding
     * @return
     */
    void updateSaleMonth(@Param("bidd") TeamBidding bidd, @Param("yL") Double yL, @Param("ym_date") String ym_date);

    Integer getBaseScore(@Param("type") String type);

    Integer getShtrengtScore(@Param("classId") Integer classId, @Param("teamId") Integer teamId);

    Integer getPerformanceScoreScore(@Param("classId") Integer classId, @Param("teamId") Integer teamId, @Param("type") String type);

    int getUnQuestionCount(@Param("classId") Integer classId);

    int countTeamScore(@Param("classId") Integer classId, @Param("upperLimitF") Double upperLimitF, @Param("lowerLimitF") Double lowerLimitF);

    TeamBidding getWinningTeam(@Param("classId") Integer classId);

    List<Map<String, Object>> getBidResult(@Param("classId") Integer classId);

    int selectPriceScore(@Param("classId") int classId, @Param("teamId") int teamId, @Param("limit") long limit);
    
    /**
     * 获取所选择车型得分
     * @param vm
     * @return
     */
    Integer getVMScore(@Param("vm") String vm);
}