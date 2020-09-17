package com.tzcpa.constant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum QuestionContractEnum {

   ID63("dep","2014-10","2019-01","H8"),
    ID108("dep","2016-02","2019-01","H6"),
    ID133("dep","2017-05","2019-01",null),
    ID25("pop","2011-03","2011-04",null),
    ID45("pop","2013-01","2014-01",null),
    ID142("loss","2017-12","2018-01",null)
    ;

   private String maitType;
    /**
     * 开始年月
     */
   private String startDate;

    /**
     * 结束年月
     */
   private String endDate;

    /**
     * 车型
     */
    private String cycleType;

    public String getMaitType() {
        return maitType;
    }

    public void setMaitType(String maitType) {
        this.maitType = maitType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }
    QuestionContractEnum(String maitType,String startDate, String endDate, String cycleType){
        this.maitType = maitType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cycleType = cycleType;
    }

    public static Map<String,Object> getMap(String code, Map<String,Object> map){
        for(QuestionContractEnum questionContractEnum:QuestionContractEnum.values()){
            if(code.equals(questionContractEnum.name())){
                map.put("bDate",questionContractEnum.startDate);
                map.put("eDate",questionContractEnum.endDate);
                if(null != questionContractEnum.cycleType){
                    Set set = new HashSet();
                    set.add(questionContractEnum.cycleType);
                    map.put("vehicleModel",set);
                }
                return map;
            }
        }
        return  null;
    }


}
