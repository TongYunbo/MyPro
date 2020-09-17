package com.tzcpa.constant;


/**
 * <p>Description: 内部控制题类型的枚举</p>
 * @author WTL
 * @date 2019年6月5日
 */
public enum InternalControlEnum {
	
	/**
	 * 要素分析
	 */
	QT1(0, 1),
	
	/**
	 * 是否恰当
	 */
	QT2(1, 2),
	
	/**
	 * 原因说明
	 */
	QT3(2, 3);
	
	private int val;
	
	private int type;
	
	InternalControlEnum(int val, int type){
		this.val = val;
		this.type = type;
	}
	

	public int getType(){
		return this.type;
	}
	
	public static int getQTType(int val){
		for (InternalControlEnum ic : InternalControlEnum.values()) {
			if (ic.val == val) {
				return ic.type;
			}
		}
		return 0;
	}

}

