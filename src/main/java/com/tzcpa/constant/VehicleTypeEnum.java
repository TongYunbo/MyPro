package com.tzcpa.constant;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年6月13日
 */
public enum VehicleTypeEnum {
	
	QOP_h6("S60", "h6"),
	QOP_wey("N70", "wey"),
	QOP_pickup("PU30", "pickup"),
	QOP_limousine("J50", "limousine");
	
	
	private String vehicleType;
	
	private String vehicleModel;
	
	VehicleTypeEnum(String vehicleType, String vehicleModel){
		this.vehicleType = vehicleType;
		this.vehicleModel = vehicleModel;
	}
	
	public static String getOptionNum(String vehicleType){
		for (VehicleTypeEnum bo : VehicleTypeEnum.values()) {
			if (bo.vehicleType.equals(vehicleType)) {
				return bo.vehicleModel;
			}
		}
		return null;
	}

}

