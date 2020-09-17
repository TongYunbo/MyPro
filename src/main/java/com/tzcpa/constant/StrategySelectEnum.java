package com.tzcpa.constant;

/**
 * 战略选择枚举
 *
 * @Author hanxf
 * @Date 18:17 2019/5/17
**/
public enum StrategySelectEnum {

    //差异化
    A("差"),
    //低成本
    B("低"),
    //自建
    C("自"),
    //外购
    D("购"),
    //退出市场
    E("TWEY"),

    F("CWEY");

    /**
     * 战略选择
     */
    private String strategySelect;

    public String getStrategySelect() {
        return strategySelect;
    }

    public void setStrategySelect(String strategySelect) {
        this.strategySelect = strategySelect;
    }

    StrategySelectEnum(String strategySelect) {
        this.strategySelect = strategySelect;
    }
}
