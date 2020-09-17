package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: 查询财务指标数据实体类</p>
 * @author WTL
 * @date 2019年5月17日
 */
@Data
@ToString
public class SearchFinanceDTO {
	
	/**
	 * 目标表
	 */
	private String targetTable;
	
	/**
	 * 目标字段
	 */
	private String targetColumn;
	
	/**
	 * 班级ID
	 */
	private Integer classId;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 所在年
	 */
	private Integer year;
	
	/**
	 * 所在月
	 */
	private Integer month;
	
	/**
	 * 所在年月
	 */
	private String ymDate;
	
	/**
	 * where后的条件，需要以and开头 ，如：and vehicle_model = 'h6'
	 */
	public String operation;
	
	public SearchFinanceDTO(HseRequest hse, String targetTable, String targetColumn, Integer year, String operation){
		this.classId = hse.getClassId();
		this.teamId = hse.getTeamId();
		this.targetTable = targetTable;
		this.targetColumn = targetColumn;
		this.year = year;
		this.operation = operation;
	}
	
}

