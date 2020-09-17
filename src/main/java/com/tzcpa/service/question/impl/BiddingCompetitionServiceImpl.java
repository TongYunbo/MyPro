package com.tzcpa.service.question.impl;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.question.TeamBiddingMapper;
import com.tzcpa.model.question.TeamBidding;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.TeamBiddingSettlementService;
import com.tzcpa.service.student.AHseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>Description: 投标竞争</p>
 *
 * @author wangbj 投标竞争 废弃
 * @date 2019年6月4日
 */
@Slf4j
@Service("biddingCompetitionService")
public class BiddingCompetitionServiceImpl extends AHseService {

    @Resource
    private TeamBiddingMapper teamBiddingMapper;

    @Resource
    private TeamBiddingSettlementService teamBiddingSettlementService;

    @Override
    public ResponseResult checkOS(List<HseRequest> hrList) {

        HseRequest hseRequest = hrList.get(0);
        List<String> answer = hseRequest.getAnswer();

        TeamBidding teamBidding = new TeamBidding();
        teamBidding.setClassId(hseRequest.getClassId());
        teamBidding.setTeamId(hseRequest.getTeamId());

        // ["N70","10","66000","5"]
        teamBidding.setVehicleType(answer.get(0));
        teamBidding.setUnitPrice(Integer.parseInt(answer.get(1)));
        teamBidding.setTotalPrice(Long.parseLong(answer.get(2)));
        teamBidding.setWarranty(Integer.parseInt(answer.get(3)));

        // 记录投标竞争信息
        teamBiddingMapper.insertTeamBidding(teamBidding);

        // 查询未完成投标团队数量
        int unQuestionCount = teamBiddingMapper.getUnQuestionCount(hseRequest.getClassId());
        if (unQuestionCount == 0) {
            // 投标得分计算
            teamBiddingSettlementService.teamBiddingSettlement(hseRequest.getClassId());
        }
        return ResponseResult.success();
    }
}
