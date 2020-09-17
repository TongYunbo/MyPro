package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: 延迟执行任务类</p>
 * @author WTL
 * @date 2019年5月17日
 */
@Data
@ToString
public class DelayedUpdateDO {
	
	/**
	 * 主键
	 */
	private Integer id;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 班级ID
	 */
	private Integer classId;
	
	/**
	 * 任务IDS
	 */
	private String taskIds;
	
	/**
	 * 执行次数
	 */
	private Integer executionTimes;
	
	/**
	 *  数据库中存储为开始时间，传参时为当前时间
	 */
	private String bTime;
	
	/**
	 * 延迟类型(1:财务指标类型(成本)，2:销量，3:单价，4:管理费用-售后服务费，5:管理费用-其他，6:
	 * 风险识别H6有需要按照延迟执行数据计算的类型,7:财务费用，8:固定资产原值，9:无形资产，10:累计折旧，11:股本，12:短期借款，13:
	 * 应付债券，14:固定资产原价，15:商誉，16:资本公积)
	 */
	private Integer delayedType;
	
	/**
	 * 查询延迟任务执行时所需参数多个以逗号分隔
	 */
	private String dTypes;
	
	/**
	 * viList值
	 */
	private String viData;
	
	/**
	 * 车型
	 */
	private String vehicleModel;
	
	/**
	 * 排序值
	 */
	private Integer sortNum;
	
	public DelayedUpdateDO(){}
	
	public DelayedUpdateDO(Integer teamId, Integer classId, String bTime, String dTypes){
		this.classId = classId;
		this.teamId = teamId;
		this.bTime = bTime;
		this.dTypes = dTypes;
	}
	
	public DelayedUpdateDO(Integer teamId, Integer classId, String bTime, Integer delayedType, String taskIds, Integer executionTimes, String viData, String vm, Integer sortNum){
		this.classId = classId;
		this.teamId = teamId;
		this.bTime = bTime;
		this.delayedType = delayedType;
		this.viData = viData;
		this.taskIds = taskIds;
		this.executionTimes = executionTimes;
		this.vehicleModel = vm;
		this.sortNum = sortNum;
	}

}

