package com.tzcpa.mapper.treatment;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.OptionDto;

/**
 * <p>Description: </p>
 * @author lrs
 * @date 2019年5月28日
 */
public interface BalanceMapper {
	/**
	 * 查询问题选项以及选项描述
	 * @param hse
	 * @return OptionDto
	 */
	List<OptionDto> findOption(@Param("answerList")String answerList,@Param("hse") HseRequest hse);
	
	/**
	 * 查询问题选项以及选项描述
	 * @param questionIds
	 * @param classId
	 * @return
	 */
	List<OptionDto> findOptionByQuestionIds(@Param("questionIds")String questionIds, @Param("classId")Integer classId, @Param("years")String years);
	
	/**
	 * 根据条件修改平衡记录表数据
	 * @param optionDto
	 * @return
	 */
	void updateBalanceScoreCard(@Param("op")OptionDto optionDto,@Param("beYear")String beYear);

	/**
	 * 批量修改平衡计分卡
	 * @param optionDto
	 * @param beYear
	 */
	void updateBatchBalanceScoreCard(@Param("list")List<OptionDto> odList);
	
	
}

