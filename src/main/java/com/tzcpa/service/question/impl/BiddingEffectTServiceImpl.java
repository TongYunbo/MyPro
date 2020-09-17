package com.tzcpa.service.question.impl;

import com.tzcpa.constant.VehicleTypeEnum;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.question.TeamBiddingMapper;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.question.TeamBidding;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.aop.framework.AopContext;
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
@Service
@SuppressWarnings("rawtypes")
public class BiddingEffectTServiceImpl extends AHseService {

    @Resource
    private TeamBiddingMapper teamBiddingMapper;
    @Resource
    private OSMapper osMapper;
    @Resource
    private StuAnswerMapper stuAnswerMapper;

    /**
     * @author: lrs
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
        try {
            HseRequest hseRequest = hrList.get(0);
            AnswerScoreDO scoreDO = null;
            scoreDO = new AnswerScoreDO(hseRequest);
            TeamBidding teamBidding = new TeamBidding();
            teamBidding.setTeamId(hseRequest.getTeamId());
            teamBidding.setClassId(hseRequest.getClassId());
            TeamBidding team = teamBiddingMapper.findTeamBiddingTop(teamBidding);
            log.info("获取到的中标团队为：" + team);
            //有中标的团队并且是本团队才执行影响
            if (team != null && team.getTeamId().equals(hseRequest.getTeamId())) {
                log.info("进入中标者团队影响处理：");
                String ym_date = hseRequest.getTimeLine().substring(0, 7);
                //根据中标信息查询上个月的调整后单价
                Double unitPrice = teamBiddingMapper.findDJ(team, Integer.valueOf(ym_date.substring(5)) - 1, Integer.valueOf(ym_date.substring(0, 4)));
                Double B = Double.valueOf(unitPrice * 6600);
                //A：投标总价（表中获取）  B:6600*当月单价（表中取值）
                Double A = Double.valueOf(team.getTotalPrice());
                Double yL = B - A;
                //销量添加团队的延迟执行任务
                //JudgeTaskByVehicleType(teamBidding, scoreDO);
                //handleFinanceImpact(osMapper, scoreDO, scoreDO.newHseInstance(), null);
                scoreDO.setVehicleModel(VehicleTypeEnum.getOptionNum(team.getVehicleType()));
                //答案置为空
                scoreDO.setAnswer(null);
                ((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
                team.setVehicleType(VehicleTypeEnum.getOptionNum(team.getVehicleType()));
                //正常影响本月
                teamBiddingMapper.updateSaleMonth(team, yL, ym_date);
            }
            return ResponseResult.success();
        }catch (Exception e){
            throw e;
        }
    }
    
    public static void main(String[] args) {
    	String a = "2015-02";
		System.out.println(Integer.valueOf(a.substring(5)));
	}

}
