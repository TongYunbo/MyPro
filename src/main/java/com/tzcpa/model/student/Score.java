package com.tzcpa.model.student;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年5月8日
 */
@Data
public class Score implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 影响积分
	 */
	private Double score;
	
	/**
	 * 答案ID
	 */
	private Integer questionId;
	
	/**
	 * 班级ID
	 */
	private Integer classId;

}

