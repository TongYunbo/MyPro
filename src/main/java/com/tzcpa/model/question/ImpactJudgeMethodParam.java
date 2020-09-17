package com.tzcpa.model.question;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;

import lombok.Data;

/**
 * <p>Description: 执行影响判断方法的传参</p>
 * @author WTL
 * @date 2019年7月1日
 */
@Data
public class ImpactJudgeMethodParam {
	
	private OSMapper osMapper;
	
	/**
	 * 用于比较值
	 */
	private Double cv;
	
	/**
	 * 存放于数组中的年份
	 */
	private Integer year;
	
	/**
	 * 主要用于获取当时题的时间和团队的ID，班级ID
	 */
	private HseRequest hse;
	
	public ImpactJudgeMethodParam(HseRequest hse, OSMapper osMapper, String[] vi){
		this.hse = hse;
		this.osMapper = osMapper;
		this.year = Integer.valueOf(vi[4] == null ? NormalConstant.YEAR_2011 : vi[4]);
		this.cv = vi[3].equals(NormalConstant.BFZ80JVFF) ? 0 : Double.valueOf(vi[2]);
	}

}

