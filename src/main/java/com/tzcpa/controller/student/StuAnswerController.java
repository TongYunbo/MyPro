package com.tzcpa.controller.student;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.StuAnswerService;
import com.tzcpa.utils.UserSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName StuAnswerController
 * @Description 学生答案
 * @Author hanxf
 * @Date 2019/5/28 14:55
 * @Version 1.0
 **/
@RestController
public class StuAnswerController {

    @Autowired
    private StuAnswerService stuAnswerService;

    @PostMapping(value = "getAnswerYear")
    public ResponseResult getAnswerYear(@RequestBody JSONObject jsonObject){
        try {

            int classId = jsonObject.getInteger("classId");
            int teamId = jsonObject.getInteger("teamId");
            List<Integer> answerYearList = stuAnswerService.getAnswerYear(classId, teamId);
            return ResponseResult.success("查询已答题年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询已答题年份失败");
    }

    /**
     * 查询年度分析表的年份
     * @author WangYao
     * @date 2019年6月12日
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "getTeamAnnualWorthGatherYear")
    public ResponseResult getTeamAnnualWorthGatherYear(@RequestBody JSONObject jsonObject){
        try {

            int classId = jsonObject.getInteger("classId");
            int teamId = jsonObject.getInteger("teamId");
            List<Integer> answerYearList = stuAnswerService.getTeamAnnualWorthGatherYear(classId, teamId);
            return ResponseResult.success("查询年度分析表的年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询年度分析表的年份失败");
    }
    
    /**
     * 查询资产负债表的年份
     * @author WangYao
     * @date 2019年6月12日
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "getTeamBalanceSheetYear")
    public ResponseResult getTeamBalanceSheetYear(@RequestBody JSONObject jsonObject){
        try {

            int classId = jsonObject.getInteger("classId");
            int teamId = jsonObject.getInteger("teamId");
            List<Integer> answerYearList = stuAnswerService.getTeamBalanceSheetYear(classId, teamId);
            return ResponseResult.success("查询资产负债表的年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询资产负债表的年份失败");
    }
    
    /**
     * 查询年度利润表的年份
     * @author WangYao
     * @date 2019年6月12日
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "getTeamProfitStatementYear")
    public ResponseResult getTeamProfitStatementYear(@RequestBody JSONObject jsonObject){
        try {

            int classId = jsonObject.getInteger("classId");
            int teamId = jsonObject.getInteger("teamId");
            List<Integer> answerYearList = stuAnswerService.getTeamProfitStatementYear(classId, teamId);
            return ResponseResult.success("查询年度利润表的年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询年度利润表的年份失败");
    }
    
    /**
     * 查询月度利润表的年份
     * @author WangYao
     * @date 2019年6月12日
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "getTeamMonthlyProfitStatementYear")
    public ResponseResult getTeamMonthlyProfitStatementYear(@RequestBody JSONObject jsonObject){
        try {

            int classId = jsonObject.getInteger("classId");
            int teamId = jsonObject.getInteger("teamId");
            List<Integer> answerYearList = stuAnswerService.getTeamMonthlyProfitStatementYear(classId, teamId);
            return ResponseResult.success("查询月度利润表的年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询月度利润表的年份失败");
    }
}
