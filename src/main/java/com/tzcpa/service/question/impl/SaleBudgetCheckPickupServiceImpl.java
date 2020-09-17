package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.BalanceMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.mapper.treatment.PickUpCarSettlementMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 皮卡车销售预算完成考核</p>
 *
 * @author wangbj 皮卡车销售预算完成考核
 * @date 2019年5月22日
 */
@SuppressWarnings("rawtypes")
@Service("saleBudgetCheckPickupService")
public class SaleBudgetCheckPickupServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private PickUpCarSettlementMapper pickUpCarSettlementMapper;

	@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{

	    try {
            HseRequest hseRequest = hrList.get(0);
            Double score = pickUpCarSettlementMapper.findKHScore(hseRequest);
            // 准备分数实体
            Map<String, Object> checkResItem = new HashMap<>();
            checkResItem.put("score", score);
            List<Map<String, Object>> checkRes = new ArrayList<>();
            checkRes.add(checkResItem);
            // 分数处理
            ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hrList.get(0), checkRes);

            return ResponseResult.success();
        }catch (Exception e){
	        throw e;
        }
    }

}
