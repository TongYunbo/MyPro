package com.tzcpa.model.student;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>Description: 用来返回查询答案结果</p>
 * @author WTL
 * @date 2019年5月8日
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class AnswerScoreDO extends Score implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标准答案
	 */
	private String answer;
	
	/**
	 * 差异化战略（如果仅仅一种车型的时候可以不添加车型）
	 */
	private String strategicChoice;
	
	/**
	 * 是否受答案影响（有的事件是必须进行影响的所以不受答案影响）
	 */
	private Boolean isImpact = true;
	
	/**
	 * 团队ID
	 */
	private Integer teamId;
	
	/**
	 * 时间线
	 */
	private String timeLine;
	
	/**
	 * 车型（查询影响的时候用的）
	 */
	private String vehicleModel;
	
	/**
	 * 开始执行时间（做延迟任务用来确定开始时间包含此事件）
	 */
	private String bImplementTime;
	
	/**
	 * 是否包含查询未选中的数据，是的话赋值为1，否则不需要赋值
	 */
	private Integer isSelect;
	
	/**
	 * 要查询的任务ID
	 */
	private Integer taskId;
	
	public AnswerScoreDO(HseRequest hse){
		this.answer = JSON.toJSONString(hse.getAnswer());
		this.setQuestionId(Integer.valueOf(hse.getQuestionId()));
		this.setClassId(hse.getClassId());
		this.teamId = hse.getTeamId();
		this.timeLine = hse.getTimeLine();
	}
	
	public AnswerScoreDO(String bImplementTime){
		this.bImplementTime = bImplementTime;
	}
	
	public AnswerScoreDO(Integer classId, Integer teamId, String timeLine){
		this.setClassId(classId);
		this.setTeamId(teamId);
		this.timeLine = timeLine;
	}
	
	public AnswerScoreDO(Integer classId, Integer teamId, String timeLine, Integer questionId){
		this.setClassId(classId);
		this.setTeamId(teamId);
		this.timeLine = timeLine;
		this.setQuestionId(questionId);
	}
	
	/**
	 * 获取HseRequest对象
	 * @return
	 */
	public HseRequest newHseInstance(){
		return new HseRequest(this);
	}
	
	/**
	 * 添加是否受答案影响和问题ID，及更改所需的值（有的问题所有的都执行同一种影响，把战略值置为空可以减少数据库的配置）
	 * @param isImpact
	 * @param questionId
	 */
	public void setIIQ(Boolean isImpact, Integer questionId){
		this.isImpact = isImpact;
		this.setQuestionId(questionId);
		this.strategicChoice = null;
		this.answer = null;
		this.vehicleModel = null;
	}
	
	/**
	 * 添加车型及所对应的战略
	 * @param vm
	 * @param sc
	 */
	public void setVMSC(String vm, String sc){
		this.vehicleModel = vm;
		this.strategicChoice = sc;
	}
	
	/**
	 * 参数是否合格(目前只是适用于单选)
	 * @return
	 */
	public Boolean isQualified(){
		if (this.getQuestionId() == null || this.getClassId() == null) {
			return false;
		}
		
		return true;
	}
	
	public Boolean checkACQ(){
		if ((isImpact && this.answer == null) || this.getClassId() == null || this.getQuestionId() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 查询时设置是否选中字段值，并给answer改格式适用于in查询（平衡计分卡不用调用）
	 * @param isSelect
	 */
	public void setISAndUA(){
		this.isSelect = 1;
		this.answer = "'" + this.answer + "'";
	}

}

