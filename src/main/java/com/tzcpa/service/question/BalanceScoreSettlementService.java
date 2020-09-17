package com.tzcpa.service.question;

import com.tzcpa.model.student.HseRequest;

/**
 * 平衡积分卡 实际值 最终得分 计算及入库
 * @author wangbj 平衡积分卡 实际值 最终得分 计算及入库
 * @date 2019年5月30日
 */
public interface BalanceScoreSettlementService {

    /**
     * 平衡积分卡 实际值 最终得分 计算及入库
     * @param hseRequest
     */
    void BalanceScoreSettlement(HseRequest hseRequest) throws Exception;
}
