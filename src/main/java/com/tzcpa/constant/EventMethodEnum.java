package com.tzcpa.constant;

/**
 * 答题结束-计算影响 需要调用的service对象位置及方法名字
 */
public enum EventMethodEnum {
	//总体战略选择
    EV2("com.tzcpa.service.question.impl.AllStrateServiceImpl","checkOS"),
	//H6战略选择2011
    EV3("com.tzcpa.service.question.impl.H6StrategicChoicesServiceImpl","checkOS"),
    //平衡计分卡H6
    EV5("com.tzcpa.service.question.impl.BalanceScoreCardH6ServiceImpl","checkOS"),
	//组织结构优化
    EV6("com.tzcpa.service.question.impl.HseServiceImpl","checkOS"),
    //4P营销H6
    EV7("com.tzcpa.service.question.impl.Marketing4PH6ServiceImpl","checkOS"),
    //研发方向选择1
    EV8("com.tzcpa.service.question.impl.ReseDevelopServiceImpl","checkOS"),
    //4p广告追加判断
    EV9("com.tzcpa.service.question.impl.Advertisement4pServiceImpl","checkOS"),
    //生产流程优化
    EV10("com.tzcpa.service.question.impl.ProcessOptimizationServiceImpl","checkOS"),
    //培训体系建设
    EV11("com.tzcpa.service.question.impl.ConstructionSystemServiceImpl","checkOS"),
    //人才引进
    EV12("com.tzcpa.service.question.impl.TalentIntroductionServiceImpl","checkOS"),
    //产品需求调研
    EV13("com.tzcpa.service.question.impl.DemandSurveyServiceImpl","checkOS"),
    //研发费用投入1
    EV14("com.tzcpa.service.question.impl.RDEIOneServiceImpl","checkOS"),
    //产能扩建决策
    EV15("com.tzcpa.service.question.impl.CapacityExpansionServiceImpl","checkOS"),
    //风险识别H6
    EV16("com.tzcpa.service.question.impl.RiskIdentificationServiceImpl","checkOS"),
    //研发费用投入2
    EV19("com.tzcpa.service.question.impl.RDEITwoServiceImpl","checkOS"),
    //运营能力分析
    EV20("com.tzcpa.service.question.impl.StockTurnoverServiceImpl","checkOS"),
    //投资决策-项目选择
    EV21("com.tzcpa.service.question.impl.ProjectSelectionServiceImpl","checkOS"),
    //五力分析
    EV22("com.tzcpa.service.question.impl.ForcesAnalysisServiceImpl","checkOS"),
    //H6战略选择修改2013
    EV23("com.tzcpa.service.question.impl.H6StrategicChoicesServiceImpl","checkOS"),
    //H8战略选择2013
    EV24("com.tzcpa.service.question.impl.H8StrategicChoicesServiceImpl","checkOS"),
    //研发费用投入3
    EV25("com.tzcpa.service.question.impl.RDEIThreeServiceImpl","checkOS"),
    //自建外购
    EV26("com.tzcpa.service.question.impl.ZwServiceImpl","checkOS"),
    //现金采购折扣决策
    EV27("com.tzcpa.service.question.impl.CpServiceImpl","checkOS"),
    //平衡积分卡H8
    EV29("com.tzcpa.service.question.impl.BalanceScoreCardH8ServiceImpl","checkOS"),
    //研发方向选择2
    EV30("com.tzcpa.service.question.impl.ReseDevelopTServiceImpl","checkOS"),
    //研发费用投入4
    EV31("com.tzcpa.service.question.impl.RDEIFourServiceImpl","checkOS"),
    //产品矩阵
    EV32("com.tzcpa.service.question.impl.ProductMatrixServiceImpl","checkOS"),
    //盈亏平衡-H8定价
    EV33("com.tzcpa.service.question.impl.BreakEvenServiceImpl","checkOS"),
    //4P营销H8
    EV34("com.tzcpa.service.question.impl.Marketing4PH8ServiceImpl","checkOS"),
    //运营-产能分配
    EV35("com.tzcpa.service.question.impl.CapacityAllocationServiceImpl","checkOS"),
    //风险识别H8
    EV36("com.tzcpa.service.question.impl.RiskIdentificationTServiceImpl","checkOS"),
    //需求采购组合决策
    EV37("com.tzcpa.service.question.impl.DemandProcurementServiceImpl","checkOS"),
    //员工能力培训H8
    EV38("com.tzcpa.service.question.impl.AbilityTrainingServiceImpl","checkOS"),
    //员工业务能力升级
    EV39("com.tzcpa.service.question.impl.CapabilityUpgradingServiceImpl","checkOS"),
    //企业文化塑造度
    EV40("com.tzcpa.service.question.impl.CorporateCultureServiceImpl","checkOS"),
    //研发费用投入5
    EV41("com.tzcpa.service.question.impl.RDEIFiveServiceImpl","checkOS"),
    //投资决策-设备更新
    EV42("com.tzcpa.service.question.impl.EquipmentRenewalServiceImpl","checkOS"),
    //盈亏平衡销量
    EV43("com.tzcpa.service.question.impl.BreakEvenH8ServiceImpl","checkOS"),
    //融资决策-发股份
    EV44("com.tzcpa.service.question.impl.IssueSharesServiceImpl","checkOS"),
    //融资决策-方式选择
    EV45("com.tzcpa.service.question.impl.ModeChoiceServiceImpl","checkOS"),
    //生产决策-停产决策
    EV47("com.tzcpa.service.question.impl.WeyStrategicChoicesServiceImpl","checkOS"),
    //发行股份决议
    EV46("com.tzcpa.service.question.impl.IssueGaoServiceImpl","checkOS"),
    //研发费用投入6
    EV48("com.tzcpa.service.question.impl.RDEISixServiceImpl","checkOS"),
    //密集型战略选择 
    EV49("com.tzcpa.service.question.impl.StrategicChoiceServiceImpl","checkOS"),
    //价值链分析
    EV50("com.tzcpa.service.question.impl.ValueAnalysisServiceImpl","checkOS"),
    //战略目标量化-3
    EV52("com.tzcpa.service.question.impl.BalanceScoreCardWEYServiceImpl","checkOS"),
    //投资决策并购
    EV53("com.tzcpa.service.question.impl.ResultInformServiceImpl","checkOS"),
    //4P营销wey
    EV54("com.tzcpa.service.question.impl.Marketing4PWEYServiceImpl","checkOS"),
    //内部控制
    EV55("com.tzcpa.service.question.impl.InternalControlServiceImpl","checkOS"),
    //WEY客户满意度-1
    EV56("com.tzcpa.service.question.impl.CustomerSatisfactionTServiceImpl","checkOS"),
    //产品设计能力提升度 
    EV57("com.tzcpa.service.question.impl.ProductDesignServiceImpl","checkOS"),
    //WEY客户满意度-2
    EV58("com.tzcpa.service.question.impl.CustomerSatisfactionServiceImpl","checkOS"),
    // 员工能力培训WEY
    EV59("com.tzcpa.service.question.impl.AbilityTrainingWeyServiceImpl","checkOS"),
    //信息化建设完成度
    EV60("com.tzcpa.service.question.impl.InformationConstructionServiceImpl","checkOS"),
    //存货经济批量分析
    EV61("com.tzcpa.service.question.impl.EconomicAnalysisServiceImpl","checkOS"),
    //皮卡预算
    EV62("com.tzcpa.service.question.impl.SaleBudgetPickupServiceImpl","checkOS"),
   //研发费用投入7
    EV63("com.tzcpa.service.question.impl.RDEISevenServiceImpl","checkOS"),
    //全线产品矩阵
    EV64("com.tzcpa.service.question.impl.FullProductMatrixServiceImpl","checkOS"),
    //产品管理结构调整
    EV65("com.tzcpa.service.question.impl.ProductAdjustmentServiceImpl","checkOS"),
    //竞争投标活动
    EV66("com.tzcpa.service.question.impl.BiddingEffectServiceImpl","checkOS"),
    //内部审计
    EV67("com.tzcpa.service.question.impl.InternalAuditingServiceImpl","checkOS"),
    //竞争投标结果通知
    EV68("com.tzcpa.service.question.impl.BiddingEffectTServiceImpl","checkOS"),
    //盈利能力分析
    EV69("com.tzcpa.service.question.impl.ProfitAnalysisServiceImpl","checkOS"),
    //盈利能力判断
    EV70("com.tzcpa.service.question.impl.ProfitJudgmentServiceImpl","checkOS"),
    //部门业绩评价指标分析
    EV71("com.tzcpa.service.question.impl.DepartmentalPerformanceServiceImpl","checkOS"),
    //行业下行经营策略调整
    EV72("com.tzcpa.service.question.impl.IndustryDownlinkServiceImpl","checkOS"),
    //皮卡销售预算完成考核
    EV73("com.tzcpa.service.question.impl.SaleBudgetCheckPickupServiceImpl","checkOS"),
    //研发费用投入8
    EV74("com.tzcpa.service.question.impl.RDEIEightServiceImpl","checkOS"),
    //并购结果告知
    EV75("com.tzcpa.service.question.impl.BingGouGaoServiceImpl","checkOS"),
    ;
    /**
     * spring bean实例名字
     */
    private String beanPosition;
    /**
     * 调用方法名
     */
    private String methodName;


    public String getBeanPosition() {
        return beanPosition;
    }

    public void setBeanPosition(String beanPosition) {
        this.beanPosition = beanPosition;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    EventMethodEnum(String beanPosition, String methodName) {
        this.beanPosition = beanPosition;
        this.methodName = methodName;
    }

    public static String getBeanPositionByKey(String code){
        for(EventMethodEnum eventMethodEnum:EventMethodEnum.values()){
            if(code.equals(eventMethodEnum.name())){
                return eventMethodEnum.beanPosition;
            }
        }
        return  null;
    }

    public static String getMethodNameByKey(String code){
        for(EventMethodEnum eventMethodEnum:EventMethodEnum.values()){
            if(code.equals(eventMethodEnum.name())){
                return eventMethodEnum.methodName;
            }
        }
        return  null;
    }

}
