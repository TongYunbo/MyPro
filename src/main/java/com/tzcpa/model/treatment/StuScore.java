package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;
/**
 * @author WangYao
 * 2019年5月10日
 */
@Data
@ToString
public class StuScore {

	/**
	 * 学员分数id
	 */
	private Integer id;
	
	/**
	 * 学员分数
	 */
	private String score;
	
	/**
	 * 问题id
	 */
	private Integer questionId;
	
	/**
	 * 角色id
	 */
	private Integer roleId;
	
	/**
	 * 团队id
	 */
	private Integer teamId;
	
	/**
	 * 班级id
	 */
	private Integer classId;
	
	/**
	 * 年限
	 */
	private Integer year;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 角色名称
	 */
	private String roleName;
}
