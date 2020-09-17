package com.tzcpa.model.student;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Description: 学生考试题传送参数</p>
 * @author WTL
 * @date 2019年5月8日
 */
@Data
@ToString
public class HseRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 学生选择的答案
	 */
	private List<String> answer;
	
	/**
	 * 考题ID
	 */
	private Integer questionId;

	/**
	 * 主题id
	 */
	private Integer rootId;
	
	/**
	 * 加分对象id 例如：1,2,3等(传入的时候为单个，在数据中计算的时候有可能会变成多个以逗号分割)
	 */
	private String roleId;
	
	/**
	 * 团队ID
	 * TODO 需要从session中取
	 */
	private Integer teamId;
	
	/**
	 * 班级ID
	 * TODO 需要从session中取
	 */
	private Integer classId;
	
	/**
	 * 时间线
	 */
	private String timeLine;
	
	/**
	 * 是否受战略影响
	 */
	private Integer isStrategic;
	
	/**
	 * 本题分数所占权重
	 */
	private Double weight;
	
	public HseRequest(){}
	
	public HseRequest(AnswerScoreDO asd){
		this.classId = asd.getClassId();
		this.teamId = asd.getTeamId();
		this.timeLine = asd.getTimeLine();
	}
	
	public HseRequest(Integer classId, Integer teamId, String timeLine){
		this.classId = classId;
		this.teamId = teamId;
		this.timeLine = timeLine;
	}
	
	public Boolean checkCTTQ(){
		if (this.classId == null || this.teamId == null || this.timeLine == null) {
			return false;
		}
		return true;
	}

}

