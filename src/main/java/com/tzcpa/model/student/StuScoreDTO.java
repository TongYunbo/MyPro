package com.tzcpa.model.student;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>Description: 用来进行添加学生积分</p>
 * @author WTL
 * @date 2019年5月8日
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StuScoreDTO extends Score implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	private Integer roleId;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 所属年（通过时间线取）
	 */
	private Integer year;
	
	/**
	 * 根结点ID
	 */
	private Integer rootId;
	
	public StuScoreDTO(HseRequest hse, Map<String, Object> crMap){
		this.setQuestionId(Integer.valueOf(hse.getQuestionId()));
		this.setClassId(hse.getClassId());
		
		//根据权重计算出最终的积分(如果权重为null的话，默认不做处理)
		this.setScore(Double.valueOf(crMap.get("score").toString()) * (hse.getWeight() == null ? 1 : hse.getWeight()));
		this.teamId = hse.getTeamId();
		this.roleId = Integer.valueOf(hse.getRoleId());
		this.year = Integer.valueOf(hse.getTimeLine().substring(0, 4));
		this.rootId = hse.getRootId();
	}
}

