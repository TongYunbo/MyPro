package com.tzcpa.service.reportform;

import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.treatment.TeamBalanceSheetDO;
import com.tzcpa.model.treatment.TeamProfitStatementDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportFormService {

    /**
     * @author:     wangzhangju
     * @date:       2019/5/29 14:16
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取月度利润数据
     * @step:
     */
     List<TeamMonthlyProfitStatementDO> getMonthlyForm(Integer classId, Integer teamId, Integer year);

    /**
     * @author:     wangzhangju
     * @date:       2019/5/29 14:16
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取年度利润数据
     * @step:
     */
     List<TeamProfitStatementDO> getAnnualForm(Integer classId, Integer teamId, Integer year);

     /**
      * @author:     wangzhangju
      * @date:       2019/5/30 17:01
      * @param:      null
      * @return:     
      * @exception:  
      * @description: 获取资产负债表数据
      * @step:
      */
    List<TeamBalanceSheetDO> getBalanceSheet(Integer classId, Integer teamId, Integer year);

    /**
     * @author:     wangzhangju
     * @date:       2019/6/3 14:40
     * @param:      null
     * @return:
     * @exception:
     * @description: 查询年度分析报表数据
     * @step:
     */
    List<Map> getTeamAnnual(@Param("classId") Integer classId, @Param("teamId") Integer teamId ,
            @Param("year") Integer year);

}
