package com.tzcpa.constant;
/**
 * <p>Description: 一般常量</p>
 * @author WTL
 * @date 2019年5月9日
 */
public class NormalConstant {
	
	/**
	 * 所有的角色ID
	 */
	public static final String ALL_ROLE_IDS = "1,2,3,4,5";
	
	/**
	 * 所有的角色所代表的ID
	 */
	public static final Integer ALL_ROLE_ID = 6;
	
	/**
	 * 修改字段调整数据需要选择战略的表(表后以逗号结尾方便判断)
	 */
	public static final String USE_SS_TABLE = "t_class_intermediate,";
	
	/**
	 * 受战略影响
	 */
	public static final Integer STRATEGIC = 1;

    /**
     * 年范围
     */
    public static final String ALL_YEAR = "2011,2012,2013,2014,2015,2016,2017,2018";

    /**
     * 月范围
     */
    public static final String ALL_MONTH = "1,2,3,4,5,6,7,8,9,10,11,12";
    /**
     * 车型H6年范围
     */
    public static final String H6_YEAR = "2011,2012,2013";
    /**
     * 车型H8年范围
     */
    public static final String H8_YEAR = "2015,2016";
    /**
     * 车型wey年范围
     */
    public static final String WEY_YEAR = "2017,2018";
    /**
     * 车型皮卡对应年
     */
    public static final Integer PK_YEAR = 2018;
    /**
     * 所有车型
     */
    public static final String All_VEHICLE_MODEL = "limousine,h6,h8,wey,pickup";
    public static final String VEHICLE_MODEL_LIMOUSINE = "limousine";
    public static final String VEHICLE_MODEL_H6 = "h6";
    public static final String VEHICLE_MODEL_H8 = "h8";
    public static final String VEHICLE_MODEL_WEY = "wey";
    public static final String VEHICLE_MODEL_PICKUP = "pickup";
    
    /**
     * 4P营销策略H6事件问题数量
     */
    public static final Integer MARKETING4PH6_QUESTION_NUM = 6;
    
    /**
     * 总体战略分析所对应的问题ID
     */
    public static final Integer Allstrategy_MAPPING_QUESTION_ID = 1;
    /**
     * 业绩分析所对应的问题ID
     */
    public static final Integer YJPJ_MAPPING_QUESTION_ID = 215;
    /**
     * 4PKHMYDH6所对应的问题ID（要获取所选的option）
     */
    public static final Integer FPKHMYDH6_MAPPING_QUESTION_ID = 21;
    /**
     * 内部控制所对应的问题ID
     */
    public static final Integer InterControl_MAPPING_QUESTION_ID = 142;
    /**
     * 内部审计所对应的问题ID
     */
    public static final Integer InterSJ_MAPPING_QUESTION_ID = 201;
    
    /**
     * FPJGMYDH6所对应的问题ID（要获取所选的option）
     */
    public static final Integer FPJGMYDH6_MAPPING_QUESTION_ID = 23;

    /**
     * XQCP所对应的问题ID（要获取所选的option）
     */
    public static final Integer XQCP_MAPPING_QUESTION_ID = 32;
    
    /**
     * 风险识别应对1所对应的问题ID
     */
    public static final Integer FXSBYD_1_QUESTION_ID = 35;
    
    /**
     * 风险识别应对1所对应的问题ID
     */
    public static final Integer FXSBYD_2_QUESTION_ID = 93;
    
    /**
     * 运营流程优化主题ID
     */
    public static final Integer YYLCYH_M_QUESTION_ID = 26;
    
    /**
     * 投资决策-设备更新非优影响数据问题ID
     */
    public static final Integer YZJC_SBGX_QUESTION_ID = 110;
    
    /**
     * 价值链分析主题ID
     */
    public static final Integer JZL_ZT_QUESTION_ID = 118;
    
    /**
     * 组装变量信息用到的类型	1:通过判定减去影响或者直接执行，2:仅仅替换值，3:通过判定减去影响或者替换值
     */
    public static final String SUBTRACT_EFFECT = "1";
    public static final String REPLACEMENT_VALUE = "2";
    public static final String JUDGE_EFFECT_REPLACE = "3";
    
    /**
     * 延迟影响的类型
     */
    public static final Integer NO_DELAYED_IMPACT_TYPE = 0;

	/**
	 * 延迟类型
	 * 1:财务指标类型(成本)，2:销量，3:单价，4:管理费用-售后服务费，5:管理费用-其他，6:风险识别H6有需要按照延迟执行数据计算的类型,7:
	 * 财务费用，8:固定资产原值，9:无形资产，10:累计折旧，11:股本，12:短期借款，13:应付债券，14:固定资产原价，15:商誉，16:
	 * 资本公积
	 */
	public static final Integer DELAYED_TYPE_CB = 1;
	public static final Integer DELAYED_TYPE_XL = 2;
	public static final Integer DELAYED_TYPE_DJ = 3;
	public static final Integer DELAYED_TYPE_SHFWWF = 4;
	public static final Integer DELAYED_TYPE_QT = 5;
	public static final Integer DELAYED_TYPE_FXSB = 6;
	public static final Integer DELAYED_TYPE_CWFY = 7;
	public static final Integer DELAYED_TYPE_GDZCYZ = 8;
	public static final Integer DELAYED_TYPE_WXZC = 9;
	public static final Integer DELAYED_TYPE_LJZJ = 10;
	public static final Integer DELAYED_TYPE_GB = 11;
	public static final Integer DELAYED_TYPE_DQJK = 12;
	public static final Integer DELAYED_TYPE_YFZQ = 13;
	public static final Integer DELAYED_TYPE_GDZCYJ = 14;
	public static final Integer DELAYED_TYPE_SY = 15;
	public static final Integer DELAYED_TYPE_ZBGJ = 16;
	public static final String DELAYED_TYPE_ZCFZ_TABLE = "8,9,10,11,12,13,14,15,16";
    
    /**
	 * 盈利能力分析对应的小问题Id
	 */
	public static final String YLNL_QUESTIONIDS = "239,240,241,242,243,244";
	/**
	 * 盈利能力分析我公司数据对应的小问题Id
	 */
	public static final String WECOMPANY_QUESTIONIDS = "459,460,461,462";
	/**
     * 我公司营业收入数据对应问题Id
     */
    public static final Integer YYSR_QUESTION_ID = 459;
    /**
     * 我公司销量数据对应问题Id
     */
    public static final Integer XL_QUESTION_ID = 460;
    /**
     * 我公司营业总成本数据对应问题Id
     */
    public static final Integer YYZCB_QUESTION_ID = 461;
    /**
     * 我公司营业成本对应问题Id
     */
    public static final Integer YYCB_QUESTION_ID = 462;
    /**
	 * 盈利能力分析对应的毛利小问题Id
	 */
	public static final Integer ML_QUESTIONIDS = 245;
	/**
	 * 盈利能力分析对应的毛利率小问题Id
	 */
	public static final Integer MLR_QUESTIONIDS = 246;
	/**
	 * 盈利能力分析对应的单车毛利小问题Id
	 */
	public static final Integer MLD_QUESTIONIDS = 247;
	/**
	 * 盈利能力分析对应的单车利润小问题Id
	 */
	public static final Integer LRD_QUESTIONIDS = 248;
	/**
	 * 盈利能力分析对应的单车售价小问题Id
	 */
	public static final Integer SJD_QUESTIONIDS = 249;
	/**
	 * 盈利能力分析对应的营业利润率小问题Id
	 */
	public static final Integer YYLRL_QUESTIONIDS = 250;

    /**
     * 4P营销策略H8
     */
    public static final Integer MARKETING4PH8_QUESTION_NUM = 7;
    public static final Integer FPKHMYDH8_MAPPING_QUESTION_ID = 88;
    public static final Integer FPXLH8_MAPPING_QUESTION_ID = 91;

    /**
     * 4P营销WEY
     */
    public static final Integer MARKETING4PWEY_QUESTION_NUM = 5;
    public static final Integer FPWEY_MAPPING_QUESTION_ID = 139;

    /**
     * 皮卡车销售预算完成考核
     */
    public static final Integer SALEBUDGETCHECKPICKUP_QUESTION_NUM = 21;

    /**
     * 皮卡车销售预算完成考核-营业利润 所对应的问题ID
     */
    public static final Integer SALEBUDGETCHECKPICKUP_MAPPING_QUESTION_ID = 427;

    /**
     * 皮卡车销售预算
     */
    public static final Integer SALEBUDGETPICKUP_QUESTION_NUM = 28;
    /**
     * 皮卡车销售预算-预计采购总金额 所对应的问题ID
     */
    public static final Integer SALEBUDGETPICKUP_MAPPING_QUESTION_ID = 411;
    /**
     * 皮卡车销售预算-预计生产量
     */
    public static final Integer SALEBUDGETPICKUP_QUESTION_ID_A = 474;
    /**
     * 皮卡车销售预算-预计采购发动机金额
     */
    public static final Integer SALEBUDGETPICKUP_QUESTION_ID_B = 475;
    /**
     * 皮卡车销售预算-预计采购变速箱金额
     */
    public static final Integer SALEBUDGETPICKUP_QUESTION_ID_C = 476;
    /**
     * 皮卡车销售预算-预计采购其他部件金额
     */
    public static final Integer SALEBUDGETPICKUP_QUESTION_ID_D = 477;

    /**
     * 营业能力判断查询单车毛利对应问题ID
     */
    public static final Integer InterPD_MAPPING_QUESTION_ID = 241;
    /**
     * 差异化战略的值
     */
    public static final String CYHZL = "A";
    public static final String CYHZL_CN = "差";
    
    /**
     * 低成本战略
     */
    public static final String DCBZL = "B";
    public static final String DCBZL_CN = "低";
    
    /**
     * 自建战略
     */
    public static final String ZJZL = "C";
    public static final String ZJZL_CN = "自";
    
    /**
     * 外购战略
     */
    public static final String WGZL = "D";
    public static final String WGZL_CN = "购";
    
    /**
     * wey停产决策战略
     */
    public static final String TWEYZL = "E";
    public static final String TWEYZL_CN = "TWEY";
    
    /**
     * wey 生产决策战略
     */
    public static final String CWEYZL = "F";
    public static final String CWEYZL_CN = "CWEY";
    
    /**
     * 一月用来拼接字符用
     */
    public static final String JANUARY = "-01";
    
    /**
     * 车型
     */
    public static final String H6 = "h6";
    public static final String LIMOUSINE = "limousine";
    public static final String H8 = "h8";
    public static final String WEY = "wey";
    public static final String PICKUP = "pickup";
    
    /**
     * 单位常量
     */
    public static final String UNIT_BFH = "%";
    
    public static final String YEAR_2011 = "2011";
    public static final String YEAR_2013 = "2013";
    public static final String YEAR_2015 = "2015";
    public static final String YEAR_2016 = "2016";
    public static final String YEAR_2017 = "2017";
    
    public static final Integer MONTH_5 = 5;
    
    public static final double cal_num_1 = 0.105;
    public static final double cal_num_2 = 0.055;


    /**
     * 调整后单价销量
     */
    public static final String ADJUSTEDSALES = "adjusted_sales";
    public static final String ADJUSTEDUNITPRICE = "adjusted_unit_price";

    /**
     * 需要维护战略地图的年份
     */

    public  static  final String[] STAGR_YEAR = new String[]{"2012","2013","2015","2016","2018"};
    public  static  final String[] STAGR_YEAR_H6 = new String[]{"2012","2013"};
    public  static  final String[] STAGR_YEAR_H8 = new String[]{"2015","2016"};
    
    /**
     * 平衡计分卡中wey需要的战略值
     */
    public static final String PHJFK_WEY_SC = "0";

    /**
     * 需要维护 最终生产折旧 的问题id
     */
    public static final Integer[] DEPRECIATION_QUESIDS = new Integer[]{63,108,133};

    /**
     * 需要维护 最终宣传推广费 的问题id
     */
    public static final Integer[] POPULARIZING_QUESIDS = new Integer[]{25,45};

    /**
     * 需要维护 最终资产减值损失-坏账 的问题id
     */
    public static final Integer[] LOSS_QUESIDS = new Integer[]{142};
    
    /**
     * 内部控制需要执行影响的问题ID
     */
    public static final Integer NBKZ_YXQT_3 = 355;
    public static final Integer NBKZ_YXQT_11 = 379;
    public static final Integer NBKZ_YXQT_12 = 382;
    
    /**
     * 不受战略的影响题ID（战略目标量化）
     */
    public static final Integer[] ZLMBLH_BSZLYX_ID = new Integer[]{9,10,69,70};
    
    /**
     * 判断百分之80几率的方法
     */
    public static final String BFZ80JVFF = "judge80Change";
    
    /**
     * 研发方向选择2H6题ID
     */
    public static final Integer YFFXXZ2H6_QUESTION_ID = 72;
    
    /**
     * 融资发股份ID
     */
    public static final Integer RZFGJY_QUESTION_ID = 112;
    
    /**
     * 皮卡销售预算制定
     */
    public static final Integer PKXSYS_QUESTION_ID = 160;
}

