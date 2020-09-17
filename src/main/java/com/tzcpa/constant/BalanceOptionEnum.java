package com.tzcpa.constant;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年6月13日
 */
public enum BalanceOptionEnum {
	
	QOP_A("A", 1),
	QOP_B("B", 2),
	QOP_C("C", 3),
	QOP_D("D", 4),
	QOP_E("E", 5),
	QOP_F("F", 6);
	
	private String qOption;
	
	private Integer optionNum;
	
	BalanceOptionEnum(String qOption, Integer optionNum){
		this.qOption = qOption;
		this.optionNum = optionNum;
	}
	
	public static int getOptionNum(String qOption){
		for (BalanceOptionEnum bo : BalanceOptionEnum.values()) {
			if (bo.qOption.equals(qOption)) {
				return bo.optionNum;
			}
		}
		return 0;
	}

}

