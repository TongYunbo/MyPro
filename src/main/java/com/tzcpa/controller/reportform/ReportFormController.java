package com.tzcpa.controller.reportform;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.treatment.TeamBalanceSheetDO;
import com.tzcpa.model.treatment.TeamProfitStatementDO;
import com.tzcpa.service.reportform.ReportFormService;
import com.tzcpa.service.student.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportFormController
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/29 11:28
 * @Version 6.0
 **/
@Slf4j
@RestController
@RequestMapping("/reportForm")
public class ReportFormController {

    @Resource
    ReportFormService reportFormService;


    /**
     * @author: wangzhangju
     * @date: 2019/5/29 11:33
     * @param: null
     * @return:
     * @exception:
     * @description: 获取当前年份 月度利润表
     * @step:
     */
    @RequestMapping(value = "getMonthlyForm", method = RequestMethod.POST)
    public ResponseResult getMonthlyForm(@RequestBody JSONObject jsonObject) {

        List<TeamMonthlyProfitStatementDO> statementDOList =
                reportFormService.getMonthlyForm(jsonObject.getInteger("classId"), jsonObject.getInteger("teamId"), jsonObject.getInteger("year"));
        if (statementDOList.size() == 0) {
            return ResponseResult.fail("数据为空");
        } else {
            return ResponseResult.success(statementDOList);
        }

    }

    /**
     * @author: wangzhangju
     * @date: 2019/5/29 14:12
     * @param: null
     * @return:
     * @exception:
     * @description: 差选年度利润报表
     * @step:
     */
    @RequestMapping(value = "getAnnualForm", method = RequestMethod.POST)
    public ResponseResult getAnnualForm(@RequestBody JSONObject jsonObject) {

        List<TeamProfitStatementDO> statementDOList =
                reportFormService.getAnnualForm(jsonObject.getInteger("classId"), jsonObject.getInteger("teamId"),
                        jsonObject.getInteger("year"));
        if (statementDOList.size() == 0) {
            return ResponseResult.fail("数据为空");
        } else {
            return ResponseResult.success(statementDOList);
        }

    }

    /**
     * @author: wangzhangju
     * @date: 2019/5/30 16:57
     * @param: null
     * @return:
     * @exception:
     * @description: 获取资产负债表数据
     * @step:
     */
    @RequestMapping(value = "getBalanceSheet", method = RequestMethod.POST)
    public ResponseResult getBalanceSheet(@RequestBody JSONObject jsonObject) {

        List<TeamBalanceSheetDO> teamBalanceSheetDOS = reportFormService.getBalanceSheet(jsonObject.getInteger("classId"), jsonObject.getInteger("teamId"),
                jsonObject.getInteger("year"));
        if (teamBalanceSheetDOS.size() > 0) {
            return ResponseResult.success(teamBalanceSheetDOS);
        } else {
            return ResponseResult.fail("数据为空!");
        }
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/3 14:20
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取年度分析报表数据
     * @step:
     */
    @RequestMapping(value = "getTeamAnnual", method = RequestMethod.POST)
    public ResponseResult getTeamAnnual(@RequestBody JSONObject jsonObject) {

        List<Map> teamAnnual = reportFormService.getTeamAnnual(jsonObject.getInteger("classId"), jsonObject.getInteger("teamId"),
                jsonObject.getInteger("year"));
        if (teamAnnual != null) {
            return ResponseResult.success(teamAnnual);
        } else {
            return ResponseResult.fail("数据为空!");
        }
    }


}


