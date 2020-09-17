package com.tzcpa.service.treatment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@SpringBootTest
@RunWith(SpringRunner.class)
public class InitTeamIntermediateServiceTest {

    @Resource
    private InitTeamIntermediateService initTeamIntermediateService;

    @Test
    public void initTeamIntermediate() {
    }

    /**
     * 战略选择
     *
     * @Author hanxf
     * @Date 9:59 2019/6/4
     * @param
     * @return void
    **/
    @Test
    public void initTeamIntermediateOfStrategic() throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        map.put("classId",String.valueOf(500));
        map.put("teamId",String.valueOf(922));
        map.put("year",String.valueOf(2012));
        map.put("strategicSelect","2012_差");

        initTeamIntermediateService.initTeamIntermediateOfStrategic(map);
    }

    @Test
    public void updateTeamSalesAdjustedUnitMaterialCost() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2012-05");
        Set set = new HashSet();
        set.add("limousine");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateTeamSalesAdjustedUnitMaterialCost(map);
    }

//    @Test
//    public void updateAdjustedSalesByH8() {
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("classId",String.valueOf(132));
//        map.put("teamId",String.valueOf(172));
//        map.put("month","5");
//        initTeamIntermediateService.updateAdjustedSalesByH8(map);
//    }
    @Test
    public void updateFinalOperatingCostPlus() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(434));
        map.put("teamId",String.valueOf(773));
        map.put("bDate","2011-01");
        map.put("eDate","2012-01");
//        Set set = new HashSet();
//        set.add("limousine");
//        map.put("vehicleModel",set);
        initTeamIntermediateService.updateFinalOperatingCostPlus(map);
    }
    @Test
    public void updateTeamFinalMaterialCost()  throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2013-01");
        Set set = new HashSet();
        set.add("limousine");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateTeamFinalMaterialCost(map);
    }
    @Test
    public void updateManagementTotal() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2013-01");
        Set set = new HashSet();
        set.add("limousine");
        set.add("pickup");
        set.add("h6");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateManagementTotal(map);

    }

    @Test
    public void updateFinalManagementTotal() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2012-05");
        Set set = new HashSet();
        set.add("limousine");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateFinalManagementTotal(map);

    }
    @Test
    public void updateFinalAssetsImpairmentLossTotal() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2012-05");
        Set set = new HashSet();
        set.add("limousine");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateFinalAssetsImpairmentLossTotal(map);

    }
    @Test
    public void updateFinalSalesTotal() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(135));
        map.put("teamId",String.valueOf(192));
        map.put("bDate","2012-01");
        map.put("eDate","2012-05");
        Set set = new HashSet();
        set.add("limousine");
        map.put("vehicleModel",set);
        initTeamIntermediateService.updateFinalSalesTotal(map);

    }
    @Test
    public void updateInitInDevelopmentCost() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(94));
        map.put("teamId",String.valueOf(31));
        map.put("year",String.valueOf(2011));
        map.put("inDevelopmentCost",String.valueOf(10));
        initTeamIntermediateService.updateInitInDevelopmentCost(map);

    }

    @Test
    public void updateAdjustedSalesByH8()throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("classId",String.valueOf(132));
        map.put("teamId",String.valueOf(172));
        map.put("month","5");
        initTeamIntermediateService.updateAdjustedSalesByH8(map);
    }
}