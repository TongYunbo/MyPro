package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 皮卡预算questionId对照表
 * @author WangYao
 * 2019年5月30日
 */
@Data
@ToString
public class ClassPickupComparisonDO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 自增id
	 */
	private Integer id;
	
	/**
	 * 销售预算制定id
	 */
	private Integer enactId;
	
	/**
	 * 销售预算考核id
	 */
	private Integer assessId;
	
	/**
	 * 班级id
	 */
	private Integer classId;
}
