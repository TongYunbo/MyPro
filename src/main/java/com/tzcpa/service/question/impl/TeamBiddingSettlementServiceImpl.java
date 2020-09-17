package com.tzcpa.service.question.impl;

import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VehicleTypeEnum;
import com.tzcpa.mapper.question.TeamBiddingMapper;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.question.TeamBidding;
import com.tzcpa.model.student.StuAnswerDO;
import com.tzcpa.service.question.TeamBiddingSettlementService;
import com.tzcpa.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投标竞争计算得分
 *
 * @author wangbj 投标竞争计算得分 废弃
 * @date 2019年6月4日
 */
@Service
@Slf4j
public class TeamBiddingSettlementServiceImpl implements TeamBiddingSettlementService {

    @Resource
    private OSMapper osMapper;

    @Resource
    private TeamBiddingMapper teamBiddingMapper;

    @Resource
    private StuAnswerMapper stuAnswerMapper;

    /**
     * 投标竞争计算得分
     *
     * @param classId
     */
    @Override
    public void teamBiddingSettlement(int classId) {

        // 根据 classId 查询投标信息
        List<TeamBidding> teamBiddingList = teamBiddingMapper.selectTeamBidding(classId);

        // 投标竞争-投标价格上限
        Map<String, Object> param = new HashMap<>();
        param.put("vCode", VNCConstant.TBJZJGSX);
        Long totalPriceLimit = Long.parseLong(osMapper.selectTeamRdv(param));

        // 计算得分
        for (TeamBidding teamBidding : teamBiddingList) {

            // 判断是否已经计算过得分 已经计算过不需要再次计算
            if (teamBidding.getFinalScore() != null && !teamBidding.getFinalScore().isEmpty()) {
                continue;
            }

            // 大于7.5亿排出样本，告知废标。
            if (teamBidding.getTotalPrice() > totalPriceLimit) {
                teamBidding.setQualitypriodScore(0);
                teamBidding.setQualityScore(0);
                teamBidding.setAssetsScore(0);
                teamBidding.setPriceScore(0);
                teamBidding.setSalesScore(0);
                teamBidding.setFinalScore("投标价格大于7.5亿, 废标!");
                teamBiddingMapper.updateTeamBidding(teamBidding);
                continue;
            }

            // 获取车型分值
            int vehicleTypeScore = teamBiddingMapper.getBaseScore(teamBidding.getVehicleType());

            // 获取关键部件质保能力分值 判断“并购告知结果”，并购成功此处得分，否则不得分
            int qualityScore = 0;
            StuAnswerDO stuAnswer = stuAnswerMapper.getAnswer(classId, teamBidding.getTeamId(), 133);
            if (!JsonUtil.jsonToList(stuAnswer.getAnswer(), String.class).get(0).equals("C")) {
                qualityScore = 10;
            }

            // 获取整车零部件质保期限分值 根据报价函所填写内容判定，3年的得5分，在3年基础上每延长1年加2分，最高得10分
            int qualitypriodScore = 0;
            if (teamBidding.getWarranty() > 3) {
                qualitypriodScore = (teamBidding.getWarranty() - 3) * 2 + 5;
                qualitypriodScore = qualitypriodScore > 10 ? 10 : qualitypriodScore;
            }

            // 获取汽车制造商实力分值 投标企业2017年底资产排名进行打分，第1-2名的得5分；第3-4名的得2分；其它不得分。
            int assetsScore = teamBiddingMapper.getShtrengtScore(classId, teamBidding.getTeamId());

            // S60-h6 N70-wey PU30-pickup J50-limousine
            String type = VehicleTypeEnum.getOptionNum(teamBidding.getVehicleType());
            // 获取投标车型销售业绩分值 投标企业2017年度的投标车型各投标人销量排名，第1-2名的得5分；第3-4名的得2分；其它不得分。
            int salesScore = teamBiddingMapper.getPerformanceScoreScore(classId, teamBidding.getTeamId(), type);

            // 计算投标价格得分 取所有有效报价计算平均值，与平均值最近的报价得20分，其他依次递减5分，最少为0分
            int priceScore = teamBiddingMapper.selectPriceScore(classId, teamBidding.getTeamId(), totalPriceLimit);

            // 总分
            int finalScore = vehicleTypeScore + qualityScore + qualitypriodScore + assetsScore + salesScore + priceScore;

            teamBidding.setQualitypriodScore(qualitypriodScore);
            teamBidding.setQualityScore(qualityScore);
            teamBidding.setAssetsScore(assetsScore);
            teamBidding.setPriceScore(priceScore);
            teamBidding.setSalesScore(salesScore);
            teamBidding.setFinalScore(finalScore + "");
            teamBiddingMapper.updateTeamBidding(teamBidding);
        }

        // 查询最高分进行数据影响
    }
}
