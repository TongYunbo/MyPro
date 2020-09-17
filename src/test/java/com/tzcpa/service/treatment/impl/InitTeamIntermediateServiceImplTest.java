package com.tzcpa.service.treatment.impl;

import com.tzcpa.service.treatment.InitTeamIntermediateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InitTeamIntermediateServiceImplTest {

    @Autowired
    private InitTeamIntermediateService initTeamIntermediateService;

    @Test
    public void initTeamIntermediate() {
    }

    @Test
    public void initTeamIntermediateOfStrategic() {
    }

    /**
     * 单车材料成本
     *
     * @Author hanxf
     * @Date 18:32 2019/6/3
     * @param
     * @return void
    **/
    @Test
    public void updateTeamSalesAdjustedUnitMaterialCost() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("classId", 122);
        map.put("teamId", 77);
        map.put("bDate", "2011-1");
        map.put("eDate", "2011-12");
        map.put("vehicleModel", new HashSet());
        initTeamIntermediateService.updateTeamSalesAdjustedUnitMaterialCost(map);
    }

//    @Test
//    public void updateTeamIntermediateOfFinalSales() {
//        Map<String,Object> map = new HashMap<>();
//        map.put("classId", 122);
//        map.put("teamId", 77);
//        map.put("bDate", "2011-01");
//        map.put("eDate", "2011-3");
//        initTeamIntermediateService.updateTeamIntermediateOfFinalSales(map);
//    }

    @Test
    public void updateTeamIntermediateSalesUnitPrice() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("classId", 135);
        map.put("teamId", 192);
        map.put("timeLine", "2012-03-01");
        initTeamIntermediateService.updatePackagingSales(map);
    }


    @Test
    public void updateInitFinancialCost() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("classId", 38);
        map.put("teamId", 66);
        map.put("year", "2011");
        initTeamIntermediateService.updateInitFinancialCost(map);
    }
}