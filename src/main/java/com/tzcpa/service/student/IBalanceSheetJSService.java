package com.tzcpa.service.student;
/**
 * <p>Description: </p>
 * @author lrs
 * @date 2019年6月25日
 */
public interface IBalanceSheetJSService {
	
	public void updateTeamBalanceSheet(Integer year,Integer classId,Integer teamId);

	public void updateTeamMonthlyProfitStatement(String ymDate, Integer classId, Integer teamId);

	

}

