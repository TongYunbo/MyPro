package com.tzcpa.service.question.impl;

import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VehicleTypeEnum;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.question.TeamBiddingMapper;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.question.TeamBidding;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.StuAnswerDO;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

/**
 * <p>Description:投标影响 </p>
 *
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("aa")
@SuppressWarnings("rawtypes")
public class BiddingEffectServiceImpl extends AHseService {

    @Resource
    private TeamBiddingMapper teamBiddingMapper;
    @Resource
    private OSMapper osMapper;
    @Resource
    private StuAnswerMapper stuAnswerMapper;

    /**
     * @author: wangzhangju
     * @date: 2019/6/6 16:00
     * @param: null
     * @return:
     * @exception:
     * @description: 竞争投标活动 -- 计算得分 生成竞争投标结果通知数据、计算中标团队的影响
     * @step:
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{

        //NO.1 计算得分 生成竞争投标结果通知数据

        try {
            HseRequest hseRequest = hrList.get(0);
            TeamBidding teamBidding1 = new TeamBidding();
            teamBidding1.setClassId(hseRequest.getClassId());
            teamBidding1.setTeamId(hseRequest.getTeamId());

            String motorcycleType = hseRequest.getAnswer().get(0);

            //NO.1-1 添加 关键部件质保能力 得分
            Integer qualityScore = this.getQualityScore(hseRequest, 0);
            //NO.1-2 添加 整车零部件质保期限 得分
            Integer qualityPriodScore = this.getQualityPriodScore(hseRequest, 0);
            //NO.1-3 添加 汽车制造商实力 得分
            Integer strengthScore = this.getStrengthScore(hseRequest, 0);
            //NO.1-4 添加 投标车型销售业绩 得分
            motorcycleType = VehicleTypeEnum.getOptionNum(motorcycleType);
            Integer performanceScore = this.getPerformanceScore(hseRequest, 0, motorcycleType);

            //获取所选车型得分
            Integer vmScore = teamBiddingMapper.getVMScore(motorcycleType);
            
            //NO.2 保存得分数据
            TeamBidding teamBidding2 = new TeamBidding();
            teamBidding2.setClassId(hseRequest.getClassId());
            teamBidding2.setTeamId(hseRequest.getTeamId());
            teamBidding2.setVehicleType(hseRequest.getAnswer().get(0));
            teamBidding2.setUnitPrice(Integer.valueOf(hseRequest.getAnswer().get(1)));
            teamBidding2.setWarranty(Integer.valueOf(hseRequest.getAnswer().get(3)));
            teamBidding2.setTotalPrice((long) (Double.valueOf(hseRequest.getAnswer().get(2)) * 100));
            teamBidding2.setQualityScore(qualityScore);
            teamBidding2.setQualitypriodScore(qualityPriodScore);
            teamBidding2.setAssetsScore(strengthScore);
            teamBidding2.setSalesScore(performanceScore);
            teamBidding2.setVmScore(vmScore);
            teamBiddingMapper.insertTeamBidding(teamBidding2);


            //如果为班级的最后一个团队则进行计算每个团队投标的价格及最终得分
            int unQuestionCount = teamBiddingMapper.getUnQuestionCount(hseRequest.getClassId());
            if (unQuestionCount == 0) {
                log.info("本班级最后一个团队，进入结算最终积分和投标价格积分处理");
                // 获取投标竞争-投标价格上限
                Double priceUpLimit = Double.valueOf(osMapper.selectConfVariable(VNCConstant.TBJZJGSX));
                // 获取投标竞争-投标价格下限
                Double priceLLimit = Double.valueOf(osMapper.selectConfVariable(VNCConstant.TBJZJGXX));

                // 修改所有团队的投标得分和最终得分
                teamBiddingMapper.countTeamScore(hseRequest.getClassId(), priceUpLimit, priceLLimit);
            }
            return ResponseResult.success();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * @author: wangzhangju
     * @date: 2019/6/6 17:27
     * @param: null
     * @return:
     * @exception:
     * @description: 添加质保能力得分
     * @step:
     */
    Integer getQualityScore(HseRequest hseRequest, Integer baseScore) {
        StuAnswerDO stuAnswer = stuAnswerMapper.getAnswer(hseRequest.getClassId(), hseRequest.getTeamId(), 133);
        if (!JsonUtil.jsonToList(stuAnswer.getAnswer(), String.class).get(0).equals("C")) {
            baseScore += 10;
        }
        return baseScore;
    }

    /**
     * @author: wangzhangju
     * @date: 2019/6/6 17:39
     * @param: null
     * @return:
     * @exception:
     * @description: 添加汽车制造商实力得分
     * @step:
     */
    Integer getStrengthScore(HseRequest hseRequest, Integer baseScore) {
        Integer strengtScore = teamBiddingMapper.getShtrengtScore(hseRequest.getClassId(), hseRequest.getTeamId());
        return baseScore + strengtScore;
    }

    /**
     * @author: wangzhangju
     * @date: 2019/6/6 17:48
     * @param: null
     * @return:
     * @exception:
     * @description: 添加质保期限得分
     * @step:
     */
    Integer getQualityPriodScore(HseRequest hseRequest, Integer baseScore) {
        Integer qualityPriod = Integer.valueOf(hseRequest.getAnswer().get(3));
        if (qualityPriod < 3) {
            baseScore += 0;
        } else if (qualityPriod == 3) {
            baseScore += 5;
        } else {
            Integer score = 5 + (qualityPriod - 3) * 2;
            if (score > 10) {
                score = 10;
            }
            baseScore += score;
        }
        return baseScore;
    }

    /**
     * @author: wangzhangju
     * @date: 2019/6/6 17:48
     * @param: null
     * @return:
     * @exception:
     * @description: 添加销售业绩得分
     * @step:
     */
    Integer getPerformanceScore(HseRequest hseRequest, Integer baseScore, String motorcycleType) {
        Integer performanceScore = teamBiddingMapper.getPerformanceScoreScore(hseRequest.getClassId(), hseRequest.getTeamId(), motorcycleType);
        performanceScore = performanceScore == null ? 0 : performanceScore;
        return baseScore + performanceScore;
    }

}
