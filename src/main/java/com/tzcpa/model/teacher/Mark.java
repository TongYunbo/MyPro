package com.tzcpa.model.teacher;

import java.io.Serializable;

import lombok.Data;

/**
 * 题干信息、学生答案、背景资料
 *
 * @author LRS
 * <p>
 * 2019年5月8日
 */
@Data
public class Mark implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 题目id
	 */
	private Integer id;
	
	/**
	 * 题干信息
	 */
	private String questionDesc;
	
	/**
	 * 背景资料
	 */
	private String backgroundDesc;
	
	/**
	 * 问题ID
	 */
	private Integer questionId;
	/**
	 * 上级ID
	 */
	private Integer parentId;
	/**
	 * 问题类型
	 */
	private Integer questionType;
	/**
	 * 班级ID
	 */
	private Integer classId;
	/**
	 * 团队ID
	 */
	private Integer teamId;
	/**
	 * 答案选择
	 */
	private String answer;
	/**
	 * 题目id
	 */
	private Double score;
	
	
	
}
