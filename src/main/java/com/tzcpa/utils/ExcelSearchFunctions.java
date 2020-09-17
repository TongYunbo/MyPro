package com.tzcpa.utils;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelSearchFunctions
 * @Description  excel的查找函数实现
 * @Author hanxf
 * @Date 2019/5/15 11:17
 * @Version 1.0
 **/
@Slf4j
public class ExcelSearchFunctions {

    /**
     * 返回指定数值在指定数组区域中的位置
     * @param lookupvalue 需要在数据表（lookup_array）中查找的值。
     * @param array ：可能包含有所要查找数值的连续的单元格区域
     * @param type 表示查询的指定方式，用数字-1、0或者1表示。
     * @param type 为1时，查找小于或等于lookup_value的最大数值在lookup_array中的位置，lookup_array必须按升序排列：
     * 为0时，查找等于lookup_value的第一个数值，lookup_array按任意顺序排列：
     * 为-1时，查找大于或等于lookup_value的最小数值在lookup_array中的位置，
     * lookup_array必须按降序排列。利用MATCH函数查找功能时，
     * 当查找条件存在时，MATCH函数结果为具体位置（数值），
     * 否则显示#N/A错误。
     *  @return
     */

    public static int match(int lookupvalue,int[] array,int type)
    {
        int res=0;
        if(array==null||array.length==0)
            throw new IllegalArgumentException("数组为空");
        //精确匹配
        if(type==0)
        {
            for(int i=0;i<array.length;i++)
            {
                if(array[i]==lookupvalue)
                    return i+1;
            }
            throw new IllegalArgumentException("#N/A");
        }

        //顺序匹配_正序匹配:在查找index的同时必须满足整个数组按照正序排列
        if(type>0)
        {
            if(lookupvalue<array[0])
                throw new IllegalArgumentException("#N/A");
            for(int i=0;i<array.length-1;i++)
            {
                if(array[i]>array[i+1])
                    throw new IllegalArgumentException("#N/A");
                if(lookupvalue<array[i])
                    res=res>0?res:i;//已经找到了res 接下来就不用赋值了
            }
            if(lookupvalue>array[array.length-1])
                res=array.length;
        }

        //顺序匹配_正序匹配:在查找index的同时必须满足整个数组按照逆序排列
        if(type<0)
        {
            if(lookupvalue>array[0])
                throw new IllegalArgumentException("#N/A");
            for(int i=0;i<array.length-1;i++)
            {
                if(array[i]<array[i+1])
                    throw new IllegalArgumentException("#N/A");
                if(lookupvalue>array[i])
                    res=res>0?res:i;
            }
            if(lookupvalue<array[array.length-1])
                res=array.length;
        }
        return res;
    }


    public static int vlookup(int target,int[][] scan,int col)
    {
        if(scan.length==0||scan[0].length==0)
            return -Integer.MAX_VALUE;
        if(col>scan[0].length)
            return -Integer.MAX_VALUE;

        for(int i=0;i<scan.length;i++)
        {
            if(target==scan[i][0])
                return scan[i][col-1];
        }
        return -Integer.MAX_VALUE;
    }

    public static int hlookup(int target,int[][] scan,int row)
    {
        if(scan.length==0||scan[0].length==0)
            return -Integer.MAX_VALUE;
        if(row>scan.length)
            return -Integer.MAX_VALUE;

        for(int i=0;i<scan[0].length;i++)
        {
            if(target==scan[0][i])
                return scan[row-1][i];
        }
        return -Integer.MAX_VALUE;
    }

    /**
     * 类似excel中lookup功能
     *
     * @Author hanxf
     * @Date 12:02 2019/5/15
     * @param target 查找的目标值
     * @param searchList  查找的内容列表
     * @param targetList  返回的内容列表
     * @return int
    **/
    public static Long lookup(Long target, List<Long> searchList, List<Long> targetList)
    {
        if(searchList==null||searchList==null) {
            log.error("lookup 查找的列表为空");
            throw new IllegalArgumentException("#N/A:searchList为空");
        }
        if(searchList.size()!=targetList.size()) {
            log.error("lookup 数组长度不匹配");
            throw new IllegalArgumentException("#N/A:列表大小不匹配");
        }
        int index=-1;
        for(int i=0;i<searchList.size();i++)
        {
            if(target>=searchList.get(i))
            {
                index=i;
            }
        }
        return targetList.get(index);
    }


    public static void main(String[] args) {
        /*******************测试查找引用函数*****************************/
//        int[] a={25,35,45,55,65};
//        int[] b={65,55,45,35,25};
        int[] a={0,25,41,91,168,250};
        int[] b={455,435,402,360,350,344};
//        System.out.println(match(70,a,1));
//        System.out.println(match(45,a,0));
//        System.out.println(match(60,b,-1));
//        System.out.println(match(55,b,-1));
//
        int[][] scan=new int[][]{{7,17,117,1117},{8,18,118,1118},{9,19,119,1119}};
//        System.out.println(vlookup(7,scan,3));
//        System.out.println(hlookup(7,scan,3));
        
        
        List<Long> searchList = new ArrayList<>();
        searchList.add(0L);
        searchList.add(25L);
        searchList.add(41L);
        searchList.add(91L);
        searchList.add(168L);
        searchList.add(250L);
        List<Long> tragetList = new ArrayList<>();
        tragetList.add(455L);
        tragetList.add(435L);
        tragetList.add(402L);
        tragetList.add(360L);
        tragetList.add(350L);
        tragetList.add(344L);
        System.out.println(lookup(161L,searchList,tragetList));
    }
}


