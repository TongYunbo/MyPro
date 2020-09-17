package com.tzcpa.model.question;

import com.tzcpa.mapper.treatment.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 平衡积分卡 实际值计算用参数
 */
@Data
@ToString
public class BalanceScoreSettlementDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级ID
     */
    private Integer classId;

    /**
     * 团队ID
     */
    private Integer teamId;

    /**
     * 时间线
     */
    private String timeLine;


    private BalanceJsMapper baJsMapper;

    private OSMapper osMapper;
    // 中间表
    private TeamIntermediateMapper teamIntermediateMapper;
    // 资产负债表
    private TeamBalanceSheetMapper teamBalanceSheetMapper;
    // 利润表
    private TeamProfitStatementMapper teamProfitStatementMapper;

    public BalanceScoreSettlementDO(BalanceJsMapper baJsMapper, OSMapper osMapper, TeamIntermediateMapper teamIntermediateMapper, TeamBalanceSheetMapper teamBalanceSheetMapper, TeamProfitStatementMapper teamProfitStatementMapper) {
        this.baJsMapper = baJsMapper;
        this.osMapper = osMapper;
        this.teamIntermediateMapper = teamIntermediateMapper;
        this.teamBalanceSheetMapper = teamBalanceSheetMapper;
        this.teamProfitStatementMapper = teamProfitStatementMapper;
    }
}
