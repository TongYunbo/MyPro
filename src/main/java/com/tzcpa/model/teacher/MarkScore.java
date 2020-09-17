package com.tzcpa.model.teacher;

import java.io.Serializable;
import lombok.Data;

/**
 * 学生分数表实体
 *
 * @author LRS
 * <p>
 * 2019年5月8日
 */
@Data
public class MarkScore implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 分数
	 */
	private Double score;
	
	/**
	 * 问题Id
	 */
	private Integer questionId;
	
	/**
	 * 团队id
	 */
	private Integer teamId;
	/**
	 * 角色id
	 */
	private Integer roleId;
	/**
	 * 根节点id
	 */
	private Integer rootId;
	
	/**
	 * 问题ID
	 */
	private Integer classId ;
	/**
	 * 本题分数所占权重
	 */
	private Double weight;
	/**
	 * 所属年（通过时间线取）
	 */
	private Integer year;
	/**
	 * 时间线
	 */
	private String timeLine;
	
}
