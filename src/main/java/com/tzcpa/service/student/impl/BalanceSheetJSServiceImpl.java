package com.tzcpa.service.student.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.PickUpCarSettlementMapper;
import com.tzcpa.service.student.IBalanceSheetJSService;


/**
 * lrs 资产负债表值影响计算
 *
 * @date 2019年6月24日
 */
@Service
public class BalanceSheetJSServiceImpl implements IBalanceSheetJSService{
	
    @Resource
	private PickUpCarSettlementMapper pickUpCarSettlementMapper;
    
	public void updateTeamBalanceSheet(Integer year, Integer classId, Integer teamId) {
	   //（1）非流动负债合计 
		pickUpCarSettlementMapper.updateTeamBalanceFLDFZJSheet(year,classId,teamId);
		//（2）流动负债合计
		pickUpCarSettlementMapper.updateTeamBalanceLDFZHJSheet(year,classId,teamId);
		//（3）负债合计=（2）+（1）
		pickUpCarSettlementMapper.updateTeamBalanceFZHJSheet(year,classId,teamId);
		//（4）所有者权益合计
		pickUpCarSettlementMapper.updateTeamBalanceSYZJSheet(year,classId,teamId);
		//（5）负债和所有者权益总计=（3）+（4）
		pickUpCarSettlementMapper.updateTeamBalanceFZSYZHJSheet(year,classId,teamId);
		//（1）固定资产净值、固定资产净额、非流动资产合计、资产总计、货币资金、流动资产合计
		pickUpCarSettlementMapper.updateTeamBalanceSheet(year,classId,teamId);
		
	}

	@Override
	public void updateTeamMonthlyProfitStatement(String ymDate, Integer classId, Integer teamId) {
		// 营业总成本
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementYYZCB(ymDate,classId,teamId);
		//营业利润 79
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementYYLR(ymDate,classId,teamId);
		//利润总额 82
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementYYZE(ymDate,classId,teamId);
		//所得税费用	利润总额15%
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementSDSFY(ymDate, classId, teamId);
		//净利润 84 未分配利润--本年净利润87
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementJLR(ymDate,classId,teamId);
		//本年未分配利润增减变动金额  86
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementBDJE(ymDate,classId,teamId);
		//本年年末未分配利润 91
		pickUpCarSettlementMapper.updateTeamMonthlyProfitStatementWFPLR(ymDate,classId,teamId);
		
		
	}
}
