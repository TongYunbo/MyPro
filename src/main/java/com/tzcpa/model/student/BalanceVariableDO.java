package com.tzcpa.model.student;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年5月16日
 */
@Data
@ToString
public class BalanceVariableDO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 班级ID
	 */
	private Integer classId;
	
	/**
	 * 变量名称
	 */
	private String variableName;
	
	/**
	 * 变量的值
	 */
	private String variableVal;
	
	/**
	 * 变量的单位
	 */
	private String unit;
	
	/**
	 * 比较的方法
	 */
	private String compareMethod;
	
	public BalanceVariableDO(){}
	
	public BalanceVariableDO(Integer teamId, Integer classId, String variableName, String variableVal, String unit){
		this.teamId = teamId;
		this.classId = classId;
		this.variableName = variableName;
		this.variableVal = variableVal;
		this.unit = unit;
	}
	
	/**
	 * 添加班级ID和团队ID
	 * @param classId
	 * @param teamId
	 */
	public void setTC(Integer classId, Integer teamId){
		this.classId = classId;
		this.teamId = teamId;
	}

}

