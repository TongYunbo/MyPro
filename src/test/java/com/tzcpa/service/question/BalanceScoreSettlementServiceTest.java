package com.tzcpa.service.question;

import com.tzcpa.model.student.HseRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class BalanceScoreSettlementServiceTest {

    @Autowired
    private BalanceScoreSettlementService balanceScoreSettlementService;


    @Test
    public void balanceScoreSettlement() {
        HseRequest hseRequest = new HseRequest();
        hseRequest.setClassId(123);
        hseRequest.setTeamId(78);
        hseRequest.setTimeLine("2011-01");
        try {
            balanceScoreSettlementService.BalanceScoreSettlement(hseRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}