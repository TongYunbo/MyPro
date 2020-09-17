package com.tzcpa.model.student;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 *
 *
 * @author lrs
 * <p>
 * 2019年5月28日
 */
@Data
@ToString
public class OptionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    private Integer classId;
    /**
     * 选项
     */
    private Integer option;
    /**
     * 团队id
     */
    private Integer teamId;
    /**
     * 题目id
     */
    private Integer questionId;
    
    /**
     * 问题选项
     */
    private String quesionOption;

    /**
     * 问题选项描述
     */
    private String quesionOptionDesc;
    /**
     * 目标值
     */
    private String targetValue;
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 计算实际值方法名
     */
    private String balanceScoreFunc;
    
    /**
     * 战略值
     */
    private String strategy;
    
    /**
     * 匹配度
     */
    private Double matchingDegree;
    
    /**
     * 年
     */
    private Integer year;
    
    public OptionDto(){}
    
    public OptionDto(HseRequest hse){
		this.setQuestionId(Integer.valueOf(hse.getQuestionId()));
		this.setClassId(hse.getClassId());
		this.teamId = hse.getTeamId();
	}

}