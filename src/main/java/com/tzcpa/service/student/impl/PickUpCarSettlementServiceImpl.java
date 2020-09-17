package com.tzcpa.service.student.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.SaleBudgetCheckPickupConstant;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.mapper.treatment.PickUpCarSettlementMapper;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.QuestionAnswerDTO;
import com.tzcpa.model.treatment.SecondQuestionDTO;
import com.tzcpa.service.student.IPickUpCarSettlementService;
import com.tzcpa.utils.FormatNumberUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * lrs 2018年皮卡产品事业部销售预算完成情况 实际值计算
 *
 * @date 2019年6月20日
 */
@Slf4j
@Service
public class PickUpCarSettlementServiceImpl implements IPickUpCarSettlementService {
	
    @Resource
	private PickUpCarSettlementMapper pickUpCarSettlementMapper;
    
    @Resource
    private OSMapper osMapper;
	
	/**
	 * 处理查询皮卡研发预算考核展示题的时候的实际值
	 * @param questionAnswerDTO1
	 * @param team
	 */
	@Override
	public void handleYSActualValue(QuestionAnswerDTO questionAnswerDTO1,Team team){
		//获取小题所对应的实际值
		Map<Integer, String> carJS = selectPickUpCarJS(team.getClassId(), team.getId());
		List<SecondQuestionDTO> childrenItem = questionAnswerDTO1.getThisChildrenItem();
		SecondQuestionDTO sqd = null;
		List<Map<String, Object>> itemOptions = null;
    	//临时存放实际值
    	String av = null;
        for (int i = 0; i < childrenItem.size(); i++) {
			sqd = childrenItem.get(i);
			//获取此问题ID的实际值
			av = carJS.get(sqd.getThisItemId());
			//如果获取的值为null则不修改，说明此问题的实际值不需要修改
			if (StringUtils.isNotBlank(av)) {
				itemOptions = sqd.getThisItemOptions();
				for (int j = 0; j < itemOptions.size(); j++) {
					//如果opt为3说明此时的下标为实际值所在的下标，所以修改此下标的实际值
					if ("3".equals(itemOptions.get(j).get("opt").toString())) {
						questionAnswerDTO1.getThisChildrenItem().get(i).getThisItemOptions().get(j).put("optCon", av);
						break;
					}
				}
			}
		}
	}
	
	 private Map<Integer,String> selectPickUpCarJS(Integer classId,Integer teamId) {
		Map<Integer, String> map = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		//添加查询数据传参
	    addParam(param,classId,teamId);
	    Map<String, Object> finalValue = pickUpCarSettlementMapper.selectTeamIntermediateYearSum(param);
	    //销售收入
	    String finalSales = finalValue.get("finalSales").toString();
	    //销量
	    String adjustedSales = finalValue.get("adjustedSales").toString();
	    Double adjustedSalesDouble = Double.valueOf(adjustedSales);
	    //销售业务在本年收到现金总额=上年赊销收现+销售收入*售当年收现比例
	    //查询上年赊销收现
	    Double  snsx=pickUpCarSettlementMapper.getSNSX()*10000*100;
	    //查询销售当年收现比例
	    Double sxbl=pickUpCarSettlementMapper.getSXBL()/100;
	    Double xjze =snsx+Double.valueOf(finalSales)*Double.valueOf(sxbl);
	    String xjzez=FormatNumberUtils.formatDouble(xjze,3);
	    
	    //营业总成本
	    String finalOperatingCostPlus = finalValue.get("finalOperatingCostPlus").toString();
	    //营业成本
	    String yycb = finalValue.get("yycb").toString();
	    //生产工人工资
	    String finalProductiveLaborCost = finalValue.get("finalProductiveLaborCost").toString();
	    //预计生产量=实际皮卡销量-期末产成品差值（数值为-657）
	    //查询期末产成品增加数
	    Double yjscl = adjustedSalesDouble - pickUpCarSettlementMapper.getCpNum();
	    
		// 最终材料成本
		String finalMaterialCostStr = finalValue.get("finalMaterialCost").toString();
		Double finalMaterialCost = Double.valueOf(finalValue.get("finalMaterialCost").toString());
		// 发动机采购单价
		Double cgdj = pickUpCarSettlementMapper.getCGdj() * 10000 * 100;
		// 发动机采购数量=预计生产量-期末发动机差值
		Double fdjsl = yjscl - Double.valueOf(osMapper.selectConfVariable(VNCConstant.FDJCZ));
		// 变速箱采购单价
		Double cgdjbsx = pickUpCarSettlementMapper.getCGDJBSX() * 10000 * 100;
		// 变速箱采购数量=预计生产量-期末变速箱差值
		Double bsxsl = yjscl - Double.valueOf(osMapper.selectConfVariable(VNCConstant.BSXCZ));
		// 其他部件采购单价
		Double cgdjOther = pickUpCarSettlementMapper.getCGDJOther() * 10000 * 100;
		// 其他部件采购数量=预计生产量-期末其他部件差值
		Double qtbjsl = yjscl - Double.valueOf(osMapper.selectConfVariable(VNCConstant.QTBJCZ));
	    
	    //（发动机采购单价*发动机采购数量+变速箱采购单价*变速箱采购数量+其他部件采购单价*其他部件采购数量）
	    Double cs = cgdj*fdjsl + cgdjbsx*bsxsl + cgdjOther*qtbjsl;
	    
	    log.info("最终材料成本:" + finalMaterialCost);
	    log.info("发动机采购单价:" + cgdj);
	    log.info("发动机采购数量:" + fdjsl);
	    log.info("变速箱采购单价:" + cgdjbsx);
	    log.info("变速箱采购数量:" + bsxsl);
	    log.info("其他部件采购单价:" + cgdjOther);
	    log.info("其他部件采购数量:" + qtbjsl);
	    log.info("除数：" + cs);
	    
	    fdjsl = fdjsl < 0 ? 0 : fdjsl;
	    bsxsl = bsxsl < 0 ? 0 : bsxsl;
	    qtbjsl = qtbjsl < 0 ? 0 : qtbjsl;
	    
	    //发动机金额=最终材料成本*发动机采购单价/（发动机采购单价+变速箱采购单价+其他部件采购单价）
	    Double fdjMoney=finalMaterialCost*cgdj*fdjsl/cs;
	    String fdjMoneyStr = FormatNumberUtils.formatDouble(fdjMoney,2);
	    //变速箱金额=最终材料成本*变速箱采购单价/（发动机采购单价+变速箱采购单价+其他部件采购单价）
	    Double bsxMoney=finalMaterialCost*cgdjbsx*bsxsl/cs;
	    String bsxMoneyStr = FormatNumberUtils.formatDouble(bsxMoney,2);
	    //其他部件金额=最终材料成本*其他部件采购单价/（发动机采购单价+变速箱采购单价+其他部件采购单价）
//	    Double otherMoney=finalMaterialCost*cgdjOther*qtbjsl/cs;
		Double otherMoney = Double.valueOf(FormatNumberUtils.formatDouble(finalMaterialCost / 1000000, 2))
				- Double.valueOf(FormatNumberUtils.formatDouble(Double.valueOf(fdjMoneyStr) / 1000000, 2))
				- Double.valueOf(FormatNumberUtils.formatDouble(Double.valueOf(bsxMoneyStr) / 1000000, 2));
		String otherMoneyStr = FormatNumberUtils.formatDouble(otherMoney * 1000000, 2);

	    //采购业务在本年支付现金总额=前期赊销付现（265700万元）+皮卡最终材料成本*55%(采购材料当年付现比例)
	    //前期赊销付现
	    Double qjsxfx=pickUpCarSettlementMapper.getQJSXFX()*10000*100;
	    //采购材料当年付现比例
	    Double dnfxbl=pickUpCarSettlementMapper.getFXBL()/100;
	    Double bnPayMoney=qjsxfx+finalMaterialCost*dnfxbl;
	    String bnPayMoneyStr = FormatNumberUtils.formatDouble(bnPayMoney,3);
	    //预计生产折旧费用
	    String productionDepreciationExpense = finalValue.get("productionDepreciationExpense").toString();
	    //最终能耗成本
	    String finalEnergyCost = finalValue.get("finalEnergyCost").toString();
	    //税金及附加
	    String finalOperatingTaxSurcharge = finalValue.get("finalOperatingTaxSurcharge").toString();
	    //销售费用-宣传推广费
	    String salesPromotionalExpenses = finalValue.get("salesPromotionalExpenses").toString();
	    //销售费用-售后服务费
	    String adjustedSalesAfterSalesServiceFee = finalValue.get("adjustedSalesAfterSalesServiceFee").toString();
	    //最终销售费用-其他
	    String finalSalesOther = finalValue.get("finalSalesOther").toString();
	    //调整后管理费用-研发费
	    String adjustedManagementDevelopmentCost = finalValue.get("adjustedManagementDevelopmentCost").toString();
	    //管理费用-折旧费
	    String managementDepreciationCost = finalValue.get("managementDepreciationCost").toString();
	    //管理费用-其他
	    String adjustedManagementOther = finalValue.get("adjustedManagementOther").toString();
	    //最终财务费用
	    String finalFinancialCost = finalValue.get("finalFinancialCost").toString();
	    //最终资产减值损失合计
	    String finalAssetsImpairmentLossTotal = finalValue.get("finalAssetsImpairmentLossTotal").toString();
	    //营业利润=皮卡实际销售收入-皮卡实际营业总成本
	    Double yylr=Double.valueOf(finalSales)-Double.valueOf(finalOperatingCostPlus);
	    String yylrStr = FormatNumberUtils.formatDouble(yylr,3);
	    //本期现金增减变动=销售业务在本年收到现金总额-采购业务在本年支付现金总额-生产工人工资-预计能耗成本-税金及附加
	    //-销售费用-宣传推广费-销售费用-售后服务费-销售费用-其他-管理费用-研发费-管理费用-其他-财务费用-资产减值损失
	    Double moneyBD=xjze-bnPayMoney-Double.valueOf(finalProductiveLaborCost)-Double.valueOf(finalEnergyCost)-Double.valueOf(finalOperatingTaxSurcharge)
	    -Double.valueOf(salesPromotionalExpenses)-Double.valueOf(adjustedSalesAfterSalesServiceFee)-Double.valueOf(finalSalesOther)-Double.valueOf(adjustedManagementDevelopmentCost)
	    -Double.valueOf(adjustedManagementOther)-Double.valueOf(finalFinancialCost)-Double.valueOf(finalAssetsImpairmentLossTotal);
	    //现金结存
	    //查询定值
	    String desc =pickUpCarSettlementMapper.getDesc(classId, SaleBudgetCheckPickupConstant.QCPKSYBXJCJ_ID);
	    Double xjjcun=Double.valueOf(desc)+moneyBD;
	    map.put(SaleBudgetCheckPickupConstant.XSSR_ID,finalSales);
	    map.put(SaleBudgetCheckPickupConstant.XL_ID,adjustedSales);
	    map.put(SaleBudgetCheckPickupConstant.XSYWZBNSDXJZE_ID,xjzez);
	    map.put(SaleBudgetCheckPickupConstant.YYZCB_ID,finalOperatingCostPlus);
	    map.put(SaleBudgetCheckPickupConstant.YYCB_ID,yycb);
	    map.put(SaleBudgetCheckPickupConstant.YJSCL_ID, FormatNumberUtils.formatDouble(yjscl,FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO));
	    map.put(SaleBudgetCheckPickupConstant.YJCGFDJJE_ID,fdjMoneyStr);
	    map.put(SaleBudgetCheckPickupConstant.YJCGBSXJE_ID,bsxMoneyStr);
	    map.put(SaleBudgetCheckPickupConstant.YJCGQTBJJE_ID,otherMoneyStr);
	    map.put(SaleBudgetCheckPickupConstant.YJCGZJE_ID,finalMaterialCostStr);
	    map.put(SaleBudgetCheckPickupConstant.CGYWZBNZFXJZE_ID,bnPayMoneyStr);
	    map.put(SaleBudgetCheckPickupConstant.YJSCGRGZ_ID,finalProductiveLaborCost);
	    map.put(SaleBudgetCheckPickupConstant.YJSCZJF_ID,productionDepreciationExpense);
	    map.put(SaleBudgetCheckPickupConstant.YJNHCB_ID,finalEnergyCost);
	    map.put(SaleBudgetCheckPickupConstant.SJJFJ_ID,finalOperatingTaxSurcharge);
	    map.put(SaleBudgetCheckPickupConstant.XSFYXCTGF_ID,salesPromotionalExpenses);
	    map.put(SaleBudgetCheckPickupConstant.XSFYSHFWF_ID,adjustedSalesAfterSalesServiceFee);
	    map.put(SaleBudgetCheckPickupConstant.XSFYQT_ID,finalSalesOther);
	    map.put(SaleBudgetCheckPickupConstant.GLFYYFF_ID,adjustedManagementDevelopmentCost);
	    map.put(SaleBudgetCheckPickupConstant.GLFYZJF_ID,managementDepreciationCost);
	    map.put(SaleBudgetCheckPickupConstant.GLFYQT_ID,adjustedManagementOther);
	    map.put(SaleBudgetCheckPickupConstant.CWFY_ID,finalFinancialCost);
	    map.put(SaleBudgetCheckPickupConstant.ZCJZSS_ID,finalAssetsImpairmentLossTotal);
	    map.put(SaleBudgetCheckPickupConstant.YYLR_ID,yylrStr);
	    map.put(SaleBudgetCheckPickupConstant.BQXJZJBD_ID,FormatNumberUtils.formatDouble(moneyBD, 0));
	    map.put(SaleBudgetCheckPickupConstant.QMPKSYBXJJC_ID,FormatNumberUtils.formatDouble(xjjcun, 0));
	    return map;
	}
	
	private void addParam(Map<String, Object> param, Integer classId, Integer teamId) {
		 param.put("classId", classId);
		 param.put("teamId", teamId);
		 param.put("year", NormalConstant.PK_YEAR);
		 param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_PICKUP);
	}

  
}
