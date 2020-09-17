package com.tzcpa.service.treatment;

/**
 * @ClassName MyTest
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 10:16
 * @Version 1.0
 **/
public class MyTest {
    public static void main(String[] args) {

//        List<String> list = new ArrayList<String>();
//        list.add("A");
//        list.add("B");
//        list.add("完美的表现，可爱,hfghghg");
//        String jsonString = JSON.toJSONString(list);
//
//        System.out.println(jsonString);

// 雷达图
//        Map<String,Object> map = new HashMap<>();
//        map.put("totalScore",110);
//        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("roleName","CEO");
//        map1.put("userName","张三");
//        map1.put("score",50);
//        lists.add(map1);
//        Map<String,Object> map2 = new HashMap<>();
//        map2.put("roleName","CFO");
//        map2.put("userName","李三");
//        map2.put("score",60);
//        lists.add(map2);
//        map.put("roleList",lists);
//        System.out.println(JSON.toJSONString(map));


        //月销量
//        Map<String,Object> map = new HashMap<>();
//
//        map.put("vehicleModelGroup","SUV");
//        map.put("strategyBenchmarkSales",100);
//        map.put("adjusted_sales",200);

//        销量
//        List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
//        Map<String,String> map001 = new HashMap<>();
//        map001.put("vehicleModelGroup","SUV");
//        map001.put("month","1");
//        map001.put("strategyBenchmarkSales","100");
//        map001.put("adjustedSales","101");
//        lists.add(map001);
//        Map<String,String> map002 = new HashMap<>();
//        map002.put("vehicleModelGroup","SUV");
//        map002.put("month","2");
//        map002.put("strategyBenchmarkSales","200");
//        map002.put("adjustedSales","");
//        lists.add(map002);
//        Map<String,String> map003 = new HashMap<>();
//        map003.put("vehicleModelGroup","limousine");
//        map003.put("month","1");
//        map003.put("strategyBenchmarkSales","100");
//        map003.put("adjustedSales","101");
//        lists.add(map003);
//        System.out.println(JSON.toJSONString(lists));


        // 市场份额
//        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
//        Map<String,Object> map001 = new HashMap<>();
//        map001.put("vehicleModelGroup","J50");
//        map001.put("finalSales",50);
//
//        lists.add(map001);
//        Map<String,Object> map002 = new HashMap<>();
//        map002.put("vehicleModelGroup","S60");
//        map002.put("finalSales",50);
//        lists.add(map002);
//
//        System.out.println(JSON.toJSONString(lists));

        //营业利润
//        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
//        Map<String,Object> map001 = new HashMap<>();
//        map001.put("vehicleModelGroup","J50");
//        map001.put("operatingProfit",50);
//
//        lists.add(map001);
//        Map<String,Object> map002 = new HashMap<>();
//        map002.put("vehicleModelGroup","S60");
//        map002.put("operatingProfit",50);
//        lists.add(map002);
//
//        System.out.println(JSON.toJSONString(lists));


//月净利润、营业收入、销售毛利图表

//        Map<String,Object> map001 = new HashMap<>();
//        List<Long> list1 = new ArrayList<>();
//        list1.add(620L);
//        list1.add(732L);
//        list1.add(701L);
//        list1.add(734L);
//        list1.add(1090L);
//        list1.add(1130L);
//        list1.add(1120L);
//        map001.put("retainedProfits",list1);
//        List<Long> list2 = new ArrayList<>();
//        list2.add(620L);
//        list2.add(732L);
//        list2.add(701L);
//        list2.add(734L);
//        list2.add(1090L);
//        list2.add(1130L);
//        list2.add(1120L);
//        map001.put("grossRevenue",list2);
//        List<Long> list3 = new ArrayList<>();
//        list3.add(620L);
//        list3.add(732L);
//        list3.add(701L);
//        list3.add(734L);
//        list3.add(1090L);
//        list3.add(1130L);
//        list3.add(1120L);
//        map001.put("salesMargin",list3);
//
//        System.out.println(JSON.toJSONString(map001));

//        List<Test> lists = new ArrayList<>();
//        lists.add(new Test(1L,2L,3L));
//        lists.add(new Test(2L,3L,4L));
//        System.out.println(JSON.toJSONString(lists));

//        List<Map<String,Object>> resultMapList = new ArrayList<Map<String,Object>>();
//
//        Map<String,Object> resultMap = new HashMap<>();
//        resultMap.put("team1",)

//        List<Test> list = new ArrayList<Test>();
//        Test test = new Test();
//        test.setGrossMargin(0L);

        try{
            aa();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("dd");
        }


        System.out.println("cc");

    }


    public static void aa() throws Exception{
        try{
            System.out.println("aa");
            int  a = 1/0;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ee");
        }finally {
            System.out.println("bb");
        }
    }


    static class Test{
        Long grossMargin;
        Long profitMargin;
        Long operatingProfitRatio;

        public Test(Long grossMargin, Long profitMargin, Long operatingProfitRatio) {
            this.grossMargin = grossMargin;
            this.profitMargin = profitMargin;
            this.operatingProfitRatio = operatingProfitRatio;
        }

        public Long getGrossMargin() {
            return grossMargin;
        }

        public void setGrossMargin(Long grossMargin) {
            this.grossMargin = grossMargin;
        }

        public Long getProfitMargin() {
            return profitMargin;
        }

        public void setProfitMargin(Long profitMargin) {
            this.profitMargin = profitMargin;
        }

        public Long getOperatingProfitRatio() {
            return operatingProfitRatio;
        }

        public void setOperatingProfitRatio(Long operatingProfitRatio) {
            this.operatingProfitRatio = operatingProfitRatio;
        }

        public Test() {
        }
    }
}
