package com.tzcpa.model.student;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: 修改影响数据</p>
 * @author WTL
 * @date 2019年5月9日
 */
@Data
@ToString
public class UacDO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 班级ID
	 */
	private Integer classId;

	/**
	 * 团队ID
	 */
	private Integer teamId;

	/**
	 * 目标字段
	 */
	private String targetColumn;

	/**
	 * 目标表
	 */
	private String targetTable;
	
	/**
	 * 操作
	 */
	private String operation;
	
	/**
	 * 是否为年度影响
	 */
	private Integer isYear;
	
	/**
	 * 影响量（年为多少年，月为多少月）
	 */
	private Integer impactNum;

	/**
	 * 开始年
	 */
	private Integer bYear;

	/**
	 * 结束年
	 */
	private Integer eYear;

	/**
	 * 开始年月
	 */
	private String ymBDate;

	/**
	 * 结束年月
	 */
	private String ymEDate;
	
	/**
	 * 选择的战略
	 */
	private String strategySelect;
	
	/**
	 * 数据库中配置的条件，用来放到where后的东西
	 */
	private String condition;
	
	/**
	 * 影响的开始时间（如果为空则为时间线的时间）
	 */
	private String bDate;
	
	public void setCTS(HseRequest hse, String ss){
		this.classId = hse.getClassId();
		this.teamId = hse.getTeamId();
		this.strategySelect = ss;
	}
	
	
	
}

