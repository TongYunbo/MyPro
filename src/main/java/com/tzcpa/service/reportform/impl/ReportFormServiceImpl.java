package com.tzcpa.service.reportform.impl;

import com.tzcpa.mapper.student.TeamAnnualWorthGatherMapper;
import com.tzcpa.mapper.student.TeamMonthlyProfitStatementMapper;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.mapper.treatment.TeamBalanceSheetMapper;
import com.tzcpa.mapper.treatment.TeamProfitStatementMapper;
import com.tzcpa.model.student.TeamAnnualWorthGather;
import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.treatment.TeamBalanceSheetDO;
import com.tzcpa.model.treatment.TeamProfitStatementDO;
import com.tzcpa.service.reportform.ReportFormService;
import com.tzcpa.utils.CompareResultUtils;
import com.tzcpa.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName ReportFormServiceImpl
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/29 11:30
 * @Version 6.0
 **/
@Service
public class ReportFormServiceImpl implements ReportFormService {

    @Resource
    private TeamMonthlyProfitStatementMapper teamMonthlyProfitStatementMapper;

    @Resource
    private TeamProfitStatementMapper teamProfitStatementMapper;

    @Resource
    private TeamBalanceSheetMapper teamBalanceSheetMapper;

    @Resource
    private ClassQuestionDescMapper classQuestionDescMapper;

    @Resource
    private TeamAnnualWorthGatherMapper teamAnnualWorthGatherMapper;

    /**
     * @author:     wangzhangju
     * @date:       2019/5/29 11:35
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取月度利润表
     * @step:
     */
    @Override
    public List<TeamMonthlyProfitStatementDO> getMonthlyForm(Integer classId, Integer teamId, Integer year) {
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        List<TeamMonthlyProfitStatementDO> results =
                teamMonthlyProfitStatementMapper.getMonthlyForm(classId,teamId,year,currentMonth);
        return results;
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/29 14:37
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取年度利润表
     * @step:
     */
    @Override
    public List<TeamProfitStatementDO> getAnnualForm(Integer classId, Integer teamId, Integer year) {

        //NO.1 获取上一年度值
        Integer lastYear = teamProfitStatementMapper.getLastYear(classId,teamId,year);
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        int nowYear = DateUtil.getDateAttr(currentMonth, Calendar.YEAR);
        int month = DateUtil.getDateAttr(currentMonth, 2)+1;
        if(nowYear == year && month != 12){
            year = 0;
        }
        //NO.2 返回年度数据
        List<TeamProfitStatementDO> doList = teamProfitStatementMapper.getAnnualForm(classId,teamId,year,lastYear);

        if(doList.size() == 1){
            List<TeamProfitStatementDO> teamProfitStatementDOS = new ArrayList<>();
            teamProfitStatementDOS.add(new TeamProfitStatementDO());
            teamProfitStatementDOS.add(doList.get(0));
            return teamProfitStatementDOS;
        }else{
            return doList;
        }
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/30 17:02
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取资产负债报表
     * @step:
     */
    @Override
    public List<TeamBalanceSheetDO> getBalanceSheet(Integer classId, Integer teamId, Integer year) {

        Integer lastYear = classQuestionDescMapper.getLastYearByYear(year,classId);
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        int nowYear = DateUtil.getDateAttr(currentMonth, Calendar.YEAR);
        int month = DateUtil.getDateAttr(currentMonth, 2)+1;
        if(nowYear == year &&  month != 12){
            year = 0;
        }
        List<TeamBalanceSheetDO> teamBalanceSheetDOS = teamBalanceSheetMapper.getBalanceSheetForm(classId,teamId,lastYear,year);
        if(teamBalanceSheetDOS.size() == 1){
            List<TeamBalanceSheetDO> teamBalanceSheetDOS2 = new ArrayList<>();
            teamBalanceSheetDOS2.add(new TeamBalanceSheetDO());
            teamBalanceSheetDOS2.add(teamBalanceSheetDOS.get(0));
            return teamBalanceSheetDOS2;
        }else{
            return teamBalanceSheetDOS;
        }

    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/3 14:41
     * @param:      classId teamId year
     * @return:
     * @exception:
     * @description: 获取年度分析报表数据
     * @step:
     */
    @Override
    public List<Map> getTeamAnnual(Integer classId, Integer teamId, Integer year) {
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        int nowYear = DateUtil.getDateAttr(currentMonth, Calendar.YEAR);
        int month = DateUtil.getDateAttr(currentMonth, 2)+1;
        if(nowYear == year && month != 12){
            year = 0;
        }
        List<Map> mapList = new ArrayList<>();

        TeamAnnualWorthGather teamAnnualWorthGather =
                teamAnnualWorthGatherMapper.getTeamAnnualWorthGather(classId,teamId,year);
        if(null == teamAnnualWorthGather){
            teamAnnualWorthGather = new TeamAnnualWorthGather();
        }
        //NO.1 封装 财务分析
         //NO.1-1 封装综合指标
        mapList.add(this.getMapData("财务分析","综合指标","净资产收益率(%)" ,teamAnnualWorthGather.getNetAssetsIncomeRate()));
        //NO.1-2 封装盈利能力
        mapList.add(this.getMapData("财务分析","盈利能力","净利润(亿元)" ,teamAnnualWorthGather.getRetainedProfits()));
        mapList.add(this.getMapData("财务分析","盈利能力","营业收入(亿元)" ,teamAnnualWorthGather.getOperationRevenue()));
        mapList.add(this.getMapData("财务分析","盈利能力","每股净资产(元)" ,teamAnnualWorthGather.getNetAssetValuePerShare()));
        mapList.add(this.getMapData("财务分析","盈利能力","每股收益(元)" ,teamAnnualWorthGather.getEarningsPerShare()));
        mapList.add(this.getMapData("财务分析","盈利能力","销售毛利(亿元)" ,teamAnnualWorthGather.getSalesMargin()));
        mapList.add(this.getMapData("财务分析","盈利能力","销售毛利率(%)" ,teamAnnualWorthGather.getGrossMargin()));
        mapList.add(this.getMapData("财务分析","盈利能力","销售净利率(%)" ,teamAnnualWorthGather.getNetProfitMarginOnSales()));
        mapList.add(this.getMapData("财务分析","盈利能力","营业利润率(%)" ,teamAnnualWorthGather.getOperatingProfitRatio()));


        //NO.1-2 封装 成长能力（同比增长率）

        mapList.add(this.getMapData("财务分析","成长能力（同比增长率）","每股收益同比增长率(%)" ,teamAnnualWorthGather.getYearOverYearGrowthInEarningsPerShare()));
        mapList.add(this.getMapData("财务分析","成长能力（同比增长率）","营业收入同比增长率(%)" ,teamAnnualWorthGather.getYearOverYearGrowthRateOfOperatingIncome()));
        mapList.add(this.getMapData("财务分析","成长能力（同比增长率）","净利润同比增长率(%)" ,teamAnnualWorthGather.getYearOnYearGrowthRateOfNetProfit()));
        mapList.add(this.getMapData("财务分析","成长能力（同比增长率）","净资产收益率同比增长率(%)" ,teamAnnualWorthGather.getYearOverYearReturnOnEquityGrowth()));


        //NO.1-3 封装 运营能力
        mapList.add(this.getMapData("财务分析","运营能力","存货周转次数(次)" ,teamAnnualWorthGather.getRateOfStockTurnover()));
        mapList.add(this.getMapData("财务分析","运营能力","总资产周转次数(次)" ,teamAnnualWorthGather.getTurnoverOfTotalCapital()));
        mapList.add(this.getMapData("财务分析","运营能力","总资产(亿元)" ,teamAnnualWorthGather.getTotalAssets()));
        mapList.add(this.getMapData("财务分析","运营能力","净资产(亿元)" ,teamAnnualWorthGather.getNetAsset()));


        //NO.1-4 封装 偿债能力
        mapList.add(this.getMapData("财务分析","偿债能力","资产负债率(%)" ,teamAnnualWorthGather.getAssetLiabilityRatio()));
        mapList.add(this.getMapData("财务分析","偿债能力","流动比率" ,teamAnnualWorthGather.getLiquidityRatio()));
        mapList.add(this.getMapData("财务分析","偿债能力","速动比率" ,teamAnnualWorthGather.getQuickRatio()));


        //NO.2 封装 经营分析
        //NO.2-1 封装 销量增长率
        mapList.add(this.getMapData("经营分析","销量增长率","销量增长率(%)" ,teamAnnualWorthGather.getSalesGrowthRate()));

        //NO.2-2 封装 营业收入构成
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfOperatingIncomeLimousine())) {
            mapList.add(this.getMapData("经营分析", "营业收入构成", "J50(%)", teamAnnualWorthGather.getCompositionOfOperatingIncomeLimousine()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfOperatingIncomeH6())) {
            mapList.add(this.getMapData("经营分析", "营业收入构成", "S60(%)", teamAnnualWorthGather.getCompositionOfOperatingIncomeH6()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfOperatingIncomePickup())) {
            mapList.add(this.getMapData("经营分析", "营业收入构成", "PU30(%)", teamAnnualWorthGather.getCompositionOfOperatingIncomePickup()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfOperatingIncomeWey())) {
            mapList.add(this.getMapData("经营分析", "营业收入构成", "N70(%)", teamAnnualWorthGather.getCompositionOfOperatingIncomeWey()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfOperatingIncomeH8())) {
            mapList.add(this.getMapData("经营分析", "营业收入构成", "S80(%)", teamAnnualWorthGather.getCompositionOfOperatingIncomeH8()));
        }

        //NO.2-3 封装 净利润构成
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfNetProfitLimousine())) {
            mapList.add(this.getMapData("经营分析", "净利润构成", "J50(%)", teamAnnualWorthGather.getCompositionOfNetProfitLimousine()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfNetProfitH6())) {
            mapList.add(this.getMapData("经营分析", "净利润构成", "S60(%)", teamAnnualWorthGather.getCompositionOfNetProfitH6()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfNetProfitPickup())) {
            mapList.add(this.getMapData("经营分析", "净利润构成", "PU30(%)", teamAnnualWorthGather.getCompositionOfNetProfitPickup()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfNetProfitWey())) {
            mapList.add(this.getMapData("经营分析", "净利润构成", "N70(%)", teamAnnualWorthGather.getCompositionOfNetProfitWey()));
        }
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getCompositionOfNetProfitH8())) {
            mapList.add(this.getMapData("经营分析", "净利润构成", "S80(%)", teamAnnualWorthGather.getCompositionOfNetProfitH8()));
        }


        //NO.2-4 封装 研发费用
        mapList.add(this.getMapData("经营分析","研发费用情况","研发费用(亿元)" ,teamAnnualWorthGather.getDevelopmentExpenditure()));

        //NO.2-5 封装 研发实际投入金额占本年营收比（%）
        mapList.add(this.getMapData("经营分析","研发费用情况","研发实际投入金额占本年营收比（%）" ,
                teamAnnualWorthGatherMapper.getYFFZB(classId, teamId, year)));


        //NO.2-6 封装 H6市场份额占比
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getH6MarketShare())) {
            mapList.add(this.getMapData("经营分析", "suv品类市场份额占比", "S60市场份额占比(%)",
                    teamAnnualWorthGather.getH6MarketShare()));
        }

        //NO.2-7 封装 H8市场份额占比
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getH8MarketShare())) {
            mapList.add(this.getMapData("经营分析", "suv品类市场份额占比", "S80市场份额占比(%)",
                    teamAnnualWorthGather.getH8MarketShare()));
        }
        //NO.2-8 封装 WEY市场份额占比
        if(CompareResultUtils.whetherInvalid(teamAnnualWorthGather.getWeyMarketShare())) {
            mapList.add(this.getMapData("经营分析", "suv品类市场份额占比", "N70市场份额占比(%)",
                    teamAnnualWorthGather.getWeyMarketShare()));
        }
        //NO.2-9 封装 新产品贡献率
        mapList.add(this.getMapData("经营分析","新产品市场情况","新产品贡献率(%)" ,
                teamAnnualWorthGather.getNewProductContributionRate()));

        return mapList;
    }

    Map getMapData(String lev1Name,String lev2Name,String lev3Name,Object val){
        Map map = new LinkedHashMap();
        map.put("lev1Name",lev1Name);
        map.put("lev2Name",lev2Name);
        map.put("lev3Name",lev3Name);
        map.put("val",val);
        return map;
    }
}
